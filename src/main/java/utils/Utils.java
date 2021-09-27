package utils;

import java.util.ArrayList;
import java.util.Collection;


public class Utils {

    public static <T> T getUnique(Collection<T> collection) {
        switch (collection.size()) {
            case 1:
                return new ArrayList<>(collection).get(0);
            case 0:
                throw new IllegalStateException("no elements found");
            default:
                throw new IllegalStateException("too many elements found: " + collection.size());
        }
    }

    public static String normalize(String arg) {
        return arg.replace(" ", " ") //nbsp->sp
                .trim()
                .toLowerCase();
    }


    public static String stripNonDigits(final CharSequence input) {
        final StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (c > 47 && c < 58) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}