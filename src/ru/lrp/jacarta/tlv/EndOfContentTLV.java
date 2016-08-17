package ru.lrp.jacarta.tlv;

/**
 *
 * @author lrp
 */
public class EndOfContentTLV extends BerTLV {

    public EndOfContentTLV(BerTLV values) {
        super(Tag.EOC, TlvUtils.toByteArray(values));
        getTag().setContainsOtherTlv(true);
    }

}
