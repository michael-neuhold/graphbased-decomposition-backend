package monolith2microservice.logic.decomposition.engine.impl.shared.tfidf;

import java.util.List;

/**
 * Break text into tokens
 */
public interface Tokenizer {
    List<String> tokenize(String text);
}
