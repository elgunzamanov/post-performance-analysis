package com.elgunzamanov.postperformanceanalysis.dto;

import java.time.DayOfWeek;

public record TimeSlotStats(
	DayOfWeek day,
	int hour,
	long totalEngagement,
	int postCount
) {
}
