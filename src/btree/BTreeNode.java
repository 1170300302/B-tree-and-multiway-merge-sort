package btree;

import java.util.ArrayList;
import java.util.List;

public class BTreeNode<T extends Comparable<T>> {

  private List<Keyword<T>> keywords = new ArrayList<>();
  private List<BTreeNode<T>> childNodes = new ArrayList<>();

  private Keyword<T> queryByKeywords(T data) {
    for (Keyword<T> keyword : keywords) {
      if (data.compareTo(keyword.value()) == 0) {
        return keyword;
      }
    }
    return null;
  }

  public Keyword<T> query(T data) {
    Keyword<T> queryByKeywordsRes = queryByKeywords(data);
    if (queryByKeywordsRes != null) {
      return queryByKeywordsRes;
    }
    
    if (data.compareTo(keywords.get(0).value()) < 0) {
      if (childNodes.get(0) == null) {
        return new Keyword<>(this, 0);
      }
      childNodes.get(0).query(data);
    }
    
    if (data.compareTo(keywords.get(keywords.size() - 1).value()) > 0) {
      if (childNodes.get(keywords.size() - 1) == null) {
        return new Keyword<>(this, keywords.size());
      }
      childNodes.get(childNodes.size() - 1).query(data);
    }
    
    for (int i = 0; i < (keywords.size() - 2); i++) {
      if ((data.compareTo(keywords.get(i).value()) > 0)
          && (data.compareTo(keywords.get(i + 1).value()) < 0)) {
        if (childNodes.get(i + 1) == null) {
          return new Keyword<>(this, i + 1);
        }
        childNodes.get(i + 1).query(data);
      }
    }
    
    return null;
  }

  public void insert(Keyword<T> keyword, int index) {
    
  }

}
