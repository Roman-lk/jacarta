package ru.lrp.jacarta.tlv;

import java.nio.charset.Charset;

/**
 *
 * @author lrp
 * Строка, символ кодируется 2 байтами
 */
public class BMPStringTLV extends BerTLV{

    public BMPStringTLV(String value) {
        super(Tag.BMPSTRING, value.getBytes(Charset.forName("UTF-16BE")));
    }
    
}
