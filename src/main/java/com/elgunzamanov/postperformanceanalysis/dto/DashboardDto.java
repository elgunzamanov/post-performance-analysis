package com.elgunzamanov.postperformanceanalysis.dto;

import java.util.List;

public record DashboardDto(
	int pageSize,
	List<PostDto> posts,
	List<PostDto> top3Posts
) {
}
