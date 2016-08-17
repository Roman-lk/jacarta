package ru.lrp.jacarta.tlv;

/**
 *
 * @author lrp
 */
public class PrintableStringTLV extends BerTLV{

    public PrintableStringTLV(String value) {
        super(Tag.PRINTABLE_STRING, value.getBytes());
    }
    
}
