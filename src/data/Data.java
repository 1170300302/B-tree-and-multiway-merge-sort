package data;

public class Data implements Comparable<Data> {

  private int A;
  @SuppressWarnings("unused")
  private String B;
  private final int minA = 1;
  private final int maxA = 1000000;
  private final long minB = 100000000000L;
  private final long maxB = 999999999999L;

  public int getA() {
    return A;
  }

  public Data() {
    A = (int) (minA + Math.random() * (maxA - minA + 1));
    B = String.valueOf(minB + Math.random() * (maxB - minB + 1));
  }

  public int compareTo(Data o) {
    if (A > o.getA()) {
      return 1;
    } else if (A < o.getA()) {
      return -1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return new StringBuilder(A + "").toString();
  }

}
