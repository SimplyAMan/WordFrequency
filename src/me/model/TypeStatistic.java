package me.model;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class TypeStatistic {
    private String type;         // We will treat it as a filename.
    private String resultPath;   // The result.txt's path.
    private int wordNumber = 15; // The default number of word we will write.

    public TypeStatistic(String resultPath, String type) {
        this.resultPath = resultPath;
        this.type = type;
    }

    // Create the type file.
    private String createTypeFile() {
        String path = "./result/type/" + this.type;
        try {
            File file = new File(path);
            if (!file.exists())
                file.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return path;
    }

    private ArrayList<String> readTypeFile() {
        String path = this.createTypeFile();
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

    public void setWordNumber(int number) {
        this.wordNumber = number;
    }

    public void writeWords() {
        String path = this.createTypeFile();
        ArrayList<String> wordsExist = this.readTypeFile();
        ProcessResult result = new ProcessResult(resultPath, wordNumber);
        ArrayList<String> words = result.getWordsArray();

        try {
            FileWriter fw = new FileWriter(path, true); // append
            BufferedWriter bw = new BufferedWriter(fw);

            for (String word : words) {
                if (!wordsExist.contains(word)) {
                    bw.write(word);
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /* Unit Tests */
    public static void main(String[] args) {
        TypeStatistic statistic = new TypeStatistic("./result.txt", "science");
        statistic.setWordNumber(100);
        statistic.writeWords();
    }
}
