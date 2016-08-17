package ru.lrp.jacarta.tlv;

import org.junit.Assert;
import org.junit.Test;
import ru.lrp.jacarta.utils.BU;

/**
 *
 * @author lrp
 * Тест построения tlv-объектов
 */
public class BerTLVTest {
    
    public BerTLVTest() {
    }

    @Test
    public void test() throws TLVCreatingException {
        
        BerTLV tlv = new BerTLV(Tag.BIT_STRING, "1234567".getBytes());
        System.out.println("BaseBerTLV: " + BU.toHexString(tlv.toByteArray()));
        Assert.assertArrayEquals(tlv.toByteArray(), BU.convertFromHex("03 07 31 32 33 34 35 36 37"));
        
        tlv = new PrintableStringTLV("1234567");
        System.out.println("PrintableStringTLV: " + BU.toHexString(tlv.toByteArray()));
        Assert.assertArrayEquals(tlv.toByteArray(), BU.convertFromHex("13 07 31 32 33 34 35 36 37"));
   
        tlv =new ObjectIdentifierTLV("1.2.643.3.131.1.1");
        System.out.println("ObjectIdentifierTLV: " + BU.toHexString(tlv.toByteArray()));
        Assert.assertArrayEquals(tlv.toByteArray(), BU.convertFromHex("06 08 2A 85 03 03 81 03 01 01"));
        
        tlv = new SequenceTLV(tlv , tlv);
        System.out.println("SequenceTLV: " + BU.toHexString(tlv.toByteArray()));
        Assert.assertArrayEquals(tlv.toByteArray(), BU.convertFromHex("30 14 06 08 2A 85 03 03 81 03 01 01 06 08 2A 85 03 03 81 03 01 01"));

        tlv = new BMPStringTLV("АААБББВВВ");
        System.out.println("BMPStringTLV: " + BU.toHexString(tlv.toByteArray()));
        Assert.assertArrayEquals(tlv.toByteArray(), BU.convertFromHex("1E 12 04 10 04 10 04 10 04 11 04 11 04 11 04 12 04 12 04 12"));
        
        tlv = new IA5StringTLV("test@test.ri");
        System.out.println("IA5StringTLV: " + BU.toHexString(tlv.toByteArray()));
        Assert.assertArrayEquals(tlv.toByteArray(), BU.convertFromHex("13 0C 74 65 73 74 40 74 65 73 74 2E 72 69"));

        tlv = new NullTLV();
        System.out.println("NullTLV: " + BU.toHexString(tlv.toByteArray()));
        Assert.assertArrayEquals(tlv.toByteArray(), BU.convertFromHex("05 00"));
       
        tlv = new IntegerTLV(1);
        System.out.println("IntegerTLV: " + BU.toHexString(tlv.toByteArray()));
        Assert.assertArrayEquals(tlv.toByteArray(), BU.convertFromHex("02 01 01"));
        
        byte[] usage = TlvUtils.integerToByteArray(0x04F0);
        BerTLV keyUsageValue = new BerTLV(Tag.OCTET_STRING, new BitStringTLV(usage).toByteArray());
         System.out.println("keyUsageValue: " + BU.toHexString(keyUsageValue.toByteArray()));
    }

}
