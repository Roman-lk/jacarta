package ru.lrp.jacarta.token.key;

/**
 *
 * @author lrp
 * Дескриптор ключа полученного с токена
 */
class Key {
    /**
     * Идентификатор ключа.
     * Используется всегда, токен работает именно с идентификаторами ключей,
     * которые былив него загружены
     */
    private final int id;
    /**
     * Значение ключ.
     * Используется только для публичного ключа,
     * приватный ключ с токена не достаем
     */
    protected final byte[] value;

    public Key(int id, byte[] value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }
}
