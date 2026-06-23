package com.elgunzamanov.postperformanceanalysis.dto;

import java.util.List;

public record MetaGraphResponse<T>(
	List<T> data
) {
}
