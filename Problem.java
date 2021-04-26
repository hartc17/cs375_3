import java.io.*;
import java.util.*;

public class Problem{
  public int size;
  public int capacity;
  public ArrayList<Item> items;

  public Problem(int s, int c){
    this.size = s;
    this.capacity = c;
    items = new ArrayList<Item>();
  }

  public int getProblemSize(){
    return this.size;
  }

  public int getProblemCapacity(){
    return this.capacity;
  }

  public ArrayList<Item> getItems(){
    return this.items;
  }

  public int getTotalProfit(){
    int t = 0;
    for (Item x: this.items){
      t += x.getItemProfit();
    }
    return t;
  }

  public void setProblemSize(int s){
    this.size = s;
  }

  public void setProblemCapacity(int c){
    this.capacity = c;
  }

  public void addItem(Item x){
    items.add(x);
  }

  public void printItems(){
    for (int i = 0; i < items.size(); i++){
      System.out.println(items.get(i).getItemWeight() + " " + items.get(i).getItemProfit());
    }
  }

}
