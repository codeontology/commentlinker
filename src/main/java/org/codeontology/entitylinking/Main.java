package org.codeontology.entitylinking;

public class Main {
    private static String gcubeToken;
    private static String inputPath;
    private static EntityLinker linker;

    public static void main(String[] args) throws Exception {
        setParams(args);
        System.out.println("Running on " + inputPath);
        System.out.println();
        new CommentLinker(inputPath).linkComments();
        System.out.println();
        System.out.println("Annotations saved to annotations.nt");
    }

    private static void setParams(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: commentlinker <GCUBE_TOKEN> <INPUT_FILE> [threshold]");
            System.exit(-1);
        }

        gcubeToken = args[0];
        inputPath = args[1];

        if (args.length < 3) {
            linker = new EntityLinker();
        } else {
            try {
                double threshold = Double.parseDouble(args[2]);
                linker = new EntityLinker(threshold);
            } catch (NumberFormatException e) {
                System.out.println("Invalid threshold");
                System.exit(-1);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        }
    }

    public static String getGcubeToken() {
        return gcubeToken;
    }

    public static EntityLinker getLinker() {
        return linker;
    }
}
