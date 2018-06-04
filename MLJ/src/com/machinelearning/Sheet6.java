package com.machinelearning;

import com.mathematischemodellierung.Vertex;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Sheet6 {
    static ArrayList <String[]> documents = new ArrayList<>();// A document is an array of strings. List of documents..
    public static void readFile(String source) throws FileNotFoundException {
        Scanner scanner;
        File f = new File(source);
        scanner = new Scanner(f);
        String s="";

        int counter =0;
        while (scanner.hasNextLine()){
            s=scanner.nextLine();
            String head = s.substring(0,1);
            head = head.concat(" ");
            s=head.concat(s.substring(3,s.length()-1));
            String[] stringSplit = s.split(" ");
            Arrays.sort(stringSplit,1,stringSplit.length);
            documents.add(stringSplit);

        }
        scanner.close();



    }

    public static void main(String[] args) throws FileNotFoundException {
        readFile("MLJ/src/train3500.txt");
        int T = 10;//Amount of topics

        //Building the set of unique words from the complete input-data
        Set<String> wordsSet = new TreeSet<String>();
        for (int i=0;i<documents.size();i++) {
            for(int j=1;j<documents.get(i).length;j++){//Inner loop starts at 1 since 0 is the label
                wordsSet.add(documents.get(i)[j]);
            }
        }
        String[] wordsArray = wordsSet.toArray(new String[0]);//Getting sorted Array from the set of unique words
        //0,...,W-1 unique words in whole corpus
        int W = wordsArray.length;
        int D = documents.size();

        //Counter variables
        int[][] Ntw = new int[T][wordsSet.size()];//# times word w assigned to topic t
        int[][] Ndt = new int[documents.size()][T];//# words assigned to topic t in document d
        int[] Nt = new int[T];//Total # any words assigned to topic t
        int[] Z = new int[wordsArray.length];//Current topic assignement of each word wi from whole corpus wordsArray


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
        for (int d=0;d<documents.size();d++){
            for (int w=1;w<documents.get(d).length;w++){
                String word = documents.get(d)[w];
                Ndt[d][Z[((TreeSet<String>) wordsSet).headSet(word).size()]]++;
            }
           // System.out.println(d);
        }
        //Nt Total # any words assigned to topic t
        for (int w =0;w<wordsArray.length;w++){
            String word = wordsArray[w];
            Nt[Z[((TreeSet<String>) wordsSet).headSet(word).size()]]++;
        }

        //For each iteration:
        //-------------------------------------
        for (int w=0;w<wordsArray.length;w++){
            String word = wordsArray[w];
            int topic = Z[w];

            Nt[topic]--;
            Ntw[topic][w]--;
            for (int d=0;d<D;d++) {
                Ndt[0][topic]--;
            }
            for (int t =0;t<T;t++){

                
            }

        }
        //Iteration ends
        //-------------------------------------

        //System.out.println(wordsArray[((TreeSet<String>) wordsSet).headSet("cosmid").size()]);

       // for (int i=9000;i<10000;i++)
       // System.out.println(wordsArray[i]);




    }
}
