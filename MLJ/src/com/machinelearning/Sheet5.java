package com.machinelearning;

import com.mathematischemodellierung.Vertex;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Sheet5 {
    static ArrayList <ArrayList<String>> documents = new ArrayList<>();// A document is an array of strings. List of documents..
    //static Set<String>documentsSet = new TreeSet<>();
    static ArrayList<String>documentsString = new ArrayList<>();
    static Set<String> ignoreWords = new TreeSet<>();
    static ArrayList<ArrayList<String>>testData = new ArrayList<>();
    public static void readFile(String source) throws FileNotFoundException {
        ignoreWords.add("about");

        Scanner scanner;
        File f = new File(source);
        scanner = new Scanner(f);
        String s="";
        int th = 0;

        int counter =0;
        int testCounter=0;
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
                if (testCounter%20!=7) {
                    documentsString.add(ss);
                    documentsString.add(se);
                }
                String head = s.substring(0, 1);
                head = head.concat(" ");
                s = head.concat(s.substring(3, s.length() - 1));
                String[] stringSplit = s.split(" ");
                Arrays.sort(stringSplit, 1, stringSplit.length);
                testCounter++;
                if (testCounter%20==7){
                    //testCounter++;
                    testData.add(new ArrayList<String>(Arrays.asList( stringSplit)));
                }else
                documents.add(new ArrayList<String>(Arrays.asList( stringSplit)));
            }


        }
        scanner.close();



    }
    public static void simpleMethod(Set<String>wordsSet){
        int[] labelCounts = new int[4];
        double[][]wordsCounts = new double[4][wordsSet.size()+1];
        for (int i=0;i<documents.size();i++){
            if (documents.get(i).get(0).equals("A")){
                labelCounts[0]++;
            }else if (documents.get(i).get(0).equals("B")){
                labelCounts[1]++;
            }else if (documents.get(i).get(0).equals("E")){
                labelCounts[2]++;
            }else if (documents.get(i).get(0).equals("V")){
                labelCounts[3]++;
            }
            for (int j=1;j<documents.get(i).size();j++){
                String word=documents.get(i).get(j);
                if (documents.get(i).get(0).equals("A")){
                    wordsCounts[0][((TreeSet<String>) wordsSet).headSet(word).size()]++;
                }else if (documents.get(i).get(0).equals("B")){
                    wordsCounts[1][((TreeSet<String>) wordsSet).headSet(word).size()]++;
                }else if (documents.get(i).get(0).equals("E")){
                    wordsCounts[2][((TreeSet<String>) wordsSet).headSet(word).size()]++;
                }else if (documents.get(i).get(0).equals("V")){
                    wordsCounts[3][((TreeSet<String>) wordsSet).headSet(word).size()]++;
                }

            }

        }
        for (int i=0;i<wordsCounts[0].length;i++){
            wordsCounts[0][i]=wordsCounts[0][i]/labelCounts[0];
            wordsCounts[1][i]=wordsCounts[1][i]/labelCounts[1];
            wordsCounts[2][i]=wordsCounts[2][i]/labelCounts[2];
            wordsCounts[3][i]=wordsCounts[3][i]/labelCounts[3];
            System.out.print(wordsCounts[0][i]+" ");
        }
        ArrayList<Integer>acc = new ArrayList<>();
        double[]testWordsCounts = new double[wordsSet.size()];
        for (int i=0;i<testData.size();i++){
            testWordsCounts = new double[wordsSet.size()];
            for (int j=1;j<testData.get(i).size();j++){
                String word = testData.get(i).get(j);
                if (wordsSet.contains(word)){
                    testWordsCounts[((TreeSet<String>) wordsSet).headSet(word).size()-1]++;
                }
            }
            double[] label = new double[4];
            for (int c = 0;c<testWordsCounts.length;c++){
                //if (testWordsCounts[c]>0) {
                    label[0] += (testWordsCounts[c] - wordsCounts[0][c]) * (testWordsCounts[c] - wordsCounts[0][c]);
                    label[1] += (testWordsCounts[c] - wordsCounts[1][c]) * (testWordsCounts[c] - wordsCounts[1][c]);
                    label[2] += (testWordsCounts[c] - wordsCounts[2][c]) * (testWordsCounts[c] - wordsCounts[2][c]);
                    label[3] += (testWordsCounts[c] - wordsCounts[3][c]) * (testWordsCounts[c] - wordsCounts[3][c]);
                //}
            }
            String currentLab = testData.get(i).get(0);
            System.out.println("-----");
            System.out.println(currentLab);
            System.out.print(label[0]+" | "+label[1]+" | "+label[2]+" | "+label[3]);
            double min=label[0];
            int minIndex = 0;
            for (int m=1;m<4;m++){
                if (label[m]<min){
                    min=label[m];
                    minIndex=m;
                }
            }
            if ((minIndex==0 && currentLab.equals("A")) ||
                    (minIndex==1 && currentLab.equals("B")) ||
                    (minIndex==2 && currentLab.equals("E")) ||
                    (minIndex==3 && currentLab.equals("V"))){
                acc.add(1);
            }
        }
        System.out.println("------");
        System.out.println(acc.size()+"----"+testData.size());

    }
    //Using Latent Dirichlet Allocation to compress words x documents into words x topics and topics x documents
    //The goal was to use the compressed representation for calculating mean values of each label
    //New data would then be assigned the label to which mean values it has the smallest sd.
    //But since the task is that simple, we can just ignore all that and simply only calculating the mean values of all words
    //For each label. New data can then be used like above.

    //Preprocessing is here maybe important, many documents are duplicated and a lot of other "useless" words can be removed
    public static void main(String[] args) throws FileNotFoundException {
        readFile("MLJ/src/train3500.txt");
        int T = 10;//Amount of topics
        int min = 10,max=documents.size()/2;//A word has to appear in at least min and maximum max documents
        int minLength = 4;//A word must be of min length 4
        double beta = 0.01,alpha = 20/T;



        //Building the set of unique words from the complete input-data
        Set<String> wordsSet = new TreeSet<String>();//Vocabulary
        System.out.println(documents.size());
        for (int i=0;i<documents.size();i++) {
            for(int j=1;j<documents.get(i).size();j++){//Inner loop starts at 1 since 0 is the label
                if (documents.get(i).get(j).length()<minLength){
                    //documents.get(i).remove(j);
                    //j--;
                }else {
                    wordsSet.add(documents.get(i).get(j));
                }
            }
        }

        ArrayList<String>wordsList = new ArrayList<>(wordsSet);

        int[]wordsCount = new int[wordsSet.size()];
        for (int i=0;i<documents.size();i++) {
            Set<String> tempSet = new TreeSet<String>();
            for(int j=1;j<documents.get(i).size();j++){//Inner loop starts at 1 since 0 is the label
                String word=documents.get(i).get(j);
                if (tempSet.add(word) && wordsSet.contains(word)) {//Using temSet to make sure that we count a word only once for a document
                    wordsCount[((TreeSet<String>) wordsSet).headSet(word).size()]++;//Counting documents of each word
                }
            }
        }
        System.out.println(wordsSet.size());
        System.out.println(wordsCount.length);
        System.out.println(wordsList.size());
        for (int n=0;n<wordsCount.length;n++){
            if (!(wordsCount[n]>min && wordsCount[n]<max && !ignoreWords.contains(wordsList.get(n)))){
                wordsSet.remove(wordsList.get(n));
            }
        }
        wordsList = new ArrayList<>(wordsSet);
        System.out.println(wordsSet.size()+" wordsPP");

        for (int i=0;i<documents.size();i++){
            for (int j=1;j<documents.get(i).size();j++){
                String word = documents.get(i).get(j);
                if (!wordsSet.contains(word)){
                    documents.get(i).remove(j);
                    j--;
                }
            }
        }

        simpleMethod(wordsSet);
/*
        //wordsArray = wordsPP.toArray(new String[0]);//Getting sorted Array from the set of unique words
        //0,...,V-1 unique words in whole corpus
        int V = wordsSet.size();
        int D = documents.size();
        int W=0;
        for (int m=0;m<D;m++){
            W+=documents.get(m).size();
        }


        //Count variables
        int[][] Ntw = new int[T][wordsSet.size()];//# times word w assigned to topic t
        int[][] Ndt = new int[documents.size()][T];//# words assigned to topic t in document d
        int[] Nt = new int[T];//Total # any words assigned to topic t
        int[] Z = new int[wordsSet.size()];//Current topic assignement of each word wi from whole corpus wordsArray
        for (int w=0;w<Z.length;w++){
            Z[w]=-1;
        }

        int[][]Zdw = new int[documents.size()][];
        for (int m=0;m<documents.size();m++){
            Zdw[m]= new int[documents.get(m).size()-1];
            for (int n=1;n<documents.get(m).size();n++){
                String word = documents.get(m).get(n);
                int wordIndex = ((TreeSet<String>) wordsSet).headSet(word).size();
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
            int[] topWordsIndex=new int[5];
            for (int c = 0; c < wordsSet.size(); c++) {
                int pos = topWords.length-1;
                while (Ntw[tt][c]>topWords[pos] && pos>=0){
                    if (pos==0){
                        break;
                    }
                    pos--;
                }
                if (Ntw[tt][c]>topWords[pos]){
                    topWords[pos]=Ntw[tt][c];
                    topWordsIndex[pos]=c;
                }else if(pos<topWords.length-1 && Ntw[tt][c]>topWords[pos+1]) {
                    topWords[pos+1] = Ntw[tt][c];
                    topWordsIndex[pos+1] = c;
                }
            }
            System.out.print(tt+"_");
            for (int top = 0;top<topWords.length;top++){
                System.out.print(wordsList.get(topWordsIndex[top])+"_");
            }
            System.out.println();
        }
        for (int m = 0; m < 20; m++) {
            for (int t = 0; t < T; t++) {
                System.out.print(Ndt[m][t] + " | ");
            }
            System.out.println();
        }

        //For each iteration:
        for (int i=0;i<10000;i++) {

            //-------------------------------------

            for (int M=0;M<D;M++) {
                for (int N = 1; N < documents.get(M).size(); N++) {
                    //for (int w = 0; w < wordsArray.length; w++) {
                        String word = documents.get(M).get(N);//wordsArray[w];
                        int w = ((TreeSet<String>) wordsSet).headSet(word).size();
                        int topic = Zdw[M][N-1];

                        Nt[topic]--;
                        Ntw[topic][w]--;
                        Ndt[M][topic]--;
                        //for (int d = 0; d < D; d++) {
                        //    Ndt[d][topic]--;
                        //}

                        double maxSample = 0;
                        int maxTopic = 0;
                        double[] pT = new double[T];
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
            System.out.print(i);
            if (i % 50 == 0) {
                System.out.println(i + "-----------------");

                for (int tt=0;tt<T;tt++) {
                    int[] topWords = new int[5];
                    int[] topWordsIndex=new int[5];
                    for (int c = 0; c < wordsSet.size(); c++) {
                        int pos = topWords.length-1;
                        while (Ntw[tt][c]>topWords[pos] && pos>=0){
                            if (pos==0){
                                break;
                            }
                            pos--;
                        }
                        if (Ntw[tt][c]>topWords[pos]){
                            if (pos<topWords.length-1){
                                topWords[pos+1]=topWords[pos];
                                topWordsIndex[pos+1]=topWordsIndex[pos];
                            }
                            topWords[pos]=Ntw[tt][c];
                            topWordsIndex[pos]=c;
                        }else if(pos<topWords.length-1 && Ntw[tt][c]>topWords[pos+1]) {
                            if (pos<topWords.length-2){
                                topWords[pos+2]=topWords[pos+1];
                                topWordsIndex[pos+2]=topWordsIndex[pos+1];
                            }
                            topWords[pos+1] = Ntw[tt][c];
                            topWordsIndex[pos+1] = c;
                        }
                    }
                    System.out.print(tt+"_");
                    for (int top = 0;top<topWords.length;top++){
                        System.out.print(wordsList.get(topWordsIndex[top])+"_");
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

        ArrayList<Integer>list = new ArrayList<>();
        for (int i=0;i<Z.length;i++){
            list.add(Z[i]);
            System.out.print(Z[i]);
        }
        PrintWriter writer = new PrintWriter("MLJ/src/model.txt");
        writer.println(list);
        PrintWriter writer2 = new PrintWriter("MLJ/src/model2.txt");
        writer2.print(list);*/
    }
}
