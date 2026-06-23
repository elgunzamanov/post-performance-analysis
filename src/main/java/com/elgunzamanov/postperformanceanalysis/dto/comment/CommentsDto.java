package com.elgunzamanov.postperformanceanalysis.dto.comment;

import java.util.List;

public record CommentsDto(
	List<CommentDto> data,
	CommentSummaryDto summary
) {
}
