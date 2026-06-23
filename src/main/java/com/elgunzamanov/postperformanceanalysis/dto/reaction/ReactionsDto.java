package com.elgunzamanov.postperformanceanalysis.dto.reaction;

import java.util.List;

public record ReactionsDto(
	List<ReactionDto> data,
	ReactionSummaryDto summary
) {
}
