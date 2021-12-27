package uwu.smsgamer.serverscripter.groovy.shell;

import lombok.Getter;
import lombok.Setter;
import uwu.smsgamer.serverscripter.senapi.utils.ChatColor;

import java.awt.*;
import java.util.List;

public class AnsiMCUtils {
    /**
     * Converts ANSI color codes to Minecraft color codes.
     *
     * @param input The ANSI formatted string.
     * @return The converted string.
     */
    public static String ansiToMC(String input) {
        List<Token> tokens = Tokenizer.tokenize(input);
        StringBuilder output = new StringBuilder();
        for (Token token : tokens) {
            if (token.getType() == TokenType.ANSI) {
                output.append(ansiTokenToMC(token.getValue()));
            } else {
                output.append(token.getValue());
            }
        }
        return output.toString();
    }

    /**
     * Converts an ANSI color code to a Minecraft color code.
     */
    private static String ansiTokenToMC(String ansiToken) {
        return SGR.fromANSI(ansiToken).getMCString();
    }

    @Getter
    public enum ANSISGR {
        RESET(0, 'r'),
        BOLD(1, 'l'),
        ITALIC(3, 'm'),
        UNDERLINE(4, 'n'),
        STRIKETHROUGH(9, 'o'),
        BLACK(30, Color.BLACK, new Color(0xAAAAAA), '0', '8'),
        RED(31, new Color(0xAA0000), new Color(0xFF5555), '4', 'c'),
        GREEN(32, new Color(0x00AA00), new Color(0x55FF55), '2', 'a'),
        YELLOW(33, new Color(0xAAAA00), new Color(0xFFFF55), '6', 'e'),
        BLUE(34, new Color(0x0000AA), new Color(0x5555FF), '1', '9'),
        MAGENTA(35, new Color(0xAA00AA), new Color(0xFF55FF), '5', 'd'),
        CYAN(36, new Color(0x00AAAA), new Color(0x55FFFF), '3', 'b'),
        WHITE(37, new Color(0xAAAAAA), new Color(0xFFFFFF), '7', 'f'),
        DEFAULT(39, new Color(0x000000), new Color(0x000000), '9', '9');

        private final int ansiCode;
        private final char mcDark;
        private final char mcBright;
        private final Color darkColor;
        private final Color brightColor;

        ANSISGR(int ansiCode, Color darkColor, Color brightColor, char mcDark, char mcBright) {
            this.ansiCode = ansiCode;
            this.darkColor = darkColor;
            this.brightColor = brightColor;
            this.mcDark = mcDark;
            this.mcBright = mcBright;
        }

        ANSISGR(int ansiCode, char mc) {
            this(ansiCode, null, null, mc, mc);
        }

        public int getAnsiBrightCode() {
            return ansiCode + 60;
        }

        public int getAnsiBGCode() {
            return ansiCode + 10;
        }

        public int getAnsiBGBrightCode() {
            return ansiCode + 70;
        }

        public static ANSISGR getByAnsiCode(int ansiCode) {
            for (ANSISGR ansisgr : ANSISGR.values()) {
                if (ansisgr.getAnsiCode() == ansiCode) {
                    return ansisgr;
                }
            }
            return null;
        }
    }

    @Getter
    public static class SGR {
        private final ANSISGR ansi;
        private boolean bright;
        private boolean background;

        SGR(ANSISGR ansi, boolean bright, boolean background) {
            this.ansi = ansi;
            this.bright = bright;
            this.background = background;
        }

        public char getMC() {
            if (bright) {
                return ansi.getMcBright();
            } else {
                return ansi.getMcDark();
            }
        }

        public Color getColor() {
            if (bright) {
                return ansi.getBrightColor();
            } else {
                return ansi.getDarkColor();
            }
        }

        public int getAnsiCode() {
            if (bright) {
                if (background) {
                    return ansi.getAnsiBGBrightCode();
                } else {
                    return ansi.getAnsiBrightCode();
                }
            } else {
                if (background) {
                    return ansi.getAnsiBGCode();
                } else {
                    return ansi.getAnsiCode();
                }
            }
        }

        public static SGR fromAnsiCode(int ansiCode) {
            if (ansiCode >= 40 && ansiCode <= 47) {
                return new SGR(ANSISGR.getByAnsiCode(ansiCode - 10), false, true);
            } else if (ansiCode >= 100 && ansiCode <= 107) {
                return new SGR(ANSISGR.getByAnsiCode(ansiCode - 70), true, true);
            } else if (ansiCode >= 90 && ansiCode <= 97) {
                return new SGR(ANSISGR.getByAnsiCode(ansiCode - 60), false, false);
            } else return new SGR(ANSISGR.getByAnsiCode(ansiCode), false, false);
        }

        public static SGR fromMC(char mc) {
            for (ANSISGR ansisgr : ANSISGR.values()) {
                if (ansisgr.getMcBright() == mc) {
                    return new SGR(ansisgr, true, false);
                } else if (ansisgr.getMcDark() == mc) {
                    return new SGR(ansisgr, false, false);
                }
            }
            return null;
        }

        public static SGR fromColor(Color color) {
            ANSISGR closest = null;
            boolean bright = false;
            double closestDistance = Integer.MAX_VALUE;
            for (ANSISGR ansisgr : ANSISGR.values()) {
                double distance = colorDifference(ansisgr.getBrightColor(), color);
                if (distance < closestDistance) {
                    closest = ansisgr;
                    closestDistance = distance;
                    bright = true;
                }
                distance = colorDifference(ansisgr.getBrightColor(), color);
                if (distance < closestDistance) {
                    closest = ansisgr;
                    closestDistance = distance;
                    bright = false;
                }
            }
            return new SGR(closest, bright, false);
        }

        public static SGR fromANSI(String ansi) {
            if (ansi.startsWith("\u001b[") && ansi.endsWith("m")) {
                ansi = ansi.substring(2, ansi.length() - 1);
            }
            String[] split = ansi.split("( )*;( )*");
            if (split.length == 1) {
                if (split[0].trim().isEmpty()) {
                    return new SGR(ANSISGR.RESET, false, false);
                }
                return fromAnsiCode(Integer.parseInt(split[0].trim()));
            }
            throw new UnsupportedOperationException("Not implemented yet");
//            if (split.length == 2) {
//                switch (split[0]) {
//                    case "1": {
//                        SGR sgr = fromAnsiCode(Integer.parseInt(split[1]));
//                        sgr.bright = true;
//                        return sgr;
//                    }
//                    case "4": {
//                        SGR sgr = fromAnsiCode(Integer.parseInt(split[1]));
//                        sgr.background = true;
//                        return sgr;
//                    }
//                    case "39":
//                    case "49": {
//                        return new SGR(ANSISGR.getByAnsiCode(Integer.parseInt(split[1])), false, false);
//                    }
//                    default: {
//                        throw new UnsupportedOperationException("Not implemented yet");
//                    }
//                }
//            }
        }

        private static double colorDifference(Color c1, Color c2) {
            return Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2) + Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2));
        }

        public String getMCString() {
            return "\u00A7" + getMC();
        }
    }

    private enum TokenType {
        ANSI,
        MC,
        //MC_HEX,
        TEXT
    }

    @Getter
    @Setter
    private static class Token {
        private String value;
        private final TokenType type;
        private final int start;
        private int end;

        public Token(String value, TokenType type, int start, int end) {
            this.value = value;
            this.type = type;
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "Token{" +
                    "value='" + value + '\'' +
                    ", type=" + type +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

    private static class Tokenizer {
        private static List<Token> tokenize(String input) {
            List<Token> tokens = new java.util.ArrayList<>();
            int start;
            int end;
            Token lastToken = null;
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                switch (c) {
                    case '\u001B':
                        if (i + 1 < input.length() && input.charAt(i + 1) == '[') {
                            start = i;
                            i++;
                            while (i < input.length() && input.charAt(i) != 'm') {
                                i++;
                            }
                            end = i + 1;
                            if (end > input.length()) {
                                end--;
                            }
                            tokens.add(lastToken = new Token(input.substring(start, end), TokenType.ANSI, start, end));
                            continue;
                        }
                        break;
                    case '&':
                    case '\u00A7':
                        if (i + 1 < input.length()) {
                            if (ChatColor.getByChar(input.charAt(i + 1)) != null) {
                                start = i;
                                end = i + 2;
                                tokens.add(lastToken = new Token(input.substring(start, end), TokenType.MC, start, end));
                                i++;
                                continue;
                            }
                        }
                        break;
                }
                if (lastToken != null && lastToken.getType() == TokenType.TEXT) {
                    lastToken.setValue(lastToken.getValue() + c);
                    lastToken.setEnd(i + 1);
                } else {
                    tokens.add(lastToken = new Token(String.valueOf(c), TokenType.TEXT, i, i + 1));
                }
            }
            return tokens;
        }
    }
}
