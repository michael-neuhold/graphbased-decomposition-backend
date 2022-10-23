package monolith2microservice.logic.decomposition.engine.impl.sc.tfidf;

import java.util.List;

/**
 * Break text into tokens
 */
public interface Tokenizer {
    List<String> tokenize(String text);
}
