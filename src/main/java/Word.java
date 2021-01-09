import java.util.ArrayList;

public class Word {
    private String name; // full name of the word
    private char initial; // initial character of the word
    private int count; // count of the word in the text
    private int length; // length of the word
    private ArrayList<Integer> indexes = new ArrayList<>(); // all of the indexes of the word in given text

    public Word(String name) { // default constructor
        this.name = name;
        this.initial = name.charAt(0); // we've just set initial character and
        this.length = name.length(); // length by just this
    }

    void addIndex(int index) { // index adding method
        this.indexes.add(index); // since the length of the word is constant
        this.count++; // we'll be calculating length by initial character much more easier all thanks to these values
    }

    ArrayList<Integer> getIndexes() { // index getting method
        return this.indexes;
    } // to calculate minimum pair distance

    public char getInitial() { // to calculate average length by initial character
        return initial;
    }

    public int getLength() { // to calculate average length by initial character
        return length;
    }

    public int getCount() { // to calculate minimum pair distance
        return count;
    }

    @Override
    public String toString() { // instead of writing getName() method we can just simply use the name of the instance of the word
        return name;
    }

    @Override
    public boolean equals(Object object) { // to use ArrayList.contains(Word word)
        boolean isEqual = false;
        if (object != null && object instanceof Word) isEqual = ((Word) object).name.equals(this.name);
        return isEqual;
    }
}
