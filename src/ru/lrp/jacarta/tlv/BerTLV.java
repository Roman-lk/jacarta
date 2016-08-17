package ru.lrp.jacarta.tlv;

import java.io.ByteArrayOutputStream;
import ru.lrp.jacarta.utils.BU;

/**
 *
 * @author lrp
 * Базовый класс для всех tlv объектов
 */
public class BerTLV {

    private final Tag tag;
    private final int length;
    private final byte[] rawEncodedLengthBytes;
    private final byte[] valueBytes;

    /**
     *
     * @param tag
     * @param valueBytes
     */
    public BerTLV(Tag tag, byte[] valueBytes) {
        this.tag = tag;
        this.length = valueBytes.length;
        this.valueBytes = valueBytes;
        this.rawEncodedLengthBytes = calcLengthArray(length);
    }

    public Tag getTag() {
        return tag;
    }

    public int getDataLength() {
        return length;
    }

    public byte[] getValueBytes() {
        return valueBytes;
    }

    public byte[] toByteArray() {
        byte[] tagBytes = tag.getTagBytes();
        int len = tagBytes.length + rawEncodedLengthBytes.length + valueBytes.length;
        ByteArrayOutputStream stream = new ByteArrayOutputStream(len);
        stream.write(tagBytes, 0, tagBytes.length);
        stream.write(rawEncodedLengthBytes, 0, rawEncodedLengthBytes.length);
        stream.write(valueBytes, 0, valueBytes.length);
        return stream.toByteArray();
    }

    private byte[] calcLengthArray(int len) {
        if (len < 0x7f) {
            return new byte[]{BU.c(len)};
        }
        if (len < 0xFF) {
            return new byte[]{(byte) 0x81, BU.c(len)};
        }
        if (len < 0xFFFF) {
            return new byte[]{(byte) 0x82, BU.c(len >> 8), BU.c(len)};
        }
        if (len < 0xFFFFFF) {
            return new byte[]{(byte) 0x83, BU.c(len >> 16), BU.c(len >> 8), BU.c(len)};
        }
        return new byte[]{(byte) 0x84, BU.c(len >> 24), BU.c(len >> 16), BU.c(len >> 8), BU.c(len)};
    }

    public void setClass(TagClass cls) {
        tag.setTagClass(cls);
    }

}
