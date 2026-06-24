package com.elgunzamanov.postperformanceanalysis;

import com.elgunzamanov.postperformanceanalysis.dto.DashboardDto;
import com.elgunzamanov.postperformanceanalysis.dto.PostDto;
import com.elgunzamanov.postperformanceanalysis.dto.comment.CommentSummaryDto;
import com.elgunzamanov.postperformanceanalysis.dto.comment.CommentsDto;
import com.elgunzamanov.postperformanceanalysis.dto.reaction.ReactionSummaryDto;
import com.elgunzamanov.postperformanceanalysis.dto.reaction.ReactionsDto;
import com.elgunzamanov.postperformanceanalysis.service.AnalyticsService;
import com.elgunzamanov.postperformanceanalysis.service.MetaGraphApiService;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {
	@Mock
	private MetaGraphApiService metaGraphApiService;
	
	@InjectMocks
	private AnalyticsService analyticsService;
	
	@Test
	void shouldReturnCorrectDashboardData() {
		List<PostDto> posts = List.of(
			createPost(100, 10),
			createPost(50, 5),
			createPost(200, 20)
		);
		
		when(metaGraphApiService.getPosts()).thenReturn(posts);
		when(metaGraphApiService.getPageSize()).thenReturn(10);
		
		DashboardDto result = analyticsService.getDashboardData();
		
		assertNotNull(result);
		
		assertEquals(10, result.pageSize());
		assertEquals(3, result.posts().size());
		
		assertTrue(result.maxReaction() > 0);
		assertTrue(result.hasReactions());
		
		assertNotNull(result.bestTimeSlot());
		assertFalse(result.formattedPeakDays().isBlank());
	}
	
	@Test
	void shouldHandleEmptyPosts() {
		when(metaGraphApiService.getPosts()).thenReturn(List.of());
		when(metaGraphApiService.getPageSize()).thenReturn(10);
		
		DashboardDto result = analyticsService.getDashboardData();
		
		assertTrue(result.posts().isEmpty());
		assertEquals(0, result.maxReaction());
		assertFalse(result.hasReactions());
		assertNull(result.bestTimeSlot());
	}
	
	@Test
	void shouldCalculateTop3PostsCorrectly() {
		List<PostDto> posts = List.of(
			createPost(10, 1),
			createPost(500, 10),
			createPost(100, 5),
			createPost(300, 3)
		);
		
		when(metaGraphApiService.getPosts()).thenReturn(posts);
		when(metaGraphApiService.getPageSize()).thenReturn(10);
		
		DashboardDto result = analyticsService.getDashboardData();
		
		assertEquals(3, result.top3Posts().size());
		
		assertTrue(
			PostDto.getEngagementScore(result.top3Posts().get(0)) >=
				PostDto.getEngagementScore(result.top3Posts().get(1))
		);
	}
	
	private @NonNull PostDto createPost(long reactions, long comments) {
		ReactionSummaryDto reactionSummary =
			new ReactionSummaryDto((int) reactions, "LIKE");
		
		ReactionsDto reactionsDto =
			new ReactionsDto(List.of(), reactionSummary);
		
		CommentSummaryDto commentSummary =
			new CommentSummaryDto("relevance", (int) comments, true);
		
		CommentsDto commentsDto =
			new CommentsDto(List.of(), commentSummary);
		
		return new PostDto(
			"1",
			"message",
			OffsetDateTime.now(),
			reactionsDto,
			commentsDto
		);
	}
}
