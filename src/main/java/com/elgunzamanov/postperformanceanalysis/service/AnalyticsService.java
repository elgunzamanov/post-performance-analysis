package com.elgunzamanov.postperformanceanalysis.service;

import com.elgunzamanov.postperformanceanalysis.dto.DashboardDto;
import com.elgunzamanov.postperformanceanalysis.dto.PostDto;
import com.elgunzamanov.postperformanceanalysis.dto.comment.CommentSummaryDto;
import com.elgunzamanov.postperformanceanalysis.dto.comment.CommentsDto;
import com.elgunzamanov.postperformanceanalysis.dto.reaction.ReactionSummaryDto;
import com.elgunzamanov.postperformanceanalysis.dto.reaction.ReactionsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
	private final MetaGraphApiService metaGraphApiService;
	
	public DashboardDto getDashboardData() {
		List<PostDto> posts = metaGraphApiService.getPosts();
		
		return new DashboardDto(
			metaGraphApiService.getPageSize(),
			posts,
			findTop3Posts()
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
}
