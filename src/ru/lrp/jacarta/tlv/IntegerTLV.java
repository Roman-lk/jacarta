package ru.lrp.jacarta.tlv;

/**
 *
 * @author lrp
 * Целое число от 0 до 0xFFFFFFFF
 */
public class IntegerTLV extends BerTLV{

    public IntegerTLV(int integer) {
        super(Tag.INTEGER, TlvUtils.integerToByteArray(integer));
    }
    
    
}
