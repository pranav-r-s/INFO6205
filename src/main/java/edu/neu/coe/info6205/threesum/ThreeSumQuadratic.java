package edu.neu.coe.info6205.threesum;

import edu.neu.coe.info6205.util.Stopwatch;

import java.util.*;
import java.util.function.Supplier;

/**
 * Implementation of ThreeSum which follows the approach of dividing the solution-space into
 * N sub-spaces where each sub-space corresponds to a fixed value for the middle index of the three values.
 * Each sub-space is then solved by expanding the scope of the other two indices outwards from the starting point.
 * Since each sub-space can be solved in O(N) time, the overall complexity is O(N^2).
 * <p>
 * NOTE: The array provided in the constructor MUST be ordered.
 */
public class ThreeSumQuadratic implements ThreeSum {
    /**
     * Construct a ThreeSumQuadratic on a.
     * @param a a sorted array.
     */
    public ThreeSumQuadratic(int[] a) {
        this.a = a;
        length = a.length;
    }

    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length; i++) triples.addAll(getTriples(i));
        Collections.sort(triples);
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    /**
     * Get a list of Triples such that the middle index is the given value j.
     *
     * @param j the index of the middle value.
     * @return a Triple such that
     */
    public List<Triple> getTriples(int j) {
        List<Triple> triples = new ArrayList<>();
        // FIXME : for each candidate, test if a[i] + a[j] + a[k] = 0.
        Set<Integer> storeElements=new HashSet<>();

        for(int i=0;i<j;i++)
            storeElements.add(a[i]);

        for(int k=j+1;k<length;k++) {
            int target=-a[j]-a[k];
            if(storeElements.contains(target))
                triples.add(new Triple(target,a[j],a[k]));
        }



        // END 
        return triples;
    }

    private final int[] a;
    private final int length;

    public static void main(String[] args) {
        if (args.length == 0)
            throw new RuntimeException("Syntax: Arguments not specified");
        int size = Integer.parseInt(args[0]);
        Supplier<int[]> intsSupplier = new Source(size, 1000).intsSupplier(10);
        int[] ints = intsSupplier.get();
        ThreeSum target = new ThreeSumQuadratic(ints);
        Stopwatch timer = new Stopwatch();
        target.getTriples();
        System.out.println("Timer: "+timer.lap());
        timer.close();
    }
}
