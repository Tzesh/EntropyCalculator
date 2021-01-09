import java.util.ArrayList;
import java.util.Objects;

public class Pair<V extends Word, K extends Word> implements Comparable<Pair> { // Created a pair class which consists the methods below.
    double formula; // value of the formula
    private V t1; // t1 according to the given formula
    private K t2; // t2 according to the given formula

    public Pair(V v, K k) { // default constructor
        this.t1 = v;
        this.t2 = k;
        this.formula = calculateFormula();
    }

    private double calculateFormula() { // It's really necessary
        ArrayList<Integer> t2Indexes = t2.getIndexes(); // we're getting indexes of t2
        ArrayList<Integer> t1Indexes = t1.getIndexes(); // we're getting indexes of t1
        ArrayList<Integer> pairs = new ArrayList<>(); // and we'll be storing all the pairs to prevent mismatching them
        int totalDistance = 0;
        for (int t2Index : t2Indexes) {
            for (int t1Index : t1Indexes) {
                if (t2Index - t1Index > 0 && !pairs.contains(t2Index) && !pairs.contains(t1Index)) { // since our indexes are sorted in the first place
                    totalDistance += t2Index - t1Index; // we just checking if t2 - t1 bigger than zero and is any of the indexes have been paired before
                    pairs.add(t2Index); // if they not we're summing all the total distances
                    pairs.add(t1Index); // and adding both index to the pairs
                }
            }
        }
        return (t1.getCount() * t2.getCount()) / (1 + (Math.log(totalDistance))); // and our formula finally can be calculated
    }

    @Override
    public String toString() { // to easily print every pair
        return "Pair{t1='" + t1 + "', " + "t2='" + t2 + "'" + ", factor=" + formula + "}"; // as it's given output example
    }

    @Override
    public int compareTo(Pair obj) {
        return Double.compare(obj.formula, formula); // to use Collections.sort and sort with descending order
    }

    @Override
    public boolean equals(Object object) { // to use ArrayList.contains(Word word)
        boolean isEqual = false;
        if (object != null && object instanceof Pair) {
            isEqual = t1.equals(((Pair) object).t1) && t2.equals(((Pair) object).t2);
        }
        return isEqual;
    }

    @Override
    public int hashCode() { // since we're storing our pairs in a LinkedHashSet hashCode will be used
        return Objects.hash(t1, t2, formula);
    }
}
