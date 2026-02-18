# 🚀 Alex Morgan — Developer Portfolio

A **premium, full-stack personal portfolio** with an integrated **AI Agent Chatbot** that represents Alex as a digital persona.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────┐
│                  PORTFOLIO SYSTEM                    │
│                                                     │
│  ┌───────────────┐   REST API   ┌────────────────┐  │
│  │   Angular 17  │ ──────────► │  Spring Boot   │  │
│  │   Frontend    │ ◄────────── │    Backend     │  │
│  │  (Port 4200)  │             │  (Port 8080)   │  │
│  └───────────────┘             └────────┬───────┘  │
│                                         │           │
│                               ┌─────────▼──────┐   │
│                               │  PostgreSQL DB  │   │
│                               │   (Port 5432)   │   │
│                               └────────────────┘   │
│                                         │           │
│                               ┌─────────▼──────┐   │
│                               │  Claude API     │   │
│                               │ (Anthropic)     │   │
│                               └────────────────┘   │
└─────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
portfolio/
├── portfolio-frontend/          # Angular 17 app
│   ├── src/app/
│   │   ├── core/services/       # API services
│   │   ├── pages/               # All route pages
│   │   │   ├── home/
│   │   │   ├── about/
│   │   │   ├── experience/
│   │   │   ├── projects/
│   │   │   ├── skills/
│   │   │   ├── achievements/
│   │   │   ├── contact/
│   │   │   └── chat/            ← AI Chatbot
│   │   └── shared/
│   └── Dockerfile
│
├── portfolio-backend/           # Spring Boot (Gradle)
│   ├── src/main/java/com/alexmorgan/portfolio/
│   │   ├── controller/          # REST Controllers
│   │   ├── service/             # Business Logic
│   │   ├── model/               # JPA Entities
│   │   ├── repository/          # Spring Data JPA
│   │   ├── dto/                 # Request/Response DTOs
│   │   ├── config/              # CORS, WebClient config
│   │   └── exception/           # Global error handling
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── db/migration/        # Flyway SQL migrations
│   ├── build.gradle
│   └── Dockerfile
│
├── docker-compose.yml
└── README.md
```

---

## ⚡ Quick Start (Local Development)

### Prerequisites
- Java 17+
- Node.js 18+
- Angular CLI 17+
- PostgreSQL 15+
- Gradle 8+

### 1. Clone & Configure

```bash
git clone https://github.com/yourusername/portfolio.git
cd portfolio
```

### 2. Setup PostgreSQL
```sql
CREATE DATABASE portfolio_db;
```

### 3. Configure Backend
Edit `portfolio-backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/portfolio_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_DB_PASSWORD
anthropic.api.key=YOUR_ANTHROPIC_API_KEY
```

### 4. Run Backend
```bash
cd portfolio-backend
./gradlew bootRun
```
Backend runs at: `http://localhost:8080`
Swagger UI: `http://localhost:8080/swagger-ui.html`

### 5. Run Frontend
```bash
cd portfolio-frontend
npm install
ng serve
```
Frontend runs at: `http://localhost:4200`

---

## 🐳 Docker Deployment

```bash
# Set environment variables
export DB_PASSWORD=yourpassword
export ANTHROPIC_API_KEY=your_api_key
export FRONTEND_URL=https://yourdomain.com
export OWNER_EMAIL=alex@example.com

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f backend
```

---

## 🌐 REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/chat` | Send message to AI Agent |
| GET | `/api/v1/chat/health` | Check chat service health |
| POST | `/api/v1/contact` | Submit contact form |

### Chat API Request
```json
POST /api/v1/chat
{
  "message": "What is your tech stack?",
  "history": [
    { "role": "user", "content": "Hello!" },
    { "role": "assistant", "content": "Hey there! How can I help?" }
  ]
}
```

### Chat API Response
```json
{
  "message": "My main stack is Java + Spring Boot...",
  "role": "assistant",
  "success": true
}
```

---

## 🤖 AI Chatbot Architecture

The chatbot is powered by **Anthropic's Claude API** via a Spring Boot proxy:

1. **Angular** sends message + conversation history to `/api/v1/chat`
2. **Spring Boot** appends Alex's personality system prompt
3. Request is forwarded to **Claude API** (claude-sonnet-4-20250514)
4. Response is returned to Angular and displayed

The system prompt defines Alex's personality, skills, experience, and communication style — making the AI feel like a real digital twin.

---

## 🔒 Security

- CORS configured for specific origins only
- Input validation on all endpoints
- No secrets in source code (use environment variables)
- API key handled server-side (never exposed to frontend)

---

## 📦 Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | Angular 17, TypeScript, SCSS |
| Backend | Spring Boot 3.3, Java 17, Gradle |
| Database | PostgreSQL 15 |
| ORM | Hibernate / Spring Data JPA |
| Migrations | Flyway |
| AI | Anthropic Claude API |
| HTTP Client | Spring WebFlux WebClient |
| Docs | Springdoc OpenAPI / Swagger |
| Containerization | Docker + Docker Compose |

---

## 🚀 Production Deployment

Recommended deployment options:
- **Railway** (simplest — push to deploy)
- **Render** (free tier available)
- **AWS** (EC2 + RDS + S3 for frontend)
- **DigitalOcean** App Platform
- **Vercel** (frontend) + **Railway** (backend)

---

## 📄 License

MIT © Alex Morgan
