package btree;

import java.util.ArrayList;
import java.util.List;

public class BTreeNode<T extends Comparable<T>> {

  private List<Keyword<T>> keywords = new ArrayList<>();
  // 设置keywords的值之后必定设置childNodes的值
  private List<BTreeNode<T>> childNodes = new ArrayList<>();
  // 同样如此
  private BTreeNode<T> parentNode;
  private BTree<T> mainBTree;

  public List<Keyword<T>> getKeywordsSafe() {
    return new ArrayList<>(keywords);
  }

  private List<Keyword<T>> getKeywords() {
    return keywords;
  }

  private void setKeywords(Keyword<T> keyword, int index) {
    keywords.add(index, keyword);
    keyword.setHomeBTreeNode(this);
    keyword.setInsetIndex();
  }

  public List<BTreeNode<T>> getChildNodesSafe() {
    return new ArrayList<>(childNodes);
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
    for (int i = 0; i < keywords.size(); i++) {
      keywords.get(i).setHomeBTreeNode(this);
    }
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

    // 结点没有该数据，第一种情况，结点为空
    if (keywords.size() == 0) {
      return new Keyword<>(this, 0);
    }

    // 结点没有该数据，第二种情况，结点不为空，递归查询
    // 递归查询注意叶结点的处理
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
    if (mainBTree.getRootBTreeNode() == this && childNodes.get(0) != null) {
      if (childNodes.size() < 2) {
        return true;
      }
    }
    if (mainBTree.getRootBTreeNode() != this && childNodes.get(0) != null) {
      if (childNodes.size() < Math.ceil(mainBTree.getRank() / 2.0)) {
        return true;
      }
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

    // 构造拆分后的左右结点，只差parentNode的赋值，此时分为两种情况
    BTreeNode<T> leftBTreeNode = new BTreeNode<>(leftKeywords, leftChildNodes, mainBTree);
    BTreeNode<T> rightBTreeNode = new BTreeNode<>(rightKeywords, rightChildNodes, mainBTree);

    if (parentNode == null) {
      BTreeNode<T> newParntNode = new BTreeNode<>(mainBTree);
      newParntNode.setKeywords(midKeyword, 0);
      newParntNode.setChildNodes(leftBTreeNode, 0);
      newParntNode.setChildNodes(rightBTreeNode, 1);
      newParntNode.setParentNode(null);
      leftBTreeNode.setParentNode(newParntNode);
      rightBTreeNode.setParentNode(newParntNode);
      // 构造拆分后的中结点，赋值为新的root
      mainBTree.setRootBTreeNode(newParntNode);
    } else {
      leftBTreeNode.setParentNode(parentNode);
      rightBTreeNode.setParentNode(parentNode);
      int index = parentNode.getChildNodes().indexOf(this);
      parentNode.setKeywords(midKeyword, index);
      parentNode.getChildNodes().remove(index);
      parentNode.setChildNodes(leftBTreeNode, index);
      parentNode.setChildNodes(rightBTreeNode, index + 1);
    }
    // 拆分后，子结点的parentNode也要发生改变
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
    // 递归拆分
    if (leftBTreeNode.getParentNode().checkKeywordsNum()) {
      leftBTreeNode.getParentNode().split();
    }
  }

  public void insert(Keyword<T> keyword, int index) {
    setKeywords(keyword, index);
    // 插入必定发生在叶结点，故只需判断是否为空即可
    if (childNodes.size() == 0) {
      childNodes.add(null);
      childNodes.add(null);
    } else {
      childNodes.add(null);
    }
//    setParentNode(parentNode);
    // 违背规则，进行拆分
    if (checkKeywordsNum()) {
      split();
    }
  }

  private BTreeNode<T> getBroNode(boolean isLeftNode) {
    if (isLeftNode) {
      if (parentNode != null) {
        int index = parentNode.getChildNodes().indexOf(this);
        if (index != 0) {
          return parentNode.getChildNodes().get(index - 1);
        }
      }
    } else {
      if (parentNode != null) {
        int index = parentNode.getChildNodes().indexOf(this);
        if (index != (parentNode.getChildNodesSafe().size() - 1)) {
          return parentNode.getChildNodes().get(index + 1);
        }
      }
    }
    return null;
  }

  private void borrowKeywords(BTreeNode<T> broNode, Keyword<T> keyword, boolean isLeftNode) {
    broNode.getChildNodes().remove(0);
    keywords.remove(keyword);
    keyword.setHomeBTreeNode(null);
    int index = parentNode.getChildNodes().indexOf(this);
    Keyword<T> parentKeyword;
    if (isLeftNode) {
      parentKeyword = parentNode.getKeywords().remove(index - 1);
      setKeywords(parentKeyword, 0);
      Keyword<T> broKeyword = broNode.getKeywords().remove(broNode.getKeywordsSafe().size() - 1);
      broKeyword.setHomeBTreeNode(null);
      parentNode.setKeywords(broKeyword, index - 1);
    } else {
      parentKeyword = parentNode.getKeywords().remove(index);
      setKeywords(parentKeyword, keywords.size());
      Keyword<T> broKeyword = broNode.getKeywords().remove(0);
      broKeyword.setHomeBTreeNode(null);
      parentNode.setKeywords(broKeyword, index);
    }
  }

  private void mergeNode(BTreeNode<T> broNode, boolean isLeftNode) {
    int index = parentNode.getChildNodes().indexOf(this);
    Keyword<T> parentKeyword;
    if (isLeftNode) {
      parentKeyword = parentNode.getKeywords().remove(index - 1);
      setKeywords(parentKeyword, 0);
      for (int i = (broNode.getKeywordsSafe().size() - 1); i >= 0; i--) {
        setKeywords(broNode.getKeywords().get(i), 0);
      }
      for (int i = 0; i < broNode.getChildNodesSafe().size(); i++) {
        setChildNodes(broNode.getChildNodes().get(i), i);
        if (broNode.getChildNodes().get(i) != null) {
          broNode.getChildNodes().get(i).setParentNode(this);
        }
      }
      parentNode.getChildNodes().remove(index - 1);
    } else {
      parentKeyword = parentNode.getKeywords().remove(index);
      setKeywords(parentKeyword, keywords.size());
      for (int i = 0; i < broNode.getKeywords().size(); i++) {
        setKeywords(broNode.getKeywords().get(i), keywords.size());
      }
      for (int i = 0; i < broNode.getChildNodesSafe().size(); i++) {
        setChildNodes(broNode.getChildNodes().get(i), childNodes.size());
        if (broNode.getChildNodes().get(i) != null) {
          broNode.getChildNodes().get(i).setParentNode(this);
        }
      }
      parentNode.getChildNodes().remove(index + 1);
      if (parentNode.getKeywordsSafe().size() == 0) {
        mainBTree.setRootBTreeNode(this);
        parentNode = null;
      }
    }
    if (parentNode != null && parentNode.checkKeywordsNum()) {
      BTreeNode<T> parentLeftBroNode = parentNode.getBroNode(true);
      BTreeNode<T> parentRightBroNode = parentNode.getBroNode(false);
      if (parentLeftBroNode != null) {
        parentNode.mergeNode(parentLeftBroNode, true);
      } else if (parentRightBroNode != null) {
        parentNode.mergeNode(parentRightBroNode, false);
      }
    }
  }

  private BTreeNode<T> adjacentNode(Keyword<T> keyword) {
    int index = keywords.indexOf(keyword);
    BTreeNode<T> childNode = childNodes.get(index);
    while (childNode.getChildNodesSafe().get(0) != null) {
      childNode = childNode.getChildNodes().get(childNode.getChildNodesSafe().size() - 1);
    }
    return childNode;
  }

  private void exchangeKeywords(Keyword<T> keyword, BTreeNode<T> adjacentNode) {
    Keyword<T> adjacentKeyword =
        adjacentNode.getKeywords().get(adjacentNode.getKeywordsSafe().size() - 1);
    int index = keywords.indexOf(keyword);
    int exchangeIndex = adjacentNode.getKeywords().indexOf(adjacentKeyword);
    keywords.remove(keyword);
    setKeywords(adjacentKeyword, index);
    adjacentNode.getKeywords().remove(adjacentKeyword);
    adjacentNode.setKeywords(keyword, exchangeIndex);
  }

  public void delete(Keyword<T> keyword, boolean isLeafNode) {
    // 非叶结点的处理
    if (!isLeafNode) {
      BTreeNode<T> adjacentNode = adjacentNode(keyword);
      exchangeKeywords(keyword, adjacentNode);
      adjacentNode.delete(keyword, true);
      return;
    }
    int keywordLimit = (int) (Math.ceil(mainBTree.getRank() / 2.0) - 1);
    // 直接删除
    if (keywords.size() > keywordLimit) {
      keywords.remove(keyword);
      keyword.setHomeBTreeNode(null);
      childNodes.remove(0);
    } else if (keywords.size() == keywordLimit) {
      BTreeNode<T> leftBroNode = getBroNode(true);
      BTreeNode<T> rightBroNode = getBroNode(false);
      // 向左兄弟借结点，右兄弟借结点，左右兄弟合并
      if ((leftBroNode != null) && (leftBroNode.getKeywordsSafe().size() > keywordLimit)) {
        borrowKeywords(leftBroNode, keyword, true);
      } else if ((rightBroNode != null) && (rightBroNode.getKeywordsSafe().size() > keywordLimit)) {
        borrowKeywords(rightBroNode, keyword, false);
      } else if (leftBroNode != null) {
        keywords.remove(keyword);
        keyword.setHomeBTreeNode(null);
        childNodes.remove(0);
        mergeNode(leftBroNode, true);
      } else if (rightBroNode != null) {
        keywords.remove(keyword);
        keyword.setHomeBTreeNode(null);
        childNodes.remove(0);
        mergeNode(rightBroNode, false);
      }
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
