package ai.project.spring_ai_rag.service;

import ai.project.spring_ai_rag.model.Answer;
import ai.project.spring_ai_rag.model.Question;

public interface OpenAIService {

    Answer getAnswer(Question question);
}