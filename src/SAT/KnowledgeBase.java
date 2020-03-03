package SAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KnowledgeBase{

    CNF cnf;
    boolean kb = false;
    public KnowledgeBase(CNF cnf){
        this.cnf = cnf;
    }

    static Set<Integer> symbols = new HashSet<>();
    static Set<Integer> symbolsCopy = new HashSet<>();
    static List<Integer> symbolsList;
    static List<Integer> symbolsListCopy;
    static Map<Integer, Boolean> model = new HashMap<>();
    static Map<Integer, Boolean> modelCopy = new HashMap<>(); // deep copy for TTcheck
    static List<Integer> track = new ArrayList<>();

    public static boolean TTEntail(KnowledgeBase b, CNF q) {
        for (Set<Integer> s : b.cnf.li) {
            symbols.addAll(s);
            symbolsCopy.addAll(s);
        }
        for (Set<Integer> s : q.li) {
            symbols.addAll(s);
            symbolsCopy.addAll(s);
        }
        for (int i : symbols) {
            model.put(i, null);
            modelCopy.put(i, null);
        }
        symbolsList = new ArrayList<>(symbols);
        symbolsListCopy = new ArrayList<>(symbolsCopy);
        return TTCheck(b, q, symbolsList, model);
    }

    public static boolean TTCheck(KnowledgeBase b, CNF q, List<Integer> symbols, Map<Integer, Boolean> model){
        if(symbols.isEmpty()){
            Boolean kb = null;
            //starting to check if the knowledge base is true in this model
            //for every clauses in the CNF, we && them
            for(Set<Integer> s : b.cnf.li){
                Boolean temp = null;
                for(int i : s){
                    if(temp == null) temp = model.get(i);
                    else{
                        try {
                            temp = temp || model.get(i);
                        } catch (NullPointerException e) {
                            return true;
                        }
                    }
                }
                try {
                    if(kb == null){ kb = temp;} else{ kb = temp && kb; }
                } catch (NullPointerException e) {
                    return true;
                }
            }
            model.put(50, kb);
            if(kb == true) {
                return PL(q, model);
            }
            return true;    //when KB is false, always return true
        }else{
            // Iterator<Integer> it = symbols.iterator();
            // int p = it.next(); 
            int p = symbols.get(0); 
            symbols.remove(0);
            if(track.isEmpty()){
                track.add(p);
            }else if(!track.contains(p)){
                track.add(p);
            }
            model.put(p, true);
            if(model.containsKey(-p)){
                model.put(-p, false);
                int index = symbols.indexOf(-p);
                if(index != -1) symbols.remove(index);  
                if(track.isEmpty()){
                    track.add(-p);
                }else if(!track.contains(-p)){
                    track.add(-p);
                }
            }
            //System.out.println("New Loop: This is track" + track);
            Map<Integer, Boolean> modelCopy = new HashMap<>();
            for(Map.Entry<Integer, Boolean> entry: model.entrySet()){
                if(entry.getKey() == p){
                    modelCopy.put(p, false);
                }else if(entry.getKey() == -p){
                    modelCopy.put(-p, true);
                }else{
                    modelCopy.put(entry.getKey(), entry.getValue());
                }
            }
            //System.out.println("I'm generating these models");
            //System.out.println("Model      == " + model);
            boolean boo1 = TTCheck(b, q, symbols, model);
            boolean boo2 = TTCheck(b, q, symbols, modelCopy);
            symbols.add(0, track.get(track.size()-1));
            modelCopy.put(track.get(track.size()-1), null);
            track.remove(track.size()-1);
            return boo1 && boo2;
        }
    }

    private static boolean PL(CNF b, Map<Integer, Boolean> model) {
        //means it is a query because only query has at most one set of integer
        if(b.li.size() == 1){
            Boolean temp = null;
            for(int i : b.li.get(0)){
                if(temp == null) temp = model.get(i);
                else{
                    temp = temp || model.get(i);
                }
            }
            if(temp == true){
                return true;
            }
        }
        return false;
    }

}