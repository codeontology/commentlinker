package org.codeontology.entitylinking;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.List;

public class CommentLinker {

    private static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    private static final String DUL = "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#";
    private static final String associatedWith = DUL + "associatedWith";

    private static final String queryString =
            "PREFIX rdfs: <" + RDFS + ">\n" +
            "SELECT DISTINCT ?subject ?comment\n" +
            "WHERE {" +
                "\t?subject rdfs:comment ?comment ." +
            "}";

    private static final Query query = QueryFactory.create(queryString);

    private Model model;
    private EntityLinker linker = Main.getLinker();
    private RDFLogger logger = RDFLogger.getInstance();
    private Model annotations;

    public CommentLinker(String path) {
        model = ModelFactory.createDefaultModel();
        model.read(path);

        annotations = ModelFactory.createDefaultModel();
        annotations.read("annotations.nt");
    }

    public void linkComments() {
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                try {
                    QuerySolution solution = results.nextSolution();
                    Resource subject = solution.getResource("subject");
                    System.out.println("Retrieving annotations for " + subject.getURI());
                    if (!annotations.containsResource(subject)) {
                        List<String> dbpediaURIs = linkComment(solution);
                        logLinks(subject.getURI(), dbpediaURIs);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            logger.writeRDF();
        }
    }

    private void logLinks(String subject, List<String> dbpediaURIs) {
        dbpediaURIs.forEach(dbpediaResource ->
            logger.addTriple(subject, associatedWith, dbpediaResource)
        );
    }

    private List<String> linkComment(QuerySolution solution) {
        String comment = solution.getLiteral("comment").getString();
        return linker.linkEntities(comment);
    }
}
