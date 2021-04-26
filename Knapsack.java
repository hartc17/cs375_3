import java.io.*;
import java.util.*;

public class Knapsack {

  public static Problem problem;
  public static int mp;
  public static ArrayList<Item> knapsack = new ArrayList<Item>();
  public static ArrayList<Integer> bestset = new ArrayList<Integer>();

  public static int greedyOne(){
    double startTime = (double)System.nanoTime();
    int maxProfit = 0;
    ArrayList<Integer> maxIndexes = new ArrayList<Integer>();
    Problem temp = problem;
    //sort temp by p/w in non-increasing order
    sortProfitPerWeight(temp.getItems());
    //temp.printItems();

    //put as many items as possible in the knapsack
    int remaining = problem.getProblemCapacity();
    for (Item x: temp.getItems()){
      if (remaining <= 0) break;
      if (x.getItemWeight() <= remaining){
        knapsack.add(x);
        remaining -= x.getItemWeight();
        maxProfit += x.getItemProfit();
        maxIndexes.add(x.getId());
      }
    }
    Collections.sort(maxIndexes);
    double endTime = (double)System.nanoTime();
    double totalTime = (endTime - startTime)/1000000000.0;
    System.out.println(problem.getProblemSize() + " " + maxProfit + " " + totalTime + " " + indexesToString(maxIndexes));
    return maxProfit;
  }

  public static ArrayList<ArrayList<Integer>> greedyOneNoPrint(){
    int maxProfit = 0;
    ArrayList<Integer> maxIndexes = new ArrayList<Integer>();
    Problem temp = problem;
    ArrayList<Item> knapsack = new ArrayList<Item>();
    ArrayList<Integer> maxP = new ArrayList<Integer>();
    ArrayList<ArrayList<Integer>> output = new ArrayList<ArrayList<Integer>>();

    //sort temp by p/w in non-increasing order
    sortProfitPerWeight(temp.getItems());
    //temp.printItems();

    //put as many items as possible in the knapsack
    int remaining = problem.getProblemCapacity();
    for (Item x: temp.getItems()){
      if (remaining <= 0) break;
      if (x.getItemWeight() <= remaining){
        knapsack.add(x);
        remaining -= x.getItemWeight();
        maxProfit += x.getItemProfit();
        maxIndexes.add(x.getId());
      }
    }
    Collections.sort(maxIndexes);
    maxP.add(maxProfit);
    output.add(maxP);
    output.add(maxIndexes);
    return output;
  }

  public static int greedyTwo(){
    double startTime = (double)System.nanoTime();
    int maxProfit = 0;
    ArrayList<Integer> maxIndexes = new ArrayList<Integer>();
    ArrayList<ArrayList<Integer>> greedyOneOut = greedyOneNoPrint();
    int pmax = 0;
    int pMaxIndex = 0;
    for (Item x: problem.getItems()){
      if (x.getItemProfit() > pmax && x.getItemWeight() <= problem.getProblemCapacity()){
        pmax = x.getItemProfit();
        pMaxIndex = x.getId();
      }
    }

    if (greedyOneOut.get(0).get(0) > pmax){
      maxIndexes = greedyOneOut.get(1);
      double endTime = (double)System.nanoTime();
      double totalTime = (endTime - startTime)/1000000000.0;
      System.out.println(problem.getProblemSize() + " " + greedyOneOut.get(0).get(0) + " " + totalTime + " " + indexesToString(maxIndexes));
      return maxProfit;
    }


    double endTime = (double)System.nanoTime();
    double totalTime = (endTime - startTime)/1000000000.0;
    System.out.println(problem.getProblemSize() + " " + pmax + " " + totalTime + " " + pMaxIndex);
    return pmax;
  }

  public static ArrayList<ArrayList<Integer>> greedyTwoNoPrint(){
    ArrayList<ArrayList<Integer>> output = new ArrayList<ArrayList<Integer>>();
    int maxProfit = 0;
    ArrayList<Integer> maxIndexes = new ArrayList<Integer>();
    ArrayList<ArrayList<Integer>> greedyOneOut = greedyOneNoPrint();
    ArrayList<Integer> pMaxList = new ArrayList<Integer>();
    ArrayList<Integer> pMaxIndexList = new ArrayList<Integer>();
    int pmax = 0;
    int pMaxIndex = 0;
    for (Item x: problem.getItems()){
      if (x.getItemProfit() > pmax && x.getItemWeight() <= problem.getProblemCapacity()){
        pmax = x.getItemProfit();
        pMaxIndex = x.getId();
      }
    }
    pMaxList.add(pmax);
    pMaxIndexList.add(pMaxIndex);

    if (greedyOneOut.get(0).get(0) > pmax){
      output.add(greedyOneOut.get(0));
      output.add(greedyOneOut.get(1));
      return output;
    }
    output.add(pMaxList);
    output.add(pMaxIndexList);
    return output;
  }

  public static void backtracking(){
    double startTime = (double)System.nanoTime();
    ArrayList<ArrayList<Integer>> greedyTwoOut = greedyTwoNoPrint();
    int mp = greedyTwoOut.get(0).get(0);
    bestset = greedyTwoOut.get(1);
    ArrayList<Integer> weights = new ArrayList<Integer>();
    ArrayList<Integer> profits = new ArrayList<Integer>();
    ArrayList<Item> temp = problem.getItems();
    sortProfitPerWeight(temp);
    for (Item i: temp){
      weights.add(i.getItemWeight());
      profits.add(i.getItemProfit());
    }
    knapsackAlg(0, temp.get(0).getItemProfit(), temp.get(0).getItemWeight(), temp, weights, profits);
    double endTime = (double)System.nanoTime();
    double totalTime = (endTime - startTime)/1000000000.0;
    System.out.println(problem.getProblemSize() + " " + mp + " " + totalTime + " " + indexesToString(bestset));
  }

  public static void knapsackAlg(int i, int p, int w, ArrayList<Item> temp, ArrayList<Integer> weights, ArrayList<Integer> profits){
    int c = problem.getProblemCapacity();
    if (temp.get(i).getItemWeight() <= c && temp.get(i).getItemProfit() > mp){
      mp = temp.get(i).getItemProfit();
    }
    if (promising(i, temp, weights, profits)){
      bestset.add(temp.get(i + 1).getId());
      knapsackAlg(i + 1, p + temp.get(i + 1).getItemProfit(), w + temp.get(i + 1).getItemWeight(), temp, weights, profits);
      bestset.remove(temp.get(i + 1).getId());
      knapsackAlg(i + 1, temp.get(i).getItemProfit(), temp.get(i).getItemWeight(), temp, weights, profits);
    }
  }

  public static boolean promising(int i, ArrayList<Item> temp, ArrayList<Integer> weights, ArrayList<Integer> profits){
    int c = problem.getProblemCapacity();
    if (temp.get(i).getItemWeight() >= c) return false;
    int bound = KWF2(i + 1, temp.get(i).getItemWeight(), temp.get(i).getItemProfit(), weights, profits, c, problem.getProblemSize());
    return (bound > mp);
  }

  public static int KWF2(int i, int weight, int profit, ArrayList<Integer> weights, ArrayList<Integer> profits, int capacity, int lastIndex){
    int[] x = new int[problem.getProblemSize()+1];
    int bound = profit;
    for (int j = i; j <= problem.getProblemSize(); j++){
      x[j] = 0;
    }
    while (weight < capacity && i <= lastIndex){
      if (weight + weights.get(i-1) <= capacity){
        x[i] = 1;
        weight += weights.get(i-1);
        bound += profits.get(i-1);
      }
      else {
        x[i] = (capacity - weight)/weights.get(i-1);
        weight = capacity;
        bound += (profits.get(i-1) * x[i]);
      }
      i++;
    }
    return bound;
  }

  public static int upperBound(int i, Item v, ArrayList<Integer> weights, ArrayList<Integer> profits, int capacity, int size){
    return KWF2(i, v.getItemWeight(), v.getItemProfit(), weights, profits, capacity, size);
  }

  public static void sortProfitPerWeight(ArrayList<Item> list){
      list.sort((o2, o1)
                    ->  Integer.compare(o1.getProfitPerWeight(), o2.getProfitPerWeight()));
  }

  public static String indexesToString(ArrayList<Integer> in){
    String out = "";
    for (Integer i: in){
      out += (String.valueOf(i) + " ");
    }
    return out;
  }

  public static void outputFile(String outFile){
    try {
      PrintStream out = new PrintStream(new FileOutputStream(outFile));
      System.setOut(out);
    }
    catch (FileNotFoundException e){
        System.out.println(e);
    }
  }

  public static void main(String args[]){
    outputFile(args[1]);
    ArrayList<String> lines = new ArrayList<String>();
    try {
      Scanner scanner = new Scanner(new File(args[0]));
      while (scanner.hasNextLine()) {
        lines.add(scanner.nextLine());
      }
    }
    catch (FileNotFoundException e){
        System.out.println(e);
    }
    //System.out.println(lines);

    for (int i = 0; i < lines.size(); i++){
      int size = Integer.parseInt(lines.get(i).substring(0, lines.get(i).indexOf(" ")));
      int capacity = Integer.parseInt(lines.get(i).substring(lines.get(i).indexOf(" ") + 1, lines.get(i).length()));
      problem = new Problem(0,0);
      problem.setProblemSize(size);
      problem.setProblemCapacity(capacity);
      for(int j = 0; j < size ; j++){
        i++;
        problem.addItem(new Item(Integer.parseInt(lines.get(i).substring(0, lines.get(i).indexOf(" "))), Integer.parseInt(lines.get(i).substring(lines.get(i).indexOf(" ") + 1, lines.get(i).length())), j+1));
      }

      switch (args[2]){
        case "0":
          //System.out.println("Calling Greedy algorithm 1.");
          //System.out.println("Problem size: " + problem.getProblemSize() + " \nProblem Capacity: " + problem.getProblemCapacity());
          //problem.printItems();
          greedyOne();
          break;
        case "1":
          //System.out.println("Calling Greedy algorithm 2.");
          //System.out.println("Problem size: " + problem.getProblemSize() + " \nProblem Capacity: " + problem.getProblemCapacity());
          //problem.printItems();
          greedyTwo();
          break;
        case "2":
          //System.out.println("Calling Backtracking algorithm.");
          //System.out.println("Problem size: " + problem.getProblemSize() + " \nProblem Capacity: " + problem.getProblemCapacity());
          //problem.printItems();
          backtracking();
          break;
        default:
          System.out.println("Unsupported 3rd argument.");
          break;
      }
    }

  }
}
