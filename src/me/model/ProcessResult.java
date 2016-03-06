package me.model;

import java.io.*;
import java.util.*;

/*
 * This class is intended exactly to process the 'result.txt'.
 */
public class ProcessResult {
    private HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
    private ArrayList<String> words = new ArrayList<String>();
    private ArrayList<Integer> frequency = new ArrayList<Integer>();
    private int lineNumber = 0;

    /* Constructor 1 */
    public ProcessResult(String path) {
        this.resultMap.clear();  // init the hashmap
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                String[] splited = line.split(":");
                resultMap.put(splited[0], Integer.parseInt(splited[1]));
                lineNumber++;
            }
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /* Constructor 2 */
    public ProcessResult(String path, int num) {
        this(path);
        this.resultMap.clear();  // init the hashmap
        if (num > this.lineNumber) num = this.lineNumber;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            for (int i = 0; i < num; i++) {
                String line = br.readLine();
                String[] splited = line.split(":");
                resultMap.put(splited[0], Integer.parseInt(splited[1]));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
     * I think the function returns a SortedSet is pretty good, because if
     * it returns the hashmap, you will get an unsorted result when iterating it.
     */
    public SortedSet<Map.Entry<String, Integer>> getResultSet() {
        SortedSet<Map.Entry<String, Integer>> resultSet = new TreeSet<Map.Entry<String, Integer>>(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                if (e1.getValue() >= e2.getValue())
                    return -1;
                else
                    return 1;
            }
        });

        resultSet.addAll(resultMap.entrySet());
        return resultSet;
    }

    /*
     * Set array list.
     */
    private void setArrayList() {
        SortedSet<Map.Entry<String, Integer>> aset = this.getResultSet();
        Iterator<Map.Entry<String, Integer>> it = aset.iterator();

        while (it.hasNext()) {
            Map.Entry<String, Integer> m = it.next();
            words.add(m.getKey());
            frequency.add(m.getValue());
        }
    }

    /*
     * Return the words array.
     * Especially useful for building a Jtable.
     */
    public ArrayList<String> getWordsArray() {
        if (this.words.isEmpty() || this.frequency.isEmpty()) {
            this.setArrayList();
        }

        return words;
    }

    /*
     * Return the frequency array.
     * Especially useful for building a Jtable.
     */
    public ArrayList<Integer> getFrequencyArray() {
        if (this.words.isEmpty() || this.frequency.isEmpty()) {
            this.setArrayList();
        }

        return frequency;
    }

    /* Just for Objects constructed by Constructor 1 */
    public int getLineNumber() {
        return lineNumber;
    }

    /** Unit Test */
    public static void main(String[] args) {
        /* First Test */
        ProcessResult result = new ProcessResult("./result.txt", 15);
        SortedSet<Map.Entry<String, Integer>> wordsSet = result.getResultSet();
        Iterator<Map.Entry<String, Integer>> it = wordsSet.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> m = it.next();
            System.out.println(m.getKey() + m.getValue());
        }

        /* Second Test */
        ProcessResult re = new ProcessResult("./result.txt");
        ArrayList<String> wo = re.getWordsArray();
        ArrayList<Integer> fr = re.getFrequencyArray();
        System.out.println(wo);
        System.out.println(fr);
        System.out.println(re.getLineNumber());
    }
}
