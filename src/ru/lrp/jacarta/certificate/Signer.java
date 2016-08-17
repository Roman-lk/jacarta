package ru.lrp.jacarta.certificate;

/**
 * Интерфейс механизма построения подписи запроса на сертификат
 * @author lrp
 */
public interface Signer {
    
    /**
     * Получить название алгоритма подписи
     * @return OID в виде строки с
     */
    String getAlgorithm();
    
    /**
     * Подписать RequestInfo
     * @param requestInfo информация о запросе на сертификат
     * @return подпись
     */
    byte[] sign(byte[] requestInfo);
    
}
