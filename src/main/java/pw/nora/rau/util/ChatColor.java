package pw.nora.rau.util;

import net.minecraft.util.EnumChatFormatting;

import java.util.regex.Pattern;

public enum ChatColor {

    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),
    MAGIC('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true),
    ITALIC('o', true),
    RESET('r');

    public static final char COLOR_CHAR = '\u00A7';

    private final char code;
    private final boolean isFormat;
    private final String toString;

    private ChatColor(char code) {
        this(code, false);
    }

    private ChatColor(char code, boolean isFormat) {
        this.code = code;
        this.isFormat = isFormat;
        this.toString = new String(new char[] {COLOR_CHAR, code});
    }

    public char getChar() {
        return code;
    }

    @Override
    public String toString() {
        return toString;
    }

    public boolean isFormat() {
        return isFormat;
    }

    public boolean isColor() {
        return !isFormat && this != RESET;
    }

    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }
        return Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]").matcher(input).replaceAll("");
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
                b[i] = ChatColor.COLOR_CHAR;
                b[i+1] = Character.toLowerCase(b[i+1]);
            }
        }
        return new String(b);
    }

    public static String removeFormatting(String msg) {
        return msg.replace(EnumChatFormatting.BLACK.toString(), "&0")
                .replace(EnumChatFormatting.DARK_BLUE.toString(), "&1")
                .replace(EnumChatFormatting.DARK_GREEN.toString(), "&2")
                .replace(EnumChatFormatting.DARK_AQUA.toString(), "&3")
                .replace(EnumChatFormatting.DARK_RED.toString(), "&4")
                .replace(EnumChatFormatting.DARK_PURPLE.toString(), "&5")
                .replace(EnumChatFormatting.GOLD.toString(), "&6")
                .replace(EnumChatFormatting.GRAY.toString(), "&7")
                .replace(EnumChatFormatting.DARK_GRAY.toString(), "&8")
                .replace(EnumChatFormatting.BLUE.toString(), "&9")
                .replace(EnumChatFormatting.GREEN.toString(), "&a")
                .replace(EnumChatFormatting.AQUA.toString(), "&b")
                .replace(EnumChatFormatting.RED.toString(), "&c")
                .replace(EnumChatFormatting.LIGHT_PURPLE.toString(), "&d")
                .replace(EnumChatFormatting.YELLOW.toString(), "&e")
                .replace(EnumChatFormatting.WHITE.toString(), "&f")
                .replace(EnumChatFormatting.OBFUSCATED.toString(), "&k")
                .replace(EnumChatFormatting.BOLD.toString(), "&l")
                .replace(EnumChatFormatting.STRIKETHROUGH.toString(), "&m")
                .replace(EnumChatFormatting.UNDERLINE.toString(), "&n")
                .replace(EnumChatFormatting.ITALIC.toString(), "&o")
                .replace(EnumChatFormatting.RESET.toString(), "&r");
    }
    public static String deleteFormatting(String msg) {
        return msg.replace(EnumChatFormatting.BLACK.toString(), "")
                .replace(EnumChatFormatting.DARK_BLUE.toString(), "")
                .replace(EnumChatFormatting.DARK_GREEN.toString(), "")
                .replace(EnumChatFormatting.DARK_AQUA.toString(), "")
                .replace(EnumChatFormatting.DARK_RED.toString(), "")
                .replace(EnumChatFormatting.DARK_PURPLE.toString(), "")
                .replace(EnumChatFormatting.GOLD.toString(), "")
                .replace(EnumChatFormatting.GRAY.toString(), "")
                .replace(EnumChatFormatting.DARK_GRAY.toString(), "")
                .replace(EnumChatFormatting.BLUE.toString(), "")
                .replace(EnumChatFormatting.GREEN.toString(), "")
                .replace(EnumChatFormatting.AQUA.toString(), "")
                .replace(EnumChatFormatting.RED.toString(), "")
                .replace(EnumChatFormatting.LIGHT_PURPLE.toString(), "")
                .replace(EnumChatFormatting.YELLOW.toString(), "")
                .replace(EnumChatFormatting.WHITE.toString(), "")
                .replace(EnumChatFormatting.OBFUSCATED.toString(), "")
                .replace(EnumChatFormatting.BOLD.toString(), "")
                .replace(EnumChatFormatting.STRIKETHROUGH.toString(), "")
                .replace(EnumChatFormatting.UNDERLINE.toString(), "")
                .replace(EnumChatFormatting.ITALIC.toString(), "")
                .replace(EnumChatFormatting.RESET.toString(), "")
                .replace("&0", "")
                .replace("&1", "")
                .replace("&2", "")
                .replace("&3", "")
                .replace("&4", "")
                .replace("&5", "")
                .replace("&6", "")
                .replace("&7", "")
                .replace("&8", "")
                .replace("&9", "")
                .replace("&a", "")
                .replace("&b", "")
                .replace("&c", "")
                .replace("&d", "")
                .replace("&e", "")
                .replace("&f", "")
                .replace("&k", "")
                .replace("&l", "")
                .replace("&m", "")
                .replace("&n", "")
                .replace("&o", "")
                .replace("&r", "");
    }
}