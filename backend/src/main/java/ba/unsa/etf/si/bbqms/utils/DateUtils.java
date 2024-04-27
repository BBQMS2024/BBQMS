package ba.unsa.etf.si.bbqms.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String formatDate(final String patternFormat, final Instant time) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternFormat)
                .withZone(ZoneId.of("Europe/Sarajevo"));

        return formatter.format(time);
    }

    public static String getHourMinute(final Instant time) {
        return formatDate("hh:mm", time);
    }
}
