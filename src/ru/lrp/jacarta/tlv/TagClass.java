package ru.lrp.jacarta.tlv;

/**
 *
 * @author user
 * Доступные классы tlv-тегов и их значения
 */
public enum TagClass {
    
    UNIVERSAL        (0x00),
    APPLIED          (0x40),
    CONTEXT_ORIENTED (0x80),
    PRIVATE          (0xC0);

    /**
     * Маска класса тега
     */
    private final int mask;

    private TagClass(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return mask;
    }

    
}
