package multiway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiwayMerge<T extends Comparable<T>> {

  private List<T> sortList;
  private final int MEMORY_SIZE = 1024;
  private final int DATA_SIZE = 16;
  private int surplus;

  public List<T> getSortList() {
    return new ArrayList<>(sortList);
  }

  public MultiwayMerge(List<T> initList) {
    sortList = new ArrayList<>(initList);
    multiwayMergeSort();
  }

  private List<List<T>> iteration(List<List<T>> tmp) {
    int baseSize = MEMORY_SIZE / DATA_SIZE - 2;
    List<List<T>> sortPreList = new ArrayList<>(tmp);
    List<T> tmpList = new ArrayList<>();
    List<T> locationList = new ArrayList<>();
    List<List<T>> sortNList = new ArrayList<>();
    surplus = sortPreList.size() - (int) (sortPreList.size() / baseSize) * baseSize;
    System.out.println((int) (sortPreList.size() / baseSize) + ", surplus: " + surplus);
    int baseIteration = (int) (sortPreList.size() / baseSize);
    if (baseIteration == 0) {
      baseIteration = sortPreList.size();
    }
    for (int i = 0; i < baseIteration; i++) {
      List<T> sortNListTmp = new ArrayList<>();

      // ¥À¥¶Œ£œ’
      while (sortNListTmp.size() != baseSize * MEMORY_SIZE / DATA_SIZE) {
        for (int j = 0; j < baseSize; j++) {
          if (sortPreList.get(i * baseSize + j).size() >= 1) {
            tmpList.add(sortPreList.get(i * baseSize + j).get(0));
            locationList.add(sortPreList.get(i * baseSize + j).get(0));
          }
        }
        Collections.sort(tmpList);

        T removeT = tmpList.get(0);
//        if (removeT.compareTo(tmpList.get(1)) == 0) {
//          System.out.println("?????");
//        }
        int index = locationList.indexOf(removeT);
        sortNListTmp.add(removeT);
        if (sortPreList.get(i * baseSize + index).size() >= 1) {
          sortPreList.get(i * baseSize + index).remove(0);
        } else {
          System.out.println("?????");
        }
        locationList.clear();
        tmpList.clear();
      }

      sortNList.add(new ArrayList<>(sortNListTmp));
    }
    for (int i = 0; i < surplus; i++) {
      sortNList.add(new ArrayList<>(sortPreList.get(sortPreList.size() - 1 - i)));
    }
    return new ArrayList<>(sortNList);
  }

  private void multiwayMergeSort() {
    List<List<T>> sortOneList = new ArrayList<>();
    List<T> tmpList = new ArrayList<>();
    for (int i = 0; i < sortList.size(); i++) {
      if (tmpList.size() < (MEMORY_SIZE / DATA_SIZE)) {
        tmpList.add(sortList.get(i));
      } else {
        sortOneList.add(new ArrayList<>(tmpList));
        tmpList.clear();
        tmpList.add(sortList.get(i));
      }
    }
    sortOneList.add(new ArrayList<>(tmpList));
    tmpList.clear();

    for (int i = 0; i < sortOneList.size(); i++) {
      Collections.sort(sortOneList.get(i));
    }

    List<List<T>> sortTwoList = iteration(sortOneList);
    System.out.println(
        sortTwoList.size() + "," + sortTwoList.get(252).size() + "," + sortTwoList.get(251).size());
//    List<List<T>> sortThreeList = iteration(sortTwoList);
//    System.out.println(sortThreeList.size());
//    List<List<T>> sortFourList = iteration(sortThreeList);
//    System.out.println(sortFourList.size());
  }

}
