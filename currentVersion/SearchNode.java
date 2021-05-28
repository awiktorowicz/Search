/**
 *	Node in a state-space search
 *   variable costs case
 *   Must implement goalPredicate, getSuccessors, sameState, node_toString
 *   node has local cost & global cost now
 *   Phil Green 2013 version
 *   Heidi Christensen (heidi.christensen@sheffield.ac.uk) 2021 version
 */

import java.util.*;

public  class SearchNode {

  private RamblersState state;

  private int Cost;
  private int globalCost;
  private int localCost;
  private SearchNode parent; // the parent node

  /**
  * constructor
  * @param s a SearchState
  * @param lc local cost of getting to this node from its predecessor */

  public SearchNode(RamblersState s, int lc) {
    state= (RamblersState) s;
    localCost=lc;
  }

  /**
   * accessor for state
   */
  public RamblersState get_State() {

    return state;
  }

  /**
   * accessor for parent
   */
  public SearchNode getParent() {
    return parent;
  }

  /**
   * mutator for parent
   */
  public void setParent(SearchNode n){
    parent = n;
  }

  /**
   * mutator for localcost
   *
   */
  public void setLocalCost(int lc) {
    localCost=lc;
  }

  /**
   * accessor for localcost
   *
   */
  public int getLocalCost() {
    return localCost;
  }

  /**
   * mutator for globalcost
   *
  */
  public void setGlobalCost(int lc) {
    globalCost=lc;
  }

  /**
   * acccessor for globalcost
   *
  */
  public int getGlobalCost() {
    return globalCost;
  }

  /**
  * goalP takes a SearchNode & returns true if it's a goal
  * @param searcher the current search
  */

  public  boolean goalPredicate(RamblersSearch searcher) {
    return state.goalPredicate(searcher);
  }

  /**
  * getSuccessors for this node
  *
  * @param searcher the current search
  * @return ArrayList of successor nodes
  */

  public ArrayList getSuccessors(RamblersSearch searcher) {
    ArrayList<RamblersState> slis = state.getSuccessors(searcher);
    ArrayList<SearchNode> nlis= new ArrayList<SearchNode>();

    for (RamblersState suc_state:slis){
         SearchNode n = new SearchNode(suc_state, suc_state.getLocalCost());
      nlis.add(n);
    }
    return nlis;
  }


  /**
  * sameState - does another node have same state as this one?
  * @param n2 the other node
  */
  public boolean sameState(SearchNode n2) {
    return state.sameState(n2.get_State());
  }

  public String toString() {
    String parent_state;
    String nodeState = state.getX()+", "+state.getY();
    if (parent == null)
      parent_state = "null";
    else
      parent_state = parent.get_State().toString();
    return "node with state (" + nodeState + ") parent state (" + parent_state + ") local cost (" + localCost
            + ") global cost (" + globalCost + ")";
  }


}
