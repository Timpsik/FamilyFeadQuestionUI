package ee.mikk.familyfeudquestionsui.exception;

public class InvalidQuestionException extends Exception{

    public InvalidQuestionException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public InvalidQuestionException(String errorMessage) {
        super(errorMessage);
    }
}
