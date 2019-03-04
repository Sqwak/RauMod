package pw.nora.rau.util;

public class Methods {
    public static boolean isNumeric(final String str) {
        try {
            Double.parseDouble(str);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
