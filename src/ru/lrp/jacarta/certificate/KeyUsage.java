package ru.lrp.jacarta.certificate;


/**
 *
 * @author lrp
 * Способы использования ключа 
 */
public enum KeyUsage {
    
    ENCIPHER_ONLY     (1),  
    CRLSIGN           (2),  
    KEY_CERTSIGN      (4),  
    KEY_AGREEMENT     (8),  
    DATA_ENCIPHERMENT (16),  
    KEY_ENCIPHERMENT  (32),  
    NON_REPUDIATION   (64),  
    DIGITAL_SIGNATURE (128),  
    DECIPHER_ONLY     (32768);
    
    private final int value;

    private KeyUsage(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    
    
}
