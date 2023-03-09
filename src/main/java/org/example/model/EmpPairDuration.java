package org.example.model;


import java.time.Duration;

public final class EmpPairDuration {

  public static final EmpPairDuration ZERO = new EmpPairDuration(0, 0, Duration.ZERO);
  private final int emp1;
  private final int emp2;
  private final Duration duration;

  public EmpPairDuration(int emp1, int emp2, Duration duration) {
    this.emp1 = emp1;
    this.emp2 = emp2;
    this.duration = duration;
  }

  @Override
  public String toString() {
    return emp1 + " " + emp2 + " " + duration.toDays();
  }
}
