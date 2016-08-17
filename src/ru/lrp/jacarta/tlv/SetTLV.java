package ru.lrp.jacarta.tlv;

/**
 *
 * @author lrp
 * Последовательсть других tlv-объектов(порядок НЕ важен)
 */
public class SetTLV extends BerTLV {

    public SetTLV(BerTLV... values) {
        super(Tag.SET_OF, TlvUtils.toByteArray(values));
        getTag().setContainsOtherTlv(values.length != 0);
    }

}
