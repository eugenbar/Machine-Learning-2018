package com.machinelearning;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.J48;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws Exception {
        // Read all the instances in the file (ARFF, CSV, XRFF, ...)
        String filename = "weather.nominal.arff";
        DataSource source = new DataSource(filename);
        Instances instances = source.getDataSet();
        ArrayList<Integer> indices;
        Integer[] intArray = {0,1,2,3,4,5,6,7,8,9,10,11,12,13};
        indices = new ArrayList<>(Arrays.asList(intArray));
        // Make the last attribute be the class

        instances.setClassIndex(instances.numAttributes() - 1);
        DecisionTree dt = new DecisionTree("root",null,instances,indices,instances.classAttribute());
        //double entropy = dt.entropyOnSubset(instances,indices,instances.classAttribute());
        //System.out.println(entropy);
        //for(int i=0;i<instances.numAttributes();i++){
        //    double igain = dt.informationGain(instances,indices,instances.classAttribute(),instances.attribute(i));
        //    System.out.println(igain);
        //}

        dt.buildTree(instances,indices,instances.classAttribute(),dt.getTreeRoot());


        // Print header and instances.
       // System.out.println("\nDataset:\n");
       // System.out.println(instances);
       // System.out.println(instances.attributeStats(0));


        /*
        String source="weather.nominal.arff";
        File f = new File(source);
        Scanner scanner=new Scanner(source);
        String s="";
        while(scanner.hasNextLine() && !s.startsWith("@relation")){
            s=scanner.nextLine();
        }
        s=scanner.nextLine();

/*
        Scanner scanner;
        File f = new File(source);
        scanner = new Scanner(f);
        String s="";
        String[] sA = new String[5];
        sA[0]="";
        while (scanner.hasNextLine() && !s.equals("end_header")){
            s=scanner.nextLine();
        }
        while (scanner.hasNextLine()&& !sA[0].equals("3")){
            s=scanner.nextLine();
            sA = s.split(" ");
            vL.add(new Vertex(Double.parseDouble(sA[0]),
                    Double.parseDouble(sA[1]),
                    Double.parseDouble(sA[2])));
            // System.out.println(vL.get(vL.size()-1).getX());
        }
        Color c = new Color(1f,0f,0f);
        c = Color.BLUE;
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        while (scanner.hasNextLine()){
            //c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
            addP(Integer.parseInt(sA[1]),
                    Integer.parseInt(sA[2]),
                    Integer.parseInt(sA[3]),
                    c);
            //System.out.println(pL.get(pL.size()-3)+" "+pL.get(pL.size()-2)+" "+pL.get(pL.size()-1));
            s=scanner.nextLine();
            sA = s.split(" ");
        }
        addP(Integer.parseInt(sA[1]),
                Integer.parseInt(sA[2]),
                Integer.parseInt(sA[3]),
                c);
        scanner.close();
*/

    }
}
