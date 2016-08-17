package ru.lrp.jacarta.token;

import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author lrp
 * Менеджер ключей
 * Занимается поиском подклченных токенов jaCarta, открывает сессию по работе
 * с найденным ключем
 */
public class TokenManager {

    /**
     * Фабрика терминалов(слотов, которые могут выступать в роли ридера для чипов, 
     * работающих в рамках ISO7816)
     */
    private static TerminalFactory factory;

    /**
     * Открыть сессию.
     * Сейчас при открытии сессии вернется первый найденый токен
     * @return крипто-токен jaCarta
     * @throws TokenNotFoundException 
     */
    public static Token openSession() throws TokenNotFoundException {
        CardChannel channel = getCurrentCardChannel();
        return new JaCartaToken(channel);
    }
    
    public static Token openSession(String pin) throws TokenNotFoundException, TokenOperationException {
        CardChannel channel = getCurrentCardChannel();
        JaCartaToken token = new JaCartaToken(channel);
        token.login(pin);
        return token;
    }

    private static TerminalFactory getTerminalFactory() {
        if (factory == null) {
            factory = TerminalFactory.getDefault();
        }
        return factory;
    }

    /**
     * Получить канал подключенной карты
     * @return канал связи с чипом найденого токена
     * @throws TokenNotFoundException ни один токен не найден
     */
    private static CardChannel getCurrentCardChannel() throws TokenNotFoundException {
        List<CardTerminal> terminals;
        try {
            CardTerminals smartCardIOTerminals = getTerminalFactory().terminals();
            terminals = smartCardIOTerminals.list(CardTerminals.State.CARD_PRESENT);
        } catch (CardException ex) {
            throw new TokenNotFoundException(ex);
        }
        // Команда выбора приложения jaCarta, если выполняется не успешно,
        // то перед нами не криптотокен, а какой-то другой чип
        CommandAPDU cmd = CardUtils.createCommandAPDU("00 A4 04 00 0a A0 00 00 04 48 01 01 01 06 02 00");
        for (CardTerminal terminal : terminals) {
            try {
                Card card = terminal.connect("*");
                CardChannel channel = card.getBasicChannel();
                ResponseAPDU res = channel.transmit(cmd);
                if (res.getSW() == 0x9000) {
                    return card.getBasicChannel();
                }
            } catch (CardException ex) {
            }
        }
        throw new TokenNotFoundException();
    }

}
