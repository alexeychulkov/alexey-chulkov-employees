package org.example;

import org.example.model.ProjectRecord;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CsvConverterTest {

  @Test
  void apply() throws URISyntaxException {
    Path path = Paths.get(CsvConverterTest.class.getClassLoader().getResource("test.csv").toURI());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    List<ProjectRecord> list = new CsvConverter().apply(path, formatter);

    assertEquals(3, list.size());
    ProjectRecord record = list.get(0);
    assertEquals(143, record.getEmployeeId());
    assertEquals(12, record.getProjectId());
    assertEquals(1383264000000L, record.getFrom().toEpochMilli());
    assertEquals(1388880000000L, record.getTo().toEpochMilli());

  }
}