package com.alexmorgan.portfolio.controller;

import com.alexmorgan.portfolio.dto.ChatRequest;
import com.alexmorgan.portfolio.dto.ChatResponse;
import com.alexmorgan.portfolio.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "AI Chat", description = "Alex's AI Agent chatbot powered by Claude")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    @Operation(summary = "Send a message to Alex's AI Agent",
               description = "Returns a personality-driven response about Alex's background, skills, and experience")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.debug("Chat request received: {}", request.getMessage());
        ChatResponse response = chatService.chat(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Check chat service health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chat service is running!");
    }
}
