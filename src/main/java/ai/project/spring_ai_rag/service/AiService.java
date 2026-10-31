package ai.project.spring_ai_rag.service;

import ai.project.spring_ai_rag.model.AiProvider;
import ai.project.spring_ai_rag.model.Answer;
import ai.project.spring_ai_rag.model.Question;
import org.springframework.web.multipart.MultipartFile;

public interface AiService {

    boolean isSupported(AiProvider provider);

    Answer getAnswer(Question question);

    Answer getWeather(Question question);

    byte[] getImage(Question question);

    String getDescription(MultipartFile file);
}
