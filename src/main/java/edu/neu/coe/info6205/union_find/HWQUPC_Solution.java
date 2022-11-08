package edu.neu.coe.info6205.union_find;

import java.util.Random;

public class HWQUPC_Solution {
    public static int count(int n) {
        int connections=0;
        UF_HWQUPC forest = new UF_HWQUPC(n);
        Random random = new Random();
        while(forest.components()!=1) {
            int siteOne = random.nextInt(n);
            int siteTwo = random.nextInt(n);

            if(siteOne==siteTwo)
                continue;
            if(!forest.isConnected(siteOne,siteTwo)) {
                forest.union(siteOne,siteTwo);
                connections++;
            }
        }
        return connections;
    }
    public static void main(String[] args) {
//        if (args.length == 0)
//            throw new RuntimeException("Argument absent");
//        int n = Integer.parseInt(args[0]);
        int n=200;
        int previous=-1;
        for(n=1000;n<=1100;n++) {
            int connections = HWQUPC_Solution.count(n);
            if(connections!=previous+1)
                System.out.println(n);
            previous=connections;
           System.out.println("Number of connections generated is: "+connections);
        }

    }
}
