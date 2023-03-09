package org.example;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.example.model.ProjectRecord;
import org.example.model.ProjectRecordStorage;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Main {
  public static void main(String[] args) {

    CommandLine cmd = getCommandLine(args);
    if (cmd == null) {
      System.exit(1);
      return;
    }

    String sourceFileParam = cmd.getOptionValue("source");
    String dateFormatParam = cmd.getOptionValue("format") == null ? "yyyy-MM-dd" : cmd.getOptionValue("format");

    Path path = Paths.get(sourceFileParam);
    DateTimeFormatter formatter;
    try {
      formatter = DateFormatParser.parse(dateFormatParam);
    }
    catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      System.exit(1);
      return;
    }

    List<ProjectRecord> records = new CsvConverter().apply(path, formatter);

    ProjectRecordStorage storage = new ProjectRecordStorage(records);
    WorkingDurationService durationService = new WorkingDurationService(storage);

    System.out.println(durationService.getLongest());
  }

  @VisibleForTesting
  @Nullable
  static CommandLine getCommandLine(String[] args) {
    Options options = new Options();

    Option source = new Option("s", "source", true, "csv file");
    source.setRequired(true);
    options.addOption(source);

    Option dateFormat = new Option("f", "format", true, "date format");
    dateFormat.setRequired(false);
    options.addOption(dateFormat);

    CommandLineParser parser = new BasicParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd;

    try {
      cmd = parser.parse(options, args);
    }
    catch (ParseException e) {
      System.out.println(e.getMessage());
      formatter.printHelp("sirma-test", options);

      return null;
    }
    return cmd;
  }
}