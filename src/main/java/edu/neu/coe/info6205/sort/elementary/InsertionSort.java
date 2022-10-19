/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.util.Config;
import edu.neu.coe.info6205.util.Stopwatch;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.List;

public class InsertionSort<X extends Comparable<X>> extends SortWithHelper<X> {

    /**
     * Constructor for any sub-classes to use.
     *
     * @param description the description.
     * @param N           the number of elements expected.
     * @param config      the configuration.
     */
    protected InsertionSort(String description, int N, Config config) {
        super(description, N, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public InsertionSort(int N, Config config) {
        this(DESCRIPTION, N, config);
    }

    public InsertionSort(Config config) {
        this(new BaseHelper<>(DESCRIPTION, config));
    }

    /**
     * Constructor for InsertionSort
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public InsertionSort(Helper<X> helper) {
        super(helper);
    }

    public InsertionSort() {
        this(BaseHelper.getHelper(InsertionSort.class));
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();

        // FIXME
        for (int i = from+1; i < to; i++) {
            for(int j=i; j>from; j--) {
                if(helper.swapStableConditional(xs,j));
                else
                    break;
            }
        }
        // END 
    }

    public static final String DESCRIPTION = "Insertion sort";

    public static <T extends Comparable<T>> void sort(T[] ts) {
        new InsertionSort<T>().mutatingSort(ts);
    }

    public boolean check(X[] xs) {
        return getHelper().sorted(xs);
    }

    public int[] reverse(int[] arr) {
        int size=arr.length;
        for(int start=0,end=size-1,temp;start<end;start++,end--) {
            temp=arr[start];
            arr[start]=arr[end];
            arr[end]=temp;
        }
        return arr;
    }

    public static void main(String[] args) {
        if (args.length == 0)
            throw new RuntimeException("Syntax: Arguments not specified");
        int size = Integer.parseInt(args[0]);
        InsertionSort object = new InsertionSort();

        Random rand = new Random();
        int[] random = new int[size];

        for(int index=0;index<size;index++)
            random[index]=rand.nextInt();

        int[] ordered = random.clone();
        Arrays.sort(ordered);

        int[] reverse = ordered.clone();
        reverse= object.reverse(reverse);

        int[] partiallyOrdered = random.clone();

        int startIndex=rand.nextInt(size);
        int stopIndex=rand.nextInt(size);

        if(startIndex==stopIndex) {
            while(startIndex!=stopIndex)
                stopIndex=rand.nextInt(size);
        }
        if(startIndex>stopIndex) {
            int temp= startIndex;
            startIndex=stopIndex;
            stopIndex=temp;
        }

        Arrays.sort(partiallyOrdered,startIndex,stopIndex);

        long randomTime,orderedTime,partiallyOrderedTime,reverseOrderedTime;

        Integer[] randomArray = Arrays.stream(random)
                .boxed()
                .toArray(Integer[]::new);
        Integer[] orderedArray = Arrays.stream(ordered)
                .boxed()
                .toArray(Integer[]::new);
        Integer[] partiallyOrderedArray = Arrays.stream(partiallyOrdered)
                .boxed()
                .toArray(Integer[]::new);
        Integer[] reverseArray = Arrays.stream(reverse)
                .boxed()
                .toArray(Integer[]::new);

//        System.out.println(object.check(randomArray));
//        System.out.println(object.check(orderedArray));
//        System.out.println(object.check(partiallyOrderedArray));
//        System.out.println(object.check(reverseArray));
        Stopwatch timer = new Stopwatch();

        object.sort(randomArray,0,size);
        randomTime=timer.lap();
        object.sort(orderedArray,0,size);
        orderedTime=timer.lap();
        object.sort(partiallyOrderedArray,0,size);
        partiallyOrderedTime=timer.lap();
        object.sort(reverseArray,0,size);
        reverseOrderedTime=timer.lap();

        System.out.println("Size of the arrays: "+ size);
        System.out.println("Time taken to sort random array: "+randomTime);
        //System.out.println(object.check(randomArray));
        System.out.println("Time taken to sort ordered array: "+orderedTime);
        //System.out.println(object.check(orderedArray));
        System.out.println("Time taken to sort partially ordered array: "+partiallyOrderedTime);
        //System.out.println(object.check(partiallyOrderedArray));
        System.out.println("Time taken to sort reverse order array: "+reverseOrderedTime);
        //System.out.println(object.check(reverseArray));


        timer.close();

    }
}
