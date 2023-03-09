package org.example.model;


import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectRecordStorage {

  private final List<ProjectRecord> records;

  public ProjectRecordStorage(List<ProjectRecord> records) {
    Preconditions.checkNotNull(records, "records should be not null");
    this.records = new ArrayList<>(records);
  }

  public List<ProjectRecord> getRecords() {
    return Collections.unmodifiableList(records);
  }
}
