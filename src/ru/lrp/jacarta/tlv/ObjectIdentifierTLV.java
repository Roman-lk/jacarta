package ru.lrp.jacarta.tlv;

import ru.lrp.jacarta.utils.OIDUtils;

/**
 *
 * @author user
 * Объектный идентификатор
 */
public class ObjectIdentifierTLV extends BerTLV{
    
    
    private final String value;
    
    public ObjectIdentifierTLV(String value) throws TLVCreatingException{
        super(Tag.OBJECT_IDENTIFIER, value.getBytes());
        this.value = value;
    }
    
    @Override
    public byte[] toByteArray() {
        // Перегружаем toByteArray, так как конвертер уже строит объекты
        // с учетом названия тега и длинной данных
        return OIDUtils.oidStringToByteArray(value);
    }

    
}
