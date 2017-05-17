package org.codeontology.entitylinking;


public class TagMeAnnotation {
    private String title;
    private double rho;

    public TagMeAnnotation(String title, double rho) {
        this.title = title;
        this.rho = rho;
    }

    public String getTitle() {
        return title;
    }

    public double getRho() {
        return rho;
    }

    @Override
    public String toString() {
        return title + " - " + rho;
    }
}
