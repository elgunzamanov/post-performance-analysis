package com.elgunzamanov.postperformanceanalysis.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record CommentDto(
	String id,
	String message,
	
	@JsonProperty("created_time")
	OffsetDateTime createdTime,
	
	FromDto from
) {
}
