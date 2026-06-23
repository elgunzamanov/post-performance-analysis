package com.elgunzamanov.postperformanceanalysis.service;

import com.elgunzamanov.postperformanceanalysis.dto.DashboardDto;
import com.elgunzamanov.postperformanceanalysis.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
	private final MetaGraphApiService metaGraphApiService;
	
	public DashboardDto getDashboardData() {
		List<PostDto> posts = metaGraphApiService.getPosts();
		
		return new DashboardDto(
			metaGraphApiService.getPageSize(),
			posts
		);
	}
}
