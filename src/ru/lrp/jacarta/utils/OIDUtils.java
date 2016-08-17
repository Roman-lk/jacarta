package ru.lrp.jacarta.utils;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

/**
 *
 * @author lrp
 * Инструмент конвертации OID
 */
public final class OIDUtils {

    private OIDUtils() {
    }
    
    public static byte[] oidStringToByteArray(String oidString) {
        try {
            Oid oid = new Oid(oidString);
            return oid.getDER();
        } catch (GSSException ex) {
            return null;
        }
    }
    
    public static String oidByteArrayToString(byte[] oidArray) {
        try {
            Oid oid = new Oid(oidArray);
            return oid.toString();
        } catch (GSSException ex) {
            return null;
        }
    }
    
}
