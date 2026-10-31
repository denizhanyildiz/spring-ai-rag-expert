package ai.project.spring_ai_rag.service;

import ai.project.spring_ai_rag.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OllamaServiceImpl implements AiService {

    private final OllamaChatModel chatModel;
    private final VectorStore vectorStore;
    private final WeatherServiceFunction weatherServiceFunction;

    @Value("classpath:/templates/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    @Value("classpath:/templates/system-message.st")
    private Resource systemMessageTemplate;

    @Value("classpath:/templates/weather-system-message.st")
    private Resource weatherSystemMessageTemplate;

    @Override
    public boolean isSupported(AiProvider provider) {
        return provider == AiProvider.OLLAMA;
    }

    @Override
    public Answer getWeather(Question question) {
        var promptOptions = OllamaOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
                        .function("CurrentWeather", weatherServiceFunction)
                        .description("Get the current weather for a location given by its latitude and longitude")
                        .inputType(WeatherRequest.class)
                        .responseConverter( response -> {
                            String schema = ModelOptionsUtils.getJsonSchema((Type) WeatherResponse.class, false);
                            String json = ModelOptionsUtils.toJsonString(response);
                            return schema + "\n" + json;
                        })
                        .build()))
                .build();

        PromptTemplate systemMessagePromptTemplate = new PromptTemplate(weatherSystemMessageTemplate);
        Message systemMessage = systemMessagePromptTemplate.createMessage();
        Message userMessage = new PromptTemplate(question.question()).createMessage();

        ChatResponse response = chatModel.call(new Prompt(List.of(systemMessage, userMessage), promptOptions));

        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public byte[] getImage(Question question) {
        throw new UnsupportedOperationException("Image is not supported by the OpenAI service");
    }

    @Override
    public String getDescription(MultipartFile file) {
        throw new UnsupportedOperationException("Vision is not supported by the OpenAI service");
    }

    @Override
    public byte[] getSpeech(Question question) {
        throw new UnsupportedOperationException("Talk is not supported by the OpenAI service");
    }

    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate systemMessagePromptTemplate = new PromptTemplate(systemMessageTemplate);
        Message systemMessage = systemMessagePromptTemplate.createMessage();

        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                .query(question.question())
                .topK(5)
                .build());
        List<String> contentList = documents.stream().map(Document::getContent).toList();

        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Message userMessage = promptTemplate.createMessage(
                Map.of(
                        "input", question.question(),
                        "documents", String.join("\n", contentList),
                        "language", question.language()
                ));

        ChatResponse response = chatModel.call(new Prompt(List.of(systemMessage, userMessage)));

        return new Answer(response.getResult().getOutput().getContent());
    }
}
