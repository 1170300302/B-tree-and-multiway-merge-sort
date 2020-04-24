package core;

import java.util.ArrayList;
import java.util.List;
import btree.BTree;
import data.Data;
import multiway.MultiwayMerge;

public class Controller {

  public static void main(String[] args) {
//    List<Integer> testList =
//        Arrays.asList(1, 2, 6, 7, 11, 4, 8, 13, 10, 5, 17, 9, 16, 20, 3, 12, 14, 18, 19, 15);
//    BTree<Integer> bTree = new BTree<>(testList);
//    List<Integer> deleteList = Arrays.asList(8, 16, 15, 4);
//    bTree.delete(deleteList);
    final int n = 1000000;
    List<Data> testList = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      testList.add(new Data());
    }
//    BTree<Data> bTree = new BTree<>(testList);
//    bTree.getRank();
    MultiwayMerge<Data> multiwayMerge = new MultiwayMerge<>(testList, new Data(1000001));
    multiwayMerge.getSortList();
  }

}
