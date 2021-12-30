package uwu.smsgamer.serverscripter.senapi.utils;

import uwu.smsgamer.serverscripter.senapi.config.ConfVal;

public class ChatUtils {
    public static void sendMessage(APlayerOfSomeSort sender, ConfVal<String> msg, String... replacements) {
        sendMessage(sender, msg.getValue(), replacements);
    }

    public static void sendMessage(APlayerOfSomeSort sender, String msg, String... replacements) {
        if (replacements.length % 2 != 0)
            throw new IllegalArgumentException("Replacement length isn't even. Length: " + replacements.length);
        for (int i = 0; i < replacements.length; i += 2) msg = msg.replace(replacements[i], replacements[i + 1]);
        sender.sendMessage(msg);
    }
}
