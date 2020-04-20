package btree;

public class Keyword<T extends Comparable<T>> {

  private BTreeNode<T> homeBTreeNode;
  private int insertIndex;
  private T t;

  public BTreeNode<T> getHomeBTreeNode() {
    return homeBTreeNode;
  }

  public int getInsertIndex() {
    return insertIndex;
  }

  public T value() {
    return t;
  }

  public void setValue(T t) {
    this.t = t;
  }

  public Keyword(BTreeNode<T> homeBTreeNode, int insertIndex) {
    this.homeBTreeNode = homeBTreeNode;
    this.insertIndex = insertIndex;
  }

}
