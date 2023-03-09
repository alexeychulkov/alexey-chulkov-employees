package org.example.model;


import java.time.Instant;

public final class ProjectRecord {

  private final int employeeId;
  private final int projectId;
  private final Instant from;
  private final Instant to;

  public ProjectRecord(int employeeId, int projectId, Instant from, Instant to) {
    this.employeeId = employeeId;
    this.projectId = projectId;
    this.from = from;
    this.to = to;
  }

  public int getEmployeeId() {
    return employeeId;
  }

  public int getProjectId() {
    return projectId;
  }

  public Instant getFrom() {
    return from;
  }

  public Instant getTo() {
    return to;
  }
}
