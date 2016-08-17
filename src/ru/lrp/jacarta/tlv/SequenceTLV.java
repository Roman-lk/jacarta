package ru.lrp.jacarta.tlv;

/**
 *
 * @author lrp
 * Последовательсть других tlv-объектов(порядок важен)
 */
public class SequenceTLV extends BerTLV {

    public SequenceTLV(BerTLV... values) {
        super(Tag.SEQUENCE_OF, TlvUtils.toByteArray(values));
        getTag().setContainsOtherTlv(values.length != 0);
    }

}
