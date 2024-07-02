package pl.kuezese.core.helper;

public final class StringHelper {

    public static String stripSpecialChars(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static String parseSeconds(int time) {
        switch (time) {
            case 4:
            case 3:
            case 2: return time + " sekundy";
            case 1: return time + " sekunde";
            default: return time + " sekund";
        }
    }

    public static String pluralizedTimeText(int time) {
        switch (time) {
            case 4:
            case 3:
            case 2: return "sekundy";
            case 1: return "sekunde";
            default: return  "sekund";
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
