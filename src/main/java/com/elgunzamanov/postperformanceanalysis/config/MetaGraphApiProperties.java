package com.elgunzamanov.postperformanceanalysis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "meta.graph")
public record MetaGraphApiProperties(
	String pageId,
	String pageAccessToken,
	int limit,
	int pageSize,
	String fields
) {
}
