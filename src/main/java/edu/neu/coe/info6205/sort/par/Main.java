package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
class Quadruple {
    public long time;
    public int thread;
    public int cutoff;
    int size;

    public Quadruple(long time,int thread,int cutoff,int size) {
        this.time=time;
        this.thread=thread;
        this.cutoff=cutoff;
        this.size=size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("Size: "+size);
        sb.append("\nCutoff: "+cutoff);
        sb.append("\nThread: "+thread);
        sb.append("\nTime taken: "+time);
        return sb.toString();
    }
}
class Triple {
    public long time;
    public int cutoff;
    int size;

    public Triple(long time,int cutoff,int size) {
        this.time=time;
        this.cutoff=cutoff;
        this.size=size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("Size: "+size);
        sb.append("\nCutoff: "+cutoff);
        sb.append("\nTime taken: "+time);
        return sb.toString();
    }
}
public class Main {

    public static void main(String[] args) {
        processArgs(args);
        int[] threadSize={2};
        //int[] arraySize={10,100,1000,10000,100000};
        int[] arraySize=new int[1000];
        int[] cutoffs=new int[20];
        for(int i=2,count=0;count<20;i*=2,count++) {
            cutoffs[count]=i;
        }
        arraySize[0]=10;
        for(int size=100,count=1;size<1000;size++,count++) {
            arraySize[count]=size;
        }
        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        Random random = new Random();
        int[] originalArray = new int[2000000];
        ArrayList<Long> timeList = new ArrayList<>();
       // ArrayList<Integer> threadList = new ArrayList<>();
       // ArrayList<Integer>
       // ArrayList<Triple> tripleList = new ArrayList<>();
//        for (int j = 50; j < 100; j++) {
//            ParSort.cutoff = 10000 * (j + 1);
//             for (int i = 0; i < originalArray.length; i++) originalArray[i] = random.nextInt(10000000);
//            long time;
//            long startTime = System.currentTimeMillis();
//            for (int t = 0; t < 10; t++) {
//                for (int i = 0; i < originalArray.length; i++) originalArray[i] = random.nextInt(10000000);
//                ParSort.sort(originalArray, 0, originalArray.length);
//            }
//            long endTime = System.currentTimeMillis();
//            time = (endTime - startTime);
//            timeList.add(time);
//
//
//            System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10times Time:" + time + "ms");
//
//        }
        int bestCutoff;
        ArrayList<Quadruple> bestList = new ArrayList<>();
        ArrayList<Triple> bestTripleList = new ArrayList<>();
        for(int size: arraySize) {
            int[] array = new int[size];
            for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
            ArrayList<Integer> cutoffList = new ArrayList<>();
            ArrayList<Long> timeCutoffList = new ArrayList<>();
            System.out.println("Size: "+size+":\n");
            for(int cutoff: cutoffs) {
                    long time;
                    ParSort.cutoff=cutoff;
                    long startTime = System.currentTimeMillis();
                    for (int t = 0; t < 10; t++) {
                        for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                        ParSort.sort(array, 0, array.length);
                    }
                    long endTime = System.currentTimeMillis();
                    time = (endTime - startTime);
                    //tripleList.add(new Triple(time,thread,cutoff,size));
                    timeList.add(time);
                    cutoffList.add(cutoff);
                    timeCutoffList.add(time);

                    System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10times Time:" + time + "ms");

            }
            long bestTime=Collections.min(timeCutoffList);
            int bestTimeIndex=timeCutoffList.indexOf(bestTime);
            bestCutoff=cutoffList.get(bestTimeIndex);
            System.out.println("Best cutoff for size: " +size+" is: "+bestCutoff);





            ArrayList<Long> timeThreadList = new ArrayList<>();
            ArrayList<Integer> threadList = new ArrayList<>();
            for(int thread: threadSize) {
                ParSort.pool = new ForkJoinPool(thread);

                System.out.println("Degree of parallelism: "+ ParSort.pool.getParallelism());
                long time;
                ParSort.cutoff=bestCutoff;
                long startTime = System.currentTimeMillis();
                for (int t = 0; t < 10; t++) {
                    for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                    ParSort.sort(array, 0, array.length);
                }
                long endTime = System.currentTimeMillis();
                time = (endTime - startTime);

                timeThreadList.add(time);
                threadList.add(thread);

                ParSort.cutoff=bestCutoff;
                //ParSort.pool.shutdown();
            }
            bestTime=Collections.min(timeThreadList);
            bestTimeIndex=timeThreadList.indexOf(bestTime);
            int bestThread=threadList.get(bestTimeIndex);

            System.out.println("Best thread for size: " +size+" is: "+bestThread + "\nBest cutoff is: "+bestCutoff);
            long startTime = System.currentTimeMillis();
            for (int t = 0; t < 10; t++) {
                for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                ParSort.sort(array, 0, array.length);
            }
            long endTime = System.currentTimeMillis();
            long time = (endTime - startTime);
            bestTripleList.add(new Triple(time,bestCutoff,size));
        }
//        for(Quadruple quad: bestList) {
//            System.out.println(quad);
//        }
        for(Triple triple: bestTripleList) {
            System.out.println(triple);
        }
        try {
            FileOutputStream fis = new FileOutputStream("./src/result.csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            int j = 0;
            for (long i : timeList) {
                String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
                j++;
                bw.write(content);
                bw.flush();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processArgs(String[] args) {
        if(args==null)return;
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}
