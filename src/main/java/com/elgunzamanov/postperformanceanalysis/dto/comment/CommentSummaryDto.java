package com.elgunzamanov.postperformanceanalysis.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentSummaryDto(
	String order,
	
	@JsonProperty("total_count")
	int totalCount,
	
	@JsonProperty("can_comment")
	boolean canComment
) {
}
