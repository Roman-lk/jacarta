package ru.lrp.jacarta.tlv;

/**
 *
 * @author lrp
 * При создании tlv-объекта произошла ошибка.
 * Например, при построении ObjectIdentifierTLV нередан не валидный OID
 */
public class TLVCreatingException extends Exception{

    public TLVCreatingException(String message) {
        super(message);
    }

    public TLVCreatingException(Throwable cause) {
        super(cause);
    }
    
}
