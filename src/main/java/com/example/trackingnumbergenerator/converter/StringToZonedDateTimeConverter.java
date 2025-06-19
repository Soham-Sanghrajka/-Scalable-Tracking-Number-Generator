package com.example.trackingnumbergenerator.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

@Component
public class StringToZonedDateTimeConverter implements Converter<String, ZonedDateTime>{
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(ISO_LOCAL_DATE)
            .appendLiteral('T')
            .append(ISO_LOCAL_TIME)
            .optionalStart()
            .appendOffset("+HH:MM", "+00:00")
            .optionalEnd()
            .optionalStart()
            .appendOffset("+HHMM", "+0000")
            .optionalEnd()
            .optionalStart()
            .appendOffset("+HH", "Z")
            .optionalEnd()
            .toFormatter();

    @Override
    public ZonedDateTime convert(String source) {
        String normalized = source.replaceFirst(" (\\d{2}:\\d{2})$", "+$1")
                .replace(" ", "+");
        return ZonedDateTime.parse(normalized, FORMATTER);
    }
}
