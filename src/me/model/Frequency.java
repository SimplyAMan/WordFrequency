package me.model;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Frequency class is the kernel of this project.
 * We create a Frequency object using a path of a text file.
 * Then, the Frequency object will calculate each word's frequency.
 * We can generate a result file just using the method "writeResult(path)".
 *
 * Usage:
 * Frequency fre = new Frequency("text.txt");
 * fre.writeResult("result.txt");
 */
public class Frequency {
    /** Instance field */
    private String targetPath;           // The 'words file' path.
    private Set<String> meanlessWords; // Useless words such as 'a' 'an' 'the' and so on.

    private HashMap<String, Integer> wordsMap;  // Unsorted RESULT Collection. Words and Words number.
    private SortedSet<Map.Entry<String, Integer>> wordsSet; // Sorted RESULT Collection. Words and words number.

    /** Constructor */
    public Frequency(String targetPath) {
        this.targetPath = targetPath;

        this.wordsMap  = new HashMap<String, Integer>();
        this.wordsSet  = new TreeSet<Map.Entry<String, Integer>>(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                if (e1.getValue() >= e2.getValue())
                    return -1;
                else
                    return 1;
            }
        });

        this.setMeanlessWords();
        this.setFrequencyMap();
        this.setWordsSet();
    }

    /** Mutator */
    /* Read words form a file, and return a word list */
    private ArrayList<String> getWordsListFromFile(String path) {
        ArrayList<String> words = new ArrayList<String>();

        try {
            Scanner scanner = new Scanner(new File(path));
            Pattern pattern = Pattern.compile("[^a-zA-Z']");
            scanner.useDelimiter(pattern);

            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();
                words.add(word);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return words;
    }

    /* Add all meanless words from the file "./config/meanless"
     * to the meanlessWords set. */
    private void setMeanlessWords() {
        ArrayList<String> words = getWordsListFromFile("./config/meanless");
        this.meanlessWords = new HashSet<String>();

        for (String word : words) {
            this.meanlessWords.add(word);
        }
    }

    /* Avoid meanless words and letters */
    private boolean wordsFilter(String word) {
        if (word.length() > 2 && !meanlessWords.contains(word))
            return true;
        else
            return false;
    }

    /* Caculate Words and Store the result in a HashMap. */
    private void setFrequencyMap() {
        ArrayList<String> words = getWordsListFromFile(targetPath);
        for (String word : words)
            if (wordsFilter(word)) {
                if (wordsMap.containsKey(word)) {
                    int cnt = wordsMap.get(word); cnt++;
                    wordsMap.put(word, (Integer) cnt);
                } else {
                    wordsMap.put(word, 1);
                }
            }
    }

    /* Sort the HashMap and generate a SortedSet. */
    private void setWordsSet() {
        wordsSet.addAll(wordsMap.entrySet());
    }

    /* Generate a file contains the final result */
    public void writeResult(String path) {
        try {
            Iterator<Map.Entry<String, Integer>> it = wordsSet.iterator();
            FileWriter fw  = new FileWriter(path);
            PrintWriter pw = new PrintWriter(fw);

            while (it.hasNext()) {
                Map.Entry<String, Integer> m = it.next();
                pw.println(m.getKey() + ":" + m.getValue());
            }

            pw.close();
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /** Unit Tests */
    public static void main(String[] args) {
        // ATTENTION the path when testing.
        Frequency fre = new Frequency("./test.txt");
        fre.writeResult("./result.txt");
    }
}
