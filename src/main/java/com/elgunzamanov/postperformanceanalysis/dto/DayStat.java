package com.elgunzamanov.postperformanceanalysis.dto;

public record DayStat(
	String day,
	long count,
	int percentage,
	boolean isPeak
) {
}
