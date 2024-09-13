import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PlantUMLRunner {

    private static String plantUmlFilePath;

    public static void setPlantUmlFilePath(String path) {
        plantUmlFilePath = path;
    }

    public static void generateUmlDiagram(String umlData, String outputDirectory, String outputFileName) throws IOException, InterruptedException {
        // Sprawdzenie, czy ścieżka do pliku jar została ustawiona
        if (plantUmlFilePath == null || plantUmlFilePath.isEmpty()) {
            throw new IllegalStateException("Ścieżka do pliku JAR PlantUML nie została ustawiona.");
        }

        // Tworzenie pliku tymczasowego z danymi UML
        File tempUmlFile = File.createTempFile("uml_temp", ".txt");
        try (FileWriter writer = new FileWriter(tempUmlFile)) {
            writer.write(umlData);
        }
        // Tworzenie katalogu wyjściowego, jeśli nie istnieje
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Przygotowanie komendy do uruchomienia PlantUML
        String[] command = {
                "java",
                "-jar",
                plantUmlFilePath,
                tempUmlFile.getAbsolutePath(),
                "-o",
                outputDirectory
        };

        // Uruchomienie procesu
        Process process = new ProcessBuilder(command).start();
        process.waitFor();

        // Zmienianie nazwy wygenerowanego pliku na pożądaną nazwę
        File generatedFile = new File(outputDirectory, tempUmlFile.getName().replace(".txt", ".png"));
        File outputFile = new File(outputDirectory, outputFileName + ".png");
        if (!generatedFile.renameTo(outputFile)) {
            throw new IOException("Nie udało się zmienić nazwy pliku wynikowego.");
        }

        // Usuwanie pliku tymczasowego
        tempUmlFile.delete();
    }
}
