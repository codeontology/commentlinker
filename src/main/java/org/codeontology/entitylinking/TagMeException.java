package org.codeontology.entitylinking;


public class TagMeException extends Exception {
    public TagMeException() {
    }

    public TagMeException(String message) {
        super(message);
    }

    public TagMeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagMeException(Throwable cause) {
        super(cause);
    }

    public TagMeException(int code, String message) {
        this("HTTP Status Code: " + code + " - " + message);
    }
}
