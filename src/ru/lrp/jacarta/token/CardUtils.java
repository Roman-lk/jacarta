package ru.lrp.jacarta.token;

import javax.smartcardio.CommandAPDU;

/**
 *
 * @author lrp
 */
public class CardUtils {

    /**
     * Команда формирования APDU запроса из массива байт, записанных
     *  в виде hex-строки
     * @param cmd
     * @return
     */
    public static CommandAPDU createCommandAPDU(String cmd) {
        return createCommandAPDU(stringToHex(cmd));
    }

    /**
     * Команда формирования APDU запроса из массива байт
     * @param cmd
     * @return
     */
    public static CommandAPDU createCommandAPDU(byte[] cmd) {
        if (cmd == null) {
            throw new IllegalArgumentException("Argument 'cmd' cannot be null");
        }
        if (cmd.length < 4) {
            throw new IllegalArgumentException("APDU must be at least 4 bytes long: " + cmd.length);
        }
        CommandAPDU commandAPDU = null;
        if (cmd.length == 4) { 
            //Case 1 (EMV doesn't use this)
            commandAPDU = new CommandAPDU(cmd);
        } else if (cmd.length == 5) { 
            //Case 2s
            commandAPDU = new CommandAPDU(
                    byteToInt(cmd[0]),
                    byteToInt(cmd[1]),
                    byteToInt(cmd[2]),
                    byteToInt(cmd[3]),
                    (cmd[4] == 0x00 ? 256 : byteToInt(cmd[4])));
        } else if (cmd.length == (5 + byteToInt(cmd[4]))) { 
            //Case 3s
            commandAPDU = new CommandAPDU(cmd);
        } else if (cmd.length == (5 + byteToInt(cmd[4]) + 1)) { 
            //Case 4s
            byte[] data = new byte[byteToInt(cmd[4])];
            System.arraycopy(cmd, 5, data, 0, data.length);
            int le = byteToInt(cmd[cmd.length - 1]);
            commandAPDU = new CommandAPDU(
                    byteToInt(cmd[0]),
                    byteToInt(cmd[1]),
                    byteToInt(cmd[2]),
                    byteToInt(cmd[3]),
                    data,
                    0, //dataOffset
                    data.length,
                    (le == 0 ? 256 : le));
        } else if (cmd.length == (7 + (byteToInt(cmd[5]) << 8) + byteToInt(cmd[6]))) {
            //это вариант используется в протоколе T=1( данных больше чем 0xFF,
            // байт Le отсутствует)
            byte[] data = new byte[(byteToInt(cmd[5]) << 8) + byteToInt(cmd[6])];
            System.arraycopy(cmd, 7, data, 0, data.length);
            int le = byteToInt(cmd[cmd.length - 1]);
            commandAPDU = new CommandAPDU(
                    byteToInt(cmd[0]),
                    byteToInt(cmd[1]),
                    byteToInt(cmd[2]),
                    byteToInt(cmd[3]),
                    data,
                    0, //dataOffset
                    data.length,
                    (le == 0 ? 256 : le));
        } else {
            //ToDo: при необходимости нужно добавить сборку команды 
            // для протокола T=1 в случае, когда данных больше чем 0xFF байт 
            // и присутствует байт Le
            throw new IllegalArgumentException("Unsupported APDU format: ");
        }

        return commandAPDU;

    }

    private static int byteToInt(byte b) {
        return b & 0XFF;
    }

    public static byte[] stringToHex(String str) {
        str = str.replace(" ", "");

        if (str.length() % 2 != 0) {
            str = str.substring(0, str.length() - 1) + '0' + str.charAt(str.length() - 1);
        }
        int len = str.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) (
                (Character.digit(str.charAt(i), 16) << 4) +
                 Character.digit(str.charAt(i + 1), 16)
            );
        }
        return data;
    }

}
