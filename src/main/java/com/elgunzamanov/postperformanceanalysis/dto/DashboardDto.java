package com.elgunzamanov.postperformanceanalysis.dto;

import java.util.List;
import java.util.Map;

public record DashboardDto(
	int pageSize,
	List<PostDto> posts,
	List<PostDto> top3Posts,
	Map<String, Long> reactionsByDayOfWeek,
	long maxReaction,
	List<DayStat> dayStats
) {
}
