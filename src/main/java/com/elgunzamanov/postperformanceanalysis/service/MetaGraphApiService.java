package com.elgunzamanov.postperformanceanalysis.service;

import com.elgunzamanov.postperformanceanalysis.config.MetaGraphApiProperties;
import com.elgunzamanov.postperformanceanalysis.dto.MetaGraphResponse;
import com.elgunzamanov.postperformanceanalysis.dto.PostDto;
import com.elgunzamanov.postperformanceanalysis.exception.MetaGraphApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaGraphApiService {
	private final RestClient restClient;
	private final MetaGraphApiProperties metaGraphApiProperties;
	private final ObjectMapper objectMapper;
	
	public List<PostDto> getPosts() {
		String json = restClient
			.get()
			.uri(uriBuilder -> uriBuilder
				.scheme("https")
				.host("graph.facebook.com")
				.path("/v25.0/{pageId}/posts")
				.queryParam("limit", metaGraphApiProperties.limit())
				.queryParam("fields", metaGraphApiProperties.fields())
				.queryParam("access_token", metaGraphApiProperties.pageAccessToken())
				.build(metaGraphApiProperties.pageId()))
			.retrieve()
			.body(String.class);
		
		try {
			MetaGraphResponse<PostDto> response = objectMapper.readValue(json, new TypeReference<>() {});
			return response != null ? response.data() : List.of();
		} catch (Exception ex) {
			throw new MetaGraphApiException("Meta Graph API response could not be parsed.", ex);
		}
	}
	
	public int getPageSize() {
		return metaGraphApiProperties.pageSize();
	}
}
