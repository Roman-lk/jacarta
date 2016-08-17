package ru.lrp.jacarta.token.key;

/**
 *
 * @author lrp
 * Дескриптор ключевой пары, сгенерированной токеном
 */
public class KeyPair {

    /**
     * Дескриптор публичного ключа
     */
    public static class PublicKey extends Key {

        public PublicKey(int id, byte[] value) {
            super(id, value);
        }
        
        public byte[] getValue() {
            return value;
        }
    }
    
    /**
     * Дескриптор приватного ключа
     */
    public static class PrivateKey extends Key {

        public PrivateKey(int id) {
            super(id, null);
        }

    }

    private final PublicKey publicKey;

    private final PrivateKey privateKey;

    public KeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
    
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    
    

}
