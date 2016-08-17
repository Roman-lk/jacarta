package ru.lrp.jacarta.tlv;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import ru.lrp.jacarta.utils.BU;

/**
 *
 * @author lrp
 * Вспомогательные методы для постоения tlv объектов
 */
public final class TlvUtils {
    
     private static Map<byte[], String> idMap;

    static {
        Map<byte[], String> map = new HashMap<byte[], String>(); 
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D"), "OBJ_rsadsi");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01,"), "OBJ_pkcs");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 02 02,"), "OBJ_md2");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 02 05,"), "OBJ_md5");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 03 04,"), "OBJ_rc4");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 01 01"), "OBJ_rsaEncryption");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 01 02"), "OBJ_md2withRSAEncryption");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 01 04"), "OBJ_md5withRSAEncryption");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 05 01"), "OBJ_pbeWithMD2AndDES_CBC");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 05 03"), "OBJ_pbeWithMD5AndDES_CBC");
        map.put(BU.convertFromHex("55,"), "OBJ_X500");
        map.put(BU.convertFromHex("55 04,"), "OBJ_X509");
        map.put(BU.convertFromHex("55 04 03,"), "OBJ_commonName");
        map.put(BU.convertFromHex("55 04 06,"), "OBJ_countryName");
        map.put(BU.convertFromHex("55 04 07,"), "OBJ_localityName");
        map.put(BU.convertFromHex("55 04 08,"), "OBJ_stateOrProvinceName");
        map.put(BU.convertFromHex("55 04 0A,"), "OBJ_organizationName");
        map.put(BU.convertFromHex("55 04 0B,"), "OBJ_organizationalUnitName");
        map.put(BU.convertFromHex("55 08 01 01,"), "OBJ_rsa");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 07,"), "OBJ_pkcs7");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 07 01"), "OBJ_pkcs7_data");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 07 02"), "OBJ_pkcs7_signed");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 07 03"), "OBJ_pkcs7_enveloped");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 07 04"), "OBJ_pkcs7_signedAndEnveloped");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 07 05"), "OBJ_pkcs7_digest");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 07 06"), "OBJ_pkcs7_encrypted");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 03,"), "OBJ_pkcs3");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 03 01"), "OBJ_dhKeyAgreement");
        map.put(BU.convertFromHex("2B 0E 03 02 06,"), "OBJ_des_ecb");
        map.put(BU.convertFromHex("2B 0E 03 02 09,"), "OBJ_des_cfb");
        map.put(BU.convertFromHex("2B 0E 03 02 07,"), "OBJ_des_cbc");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 03 11,"), "OBJ_des_ede3");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 03 02,"), "OBJ_rc2_cbc");
        map.put(BU.convertFromHex("2B 0E 03 02 12,"), "OBJ_sha");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 03 07,"), "OBJ_des_ede3_cbc");
        map.put(BU.convertFromHex("2B 0E 03 02 08,"), "OBJ_des_ofb");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 09,"), "OBJ_pkcs9");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 09 01"), "OBJ_pkcs9_emailAddress");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 09 02"), "OBJ_pkcs9_unstructuredName");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 09 03"), "OBJ_pkcs9_contentType");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 09 04"), "OBJ_pkcs9_messageDigest");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 09 05"), "OBJ_pkcs9_signingTime");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 09 06"), "OBJ_pkcs9_countersignature");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 09 07"), "OBJ_pkcs9_challengePassword");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 09 08"), "OBJ_pkcs9_unstructuredAddress");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 09 09"), "OBJ_pkcs9_extCertAttributes");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42,"), "OBJ_netscape");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42 01,"), "OBJ_netscape_cert_extension");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42 02,"), "OBJ_netscape_data_type");
        map.put(BU.convertFromHex("2B 0E 02 1A 05,"), "OBJ_sha1");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 01 05"), "OBJ_sha1WithRSAEncryption");
        map.put(BU.convertFromHex("2B 0E 03 02 0D,"), "OBJ_dsaWithSHA");
        map.put(BU.convertFromHex("2B 0E 03 02 0C,"), "OBJ_dsa");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 05 0B"), "OBJ_pbeWithSHA1AndRC2_CBC");
        map.put(BU.convertFromHex("2A 86 48 86 F7 0D 01 05 0C"), "OBJ_pbeWithSHA1AndRC4");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42 01 01"), "OBJ_netscape_cert_type");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42 01 02"), "OBJ_netscape_base_url");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42 01 03"), "OBJ_netscape_revocation_url");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42 01 04"), "OBJ_netscape_ca_revocation_url");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42 01 07"), "OBJ_netscape_renewal_url");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42 01 08"), "OBJ_netscape_ca_policy_url");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42 01 0C"), "OBJ_netscape_ssl_server_name");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42 01 0D"), "OBJ_netscape_comment");
        map.put(BU.convertFromHex("60 86 48 01 86 F8 42 02 05"), "OBJ_netscape_cert_sequence");
        idMap = map;
    }

    private TlvUtils() {
    }
    
    /**
     * Представить список tlv-объектов в виде массива байт,
     * используется при создании составных объектов
     * @param values
     * @return 
     */
    public static byte[] toByteArray(BerTLV... values) {
        if (values == null) {
            throw new IllegalStateException("Set. Values not set");
        }
        int len = 0;
        for (BerTLV tlv : values) {
            len += tlv.toByteArray().length;
        }
        byte[] result = new byte[len];
        int startPos = 0;
        for (BerTLV tlv : values) {
            byte[] tvlData = tlv.toByteArray();
            System.arraycopy(tvlData, 0, result, startPos, tvlData.length);
            startPos += tvlData.length;
        }
        return result;
    }

    /**
     * Получить значение int в виде последовательности байт.
     * Используется для постоения tlv объекта INTEGER
     * @param value
     * @return 
     */
    public static byte[] integerToByteArray(int value) {
        if (value < 0xFF) {
            return new byte[]{BU.c(value)};
        }
        if (value < 0xFFFF) {
            return new byte[]{BU.c(value >> 8), BU.c(value)};
        }
        if (value < 0xFFFFFF) {
            return new byte[]{BU.c(value >> 16), BU.c(value >> 8), BU.c(value)};
        }
        return new byte[]{BU.c(value >> 24), BU.c(value >> 16), BU.c(value >> 8), BU.c(value)};
    }
    
    
    public static void parseTlvStructure(byte... data) throws UnsupportedEncodingException {
        parseTlvStructure(0, data);
    }

    private static void parseTlvStructure(int level, byte... data) throws UnsupportedEncodingException {
        if (data.length < 4) {
            return;
        }
        byte[] curData = new byte[data.length];
        System.arraycopy(data, 0, curData, 0, curData.length);
        String indent = "";
        for (int i = 0; i < level * 4; i++) {
            indent += " ";
        }

        for (int i = 0; i < data.length;) {
            byte tag = data[i + 0];
            int lenByte = (data[i + 1] & 0xff);
            int lenCount = lenByte < 0x80 ? 1 : lenByte - 0x80;
            int len = 0;
            if (lenCount == 1) {
                if (lenByte < 0x80) {
                    len = data[i + 1];
                } else {
                    len = data[i + 2] & 0xff;
                }
            } else {
                for (int j = 0; j < lenCount; j++) {
                    len += (data[i + 2 + j] & 0xff) << (lenCount - 1 - j) * 8;
                }
            }
            System.out.println(indent + level + " Tag: " + getTagType(tag) + " " + BU.toHexString(tag));

            byte[] nestedData = new byte[len];
            System.arraycopy(data, i + 2 + (lenByte < 0x80 ? 0 : lenCount), nestedData, 0, len);

            if ((tag & 32) > 0) {
                parseTlvStructure(level + 1, nestedData);
                System.out.println("");
            } else {
                if (tag == 0x06) {
                    System.out.println(indent + "  Data(" + len + "): " + getIdentifireName(nestedData) + "|" + BU.toHexString(nestedData));
                } else if (tag == 0x1E) {
                    System.out.println(indent + "  Data(" + len + "): " + new String(nestedData, Charset.forName("UTF-16")) + "|" + BU.toHexString(nestedData));
                } else if (tag == 0x04) {
                    System.out.println(indent + "  Data(" + len + "): " + new String(nestedData) + "|" + BU.toHexString(nestedData));
                } else {
                    System.out.println(indent + "  Data(" + len + "): " + new String(nestedData).replace("\n", "") + "|" + BU.toHexString(nestedData));
                }
            }
            i += 2 + (lenByte < 0x80 ? 0 : lenCount) + len;
        }

    }

    private static String getTagType(byte tag) {
        switch (tag & 0x1f) {
            case 0:
                return "EOC (End-of-Content)";
            case 1:
                return "BOOLEAN";
            case 2:
                return "INTEGER";
            case 3:
                return "BIT STRING";
            case 4:
                return "OCTET STRING";
            case 5:
                return "NULL";
            case 6:
                return "OBJECT IDENTIFIER";
            case 7:
                return "Object Descriptor";
            case 8:
                return "EXTERNAL";
            case 9:
                return "REAL";
            case 0xa:
                return "ENUMERATED";
            case 0xb:
                return "EMBEDDED PDV";
            case 0xc:
                return "UTF8String";
            case 0xd:
                return "RELATIVE-OID";
            case 0x10:
                return "SEQUENCE и SEQUENCE OF";
            case 0x11:
                return "SET и SET OF";
            case 0x12:
                return "NumericString";
            case 0x13:
                return "PrintableString";
            case 0x14:
                return "T61String";
            case 0x15:
                return "VideotexString";
            case 0x16:
                return "IA5String";
            case 0x17:
                return "UTCTime";
            case 0x18:
                return "GeneralizedTime";
            case 0x19:
                return "GraphicString";
            case 0x1A:
                return "VisibleString";
            case 0x1B:
                return "GeneralString";
            case 0x1C:
                return "UniversalString";
            case 0x1D:
                return "CHARACTER STRING";
            case 0x1E:
                return "BMPString";
            default:
                Integer.toHexString(tag);
        }
        return Integer.toHexString(tag);
    }

    private static String octetStrToString(String s) throws UnsupportedEncodingException {
        byte bs[] = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            bs[i / 2] = (byte) Integer.parseInt(s.substring(i, i + 2), 16);
        }
        return new String(bs, "UTF8");
    }

    private static String getIdentifireName(byte[] data) {
        for (byte[] key : idMap.keySet()) {
            if (Arrays.equals(key, data)) {
                System.out.println("" + BU.toHexString(key));
                System.out.println("    " + BU.toHexString(data));
                return idMap.get(key);
            }
        }
        return "";
    }
    
}
