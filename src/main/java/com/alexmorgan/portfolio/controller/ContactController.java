package com.alexmorgan.portfolio.controller;

import com.alexmorgan.portfolio.dto.ContactRequest;
import com.alexmorgan.portfolio.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
@Tag(name = "Contact", description = "Portfolio contact form endpoint")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @Operation(summary = "Submit a contact message")
    public ResponseEntity<Map<String, String>> contact(@Valid @RequestBody ContactRequest request) {
        log.info("Contact form submitted by: {}", request.getEmail());
        contactService.processContactMessage(request);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Your message has been received! Alex will get back to you within 24 hours."
        ));
    }
}
