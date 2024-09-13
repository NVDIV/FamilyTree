import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            // Sample CSV file path
            String filePath = "D:\\Java\\FamilyTree\\src\\family.csv";

            // Read persons from CSV
            ArrayList<Person> persons = Person.fromCsv(filePath);

            // Generate PlantUML for the list of persons
            String plantUml = Person.listToPlantUml(persons);

            // Create PlantUML file
            String outputDir = "D:\\Java\\FamilyTree";
            String outputName = "output";
            PlantUMLRunner runner = new PlantUMLRunner();
            runner.setPlantUmlFilePath("D:\\Java\\plantuml-1.2024.6.jar");
            runner.generateUmlDiagram(plantUml, outputDir, outputName);

        } catch (NegativeLifespanException | AmbiguousPersonException | ParentingAgeException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}