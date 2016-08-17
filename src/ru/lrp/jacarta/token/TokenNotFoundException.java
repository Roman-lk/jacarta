package ru.lrp.jacarta.token;

/**
 *
 * @author lrp
 * Исключение. При открытии сессии не был найден ни один криптотокен
 */
public class TokenNotFoundException extends Exception{

    public TokenNotFoundException() {
    }

    public TokenNotFoundException(String message) {
        super(message);
    }

    public TokenNotFoundException(Throwable cause) {
        super(cause);
    }
    
    public TokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
