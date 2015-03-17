import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metrics {
    public int numberOfRows;
    public int numberOfChars;
    public int numberOfMethods;
    public int averageRowPerMethods;//media righe x metodo nella classe

    public File file;

    Metrics(File file) throws Exception {
        this.file = file;
        findNumberOfRowsAndChars();
        findNumberOfMethodsAndAverage();
    }


    private void findNumberOfMethodsAndAverage() throws Exception {


        boolean insideMethod = false;
        int rowsOfMethods = 0;
        int graffeAperte = 0;

        Pattern pattern = Pattern.compile("^ *(public +|private +|protected +|static +)* *[a-zA-Z0-9<>\\.]+ +[a-zA-Z0-9]+ *\\(.*\\) *.*\\{ *$");
        Pattern costruttorePattern = Pattern.compile("^ *[A-Z][a-zA-Z0-9]+ *\\(.*\\) *.*\\{ *$");
        Pattern graffaApertaPattern = Pattern.compile(".*\\{$");
        Pattern graffaChiusaPattern = Pattern.compile(".*}$");
        Pattern graffaChiusaClasseAnonimaPattern = Pattern.compile(".*}\\);$");

        Scanner sc = new Scanner(file);
        sc.useDelimiter("\n");

        while (sc.hasNext()) {
            String s = sc.next();
            if (s.matches("package.*")) continue;
            if (s.matches("import.*")) continue;//escludi gli import
            if (s.replace(" ", "").matches("^[(//)|(*)]|[/**].*")) continue;//escludi i commenti
            if (0 == s.replace(" ", "").length()) continue;//escludi righe vuote

            Matcher matcher = pattern.matcher(s);
            Matcher costruttoreMatcher = costruttorePattern.matcher(s);
            Matcher graffaApertaMatcher = graffaApertaPattern.matcher(s);
            Matcher graffaChiusaMatcher = graffaChiusaPattern.matcher(s);
            Matcher graffaChiusaClasseAnonimaMatcher = graffaChiusaClasseAnonimaPattern.matcher(s);


            if (matcher.matches()) {
                numberOfMethods++;
                insideMethod = true;
            }

            if (costruttoreMatcher.matches()) {
                numberOfMethods++;
                insideMethod = true;
            }

            if (insideMethod) {
                rowsOfMethods++;
            }

            if (graffaApertaMatcher.matches()) {
                if (insideMethod) graffeAperte++;
            }

            if (graffaChiusaMatcher.matches() | graffaChiusaClasseAnonimaMatcher.matches()) {
                if (insideMethod) graffeAperte--;
                if (0 == graffeAperte) insideMethod = false;
            }

        }

        if (0 != numberOfMethods) averageRowPerMethods = rowsOfMethods / numberOfMethods;

    }


    private void findNumberOfRowsAndChars() throws Exception {
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\n");
        while (sc.hasNext()) {
            String s = sc.next();
            if (s.matches("package.*")) continue;
            if (s.matches("import.*")) continue;//escludi gli import
            if (s.replace(" ", "").matches("^[(//)|(*)]|[/**].*")) continue;//escludi i commenti
            if (0 == s.replace(" ", "").length()) continue;//escludi righe vuote

            numberOfChars = numberOfChars + s.replace(" ", "").length();
            numberOfRows++;
        }
    }


    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfChars() {
        return numberOfChars;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public int getAverageRowPerMethods() {
        return averageRowPerMethods;
    }
}