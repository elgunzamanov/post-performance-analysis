package com.elgunzamanov.postperformanceanalysis.controller;

import com.elgunzamanov.postperformanceanalysis.service.AnalyticsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AnalysisController {
	private final AnalyticsService analyticsService;
	
	@GetMapping("/")
	public String showDashboard(@NonNull Model model) {
		model.addAttribute("dashboard", analyticsService.getDashboardData());
		return "index";
	}
}
