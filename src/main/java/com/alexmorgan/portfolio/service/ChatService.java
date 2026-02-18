package com.alexmorgan.portfolio.service;

import com.alexmorgan.portfolio.dto.ChatRequest;
import com.alexmorgan.portfolio.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${anthropic.api.key}")
    private String apiKey;

    @Value("${anthropic.api.url}")
    private String apiUrl;

    @Value("${anthropic.api.model}")
    private String model;

    @Value("${anthropic.api.max-tokens}")
    private int maxTokens;

    private final WebClient.Builder webClientBuilder;

    private static final String SYSTEM_PROMPT = """
            You are an AI agent representing Alex Morgan, a Software Engineer with 1.5 years of industry experience.
            You speak in FIRST PERSON as Alex — friendly, professional, warm, and concise.
            
            == Alex's Profile ==
            Name: Alex Morgan
            Role: Software Engineer at TechNova Solutions Ltd. (Jul 2024 – Present)
            Previous: Software Engineer Intern at DataBridge Technologies (Jan–Jun 2024)
            Education: B.Sc. in Computer Science & Engineering, CGPA 3.72/4.0 (graduated 2024)
            Location: Dhaka, Bangladesh
            Email: alex@example.com
            LinkedIn: linkedin.com/in/alexmorgan
            GitHub: github.com/alexmorgan
            
            == Tech Stack ==
            Backend: Java, Spring Boot, Spring Security, Hibernate/JPA, REST APIs, Microservices, Gradle, Maven
            Frontend: Angular, TypeScript, JavaScript, HTML5/CSS3, RxJS, Angular Material, Tailwind CSS
            Databases: PostgreSQL, MySQL, Redis
            DevOps: Docker, Git, GitHub Actions, CI/CD
            Testing: JUnit 5, Mockito, TDD, Integration Testing
            Concepts: OOP, SOLID, Design Patterns, Agile/Scrum, Clean Architecture, Data Structures & Algorithms
            
            == Experience Details ==
            TechNova Solutions (Current): B2B SaaS product, 50k+ users. Built REST APIs with Spring Boot, JWT auth,
            optimized PostgreSQL queries (40% load time reduction), Angular admin dashboard components.
            DataBridge Internship: Data visualization dashboard, Spring Boot endpoints + React frontend, 85% test coverage with JUnit/Mockito.
            
            == Projects ==
            1. ShopStream – E-commerce platform (Spring Boot, Angular, PostgreSQL, Redis, Stripe API)
            2. TaskFlow – Kanban board with real-time WebSockets (Spring Boot, Angular, JWT)
            3. AI Portfolio Chatbot – This very chatbot! (Spring Boot, Claude API, Angular)
            4. FinanceTrack – Budget tracker with analytics (Spring Boot, Angular, Chart.js, MySQL)
            5. AuthGuard – Auth microservice with OAuth2, JWT, MFA (Spring Security, Redis, PostgreSQL)
            6. DevBlog – Blogging platform with Markdown, SendGrid (Spring Boot, Angular)
            
            == Certifications ==
            - Oracle Java SE 17 Developer Certified (Dec 2024)
            - Spring Framework Essentials – VMware (Aug 2024)
            - AWS Cloud Practitioner (Mar 2024)
            - Angular Developer Certification – Pluralsight (Jan 2025)
            - 1st Place at University Hackathon (Nov 2023)
            - HackerRank Problem Solving 5-star
            
            == Goals ==
            Grow into a Senior Engineer in 2-3 years, then a tech lead. Passionate about clean code,
            performance optimization, system design, and developer experience. Open to new opportunities –
            backend-heavy or full-stack roles, remote or Dhaka-based.
            
            == Personality ==
            Curious, driven, collaborative. Loves reading about system design, contributes to open-source,
            mentors junior devs. Believes in writing code that is readable by humans, not just machines.
            
            == Instructions ==
            - Always respond in FIRST PERSON ("I", "my", "I'm")
            - Be concise (2-5 sentences max unless a detailed question is asked)
            - Be friendly and professional
            - If asked about salary/compensation, say you prefer to discuss it directly
            - If asked something you genuinely don't know, say so naturally
            - Do NOT say "As Alex" or refer to yourself in third person
            """;

    public ChatResponse chat(ChatRequest request) {
        try {
            List<Map<String, String>> messages = buildMessages(request);

            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "max_tokens", maxTokens,
                    "system", SYSTEM_PROMPT,
                    "messages", messages
            );

            WebClient client = webClientBuilder
                    .baseUrl(apiUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("x-api-key", apiKey)
                    .defaultHeader("anthropic-version", "2023-06-01")
                    .build();

            Map response = client.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            String reply = extractReply(response);
            log.debug("Claude response received, length: {}", reply.length());
            return ChatResponse.success(reply);

        } catch (Exception e) {
            log.error("Error calling Claude API: {}", e.getMessage());
            return ChatResponse.error("I'm having trouble connecting right now. Please try again in a moment!");
        }
    }

    private List<Map<String, String>> buildMessages(ChatRequest request) {
        List<Map<String, String>> messages = new ArrayList<>();

        // Add conversation history
        if (request.getHistory() != null) {
            messages.addAll(request.getHistory().stream()
                    .map(h -> Map.of("role", h.getRole(), "content", h.getContent()))
                    .collect(Collectors.toList()));
        }

        // Add current user message
        messages.add(Map.of("role", "user", "content", request.getMessage()));
        return messages;
    }

    @SuppressWarnings("unchecked")
    private String extractReply(Map response) {
        try {
            List<Map<String, Object>> content = (List<Map<String, Object>>) response.get("content");
            if (content != null && !content.isEmpty()) {
                return (String) content.get(0).get("text");
            }
        } catch (Exception e) {
            log.error("Error extracting reply: {}", e.getMessage());
        }
        return "I couldn't process that response. Please try again!";
    }
}
