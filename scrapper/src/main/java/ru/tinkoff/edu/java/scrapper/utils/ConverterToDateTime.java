package ru.tinkoff.edu.java.scrapper.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class ConverterToDateTime {

    public static LocalDateTime convertOffset(OffsetDateTime time) {
        return time.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }
}
