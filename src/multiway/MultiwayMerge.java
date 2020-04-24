package multiway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiwayMerge<T extends Comparable<T>> {

  private List<T> sortList;
  private final int MEMORY_SIZE = 1024;
  private final int DATA_SIZE = 16;
  private int surplus;
  private T maxT;

  public List<T> getSortList() {
    return new ArrayList<>(sortList);
  }

  public MultiwayMerge(List<T> initList, T maxT) {
    sortList = new ArrayList<>(initList);
    this.maxT = maxT;
    multiwayMergeSort();
  }

  private List<List<T>> iteration(List<List<T>> tmp) {
    int baseSize = MEMORY_SIZE / DATA_SIZE - 2;
    List<List<T>> sortPreList = new ArrayList<>(tmp);
    List<T> tmpList = new ArrayList<>();
    List<T> locationList = new ArrayList<>();
    List<List<T>> sortResList = new ArrayList<>();
    surplus = sortPreList.size() - sortPreList.size() / baseSize * baseSize;
    System.out.println(sortPreList.size() / baseSize + ", surplus: " + surplus);
    int baseIteration = sortPreList.size() / baseSize;
    if (baseIteration == 0) {
      baseIteration = 1;
      baseSize = sortPreList.size();
    }
    for (int i = 0; i < baseIteration; i++) {
      List<T> sortResListTmp = new ArrayList<>();

      while (true) {
        for (int j = 0; j < baseSize; j++) {
          if (sortPreList.get(i * baseSize + j).size() >= 1) {
            tmpList.add(sortPreList.get(i * baseSize + j).get(0));
            locationList.add(sortPreList.get(i * baseSize + j).get(0));
          } else {
            tmpList.add(maxT);
            locationList.add(maxT);
          }
        }
        Collections.sort(tmpList);

        T removeT = tmpList.get(0);
        int index = locationList.indexOf(removeT);
        if (removeT == maxT) {
          break;
        }
        sortResListTmp.add(removeT);
        sortPreList.get(i * baseSize + index).remove(0);
        locationList.clear();
        tmpList.clear();
      }
      locationList.clear();
      tmpList.clear();

      sortResList.add(new ArrayList<>(sortResListTmp));
    }
    if (baseIteration != 1) {
      for (int i = 0; i < surplus; i++) {
        sortResList.add(new ArrayList<>(sortPreList.get(sortPreList.size() - 1 - i)));
      }
    }
    return new ArrayList<>(sortResList);
  }

  private void multiwayMergeSort() {
    List<List<T>> sortInitList = new ArrayList<>();
    List<T> tmpList = new ArrayList<>();
    for (int i = 0; i < sortList.size(); i++) {
      if (tmpList.size() < (MEMORY_SIZE / DATA_SIZE)) {
        tmpList.add(sortList.get(i));
      } else {
        sortInitList.add(new ArrayList<>(tmpList));
        tmpList.clear();
        tmpList.add(sortList.get(i));
      }
    }
    sortInitList.add(new ArrayList<>(tmpList));
    tmpList.clear();

    for (int i = 0; i < sortInitList.size(); i++) {
      Collections.sort(sortInitList.get(i));
    }

    // 这里是对实验结果的分析，事实上，这里的编码习惯十分不好，但是为了调试，细节可以忽略
    List<List<T>> sortIteList = iteration(sortInitList);
    System.out
        .println(sortIteList.size() + "," + sortIteList.get(sortIteList.size() - 1).size() + ","
            + sortIteList.get(sortIteList.size() - 2).size() + "," + sortIteList.get(0).get(3967));
    List<List<T>> sortIteList2 = iteration(sortIteList);
    System.out.println(sortIteList2.size() + "," + sortIteList2.get(sortIteList2.size() - 1).size()
        + "," + sortIteList2.get(0).size());
    List<List<T>> sortIteList3 = iteration(sortIteList2);
    System.out.println(sortIteList3.size());
  }

}
