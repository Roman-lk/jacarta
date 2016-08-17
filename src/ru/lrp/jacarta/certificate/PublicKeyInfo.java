package ru.lrp.jacarta.certificate;

import java.util.ArrayList;
import java.util.List;
import ru.lrp.jacarta.token.key.KeyPair.PublicKey;

/**
 *
 * @author lrp Описание публичного ключа Используется при генерации запроса на
 * сертификат
 */
class PublicKeyInfo {

    private final String algorithmOID;

    private final List<String> algorithmParams;

    private final PublicKey publicKey;

    public PublicKeyInfo(String algorithmOID, PublicKey publicKey) {
        this.algorithmOID = algorithmOID;
        this.publicKey = publicKey;
        this.algorithmParams = new ArrayList<String>();
    }

    public PublicKeyInfo(String algorithmOID, List<String> algorithmParams, PublicKey publicKey) {
        this.algorithmOID = algorithmOID;
        this.publicKey = publicKey;
        this.algorithmParams = new ArrayList<String>();
        if (algorithmParams != null) {
            this.algorithmParams.addAll(algorithmParams);
        }
    }

    public String getAlgorithmOID() {
        return algorithmOID;
    }

    public List<String> getAlgorithmParams() {
        return algorithmParams;
    }

    public void addAlgorithmParams(String oid) {
        algorithmParams.add(oid);
    }

    public byte[] getPublicKey() {
        byte[] value = publicKey.getValue();
        byte[] data = new byte[value.length + 3];
        data[0] = (byte) ((publicKey.getId() >> 8) & 0xFF);
        data[1] = (byte) (publicKey.getId() & 0xFF);
        data[2] = (byte) (value.length & 0xFF);
        System.arraycopy(value, 0, data, 3, value.length);
        return data;
    }

}
