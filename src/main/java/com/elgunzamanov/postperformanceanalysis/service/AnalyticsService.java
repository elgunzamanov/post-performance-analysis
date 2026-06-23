package com.elgunzamanov.postperformanceanalysis.service;

import com.elgunzamanov.postperformanceanalysis.dto.DashboardDto;
import com.elgunzamanov.postperformanceanalysis.dto.DayStat;
import com.elgunzamanov.postperformanceanalysis.dto.PostDto;
import com.elgunzamanov.postperformanceanalysis.dto.comment.CommentSummaryDto;
import com.elgunzamanov.postperformanceanalysis.dto.comment.CommentsDto;
import com.elgunzamanov.postperformanceanalysis.dto.reaction.ReactionSummaryDto;
import com.elgunzamanov.postperformanceanalysis.dto.reaction.ReactionsDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
	private final MetaGraphApiService metaGraphApiService;
	
	public DashboardDto getDashboardData() {
		List<PostDto> posts = metaGraphApiService.getPosts();
		Map<String, Long> reactionsByDayOfWeek = calculateReactionsByDayOfWeek(posts);
		long maxReaction = calculateMaxReaction(reactionsByDayOfWeek);
		List<DayStat> dayStats = prepareDayStats(reactionsByDayOfWeek, maxReaction);
		List<String> peakDays = getPeakDays(dayStats);
		long peakReaction = getPeakReaction(dayStats);
		double regularDaysAverage = getRegularDaysAverage(dayStats);
		
		return new DashboardDto(
			metaGraphApiService.getPageSize(),
			posts,
			findTop3Posts(),
			reactionsByDayOfWeek,
			maxReaction,
			dayStats,
			formatPeakDays(peakDays),
			calculateEngagementSurgePercentage(peakReaction, regularDaysAverage),
			hasReactions(peakReaction)
		);
	}
	
	private List<PostDto> findTop3Posts() {
		return metaGraphApiService.getPosts().stream()
			.sorted((p1, p2) -> Long.compare(
				calculateEngagement(p2),
				calculateEngagement(p1)))
			.limit(3)
			.toList();
	}
	
	private long calculateEngagement(PostDto post) {
		if (post == null) return 0;
		
		long reactions = Optional.ofNullable(post.reactions())
			.map(ReactionsDto::summary)
			.map(ReactionSummaryDto::totalCount)
			.orElse(0);
		
		long comments = Optional.ofNullable(post.comments())
			.map(CommentsDto::summary)
			.map(CommentSummaryDto::totalCount)
			.orElse(0);
		
		return reactions + comments;
	}
	
	private @NonNull Map<String, Long> calculateReactionsByDayOfWeek(@NonNull List<PostDto> posts) {
		Map<String, Long> reactionsByDayOfWeek = new LinkedHashMap<>();
		
		List<String> days = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
		days.forEach(day -> reactionsByDayOfWeek.put(day, 0L));
		
		for (PostDto post : posts) {
			if (post.createdTime() == null) continue;
			
			String dayAbbr = post.createdTime()
				.getDayOfWeek()
				.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
			
			long reactions = 0L;
			if (post.reactions() != null && post.reactions().summary() != null) {
				reactions = post.reactions().summary().totalCount();
			}
			
			reactionsByDayOfWeek.merge(dayAbbr, reactions, Long::sum);
		}
		
		return reactionsByDayOfWeek;
	}
	
	private long calculateMaxReaction(@NonNull Map<String, Long> reactionsByDayOfWeek) {
		return reactionsByDayOfWeek.values().stream()
			.mapToLong(Long::longValue)
			.max()
			.orElse(0);
	}
	
	private @NonNull List<DayStat> prepareDayStats(
		@NonNull Map<String, Long> reactionsByDayOfWeek,
		long maxReaction
	) {
		return reactionsByDayOfWeek.entrySet().stream()
			.map(entry -> {
				String day = entry.getKey();
				long count = entry.getValue();
				
				int percentage = maxReaction == 0 ? 0 : (int) ((count * 100) / maxReaction);
				boolean isPeak = (count == maxReaction) && maxReaction > 0;
				
				return new DayStat(day, count, percentage, isPeak);
			})
			.toList();
	}
	
	private @NonNull List<String> getPeakDays(@NonNull List<DayStat> dayStats) {
		return dayStats.stream()
			.filter(DayStat::isPeak)
			.map(DayStat::day)
			.toList();
	}
	
	private @NonNull String formatPeakDays(List<String> peakDays) {
		return String.join(", ", peakDays);
	}
	
	private long getPeakReaction(@NonNull List<DayStat> dayStats) {
		return dayStats.stream()
			.filter(DayStat::isPeak)
			.mapToLong(DayStat::count)
			.findFirst()
			.orElse(0);
	}
	
	private double getRegularDaysAverage(@NonNull List<DayStat> dayStats) {
		return dayStats.stream()
			.filter(stat -> !stat.isPeak())
			.mapToLong(DayStat::count)
			.average()
			.orElse(0);
	}
	
	private int calculateEngagementSurgePercentage(long peakReaction, double regularDaysAverage) {
		if (regularDaysAverage > 0) {
			return (int) Math.round(
				((peakReaction - regularDaysAverage) / regularDaysAverage) * 100
			);
		}
		
		return peakReaction > 0 ? 100 : 0;
	}
	
	private boolean hasReactions(long peakReaction) {
		return peakReaction > 0;
	}
}
