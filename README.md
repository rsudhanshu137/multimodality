# Multimodality

A Spring Boot REST API demonstrating multimodal AI capabilities using **Spring AI** and **OpenAI** — converting between speech, text, and images through simple HTTP endpoints.

## What it does

| Capability | Endpoint | Description |
|---|---|---|
| Speech → Text | `GET /api/multimodality/audio-to-text` | Transcribes a bundled audio sample to text using OpenAI's transcription model |
| Text → Speech | `GET /api/multimodality/text-to-audio?text={text}` | Converts input text into spoken audio (MP3), saved to disk |
| Text → Image | `GET /api/multimodality/text-to-image?prompt={prompt}` | Generates an image from a text prompt and returns it as a PNG |

## Tech stack

- **Java 21**
- **Spring Boot 4** (Spring Web MVC)
- **Spring AI 2.0.1** — abstraction layer over OpenAI's transcription, text-to-speech, and image generation models
- **OpenAI API** — `gpt-image-1` family for image generation, Whisper-based model for transcription, TTS model for speech synthesis
- **springdoc-openapi / Swagger UI** — interactive API documentation

## Project structure

```
src/main/java/com/javatechie/
├── MultimodalityApplication.java      # Spring Boot entry point
├── controller/
│   └── MultiModalityController.java   # REST endpoints
└── service/
    └── MultiModalityService.java      # Business logic calling Spring AI models
```

## Getting started

### Prerequisites

- Java 21+
- Maven (or use the bundled `./mvnw` wrapper)
- An [OpenAI API key](https://platform.openai.com/api-keys)

### Configuration

Set your OpenAI API key as an environment variable before running the app:

```bash
export OPENAI_API_KEY=your-api-key-here
```

This is read in `src/main/resources/application.yaml`:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      timeout: 90s
```

### Run

```bash
./mvnw spring-boot:run
```

The app starts on **http://localhost:9292**.

### Try it out

Interactive Swagger UI:

```
http://localhost:9292/swagger-ui/index.html
```

Or call the endpoints directly, e.g.:

```bash
curl "http://localhost:9292/api/multimodality/text-to-image?prompt=a%20man%20on%20a%20train"
curl "http://localhost:9292/api/multimodality/text-to-audio?text=Hello%20world"
curl "http://localhost:9292/api/multimodality/audio-to-text"
```

## Notes

- Image generation uses OpenAI's `gpt-image-1` model family, which returns base64-encoded images (no hosted URL) — the service decodes this server-side and serves it as a PNG.
- Generated audio/image artifacts are written to the project's working directory at runtime and are excluded from version control.
