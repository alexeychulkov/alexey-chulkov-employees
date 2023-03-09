package org.example;


import org.example.model.EmpPairDuration;
import org.example.model.ProjectRecordStorage;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WorkingDurationService {
  private final ProjectRecordStorage storage;

  public WorkingDurationService(ProjectRecordStorage storage) {
    this.storage = storage;
  }

  public EmpPairDuration getLongest() {
    Collection<Project> projects = getProjects();
    Map<EmpPair, Long> pairPeriods = new HashMap<>();
    projects.forEach(project -> {
      for (int i = 0; i < project.periods.size(); i++) {
        for (int j = i + 1; j < project.periods.size(); j++) {
          EmpPeriod emp1Period = project.periods.get(i);
          EmpPeriod emp2Period = project.periods.get(j);
          long commonPeriod = getCommonPeriod(emp1Period, emp2Period);
          if (commonPeriod > 0) {
            EmpPair pair = EmpPair.of(emp1Period.empId, emp2Period.empId);
            pairPeriods.merge(pair, commonPeriod, Long::sum);
          }
        }
      }
    });

    if(pairPeriods.isEmpty()) {
      return EmpPairDuration.ZERO;
    }
    Map.Entry<EmpPair, Long> entry = Collections.max(pairPeriods.entrySet(), Comparator.comparingLong(Map.Entry::getValue));
    return new EmpPairDuration(entry.getKey().empId1, entry.getKey().empId2, Duration.ofMillis(entry.getValue()));
  }

  private long getCommonPeriod(EmpPeriod emp1Period, EmpPeriod emp2Period) {
    long start1 = emp1Period.from.toEpochMilli();
    long end1 = emp1Period.to.toEpochMilli();

    long start2 = emp2Period.from.toEpochMilli();
    long end2 = emp2Period.to.toEpochMilli();

    long end = Math.min(end1, end2);
    long start = Math.max(start1, start2);
    return Math.max(0, end - start);
  }

  private Collection<Project> getProjects() {
    Map<Integer, Project> projects = new HashMap<>();

    storage.getRecords().forEach(r -> {
      int projectId = r.getProjectId();
      Project project = projects.get(projectId);
      if (project == null) {
        project = new Project(projectId);
        projects.put(projectId, project);
      }

      int employeeId = r.getEmployeeId();
      Instant from = r.getFrom();
      Instant to = r.getTo();
      project.add(new EmpPeriod(employeeId, from, to));
    });

    return projects.values();
  }

  private static class EmpPeriod {
    int empId;

    Instant from;
    Instant to;

    public EmpPeriod(int empId, Instant from, Instant to) {
      this.empId = empId;
      this.from = from;
      this.to = to;
    }
  }

  private static class EmpPair {
    int empId1;

    int empId2;

    static EmpPair of(int empId1, int empId2) {
      if (empId1 > empId2) {
        return new EmpPair(empId1, empId2);
      }
      else {
        return new EmpPair(empId2, empId1);
      }

    }

    public EmpPair(int empId1, int empId2) {
      this.empId1 = empId1;
      this.empId2 = empId2;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      EmpPair empPair = (EmpPair) o;
      return empId1 == empPair.empId1 && empId2 == empPair.empId2;
    }

    @Override
    public int hashCode() {
      return Objects.hash(empId1, empId2);
    }

    @Override
    public String toString() {
      return "EmpPair{" +
          "empId1=" + empId1 +
          ", empId2=" + empId2 +
          '}';
    }
  }

  private static class Project {
    int id;

    List<EmpPeriod> periods = new ArrayList<>();

    public Project(int id) {
      this.id = id;
    }

    public void add(EmpPeriod empPeriod) {
      periods.add(empPeriod);
    }
  }
}
