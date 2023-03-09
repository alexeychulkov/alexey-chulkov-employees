package org.example;


import org.example.model.ProjectRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CsvConverter implements BiFunction<Path, DateTimeFormatter, List<ProjectRecord>> {

  private static final ZoneId utc = ZoneId.of("UTC");
  private static final Instant now = LocalDate.now().atStartOfDay(utc).toInstant();

  @Override
  public List<ProjectRecord> apply(Path path, DateTimeFormatter formatter) {
    try {
      List<String> lines = Files.readAllLines(path);
      return lines.stream().skip(1).map(s -> s.split(", ", 0)).map(strings -> {
        int employeeId = Integer.parseInt(strings[0]);
        int projectId = Integer.parseInt(strings[1]);
        Instant from = getInstant(strings, 2, formatter);
        Instant to = getInstant(strings, 3, formatter);

        return new ProjectRecord(employeeId, projectId, from, to);
      }).collect(Collectors.toList());
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Instant getInstant(String[] strings, int index, DateTimeFormatter formatter) {
    String date = strings[index];
    if(date.equals("NULL")) {
      return now;
    }
    return LocalDate.parse(date, formatter).atStartOfDay(utc).toInstant();
  }
}
