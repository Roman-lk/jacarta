package ru.lrp.jacarta.tlv;

/**
 *
 * @author lrp
 * Null-объект
 */
public class NullTLV extends BerTLV{

    public NullTLV() {
        super(Tag.NULL, new byte[]{});
    }
    
}
