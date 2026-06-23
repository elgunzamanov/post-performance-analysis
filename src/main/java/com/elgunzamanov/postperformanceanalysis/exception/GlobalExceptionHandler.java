package com.elgunzamanov.postperformanceanalysis.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MetaGraphApiException.class)
	public String handleMetaGraphApiException(MetaGraphApiException ex, Model model) {
		model.addAttribute("error", ex.getMessage());
		return "error";
	}
}
