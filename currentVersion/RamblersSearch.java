/*
 *	Search.java - abstract class specialising to MapSearch etc
 * Phil Green 2013 version
 * Heidi Christensen (heidi.christensen@sheffield.ac.uk) 2021 version
 */

import java.util.*;

public class RamblersSearch {

  private SearchNode initNode; //initial node
  private SearchNode currentNode; // current node
  private SearchNode old_node; //node found on open with same state as new one
  private ArrayList<SearchNode> open;  //open - list of SearchNodes
  private ArrayList<SearchNode> closed; //closed - .......
  private ArrayList<SearchNode> successorNodes; //used in expand & vetSuccessors
  private RamblersState goal;
  private int[][] map;
  private int width;
  private int depth;

    /**
     * constructor
     *
     * @param w  width
     * @param d depth
     * @param _map map
     */

  public RamblersSearch(int w, int d, int[][] _map){
    width = w;
    depth = d;
    map = _map;
  }

    /**
     * accessor for width
     */

  public int getWidth(){
    return width;
  }

    /**
     * accessor for depth
     */

  public int getDepth(){
    return depth;
  }

    /**
     * accessor for height
     */

  public int getHeight(int x, int y){
    return map[y][x];
  }

    /**
     * accessor for goal
     */

  public RamblersState getGoal(){
    return goal;
  }

    /**
     * mutator for width
     */

  public void setWidth(int w){
        width = w;
    }

    /**
     * mutator for depth
     */

  public void setDepth(int d){
        depth = d;
    }

    /**
     * mutator for goal
     */

  public void setGoal(RamblersState g){
    goal = g;
  }

    /**
     * mutator for map
     */

  public void setMap(int[][] currmap){
    map = currmap;
  }


  /**
  * run a search
  * @param initState initial state
  * @param strat - String specifying strategy
  * @return indication of success or failure
  */

  //run a search
  public  String runSearch (RamblersState initState, RamblersState end, String strat) {

    initNode = new SearchNode(initState,0,0); // create initial node
    initNode.setGlobalCost(0);
    setGoal(end);

    //print strategy
    System.out.println("Starting "+strat+" Search");

    open = new ArrayList<SearchNode>(); // initial open, closed
    open.add(initNode);
    closed=new ArrayList<SearchNode>();

    int numIteration = 1;

    while (!open.isEmpty()) {

    //print contents of open
    System.out.println("-------------------------");
    System.out.println("iteration no " + numIteration);
    System.out.println("open is");
    for (SearchNode nn: open) {
      String nodestr = nn.toString();
        System.out.println(nodestr);
    }

      selectNode(strat); //selectNode selects next node given strategy,
      
      //makes it currentNode & removes it from open
      System.out.println("Current node: "+currentNode.toString());

      if (currentNode.goalPredicate(this)) return reportSuccess();  //success
      //call reportSuccess

      expand(); //go again
      closed.add(currentNode); //put current node on closed
      numIteration = numIteration + 1;
  	}
  	return "Search Fails";  //out of the while loop - failure
	}


  //expand current node
  private void expand () {

	  //get all successor nodes
	  successorNodes = currentNode.getSuccessors(this); //pass search instance

    // set global costs and parents for successors
    // A* - set estTotalCost
    for (SearchNode snode: successorNodes){
      snode.setGlobalCost(currentNode.getGlobalCost()+ snode.getLocalCost());
      snode.setParent(currentNode);
      snode.setestTotalCost(snode.getGlobalCost()+snode.getestRemCost()); //A*
    }

    vetSuccessors(); //filter out unwanted - DP check

    //add surviving nodes to open
    for (SearchNode snode: successorNodes) open.add(snode);
  }


  // vet the successors
  // A* - can't ignore nodes already closed
  private void vetSuccessors() {
    ArrayList<SearchNode> vslis = new ArrayList<SearchNode>();

    for (SearchNode snode: successorNodes){
      if (onOpen(snode)) { //on open - usual DP check
        if (snode.getGlobalCost()<old_node.getGlobalCost()) {
          old_node.setParent(snode.getParent()); //better route, modify node
          old_node.setGlobalCost(snode.getGlobalCost());
          old_node.setLocalCost(snode.getLocalCost());
          old_node.setestTotalCost(snode.getestTotalCost());
        }
      }
      else {
        if (onClosed(snode)) { //A* - on closed - DP check again
          if (snode.getGlobalCost()<old_node.getGlobalCost()) {
            old_node.setParent(snode.getParent()); //better route, modify node
            old_node.setGlobalCost(snode.getGlobalCost());
            old_node.setLocalCost(snode.getLocalCost());
            old_node.setestTotalCost(snode.getestTotalCost());
            open.add(old_node); //A* - add the node back to open
            closed.remove(old_node); //A* - & remove it from closed
          }
        }
        else vslis.add(snode); //not seen before
      }
    }
    successorNodes = vslis;
  }

  //onClosed - is the state for a node the same as one on closed?
  private boolean onClosed(SearchNode newNode){
	  boolean ans = false;
	  Iterator ic = closed.iterator();
    while ((ic.hasNext())&& !ans){ //there can only be one node on open with same state
      SearchNode closedNode = (SearchNode) ic.next();
      if (newNode.sameState(closedNode)) {
        ans=true;
        old_node=closedNode;
      }
    }
	  return ans;
  }

  //onOpen - is the state for a node the same as one on open?
  // if node found, remember it in old_node
  private boolean onOpen(SearchNode newNode){
  	boolean ans = false;
    Iterator ic = open.iterator();
    while ((ic.hasNext())&& !ans){ //there can only be one node on open with same state
      SearchNode openNode = (SearchNode) ic.next();
      if (newNode.sameState(openNode)) {
        ans=true;
        old_node=openNode;
      }
    }
	return ans;
  }


   //Selection Strategies
   private void selectNode(String strat) {
	  if (strat== "depthFirst")
      depthFirst();
      else
      if(strat=="breadthFirst")
        breadthFirst();
      else
        if(strat=="branchAndBound")
          branchAndBound();
        else AStar();
   }

    private void depthFirst () {
      int osize=open.size();
		  currentNode= (SearchNode) open.get(osize-1); // last node added to open
		  open.remove(osize-1); //remove it
    }

	  private void breadthFirst(){
		  currentNode= (SearchNode) open.get(0); //first node on open
		  open.remove(0);
    }

    private void branchAndBound(){

      Iterator i = open.iterator();
      SearchNode minCostNode=(SearchNode) i.next();
      for (;i.hasNext();){
        SearchNode n=(SearchNode) i.next();
        if (n.getGlobalCost()<minCostNode.getGlobalCost()){
          minCostNode=n;};
        }

        currentNode=minCostNode;
        open.remove(minCostNode);
    }

    //A* - select node according to estTotalCost

    private void AStar(){

      Iterator i = open.iterator();
      SearchNode minCostNode=(SearchNode) i.next();
      for (;i.hasNext();){
        SearchNode n=(SearchNode) i.next();
        if (n.getestTotalCost()<minCostNode.getestTotalCost()){
          minCostNode=n;};
      }

      currentNode=minCostNode;
      open.remove(minCostNode);
    }

    	// report success - reconstruct path, convert to string & return
      private String reportSuccess(){

	    SearchNode n = currentNode;
	    StringBuffer buf = new StringBuffer(n.toString());
	    int plen=1;

	    while (n.getParent() != null){
	      buf.insert(0,"\n");
	      n=n.getParent();
	      buf.insert(0,n.toString());
	      plen=plen+1;
      }

    	System.out.println("=========================== \n");
	    System.out.println("Search Succeeds");

    	System.out.println("Efficiency "+ ((float) plen/(closed.size()+1)));
	    System.out.println("Solution Path\n");
	    return buf.toString();
    }

}
