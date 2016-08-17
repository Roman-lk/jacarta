package ru.lrp.jacarta.tlv;

import java.nio.charset.Charset;

/**
 *
 * @author lrp
 * Строка, которая хранит символы вкодировке ASCII
 */
public class IA5StringTLV extends BerTLV{

    public IA5StringTLV(String value) {
        super(Tag.PRINTABLE_STRING, value.getBytes(Charset.forName("ASCII")));
    }
    
}
