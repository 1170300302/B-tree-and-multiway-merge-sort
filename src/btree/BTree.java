package btree;

import java.util.List;

public class BTree<T extends Comparable<T>> {

  private BTreeNode<T> rootBTreeNode = new BTreeNode<>(this);
  private int rank = 5;

  public void setRootBTreeNode(BTreeNode<T> rootBTreeNode) {
    this.rootBTreeNode = rootBTreeNode;
  }

  public int getRank() {
    return rank;
  }

  public BTree(List<T> initList) {
    for (T initData : initList) {
      insert(initData);
    }
  }

  public void insert(T data) {
    Keyword<T> queryKeyword = query(data);
    if (queryKeyword.getInsertIndex() == -1) {
      System.out.println("This keyword has been inserted");
    } else {
      queryKeyword.setValue(data);
      queryKeyword.getHomeBTreeNode().insert(queryKeyword, queryKeyword.getInsertIndex());
    }
  }

  public Keyword<T> query(T data) {
    return rootBTreeNode.query(data);
  }

}
