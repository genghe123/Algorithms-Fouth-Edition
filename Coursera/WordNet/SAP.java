package WordNet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class SAP {
    private Digraph digraph;

    //constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        digraph = new Digraph(G);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    //length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        //return alternativeBFS(v,w,true);
        ArrayList<Integer> list1 = new ArrayList<Integer>(1);
        ArrayList<Integer> list2 = new ArrayList<Integer>(1);
        list1.add(v);
        list2.add(w);
        return length(list1, list2);
    }

    //a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        //return alternativeBFS(v,w,false);
        ArrayList<Integer> list1 = new ArrayList<Integer>(1);
        ArrayList<Integer> list2 = new ArrayList<Integer>(1);
        list1.add(v);
        list2.add(w);
        return ancestor(list1, list2);
    }

    //length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return shortestSearch(v, w, true);
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return shortestSearch(v, w, false);
    }

    /*
    private int alternativeBFS(Iterable<Integer> v, Iterable<Integer> w, boolean isLengthMode){
        int shortestLength = Integer.MAX_VALUE;
        int shortestAncestor = -1;

        Queue<Integer> queue = new Queue<>();
        int[] lengthToSourceV = new int[digraph.V()];
        int[] lengthToSourceW = new int[digraph.V()];
        boolean[] sourceV = new boolean[digraph.V()];
        boolean[] sourceW = new boolean[digraph.V()];

        for (int i=0; i<lengthToSourceV.length; i++){
            lengthToSourceV[i] = -1;
            lengthToSourceW[i] = -1;
        }

        //Parameter validity check. If there has any valid integers in two Iterable object equals,
        //Return it directly.
        for (int i :v ){
            if (i<0 || i>=digraph.V()) throw new IllegalArgumentException();
            for (int j : w){
                if (j<0 || j>=digraph.V()) throw new IllegalArgumentException();
                if (i==j) return isLengthMode ? 0 : i;
            }
            queue.enqueue(i);
            lengthToSourceV[i] = 0;
            sourceV[i] = true; ;
        }
        for (int j : w) {
            queue.enqueue(j);
            lengthToSourceW[j] = 0;
            sourceW[j] = true;
        }

        while (!queue.isEmpty()){
            int s = queue.dequeue();
            if (sourceV[s]){
                for (int n : digraph.adj(s)){
                    if (lengthToSourceV[n] == -1){
                        sourceV[n] = true;
                        lengthToSourceV[n] = lengthToSourceV[s] + 1;
                        queue.enqueue(n);
                    }
            }
            if (sourceW[s]){

            }
            for (int n : digraph.adj(s)){
                if (lengthToSourceV[n] == -1){
                    sourceV[n] = sourceV[s];
                    lengthToSourceV[n] = lengthToSourceV[s] + 1;
                    queue.enqueue(n);
                }
                else if ((sourceV[n]) == (sourceV[s])){
                    if (lengthToSourceV[n] > lengthToSourceV[s]+1){
                        lengthToSourceV[n] = lengthToSourceV[s] + 1;
                        sourceV[n] = sourceV[s];
                    }
                }
                else if (lengthToSourceV[s]+1 < shortestLength){
                    shortestLength = Math.min(shortestLength,lengthToSourceV[n] + lengthToSourceV[s] + 1);
                    shortestAncestor = n;
                    lengthToSourceV[n] = lengthToSourceV[s] + 1;
                    queue.enqueue(n);
                }
            }
        }
        if (!isLengthMode) return shortestAncestor;
        else              return shortestLength==Integer.MAX_VALUE ? -1 : shortestLength;
    }
    */

    private int shortestSearch(Iterable<Integer> v, Iterable<Integer> w, boolean isLengthMode) {
        DeluxeBFS vGraph = new DeluxeBFS(digraph, v);
        DeluxeBFS wGraph = new DeluxeBFS(digraph, w);
        int shortestLength = Integer.MAX_VALUE;
        int shortestAncestor = -1;
        for (int i = 0; i < digraph.V(); i++) {
            int distToi = vGraph.distTo(i) + wGraph.distTo(i);  //Worst case : Integer.MAX_VALUE + Integer.MAX_VALUE
            if (distToi < 0) continue;  //Overflow exception check
            if (shortestLength > distToi) {
                shortestLength = distToi;
                shortestAncestor = i;
            }
        }
        if (isLengthMode) return shortestLength == Integer.MAX_VALUE ? -1 : shortestLength;
        else return shortestAncestor;
    }
}
