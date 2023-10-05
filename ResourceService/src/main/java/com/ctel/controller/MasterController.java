package com.ctel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MasterController {

	@GetMapping("/api/get")
	public ResponseEntity<?> getData() {
		return ResponseEntity.ok("Data fetched successfully");
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getmessage() {
		return ResponseEntity.ok("with out security......");
	}
	
	
}
