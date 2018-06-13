package com.machinelearning;

import com.mathematischemodellierung.Vertex;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Sheet6 {
    static ArrayList <String[]> documents = new ArrayList<>();// A document is an array of strings. List of documents..
    static Set<String>documentsSet = new TreeSet<>();
    static ArrayList<String>documentsString = new ArrayList<>();
    static Set<String> ignoreWords = new TreeSet<>();
    public static void readFile(String source) throws FileNotFoundException {
        ignoreWords.add("about");
        //ignoreWords.add("10-fold");
        Scanner scanner;
        File f = new File(source);
        scanner = new Scanner(f);
        String s="";
        int th = 0;

        int counter =0;
        while (scanner.hasNextLine()) {

            s = scanner.nextLine();
            String ss = s.substring(3, 30);
            String se = s.substring(s.length()-30,s.length()-1);
            //Making sure of no duplicated documents
            int min = 999;
            for (String ds : documentsString) {
                if (Math.abs(ss.compareToIgnoreCase(ds)) < min) {
                    min = Math.abs(ss.compareToIgnoreCase(ds));
                }else if (Math.abs(se.compareToIgnoreCase(ds))<min){
                    min=Math.abs(se.compareToIgnoreCase(ds));
                }
            }

            if (min > th) {
                documentsString.add(ss);
                documentsString.add(se);
                String head = s.substring(0, 1);
                head = head.concat(" ");
                s = head.concat(s.substring(3, s.length() - 1));



                String[] stringSplit = s.split(" ");
                Arrays.sort(stringSplit, 1, stringSplit.length);
                documents.add(stringSplit);
            }


        }
        scanner.close();



    }

    public static void main(String[] args) throws FileNotFoundException {
        readFile("MLJ/src/train3500.txt");
        int T = 100;//Amount of topics
        int min = 10,max=documents.size()/3;
        int minLength = 4;

        //Building the set of unique words from the complete input-data
        Set<String> wordsSet = new TreeSet<String>();
        Set<String> wordsPP = new TreeSet<>();
        ArrayList<String>[]documentsPP = new ArrayList[documents.size()];
        System.out.println(documents.size());

        for (int i=0;i<documents.size();i++) {
            for(int j=1;j<documents.get(i).length;j++){//Inner loop starts at 1 since 0 is the label
                wordsSet.add(documents.get(i)[j]);
                //if(!wordsSet.add(documents.get(i)[j])){
                   // wordsPP.add(documents.get(i)[j]);//Getting rid of words that appear only once in the whole corpus
                //};
            }
        }
        String[] wordsArray = wordsSet.toArray(new String[0]);//Getting sorted Array from the set of unique words
        System.out.println(wordsSet.size()+" wordsSet");
        int[]wordsCount = new int[wordsSet.size()];
        for (int i=0;i<documents.size();i++) {
            Set<String> tempSet = new TreeSet<String>();
            for(int j=1;j<documents.get(i).length;j++){//Inner loop starts at 1 since 0 is the label
                String word=documents.get(i)[j];
                if (word.length()>=minLength) {
                    if (tempSet.add(word)) {
                        wordsCount[((TreeSet<String>) wordsSet).headSet(word).size()]++;
                    }
                }
                //if(!wordsSet.add(documents.get(i)[j])){
                // wordsPP.add(documents.get(i)[j]);//Getting rid of words that appear only once in the whole corpus
                //};
            }
        }

        for (int n=0;n<wordsCount.length;n++){
            if (wordsCount[n]>min && wordsCount[n]<max && !ignoreWords.contains(wordsArray[n])){
                wordsPP.add(wordsArray[n]);
            }
        }
        System.out.println(wordsPP.size()+" wordsPP");

        //wordsPP is the whole corpus of words that appear at least twice
        //documentsPP
        for (int i=0;i<documentsPP.length;i++){
            documentsPP[i] = new ArrayList<>();
            documentsPP[i].add(documents.get(i)[0]);//Adding labels to position 0 of each document
            for (int j=1;j<documents.get(i).length;j++){
                String word = documents.get(i)[j];
                if(wordsPP.contains(word)){
                    documentsPP[i].add(word);
                }

            }
        }
        wordsArray = wordsPP.toArray(new String[0]);//Getting sorted Array from the set of unique words
        //0,...,V-1 unique words in whole corpus
        int V = wordsArray.length;
        int D = documentsPP.length;
        int W=0;
        for (int m=0;m<D;m++){
            W+=documentsPP[m].size();
        }

/*
        //Init
        int[][] Nmk = new int[documentsPP.length][T];
        int [] Nm = new int[documentsPP.length];
        int [][] Nkt= new int[T][wordsPP.size()];
        int [] Nk = new int[T];

        System.out.println(wordsPP.size());
        for (int m=0;m<D;m++){
            //System.out.println(m+ " | "+D);
            for (int n=1;n<documentsPP[m].size();n++){
                int k = (int)(Math.random()*T);
                Nmk[m][k]++;
                Nm[m]++;
                String word = documentsPP[m].get(n);
                //System.out.println(((TreeSet<String>) wordsPP).headSet(word).size());
                Nkt[k][((TreeSet<String>) wordsPP).headSet(word).size()]++;
                Nk[k]++;
            }
        }

        //Gibbs
        for (int m=0;m<D;m++){
            for (int n=1;n<documentsPP[m].size();n++){
                String word = documentsPP[m].get(n);
            }
        }

        */
        //----------

        //Count variables
        int[][] Ntw = new int[T][wordsPP.size()];//# times word w assigned to topic t
        int[][] Ndt = new int[documentsPP.length][T];//# words assigned to topic t in document d
        int[] Nt = new int[T];//Total # any words assigned to topic t
        int[] Z = new int[wordsPP.size()];//Current topic assignement of each word wi from whole corpus wordsArray
        for (int w=0;w<Z.length;w++){
            Z[w]=-1;
        }

        int[][]Zdw = new int[documentsPP.length][];
        for (int m=0;m<documentsPP.length;m++){
            Zdw[m]= new int[documentsPP[m].size()-1];
            for (int n=1;n<documentsPP[m].size();n++){
                String word = documentsPP[m].get(n);
                int wordIndex = ((TreeSet<String>) wordsPP).headSet(word).size();
                if (Z[wordIndex]>=0) {

                    Zdw[m][n - 1] = Z[wordIndex];
                }else {
                    Zdw[m][n - 1] = (int) (Math.random() * T);
                }
                Z[wordIndex]=Zdw[m][n-1];

                Ntw[Zdw[m][n-1]][wordIndex]++;
                Ndt[m][Zdw[m][n-1]]++;
                Nt[Zdw[m][n-1]]++;
            }
        }
        for (int tt=0;tt<T;tt++) {
            int[] topWords = new int[5];
            int maxCount =1;
            for (int c = 0; c < wordsPP.size(); c++) {
                if (Ntw[tt][c]>maxCount){
                    for (int top =topWords.length-1;top>0;top--){
                        if (topWords[top-1]>0) {
                            topWords[top] = topWords[top - 1];
                        }
                    }
                    topWords[0]=c;
                    maxCount=Ntw[tt][c];
                }

            }
            System.out.print(tt+" ");
            for (int top = 0;top<topWords.length;top++){
                System.out.print(wordsArray[topWords[top]]+" - ");
            }
            System.out.println();
        }
        for (int m = 0; m < 20; m++) {
            for (int t = 0; t < T; t++) {
                System.out.print(Ndt[m][t] + " | ");
            }
            System.out.println();
        }

/*
        //Random init of current topic assignement for each word from wordsArray before first iteration
        //Z[0],...,Z[W-1] --> {0,...,T-1} word to topic
        for (int i=0;i<wordsArray.length;i++){
            Z[i]=(int)(Math.random()*T); //0..T-1
        }
        //Adjust counters to current assignement
        //Ntw # times word w assigned to topic t
        for (int i=0;i<wordsArray.length;i++){
            Ntw[Z[i]][i]++;
        }
        //Ndt # words assigned to topic t in document d
        for (int d=0;d<documentsPP.length;d++){
            for (int w=1;w<documentsPP[d].size();w++){
                String word = documentsPP[d].get(w);
                Ndt[d][Z[((TreeSet<String>) wordsPP).headSet(word).size()]]++;
            }
           // System.out.println(d);
        }
        //Nt Total # any words assigned to topic t
        for (int w =0;w<wordsArray.length;w++){
            String word = wordsArray[w];
            Nt[Z[((TreeSet<String>) wordsPP).headSet(word).size()]]++;
        }*/

        double beta = 0.1,alpha = 50/T;
        //For each iteration:
        for (int i=0;i<1000;i++) {

            //-------------------------------------
            double[] pT = new double[T];
            for (int M=0;M<D;M++) {
                for (int N = 1; N < documentsPP[M].size(); N++) {
                    //for (int w = 0; w < wordsArray.length; w++) {
                        String word = documentsPP[M].get(N);//wordsArray[w];
                        int w = ((TreeSet<String>) wordsPP).headSet(word).size();
                        int topic = Zdw[M][N-1];

                        Nt[topic]--;
                        Ntw[topic][w]--;
                        Ndt[M][topic]--;
                        //for (int d = 0; d < D; d++) {
                        //    Ndt[d][topic]--;
                        //}

                        double maxSample = 0;
                        int maxTopic = 0;
                        for (int t = 0; t < T; t++) {
                            // pT[t] =
                            //for (int m = 0; m < D; m++) {
                            //    pT[t] += (Ndt[m][t] + alpha) * (Ntw[t][w] + beta) / (V * beta + Nt[t]);
                            //}
                            pT[t] += (Ndt[M][t] + alpha) * (Ntw[t][w] + beta) / (V * beta + Nt[t]);
                            double s = Math.random() * pT[t];
                            if (s > maxSample) {
                                maxSample = s;
                                maxTopic = t;
                            }

                        }

                        topic = maxTopic;
                        Zdw[M][N-1]=topic;

                        Nt[topic]++;
                        Ntw[topic][w]++;
                        //for (int d = 0; d < D; d++) {
                        //    Ndt[d][topic]++;
                        //}
                        Ndt[M][topic]++;

                    }

                //}
            }
            System.out.print(i+"_");
            if (i % 20 == 0) {
                System.out.println(i + "-----------------");

                for (int tt=0;tt<T;tt++) {
                    int[] topWords = new int[10];
                    int maxCount =0;
                    for (int c = 0; c < wordsPP.size(); c++) {
                        if (Ntw[tt][c]>maxCount){
                            for (int top =topWords.length-1;top>0;top--){
                                topWords[top]=topWords[top-1];
                            }
                            topWords[0]=c;
                            maxCount=Ntw[tt][c];
                        }

                    }
                    System.out.print(tt+" ");
                    for (int top = 0;top<topWords.length;top++){
                        System.out.print(wordsArray[topWords[top]]+" - ");
                    }
                    System.out.println();
                }
                for (int m = 0; m < 20; m++) {
                    for (int t = 0; t < T; t++) {
                        System.out.print(Ndt[m][t] + " | ");
                    }
                    System.out.println();
                }
            }
        }

        //Iteration ends
        //-------------------------------------

        //System.out.println(wordsArray[((TreeSet<String>) wordsSet).headSet("cosmid").size()]);

       // for (int i=9000;i<10000;i++)
       // System.out.println(wordsArray[i]);




    }
}
