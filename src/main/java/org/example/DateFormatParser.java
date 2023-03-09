package org.example;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateFormatParser {

  public static DateTimeFormatter parse(String pattern) {
    return DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
  }
}
