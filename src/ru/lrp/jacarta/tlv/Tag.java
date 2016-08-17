package ru.lrp.jacarta.tlv;

/**
 *
 * @author lrp
 * Доступные TLV теги и их значения
 */
public enum Tag {
    EOC(0),
    BOOLEAN(1),
    INTEGER(2),
    BIT_STRING(3),
    OCTET_STRING(4),
    NULL(5),
    OBJECT_IDENTIFIER(6),
    OBJECT_DESCRIPTOR(7),
    EXTERNAL(8),
    REAL(9),
    ENUMERATED(0XA),
    EMBEDDED_PDV(0XB),
    UTF8STRING(0XC),
    RELATIVE_OID(0XD),
    SEQUENCE_OF(0X10),
    SET_OF(0X11),
    NUMERICSTRING(0X12),
    PRINTABLE_STRING(0X13),
    T61STRING(0X14),
    VIDEOTEXSTRING(0X15),
    IA5STRING(0X16),
    UTCTIME(0X17),
    GENERALIZEDTIME(0X18),
    GRAPHICSTRING(0X19),
    VISIBLESTRING(0X1A),
    GENERALSTRING(0X1B),
    UNIVERSALSTRING(0X1C),
    CHARACTER_STRING(0X1D),
    BMPSTRING(0X1E);
    
    /**
     * Идентификатор тега
     */
    private final int id;
    
    /**
     * Содержит ли в себе другие теги
     */
    private boolean containsOtherTlv;
    
    /**
     * Класс тега
     */
    private TagClass tagClass = TagClass.UNIVERSAL;
    
    private Tag(int id) {
        this.id = id;
    }

    public byte[] getTagBytes() {
        byte tagValue = (byte)((id | (containsOtherTlv ? 0x20 : 0x00) | tagClass.getMask()) & 0xFF );
        return new byte[]{tagValue};
    }

    public void setContainsOtherTlv(boolean containsOtherTlv) {
        this.containsOtherTlv = containsOtherTlv;
    }

    public void setTagClass(TagClass tagClass) {
        this.tagClass = tagClass;
    }
    
    
    
}
