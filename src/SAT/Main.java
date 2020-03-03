package SAT;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    static int var = 0, clause = 0;

    public static void part1(String args) throws FileNotFoundException {
        File cnf = new File(args);
        readCNF(cnf);
    }

    public static CNF readCNF(File cnf) throws FileNotFoundException {
        Scanner scan = new Scanner(cnf);
        List<Set<Integer>> li = new ArrayList<>();

        //the whole list is a CNF, each set in the list is a clause
        while (scan.hasNextLine()) {
            String str = scan.nextLine();
            if(str.charAt(0) == 'p'){
                String[] arr = str.split(" ");
                var = Integer.parseInt(arr[2]);
                clause = Integer.parseInt(arr[3]);
                break;
            }
        }
        while (scan.hasNextLine()) {
            
            String str = scan.nextLine().trim();
            if(str.equals("")){break;}
            String[]temp = str.split("\\s+");
            Set<Integer> set = new HashSet<>();
            for(int i = 0; i < temp.length; i++){
                if(temp[i].equals("0")){
                    li.add(set);
                    continue;
                }
                set.add(Integer.parseInt(temp[i]));
            }
        }
        CNF c = new CNF(clause, li);
        System.out.println("READ SUCCESS, CREATING CNF NOW...");
        System.out.println(li);
        return c;
    }

    public static void part2(){
        System.out.println("This is Part 2 (1): \nShow that {P, P => Q}|= Q");
        System.out.println("Here I use X1 == P, X2 == Q\n");
        System.out.println("Therefore I need to show that {1, -1 V 2} => 2 ");
        Set<Integer> set1 = new HashSet<>();    set1.add(1);
        Set<Integer> set2 = new HashSet<>();    set2.add(-1); set2.add(2);
        CNF query = new CNF(1, set1);
        CNF cnf = new CNF(2, set1, set2);
        KnowledgeBase kb = new KnowledgeBase(cnf);
        if(KnowledgeBase.TTEntail(kb, query)){
            System.out.println("According to the algorithm: {P, P=>Q} entails Q");
        }
        reset();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");

        // 2. Wumpus world
        // first: show that the knowledge base entails -P1,2 and -P2,1, but not P2,2 or not P2,2
        System.out.println("This is Part2(2):\nShow that the knowledge base entails -P1,2 and -P2,1, but not P2,2 or not P2,2");
        System.out.println("First we will test that the KB when agent starts at [1,1] entails -P1,2 and -P2,1");
        System.out.println("I use P1,1 == X11\nP1,2 == X12\nP2,1 == X21\nP2,2 == X22\nP3,1 == X31\nP1,3 == X13");
        Set<Integer> R1 = new HashSet<>(Arrays.asList(-11));  
        Set<Integer> R3 = new HashSet<>(Arrays.asList(11, 22, 31));
        Set<Integer> R7 = new HashSet<>(Arrays.asList(11, 22, 13));

        Set<Integer> R2 = new HashSet<>(Arrays.asList(12, 21)); 
        Set<Integer> R12 = new HashSet<>(Arrays.asList(-12));
        Set<Integer> R21 = new HashSet<>(Arrays.asList(-21));
        Set<Integer> R22 = new HashSet<>(Arrays.asList(-22));
        Set<Integer> R13 = new HashSet<>(Arrays.asList(-13));

        //the first part
        List<CNF> listOfQuery = new ArrayList<>();                  //list of queries to test
        CNF cnf1 = new CNF(3, R1, R12, R21);     
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(22))));     
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(-22))));
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(-12))));
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(-21))));
        KnowledgeBase kb1 = new KnowledgeBase(cnf1);

        System.out.println("Now the agent is in [1,1]");
        for(CNF q : listOfQuery){
            if(KnowledgeBase.TTEntail(kb1, q)){
                System.out.println("The current Knowledge base entails" + q.li);
            }else{
                System.out.println("The current Knowledge base doesn't entail" + q.li);
            }
        }
        reset();

        //the second part
        System.out.println("Now the agent is in [2,1]");
        listOfQuery.clear();
        CNF cnf2 = new CNF(4, R3, R1, R12,R21);
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(22, 31))));  
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(22))));
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(-22))));
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(31))));
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(-31))));
        KnowledgeBase kb2 = new KnowledgeBase(cnf2);
        for(CNF q : listOfQuery){
            if(KnowledgeBase.TTEntail(kb2, q)){
                System.out.println("The current Knowledge base entails" + q.li);
            }else{
                System.out.println("The current Knowledge base doesn't entail" + q.li);
            }
        }
        reset();

        System.out.println("Now the agent is in [1,2]");
        listOfQuery.clear();
        CNF cnf3 = new CNF(6, R1,R3,R12,R21,R22,R13);
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(31)))); 
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(-22)))); 
        KnowledgeBase kb3 = new KnowledgeBase(cnf3);
        for(CNF q : listOfQuery){
            if(KnowledgeBase.TTEntail(kb3, q)){
                System.out.println("The current Knowledge base entails" + q.li);
            }else{
                System.out.println("The current Knowledge base doesn't entail" + q.li);
            }
        }
        reset();

        //3. Unicorn
        //KB: 1. -Mythical V -mortal                   1 = Mythical
        //    2. Mythical V Mortal                     2 = Mortal
        //    3. Mythical V mammal                     3 = Mammal
        //    4. Mortal V horned                       4 = Horned
        //    5. -Mammal V horned                      5 = magical
        //    6. -horned V magical
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Horn clause problem");
        System.out.println("I use the following representation: \n1 = Mythical\n2 = Mortal\n3 = Mammal\n4 = Horned\n5 = magical");
        listOfQuery.clear();
        Set<Integer> H1 = new HashSet<>(Arrays.asList(-1, -2));
        //Set<Integer> H2 = new HashSet<>(Arrays.asList(1, 2));
        Set<Integer> H3 = new HashSet<>(Arrays.asList(1, 3));
        Set<Integer> H4 = new HashSet<>(Arrays.asList(2, 4));
        Set<Integer> H5 = new HashSet<>(Arrays.asList(-3, 4));
        Set<Integer> H6 = new HashSet<>(Arrays.asList(-4, 5));
        KnowledgeBase h = new KnowledgeBase(new CNF(6, H1,H3,H4,H5,H6));
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(1))));
        listOfQuery.add(new CNF(1, new HashSet<>(Arrays.asList(4))));
        for(CNF q : listOfQuery){
            if(KnowledgeBase.TTEntail(h, q)){
                System.out.println("The current Knowledge base entails " + q.li);
            }else{
                System.out.println("The current Knowledge base doesn't entail " + q.li);
            }
        }
    }

    public static void part3(String args) throws FileNotFoundException {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("This is part 3 Q1");
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1 ,3, -4));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(4));
        Set<Integer> set3 = new HashSet<>(Arrays.asList(2, -3));
        CNF cnf = new CNF(3, set1, set2, set3);
        CNF.GSAT(cnf, 5, 5);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
        CNF.GSAT(readCNF(new File(args)), 100, 100);
        // System.out.println("Now, we are trying on the N-Queen one");
        // File nqueens4 = new File("/Users/jiahaolu/Desktop/CSC242/CSC242_Project_2_cnf/nqueens/nqueens_4.cnf.txt");
        // CNF.GSAT(readCNF(nqueens4), 100, 100);
        // System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
        // System.out.println("This time, we are trying to read Quinn.cnf and let's see what happen");
        // File quinn = new File("/Users/jiahaolu/Desktop/CSC242/CSC242_Project_2_cnf/cnf/quinn.cnf.txt");
        // //CNF.GSAT(readCNF(quinn), 50, 50);
        // System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
        // System.out.println("Now, we are trying on the aim one");
        // File aim = new File("/Users/jiahaolu/Desktop/CSC242/CSC242_Project_2_cnf/cnf/aim.cnf.txt");
        // //CNF.GSAT(readCNF(aim), 200, 200);
    }
 
    public static void reset(){
        KnowledgeBase.model.clear();
        KnowledgeBase.modelCopy.clear();
        KnowledgeBase.symbols.clear();
        KnowledgeBase.symbolsCopy.clear();
        KnowledgeBase.symbolsList.clear();
        KnowledgeBase.symbolsListCopy.clear();
        KnowledgeBase.track.clear();
    }
    public static void main(String[] args) throws Exception {
        System.out.println("Which part do you want to test first? Type 1, 2 or 3. Type 0 for everything all together\nDon't forget to put in your command line test file for part 3\nRemember that if you don't get assignment, do try again\nI have listed my success assignment in README");
        Scanner scan = new Scanner(System.in);
        int read = scan.nextInt();
        switch (read) {
            case 1:
                part1(args[0]);
                break;
            case 2:
                part2();
                break;
            case 3: 
                part3(args[0]);
                break;
            case 0:
                part1(args[0]);
                part2();
                part3(args[0]);
                break;
        }
        scan.close();
    }
}