package btree;

public class Keyword<T extends Comparable<T>> {

  private BTreeNode<T> homeBTreeNode;
  private int insertIndex;
  private T value;

  public BTreeNode<T> getHomeBTreeNode() {
    return homeBTreeNode;
  }

  public void setHomeBTreeNode(BTreeNode<T> homeBTreeNode) {
    this.homeBTreeNode = homeBTreeNode;
  }

  public int getInsertIndex() {
    return insertIndex;
  }

  public void setInsetIndex() {
    insertIndex = -1;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public Keyword(BTreeNode<T> homeBTreeNode, int insertIndex) {
    this.homeBTreeNode = homeBTreeNode;
    this.insertIndex = insertIndex;
  }

}
