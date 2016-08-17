package ru.lrp.jacarta.certificate;

import java.io.UnsupportedEncodingException;
import org.junit.Test;
import ru.lrp.jacarta.certificate.CerificateRequest.Builder;
import ru.lrp.jacarta.tlv.TLVCreatingException;
import ru.lrp.jacarta.tlv.TlvUtils;
import ru.lrp.jacarta.token.Token;
import ru.lrp.jacarta.token.TokenManager;
import ru.lrp.jacarta.token.TokenNotFoundException;
import ru.lrp.jacarta.token.TokenOperationException;
import ru.lrp.jacarta.token.key.KeyPair;
import ru.lrp.jacarta.utils.BU;

/**
 *
 * @author lrp
 */
public class CerificateRequestTest {

    @Test
    public void test() throws TokenNotFoundException, TokenOperationException, TLVCreatingException, UnsupportedEncodingException {
        Token token = TokenManager.openSession("0987654321");
        
        KeyPair pair = token.generateKeyPair();
        Signer signer = new GostR3410TokenSigner(token, pair.getPrivateKey());
        PublicKeyInfo keyInfo = new PublicKeyInfo("1.2.643.2.2.19", pair.getPublicKey());
        keyInfo.addAlgorithmParams("1.2.643.2.2.35.1");
        keyInfo.addAlgorithmParams("1.2.643.2.2.30.1");

        Builder builder = new Builder(1, signer, keyInfo, "\f\fJaCarta GOST");

        builder.addSubject("L", "Барнаул")
                .addSubject("SN", "Тест")
                .addSubject("G", "Тест")
                .addSubject("T", "Тест")
                .addSubject("STREET", "Тест")
                .addSubject("OU", "test")
                .addSubject("O", "test")
                .addSubject("C", "RU")
                .addSubject("E", "test@test.ts")
                .addSubject("СНИЛС", "123456789");

        builder.addKeyUsage(KeyUsage.KEY_ENCIPHERMENT)
                .addKeyUsage(KeyUsage.NON_REPUDIATION)
                .addKeyUsage(KeyUsage.DATA_ENCIPHERMENT)
                .addKeyUsage(KeyUsage.DIGITAL_SIGNATURE);

        builder.addExtendedKeyUsage("1.3.6.1.5.5.7.3.2") // Проверка подлинности клиента 
                .addExtendedKeyUsage("1.2.643.2.2.34.6") // Пользователь Центра Регистрации, HTTP, TLS клиент
                .addExtendedKeyUsage("1.2.643.5.1.28.2") // Система декларирования ФСРАР
                .addExtendedKeyUsage("1.2.643.5.1.28.3") // Система декларирования ФСРАР-розничная АП
                .addExtendedKeyUsage("1.2.643.5.1.28.4") // Система декларирования ФСРАР-лицензиат
                .addExtendedKeyUsage("1.3.6.1.5.5.7.3.4") // Защищенная электронная почта 
                .addExtendedKeyUsage("1.2.643.3.6.0.3");    // Срок действия сертификата 3 месяца

        CerificateRequest request = builder.build();
        System.out.println("" + request.getBAse64Data());
        System.out.println(BU.toHexString(request.getData()));
        TlvUtils.parseTlvStructure(request.getData());
    }
    
    Token stubToken = new Token() {

        @Override
        public void login(String pin) throws TokenOperationException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public byte[] getRandomData(int len) throws TokenOperationException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public KeyPair generateKeyPair() throws TokenOperationException {
            return new KeyPair(
                    new KeyPair.PublicKey(1, BU.convertFromHex("12345678901234567890123456789012")),
                    new KeyPair.PrivateKey(2)
            );
        }

        @Override
        public byte[] hash(byte[] data) throws TokenOperationException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public byte[] sign(KeyPair.PrivateKey key, byte[] data) throws TokenOperationException {
            return  BU.convertFromHex("1234567890123456789012345678901212345678901234567890123456789012");
        }

        @Override
        public boolean verify(KeyPair.PublicKey key, byte[] data, byte[] sign) throws TokenOperationException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void loadCert(byte[] certificate) throws TokenOperationException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };

}
