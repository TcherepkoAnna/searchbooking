package utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


public class DateUtils {


    public static LocalDate parse(String token, String format) {
        LocalDate date = LocalDate.parse(token, DateTimeFormatter.ofPattern(format));
        return date;
    }

    public static YearMonth parseYearMonth(String token, String format) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);
        return YearMonth.parse(token, fmt);

    }

    public static String convertToformat(String token, String formatFrom, String formatTo) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(formatTo);
        return parse(token, formatFrom).format(fmt);
    }
}


