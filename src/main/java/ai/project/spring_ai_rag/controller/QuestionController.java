package ai.project.spring_ai_rag.controller;

import ai.project.spring_ai_rag.model.AiProvider;
import ai.project.spring_ai_rag.model.Answer;
import ai.project.spring_ai_rag.model.Question;
import ai.project.spring_ai_rag.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    private AiService getService(AiProvider provider) {
        return aiServices.stream()
                .filter(service -> service.isSupported(provider))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No AiService supports " + provider));
    }

}
