package ai.project.spring_ai_rag.service;

import ai.project.spring_ai_rag.model.AiProvider;
import ai.project.spring_ai_rag.model.Answer;
import ai.project.spring_ai_rag.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OpenAIServiceImpl implements AiService {

    private final OpenAiChatModel chatModel;
    private final VectorStore simpleVectorStore;

    @Value("classpath:/templates/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    @Value("classpath:/templates/system-message.st")
    private Resource systemMessageTemplate;

    @Override
    public boolean isSupported(AiProvider provider) {
        return provider == AiProvider.OPENAI;
    }

    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate systemMessagePromptTemplate = new PromptTemplate(systemMessageTemplate);
        Message systemMessage = systemMessagePromptTemplate.createMessage();

        List<Document> documents = simpleVectorStore.similaritySearch(SearchRequest.builder()
                .query(question.question())
                .topK(5)
                .build());
        List<String> contentList = documents.stream().map(Document::getContent).toList();

        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Message userMessage = promptTemplate.createMessage(Map.of("input", question.question(), "documents",
                String.join("\n", contentList)));


        ChatResponse response = chatModel.call(new Prompt(List.of(systemMessage, userMessage)));

        return new Answer(response.getResult().getOutput().getContent());
    }
}