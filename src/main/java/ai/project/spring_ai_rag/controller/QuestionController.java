package ai.project.spring_ai_rag.controller;

import ai.project.spring_ai_rag.model.AiProvider;
import ai.project.spring_ai_rag.model.Answer;
import ai.project.spring_ai_rag.model.Question;
import ai.project.spring_ai_rag.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final List<AiService> aiServices;

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return getService(AiProvider.OLLAMA).getAnswer(question);
    }

    @PostMapping("/weather")
    public Answer askWeather(@RequestBody Question question) {
        return getService(AiProvider.OLLAMA).getWeather(question);
    }

    @PostMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@RequestBody Question question) {
        return getService(AiProvider.OPENAI).getImage(question);
    }

    @PostMapping(value = "/vision", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> upload(@Validated @RequestParam("file") MultipartFile file, @RequestParam("name") String name) throws IOException {
        return ResponseEntity.ok(getService(AiProvider.OPENAI).getDescription(file));
    }

    private AiService getService(AiProvider provider) {
        return aiServices.stream()
                .filter(service -> service.isSupported(provider))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No AiService supports " + provider));
    }

}
