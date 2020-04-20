package btree;

import java.util.ArrayList;
import java.util.List;

public class BTreeNode<T extends Comparable<T>> {

  private List<Keyword<T>> keywords = new ArrayList<>();
  private List<BTreeNode<T>> childNodes = new ArrayList<>();
  private BTreeNode<T> parentNode;
  private BTree<T> mainBTree;

  private List<Keyword<T>> getKeywords() {
    return keywords;
  }

  private void setKeywords(Keyword<T> keyword, int index) {
    keywords.add(index, keyword);
  }

  private List<BTreeNode<T>> getChildNodes() {
    return childNodes;
  }

  private void setChildNodes(BTreeNode<T> childNode, int index) {
    childNodes.add(index, childNode);
  }

  private BTreeNode<T> getParentNode() {
    return parentNode;
  }

  private void setParentNode(BTreeNode<T> parentNode) {
    this.parentNode = parentNode;
  }

  public BTreeNode(BTree<T> mainBTree) {
    this.mainBTree = mainBTree;
  }

  private BTreeNode(List<Keyword<T>> keywords, List<BTreeNode<T>> childNodes, BTree<T> mainBTree) {
    this.keywords = new ArrayList<>(keywords);
    this.childNodes = new ArrayList<>(childNodes);
    this.mainBTree = mainBTree;
  }

  private Keyword<T> queryByKeywords(T data) {
    for (Keyword<T> keyword : keywords) {
      if (data.compareTo(keyword.getValue()) == 0) {
        return keyword;
      }
    }
    return null;
  }

  public Keyword<T> query(T data) {
    // 查找结点是否已经有该数据
    Keyword<T> queryByKeywordsRes = queryByKeywords(data);
    if (queryByKeywordsRes != null) {
      return queryByKeywordsRes;
    }

    // keywords为空的情况
    if (keywords.size() == 0) {
      return new Keyword<>(this, 0);
    }

    if (data.compareTo(keywords.get(0).getValue()) < 0) {
      if (childNodes.get(0) == null) {
        return new Keyword<>(this, 0);
      }
      return childNodes.get(0).query(data);
    }

    if (data.compareTo(keywords.get(keywords.size() - 1).getValue()) > 0) {
      if (childNodes.get(childNodes.size() - 1) == null) {
        return new Keyword<>(this, keywords.size());
      }
      return childNodes.get(childNodes.size() - 1).query(data);
    }

    for (int i = 0; i < (keywords.size() - 1); i++) {
      if ((data.compareTo(keywords.get(i).getValue()) > 0)
          && (data.compareTo(keywords.get(i + 1).getValue()) < 0)) {
        if (childNodes.get(i + 1) == null) {
          return new Keyword<>(this, i + 1);
        }
        return childNodes.get(i + 1).query(data);
      }
    }

    return null;
  }

  private boolean checkKeywordsNum() {
    if (keywords.size() > (mainBTree.getRank() - 1)) {
      return true;
    }
    return false;
  }

  private void split() {
    List<Keyword<T>> leftKeywords = new ArrayList<>();
    Keyword<T> midKeyword;
    List<Keyword<T>> rightKeywords = new ArrayList<>();
    int midIndex = (keywords.size() - 1) / 2;
    for (int i = 0; i < midIndex; i++) {
      leftKeywords.add(keywords.get(i));
    }
    midKeyword = keywords.get(midIndex);
    for (int i = (midIndex + 1); i < keywords.size(); i++) {
      rightKeywords.add(keywords.get(i));
    }

    List<BTreeNode<T>> leftChildNodes = new ArrayList<>();
    List<BTreeNode<T>> rightChildNodes = new ArrayList<>();
    midIndex = childNodes.size() / 2;
    for (int i = 0; i < midIndex; i++) {
      leftChildNodes.add(childNodes.get(i));
    }
    for (int i = midIndex; i < childNodes.size(); i++) {
      rightChildNodes.add(childNodes.get(i));
    }

    BTreeNode<T> leftBTreeNode = new BTreeNode<>(leftKeywords, leftChildNodes, mainBTree);
    BTreeNode<T> rightBTreeNode = new BTreeNode<>(rightKeywords, rightChildNodes, mainBTree);

    if (parentNode == null) {
      BTreeNode<T> newParntNode = new BTreeNode<>(mainBTree);
      newParntNode.setKeywords(midKeyword, 0);
      newParntNode.setChildNodes(leftBTreeNode, 0);
      newParntNode.setChildNodes(rightBTreeNode, 1);
      leftBTreeNode.setParentNode(newParntNode);
      rightBTreeNode.setParentNode(newParntNode);
      this.mainBTree.setRootBTreeNode(newParntNode);
      for (int i = 0; i < leftBTreeNode.getChildNodes().size(); i++) {
        if (leftBTreeNode.getChildNodes().get(i) != null) {
          leftBTreeNode.getChildNodes().get(i).setParentNode(leftBTreeNode);
        }
      }
      for (int i = 0; i < rightBTreeNode.getChildNodes().size(); i++) {
        if (rightBTreeNode.getChildNodes().get(i) != null) {
          rightBTreeNode.getChildNodes().get(i).setParentNode(rightBTreeNode);
        }
      }
      if (newParntNode.checkKeywordsNum()) {
        newParntNode.split();
      }
    } else {
      leftBTreeNode.setParentNode(parentNode);
      rightBTreeNode.setParentNode(parentNode);
      int index = parentNode.getChildNodes().indexOf(this);
      parentNode.setKeywords(midKeyword, index);
      parentNode.getChildNodes().remove(index);
      parentNode.setChildNodes(leftBTreeNode, index);
      parentNode.setChildNodes(rightBTreeNode, index + 1);
      if (leftBTreeNode.getParentNode().checkKeywordsNum()) {
        leftBTreeNode.getParentNode().split();
      }
    }
  }

  public void insert(Keyword<T> keyword, int index) {
    keywords.add(index, keyword);
    keyword.setInsetIndex();
    if (childNodes.size() == 0) {
      childNodes.add(null);
      childNodes.add(null);
    } else {
      childNodes.add(null);
    }
    if (checkKeywordsNum()) {
      split();
    }
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    for (Keyword<T> keyword : keywords) {
      stringBuilder.append(keyword.getValue() + ", ");
    }
    stringBuilder.append("]");
    return stringBuilder.toString();
  }

}
