package ru.lrp.jacarta.certificate;

import ru.lrp.jacarta.token.Token;
import ru.lrp.jacarta.token.TokenOperationException;
import ru.lrp.jacarta.token.key.KeyPair;

/**
 * Реализация механизма подписи запроса на сертификат
 * Подпись осуществляется криптоключом
 * @author lrp
 */
public class GostR3410TokenSigner implements Signer {
    /**
     * Криптотокен
     */
    private final Token token;
    
    /**
     * Приватный ключ, который используется для подписи
     */
    private final KeyPair.PrivateKey privayeKey;

    public GostR3410TokenSigner(Token token, KeyPair.PrivateKey privayeKey) {
        this.token = token;
        this.privayeKey = privayeKey;
    }
    
    @Override
    public String getAlgorithm() {
        return "1.2.643.2.2.3"; //ГОСТ R 34.11/34.10-2001
    }

    @Override
    public byte[] sign(byte[] requestInfo) {
        try {
            byte[] sign = token.sign(privayeKey, requestInfo);
            byte[] data = new byte[sign.length + 1];
            data[0] = 0;
            System.arraycopy(sign, 0, data, 1, sign.length);
            return data;
        } catch (TokenOperationException ex) {
            return null;
        }
    }
    
}
