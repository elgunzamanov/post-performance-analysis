package com.elgunzamanov.postperformanceanalysis.dto;

import com.elgunzamanov.postperformanceanalysis.dto.comment.CommentsDto;
import com.elgunzamanov.postperformanceanalysis.dto.reaction.ReactionsDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record PostDto(
	String id,
	String message,
	@JsonProperty("created_time") OffsetDateTime createdTime,
	ReactionsDto reactions,
	CommentsDto comments
) {
}
