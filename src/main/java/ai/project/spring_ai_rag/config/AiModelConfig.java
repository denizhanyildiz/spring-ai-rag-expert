package ai.project.spring_ai_rag.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Both the OpenAI and Ollama starters register a ChatModel and an EmbeddingModel.
 * Ollama is marked primary so auto-configurations that need a single bean (e.g. the Milvus vector store) pick it.
 */
@Configuration
public class AiModelConfig {

    @Bean
    @Primary
    public EmbeddingModel primaryEmbeddingModel(OllamaEmbeddingModel ollamaEmbeddingModel) {
        return ollamaEmbeddingModel;
    }

    @Bean
    @Primary
    public ChatModel primaryChatModel(OllamaChatModel ollamaChatModel) {
        return ollamaChatModel;
    }
}
