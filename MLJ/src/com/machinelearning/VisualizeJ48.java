package com.machinelearning;

import java.awt.BorderLayout;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

/**
 * Displays a trained J48 as tree.
 * Expects an ARFF filename as first argument.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class VisualizeJ48 {
    public static void main(String args[]) throws Exception {
        // train classifier
        J48 cls = new J48();
        String filename = "weather.arff";
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(filename);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);
        cls.buildClassifier(data);

        // display classifier
        final javax.swing.JFrame jf =
                new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
        jf.setSize(1000,700);
        jf.getContentPane().setLayout(new BorderLayout());
        TreeVisualizer tv = new TreeVisualizer(null,
                cls.graph(),
                new PlaceNode2());
        jf.getContentPane().add(tv, BorderLayout.CENTER);
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                jf.dispose();
            }
        });

        jf.setVisible(true);
        tv.fitToScreen();
    }
}