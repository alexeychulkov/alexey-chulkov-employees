package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

class MainTest {

  private final ByteArrayOutputStream captor = new ByteArrayOutputStream();

  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(captor));
  }

  @AfterEach
  public void tearDown() {
    System.setOut(System.out);
  }

  @Test
  void print_arguments() {
    Main.getCommandLine(new String[]{});

    Assertions.assertEquals(new String(
        "Missing required option: s\n" +
            "usage: sirma-test\n" +
            " -f,--format <arg>   date format\n" +
            " -s,--source <arg>   csv file").replaceAll("\\s+", ""), captor.toString().replaceAll("\\s+", ""));
  }

  @ParameterizedTest
  @CsvSource({
      "test.csv,0 0 0",
      "test_3_days.csv,144 143 3",
      "test_two_projects.csv,2 1 9",
  })
  void get_max_period(String file, String expected) throws URISyntaxException {
    Path path = Paths.get(CsvConverterTest.class.getClassLoader().getResource(file).toURI()).toAbsolutePath();

    Main.main(new String[]{"-s", path.toString()});

    Assertions.assertEquals(expected, captor.toString().trim());
  }

  @Test
  void date_formats() throws URISyntaxException {
    Path path = Paths.get(CsvConverterTest.class.getClassLoader().getResource("test_format.csv").toURI()).toAbsolutePath();
    Main.main(new String[]{"-s", path.toString(), "-f", "yyyy/MM/dd"});

    Assertions.assertEquals("144 143 3", captor.toString().trim());
  }
}