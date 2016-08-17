package ru.lrp.jacarta.token;

import ru.lrp.jacarta.token.key.KeyPair;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import ru.lrp.jacarta.token.key.KeyPair.PrivateKey;
import ru.lrp.jacarta.token.key.KeyPair.PublicKey;
import ru.lrp.jacarta.utils.BU;

/**
 * Апи по работе с токеном. Все взаимодействие ведется через Java SmartCardIO.
 * 
 *
 * @author lrp
 */
public final class JaCartaToken implements Token{
    /**
     * Максимальный размер данных которые можно отправить в одной команде
     */
    private static final int MAX_LEN = 32766;

    /**
     * Канал связи с чипом карты/токеном
     */
    private final CardChannel channel;

    public JaCartaToken(CardChannel channel) throws TokenNotFoundException {
        this.channel = channel;
    }

    /**
     * Перейти в режим пользователя
     * @param pin пин-код пользователя
     * @throws TokenOperationException на любом этапе выполнения команды чип 
     * ответил отрицательным статусом(отличным от 0x9000)
     */
    @Override
    public void login(String pin) throws TokenOperationException {
        if (pin == null || pin.isEmpty()) {
            throw new IllegalArgumentException("Pin is not set");
        }
        int len = 2 + pin.length();
        String cmd = "80 10 20";
        cmd += String.format("%02X", len >> 8);    // длина данных
        cmd += String.format("%02X", len);
        cmd += "01";                               // режим пользователя 
        cmd += String.format("%02X", pin.length());// длина пин-кода
        cmd += BU.toHexString(pin.getBytes());     // пин-код
        cmd += "00";
        executeCommand("Login", BU.convertFromHex(cmd));
    }

    /**
     * Получить случайную байтовую послеовательность
     * @param len длина последовательности
     * @return байтовая последовательность
     * @throws TokenOperationException на любом этапе выполнения команды чип 
     * ответил отрицательным статусом(отличным от 0x9000)
     */
    @Override
    public byte[] getRandomData(int len) throws TokenOperationException {
        if (len <= 0 || len > 0xFFFF) {
            throw new IllegalArgumentException("Length is wrong");
        }
        byte[] cmd = BU.convertFromHex("80 14 40 00 02 00 09 00");
        cmd[5] = BU.c(len >> 8);
        cmd[6] = BU.c(len);
        return executeCommand("Random", cmd);
    }

    /**
     * Генерировать ключевую пару
     * @return объект KeyPair, содержащий идентификаторы каждого из ключей
     * и значение публичного ключа
     * @throws TokenOperationException 
     */
    @Override
    public KeyPair generateKeyPair() throws TokenOperationException {
        //генерируем пару ключей
        byte[] cmd = BU.convertFromHex("80 11 20 00 01 01 04");
        byte[] result = executeCommand("Generate key pair", cmd);
        int pubId = (result[2] << 8) | result[3];
        int privId = (result[0] << 8) | result[1];

        // получаем значение атрибута 00 00 00 11 для публичного ключа
        // в команде используется обратная запись идентификатора ключа
        cmd = BU.convertFromHex("80 13 20 00 06 FF FF 11 00 00 00");
        cmd[5] = result[2];   // устанавливаем номер публичного ключа, ...
        cmd[6] = result[3];   // ... который получили при генерации
        result = executeCommand("Get key attribute", cmd);
        byte[] keyValue = new byte[64];
        System.arraycopy(result, 8, keyValue, 0, keyValue.length);

        return new KeyPair(new PublicKey(pubId, keyValue), new PrivateKey(privId));
    }

    /**
     * Хеширование данных любого объема
     * @param data хешируемые данные
     * @return значение хеш-суммы
     * @throws TokenOperationException 
     */
    @Override
    public byte[] hash(byte[] data) throws TokenOperationException {
        byte[] cmd = BU.convertFromHex("80 14 31 00 00 01");
        byte[] result = executeCommand("Start hash", cmd);
        byte transactionId = result[0];

        for (int i = 0; i < data.length / MAX_LEN + 1; i++) {
            int len = Math.min(MAX_LEN, data.length - i * MAX_LEN);
            byte[] part = new byte[len];
            System.arraycopy(data, i * 32766, part, 0, len);
            String continueCmd = "80 14 32 00 00";
            len++;
            continueCmd += String.format(" %02X", (len >> 8) & 0xFF);
            continueCmd += String.format(" %02X", len & 0xFF);
            continueCmd += String.format(" %02X", transactionId);
            continueCmd += " " + BU.toHexString(part);
            executeCommand("Continue hash", BU.convertFromHex(continueCmd));
        }

        cmd = BU.convertFromHex("80 14 33 00 01 " + String.format("%02X", transactionId) + " 20");
        return executeCommand("Start hash", cmd);
    }

    /**
     * Получить подпись данных
     * @param key ключ которым будут подписаныданные
     * @param data данные
     * @return 32-байтная подпись
     * @throws TokenOperationException 
     */
    @Override
    public byte[] sign(PrivateKey key, byte[] data) throws TokenOperationException {
        if (key == null) {
            throw  new IllegalArgumentException("Key not set");
        }
        if (data == null) {
            throw  new IllegalArgumentException("Data not set");
        }
        int keyId = key.getId();
        byte[] hash = hash(data);
        String cmd = "80 14 10 00 22";
        cmd += String.format(" %02X", (keyId >> 8) & 0xFF);
        cmd += String.format(" %02X", keyId & 0xFF);
        cmd += " " + BU.toHexString(hash);
        cmd += " 40";
        return executeCommand("Sign data", BU.convertFromHex(cmd));
    }

    /**
     * Проверить подпись
     * @param key публичный ключ, соответствующий ключу, которым были подписаны данные
     * @param data данные 
     * @param sign подпись
     * @return true - подпись верна
     * @throws TokenOperationException 
     */
    @Override
    public boolean verify(PublicKey key, byte[] data, byte[] sign) throws TokenOperationException {
        if (key == null) {
            throw  new IllegalArgumentException("Key not set");
        }
        if (data == null) {
            throw  new IllegalArgumentException("Data not set");
        }
        if (sign == null) {
            throw  new IllegalArgumentException("Sign not set");
        }
        int keyId = key.getId();
        byte[] hash = hash(data);
        String cmd = "80 14 20 00 62";
        cmd += String.format(" %02X", (keyId >> 8) & 0xFF);
        cmd += String.format(" %02X", keyId & 0xFF);
        cmd += " " + BU.toHexString(hash);
        cmd += " " + BU.toHexString(sign);
        try {
            CommandAPDU apduCmd = CardUtils.createCommandAPDU(BU.convertFromHex(cmd));
            ResponseAPDU res;
            res = channel.transmit(apduCmd);
            int errorCode = res.getSW();
            return (errorCode == 0x9000);
        } catch (CardException ex) {
            throw new TokenOperationException(ex);
        }
    }

    /**
     * Загрузить сертификат 
     * @param certificate последовательность байт, которая является сертификатом
     * @throws TokenOperationException 
     */
    @Override
    public void loadCert(byte[] certificate) throws TokenOperationException {
        if (certificate == null || certificate.length == 0) {
            throw new IllegalArgumentException("Certificate not set");
        }
        byte[] cmd = BU.convertFromHex("80 12 20 00 01 03 02");
        byte[] res = executeCommand("Create certificate", cmd);
    
        
        int len = certificate.length + 8;
        System.out.println("Len: " + len);
        String loadCmd ="80 13 30 00 00";
        loadCmd += String.format(" %02X", (len >> 8) & 0xFF);
        loadCmd += String.format(" %02X", len & 0xFF);
        loadCmd += String.format(" %02X", res[0]);
        loadCmd += String.format(" %02X", res[1]);
        loadCmd += "11 00 00 00"; //CKA_VALUE
        loadCmd += String.format(" %02X", (certificate.length >> 8) & 0xFF);
        loadCmd += String.format(" %02X", certificate.length & 0xFF);
        loadCmd += " " + BU.toHexString(certificate);
        
        System.out.println("LOAD: " + loadCmd);
        
        // ToDo: тут же нужно грузить другие атрибуты
        
        executeCommand("Load certificate", BU.convertFromHex(loadCmd));
    }
    
    /**
     * Выполнить APDU команду
     * @param name название команды
     * @param command команда
     * @return полученные данные, если чип ответил 0x9000
     * @throws TokenOperationException чип ответил статусом ошибки
     */
    private byte[] executeCommand(String name, byte[] command) throws TokenOperationException {
        try {
            CommandAPDU apduCmd = CardUtils.createCommandAPDU(command);
            ResponseAPDU res = channel.transmit(apduCmd);
            int errorCode = res.getSW();
            if (errorCode != 0x9000) {
                throw new TokenOperationException(name + ". Error: " + Integer.toHexString(errorCode));
            }
            return res.getData();
        } catch (CardException ex) {
            throw new TokenOperationException(ex);
        }
    }

}
