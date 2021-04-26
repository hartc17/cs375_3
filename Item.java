import java.io.*;
import java.util.*;

public class Item {
  public int weight;
  public int profit;
  public int profitPerWeight;
  public int id;

  public Item(int w ,int p, int i){
    this.weight = w;
    this.profit = p;
    this.profitPerWeight = p/w;
    this.id = i;
  }

  public int getItemWeight(){
    return this.weight;
  }

  public int getItemProfit(){
    return this.profit;
  }

  public int getProfitPerWeight(){
    return this.profitPerWeight;
  }

  public int getId(){
    return this.id;
  }

  public void setItemWeight(int w){
    this.weight = w;
  }

  public void setItemProfit(int p){
    this.profit = p;
  }
}
