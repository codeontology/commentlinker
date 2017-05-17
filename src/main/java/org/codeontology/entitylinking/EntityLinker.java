package org.codeontology.entitylinking;


import java.util.List;
import java.util.stream.Collectors;

public class EntityLinker {
    private final double threshold;
    private Tagger tagger;
    private DBpediaMatcher matcher;

    public EntityLinker() {
        this(0.15);
    }

    public EntityLinker(double threshold) {
        if (threshold < 0 || threshold > 1) {
            throw new IllegalArgumentException("Threshold must be a real number between 0 and 1");
        }
        this.threshold = threshold;
        tagger = new Tagger();
        matcher = new DBpediaMatcher();
    }

    public List<String> linkEntities(String text) {
        List<String> annotations = tagger.tag(text).stream()
                .filter(annotation -> annotation.getRho() >= threshold)
                .map(TagMeAnnotation::getTitle)
                .collect(Collectors.toList());

        return matcher.matchAll(annotations);
    }
}
