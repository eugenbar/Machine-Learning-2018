package com.machinelearning;

import weka.core.Attribute;
import weka.core.Instances;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Datatable extends Instances {
    public Datatable(Reader reader) throws IOException {
        super(reader);
    }

    public Datatable(Reader reader, int capacity) throws IOException {
        super(reader, capacity);
    }

    public Datatable(Instances dataset) {
        super(dataset);
    }

    public Datatable(Instances dataset, int capacity) {
        super(dataset, capacity);
    }

    public Datatable(Instances source, int first, int toCopy) {
        super(source, first, toCopy);
    }

    public Datatable(String name, ArrayList<Attribute> attInfo, int capacity) {
        super(name, attInfo, capacity);
    }
}
