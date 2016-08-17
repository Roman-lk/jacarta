package ru.lrp.jacarta.token;

/**
 *
 * @author lrp
 * Исключение. Во время выполнения Apdu-команд был получен
 * негативный ответ(отличный от 0x9000). Не правильно с точки зрения ISO7816,
 * но для jaCarta работает
 */
public class TokenOperationException extends Exception{

    public TokenOperationException() {
    }

    public TokenOperationException(String message) {
        super(message);
    }

    public TokenOperationException(Throwable cause) {
        super(cause);
    }
    
    public TokenOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
