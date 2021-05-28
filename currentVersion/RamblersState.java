/**
 *	State in a state-space search
 *	abstract class
 *   must implement goalPredicate, getSuccessors, sameState, toString
 *   variable cost version - has localCost variable
 *   Phil Green 2013 version
 *   Heidi Christensen (heidi.christensen@sheffield.ac.uk) 2021 version
 */

import java.util.*;

public class RamblersState {

  private int localCost;

  // A*
  private int estRemCost;

  private int x;

  private int y;

  private int height;

  public RamblersState(int x_, int y_){
    x = x_;
    y = y_;
  }

  /**
  * accessor for local cost
  */
  public int getLocalCost(){
    return localCost;
  }

  /**
   * accessor for Estimated Reaming Cost
   */
  // A*
  public int getestRemCost(){
    return estRemCost;
  }

  /**
   * accessor for height
   */
  public int getHeight(){
    return height;
  }

  /**
   * mutator for height
   */
  public void setHeight(int h){
    height = h;
  }

  /**
   * accessor for x coordinate
   */
  public int getX(){
    return x;
  }

  /**
   * accessor for y coordinate
   */
  public int getY(){
    return y;
  }

  /**
   * mutator for local cost
   */
  public void setLocalCost(int c){
    localCost = c;
  }


  /**
   * mutator for Estimated Reaming cost
   */
  public void setEstCost(int c){
    estRemCost = c;
  }

  /**
   * mutator for Reaming cost
   */
  public void setRemCost(int g_x, int g_y){
    int d_x = x - g_x;
    if(d_x < 0){
      d_x = d_x*-1;
    }

    int d_y = g_y - y;
    if(d_y < 0){
      d_y = d_y*-1;
    }
    estRemCost = d_y + d_x;

  }


  public boolean goalPredicate(RamblersSearch searcher){
    RamblersState goal = searcher.getGoal();

    if(goal.getX() == x && goal.getY() == y){
      return true;
    }
    return false;
  }

  public int calcCost(int h){
    if(height >= h ){
      return 1;
    }
    return 1 + (h-height);
  }
  public ArrayList<RamblersState> getSuccessors(RamblersSearch searcher){
    ArrayList<RamblersState> t = new ArrayList<RamblersState>();
    RamblersState goal = searcher.getGoal();
    if(x >0){
      RamblersState temp = new RamblersState(x-1, y);
      int h = searcher.getHeight(x-1, y);
      temp.setHeight(h);
      temp.setLocalCost(calcCost(h));
      temp.setRemCost(goal.getX(), goal.getY());
      t.add(temp);
    }

    if(x < searcher.getWidth()-1){
      RamblersState temp = new RamblersState(x+1, y);
      int h = searcher.getHeight(x+1, y);
      temp.setHeight(h);
      temp.setLocalCost(calcCost(h));
      temp.setRemCost(goal.getX(), goal.getY());
      t.add(temp);
    }
    if(y >0){
      RamblersState temp = new RamblersState(x, y-1);
      int h = searcher.getHeight(x, y-1);
      temp.setHeight(h);
      temp.setLocalCost(calcCost(h));
      temp.setRemCost(goal.getX(), goal.getY());
      t.add(temp);
    }

    if(y < searcher.getDepth()-1){
      RamblersState temp = new RamblersState(x, y+1);
      int h = searcher.getHeight(x, y+1);
      temp.setHeight(h);
      temp.setLocalCost(calcCost(h));
      temp.setRemCost(goal.getX(), goal.getY());
      t.add(temp);
    }



    return t;
  }

  public boolean sameState(RamblersState n2){
      if(x == n2.getX() && y == n2.getY()){
        return true;
      }
      return false;
  }

  public String toString() {
    return (x +", "+ y);
  }

}
