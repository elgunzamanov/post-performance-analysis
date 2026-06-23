package com.elgunzamanov.postperformanceanalysis.dto.reaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReactionSummaryDto(
	@JsonProperty("total_count")
	int totalCount,
	
	@JsonProperty("viewer_reaction")
	String viewerReaction
) {
}
