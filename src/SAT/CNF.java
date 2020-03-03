package SAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CNF {

    List<Set<Integer>> li = new ArrayList<>();
    int num = 0;

    @SafeVarargs
    public CNF(int numOfClauses, Set<Integer> ...s){
        for(Set<Integer> i : s){
            li.add(i);
        }
        this.num = numOfClauses;
    }

    //this constructor is for reading CNF from a file
    public CNF(int numOfClauses, List<Set<Integer>> li){
        this.li = li;
        this.num = numOfClauses;
    }

    public void add(Set<Integer> s){
        li.add(s);
    }

    public void remove(Set<Integer> s){
        li.remove(s);
    }

    public static Map<Integer, Boolean> GSAT(CNF c, int flips, int tries){
        Random rand = new Random();
        Map<Integer, Boolean> ass = new HashMap<>();
        Map<Integer, Boolean> assCopy = new HashMap<>();
        Map<Integer, Boolean> assCopy2 = new HashMap<>();
        //initiating hashmap assignment
        for(Set<Integer> s : c.li){
            for(int i : s){
                if(!ass.containsKey(Math.abs(i))){
                    ass.put(Math.abs(i), null);
                }
            }
        }
        //begin
        for(int i = 0; i < tries; i++){
            for(Map.Entry<Integer, Boolean> entry: ass.entrySet()){
                entry.setValue(rand.nextBoolean());
                assCopy.put(entry.getKey(), entry.getValue());
            }
            
            for(int j = 0; j < flips; j++){
                System.out.println(ass);
                if(satisfy(c, ass)){
                    System.out.println("The initial assignment Already satisfy");
                    return ass;
                }
                System.out.println("The initial assignment does not satisfy");
                System.out.println("Now there are only " + numOfSatisfy(c, ass) + " clauses satisfy");
                for(Map.Entry<Integer, Boolean> entry: ass.entrySet()){
                    assCopy.put(entry.getKey(), entry.getValue());
                    assCopy2.put(entry.getKey(), entry.getValue());
                }

                int temp = Integer.MIN_VALUE;
                for(int p : assCopy.keySet()){
                    assCopy.put(p, !assCopy.get(p));
                    System.out.println("We flip p's value and now it looks like " + assCopy);
                    if(satisfy(c, assCopy) || numOfSatisfy(c, assCopy) == c.num){
                        System.out.println("We found one! It looks like this " + assCopy);
                        return assCopy;
                    }else if(numOfSatisfy(c, assCopy) - numOfSatisfy(c, ass) > temp){ //the largest increase
                        temp = numOfSatisfy(c, assCopy) - numOfSatisfy(c, ass);
                        assCopy2 = assCopy;
                    }
                    assCopy = ass;
                }
                ass = assCopy2;         //with p reversed
            }
        }
        System.out.println("I'm sorry but I don't think there's a good assignment. Try again tho!");
        return ass;
    }

    private static int numOfSatisfy(CNF c, Map<Integer, Boolean> assCopy) {
        int counter = 0;                        //counting how many clauses satisfy
        for(Set<Integer> s  : c.li){
            Boolean boo = null;
            for(int i : s){
                Boolean temp2 = null;
                if(assCopy.containsKey(i)){
                    temp2 = assCopy.get(i);
                }else{
                    temp2 = !assCopy.get(-i);    //it is the case where only 
                }
                if(boo == null) boo = temp2;
                else{
                    boo = boo || temp2;
                }
            }
            if (boo == true) {
                counter++;
            }
        }
        return counter;
    }

    private static boolean satisfy(CNF c, Map<Integer, Boolean> ass) {
        //System.out.println(ass);
        Boolean total = null;
        for(Set<Integer> s : c.li){
            Boolean temp = null;
            for(int i : s){
                Boolean temp2 = null;
                if(ass.containsKey(i)){
                    temp2 = ass.get(i);
                }else{
                    temp2 = !ass.get(-i);    //it is the case where only 
                }
                if(temp == null) temp = temp2;
                else{
                    temp = temp || temp2;
                }
            }
            if(total == null){
                total = temp;
            }else{
                total = temp && total;
            }
        }
        return (total == true) ? true : false;
    }
}
