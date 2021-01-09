import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    List<Word> vocabulary = new ArrayList<>(); // we'll putting every different word into this local variable

    public Main(String fileName, int topN) throws IOException {
        processWords(fileName); // getting all of the words and saving them into words arraylist
        computeAvgLengthByFirstChar(); // computing average lengths by first character
        Set pairs = calculateMinPairDist(); // calculating minimum pair distances and saving them into a set
        printTopN(pairs, topN); // printing the topN biggest pair distances
    }

    public static void main(String[] args) throws IOException {
        System.setOut(new PrintStream(System.out, true, "UTF-8")); // to avoid platform (OS) dependent outputs like yerleşkesinde -> yerle?kesinde
        try { // to prevent NumberFormatException
            if (args.length != 2 || Integer.parseInt(args[1]) < 0) { // checking both arguments
                System.out.println("Please provide a path of file and desired N which denotes the positive number of biggest minimum pair distances." +
                        "\nAs an example: \"java -jar bim207hw.jar sampleText.txt 10\"");
                return;
            }
            int topN = Integer.parseInt(args[1]);
            new Main(args[0], topN);
        } catch (NumberFormatException exception) {
            System.out.println("Please provide a positive integer as second argument.");
        }
    }

    private void processWords(String fileName) throws IOException {
        if (!Paths.get(fileName).toFile().exists()) { // we have to consider does the file exist according to the given path or not
            System.out.println("The given path of the file '" + fileName + "' is either wrong or the file is not exists.");
            System.exit(0); // since the given path is wrong and file is not reachable we have to stop the processes
        }
        List<String> wordsSimple = new ArrayList<>(); // All of the words written in the given file without punctuation or white space
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)));
        List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        for (String line : lines) {
            String[] array = line.replaceAll("\\p{Punct}", "").toLowerCase(new Locale("tr", "TR")).split("\\s+"); // removing punctuations with white space and saving the all words in an array by splitting by spaces between them
            wordsSimple.addAll(Arrays.asList(array)); // adding all the words into an arraylist to access them in other methods
        }
        scanner.close(); // closing the scanner to prevent memory leak
        for (int i = 0; i < wordsSimple.size(); i++) {
            Word word = new Word(wordsSimple.get(i));
            if (vocabulary.contains(word)) {
                Word existing = vocabulary.get(vocabulary.indexOf(word));
                existing.addIndex(i);
                vocabulary.add(vocabulary.indexOf(word), existing);
                continue;
            }
            word.addIndex(i);
            vocabulary.add(word);
        }
    }

    private void computeAvgLengthByFirstChar() {
        TreeMap<Character, String> totalValues = new TreeMap<>(); // TreeMap is a best choice in here due to it's automatically sorts the unique first value (key)

        for (Word word : vocabulary) {
            String innerInformation = totalValues.getOrDefault(word.getInitial(), ""); // if the word's initial character does not exist in the map it's will be just "" empty String
            if (!innerInformation.equals("")) { // rather than using nested maps in the map above, I've just create a basic methodology to store and access all the necessary values in this function by parsing them from String
                String[] array = innerInformation.split(" "); // "(total lengths) (total words)" -> "15 3" -> this is the basic idea of the methodology
                int totalLength = Integer.parseInt(array[0]) + word.getLength(); // -> total lengths
                int totalWords = Integer.parseInt(array[1]) + 1; // -> total words + 1 (since we've been found a new one)
                totalValues.put(word.getInitial(), totalLength + " " + totalWords); // "(total lengths + totalLength) (total words)" -> "10 2"
            } else {
                totalValues.put(word.getInitial(), word.getLength() + " " + 1); // (total lengths) (total words) -> "5 1"
            }
        }
        System.out.println("InitialCharacter AverageLength");
        for (Character ch : totalValues.keySet()) {
            String innerInformation = totalValues.get(ch);
            String[] array = innerInformation.split(" "); // totalLength / totalWords
            System.out.println(ch + " " + (double) Integer.parseInt(array[0]) / Integer.parseInt(array[1]));
        }
    }

    private Set calculateMinPairDist() { // to calculate minimum pair distance to according formula
        Set<Pair<Word, Word>> pairs = new LinkedHashSet<>(); // we'll be using LinkedHashSet which is the most suitable data structure for this situation.
        for (int i = 0; i < vocabulary.size(); i++) {
            Word t1 = vocabulary.get(i);
            for (int j = 0; j < vocabulary.size(); j++) {
                Word t2 = vocabulary.get(j);
                if (t1 == t2) continue; // we don't want to pair exact the same word with itself
                Pair<Word, Word> pair = new Pair<>(t1, t2); // when we create a pair minimum pair distance will automatically calculated
                pairs.add(pair); // we don't have to check if pairs contains pair it's already checks if the pair will be added is already in exists in pairs or not
            }
        }
        return pairs; // returning pairs as expected
    }

    private void printTopN(Set<Pair> pairs, int topN) { // to print topN greatest pairs according to their factors
        ArrayList<Pair> sortedPairs = new ArrayList<Pair>(pairs); // we just copy all of the elements of set into arraylist
        Collections.sort(sortedPairs); // and we use the sorting mechanism of collection interface
        for (int i = 0; i < topN; i++) { // afterwards we can just print topN pairs
            System.out.println(sortedPairs.get(i));
        }
    }
}