package ru.lrp.jacarta.tlv;

/**
 *
 * @author lrp
 * Произвольная байтовая последовательность.
 * В основном используется для хранения ключей, подписей
 */
public class BitStringTLV extends BerTLV{

    public BitStringTLV(byte[] value) {
        super(Tag.BIT_STRING, value);
    }
    
}
