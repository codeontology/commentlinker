package org.codeontology.entitylinking;

import com.hp.hpl.jena.rdf.model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class RDFLogger {

    public static final String OUTPUT_PATH = "annotations.nt";
    private int counter;
    private static RDFLogger instance;
    private Model model;

    public static final int MAX_SIZE = 100;

    private RDFLogger() {
        model = ModelFactory.createDefaultModel();
        counter = 0;
    }

    public static RDFLogger getInstance() {
        if (instance == null) {
            instance = new RDFLogger();
        }
        return instance;
    }

    public void writeRDF() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_PATH, true)))) {
            model.write(writer, "N-TRIPLE");
        } catch (IOException e) {
            System.out.println("Cannot write " + OUTPUT_PATH + ".");
            System.exit(-1);
        }
    }

    public void addTriple(String subjectURI, String propertyURI, String objectURI) {
        Resource subject = model.getResource(subjectURI);
        Property property = model.getProperty(propertyURI);
        Resource object = model.getResource(objectURI);
        addTriple(subject, property, object);
    }


    public void addTriple(Resource subject, Property property, RDFNode object) {
        if (property != null && object != null) {
            Statement triple = model.createStatement(subject, property, object);
            model.add(triple);
            counter++;
            if (counter > MAX_SIZE) {
                writeRDF();
                free();
            }
        }
    }

    private void free() {
        model = ModelFactory.createDefaultModel();
        counter = 0;
    }
}