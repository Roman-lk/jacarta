package ru.lrp.jacarta.tlv;

/**
 *
 * @author lrp
 */
public class OctetStringTLV extends BerTLV{

    public OctetStringTLV(String value) {
        super(Tag.OCTET_STRING, value.getBytes());
    }
    
}
