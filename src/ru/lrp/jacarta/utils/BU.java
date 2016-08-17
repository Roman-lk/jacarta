package ru.lrp.jacarta.utils;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author lrp
 * Инструмент по работе с байтовыми последовательностями
 */
public final class BU {

    private BU() {
    }
    
    public static byte[] convertFromHex(String str) {
        str = str.replace(" ", "");
        
        if (str.length() % 2 != 0) {
            str = str.substring(0, str.length() - 1) + "0" +  str.charAt(str.length() - 1);
        }
        int len = str.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4)
                                 + Character.digit(str.charAt(i+1), 16));
        }
        return data;
    }
    
    public static byte[] concatenate (byte[] a, byte... b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c,  a.length, b.length);
        return c;
    }
    
    public static String toHexString(byte... bytes) {
        return DatatypeConverter.printHexBinary(bytes).replaceAll("(..)", "$1 ");
    }
    
    public static int c(byte b) {
        return b & 0xff;
    }

    public static byte c(int i) {
        return (byte)(i & 0xff);
    }
    
}
