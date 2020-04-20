package core;

import java.util.Arrays;
import java.util.List;

import btree.BTree;

public class Controller {

  public static void main(String[] args) {
    List<Integer> testList =
        Arrays.asList(1, 2, 6, 7, 11, 4, 8, 13, 10, 5, 17, 9, 16, 20, 3, 12, 14, 18, 19, 15);
    @SuppressWarnings("unused")
    BTree<Integer> bTree = new BTree<>(testList);
  }

}
