import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Person {
    private String name;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private ArrayList<Person> parents = new ArrayList<>();

    public Person(String fullName, LocalDate birthDate, LocalDate deathDate) throws NegativeLifespanException {
        if (deathDate != null && deathDate.isBefore(birthDate)) {
            throw new NegativeLifespanException("Data śmierci " + deathDate + " jest wcześniejsza niż data urodzin " + birthDate + " dla osoby " + fullName);
        }
        this.name = fullName;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }

    public void addParent(Person parent) throws ParentingAgeException {
        if (parent.birthDate != null && this.birthDate != null) {
            int ageAtBirth = this.birthDate.getYear() - parent.birthDate.getYear();
            if (ageAtBirth < 15) {
                throw new ParentingAgeException("Rodzic " + parent.name + " miał mniej niż 15 lat w momencie narodzin dziecka " + this.name);
            }
        }
        if (parent.deathDate != null && this.birthDate != null && parent.deathDate.isBefore(this.birthDate)) {
            throw new ParentingAgeException("Rodzic " + parent.name + " nie żył w chwili narodzin dziecka " + this.name);
        }
        this.parents.add(parent);
    }

    private static LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Person fromCsvLine(String line) throws NegativeLifespanException {
        String[] fields = line.split(",");
        String fullName = fields[0];
        LocalDate birthDate = parseDate(fields[1]);
        LocalDate deathDate = fields.length > 2 && !fields[2].isEmpty() ? parseDate(fields[2]) : null;

        return new Person(fullName, birthDate, deathDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static ArrayList<Person> fromCsv(String filePath) throws NegativeLifespanException, AmbiguousPersonException, ParentingAgeException {
        ArrayList<Person> persons = new ArrayList<>();
        HashMap<String, Person> personsMap = new HashMap<>();
        ArrayList<String[]> rawLines = new ArrayList<>();

        // Try to read all of the persons without parents
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("imię i nazwisko")) {
                    continue;
                }
                rawLines.add(line.split(","));
                Person person = fromCsvLine(line);
                if (personsMap.containsKey(person.getName())) {
                    throw new AmbiguousPersonException(person.getName());
                }
                personsMap.put(person.getName(), person);
                persons.add(person);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assign parents
        Scanner scanner = new Scanner(System.in);
        for (String[] rawLine : rawLines) {
            String name = rawLine[0];
            Person person = personsMap.get(name);
            if (rawLine.length > 3 && !rawLine[3].isEmpty()) {
                Person parent1 = personsMap.get(rawLine[3]);
                if (parent1 != null) {
                    try {
                        person.addParent(parent1);
                    } catch (ParentingAgeException e) {
                        System.out.println(e.getMessage() + " Czy chcesz kontynuować? (Y/N): ");
                        String response = scanner.nextLine();
                        if (response.equalsIgnoreCase("Y")) {
                            person.parents.add(parent1); // Add parent despite the exception
                        }
                    }
                }
            }
            if (rawLine.length > 4 && !rawLine[4].isEmpty()) {
                Person parent2 = personsMap.get(rawLine[4]);
                if (parent2 != null) {
                    try {
                        person.addParent(parent2);
                    } catch (ParentingAgeException e) {
                        System.out.println(e.getMessage() + " Czy chcesz kontynuować? (Y/N): ");
                        String response = scanner.nextLine();
                        if (response.equalsIgnoreCase("Y")) {
                            person.parents.add(parent2); // Add parent despite the exception
                        }
                    }
                }
            }
        }

        return persons;
    }

    // Method to generate PlantUML representation
    public String toPlantUml() {
        StringBuilder sb = new StringBuilder();
        sb.append("object \"" + this.name + "\" as " + this.name.replace(" ", "_") + "\n");

        for (Person parent : parents) {
            sb.append("object \"" + parent.name + "\" as " + parent.name.replace(" ", "_") + "\n");
            sb.append(this.name.replace(" ", "_") + " --> " + parent.name.replace(" ", "_") + "\n");
        }

        return sb.toString();
    }

    // Static method to generate PlantUML for a list of persons
    public static String listToPlantUml(List<Person> persons) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");

        // Map to keep track of processed persons to avoid duplication
        Map<String, String> processed = new HashMap<>();

        // Helper method to process each person
        for (Person person : persons) {
            addPersonToPlantUml(person, sb, processed);
        }

        sb.append("@enduml");
        return sb.toString();
    }

    // Helper method to add a person and their parents to the PlantUML diagram
    private static void addPersonToPlantUml(Person person, StringBuilder sb, Map<String, String> processed) {
        String personKey = person.name.replace(" ", "_");
        if (!processed.containsKey(personKey)) {
            sb.append("object \"" + person.name + "\" as " + personKey + "\n");
            processed.put(personKey, person.name);

            for (Person parent : person.parents) {
                String parentKey = parent.name.replace(" ", "_");
                if (!processed.containsKey(parentKey)) {
                    sb.append("object \"" + parent.name + "\" as " + parentKey + "\n");
                    processed.put(parentKey, parent.name);
                }
                sb.append(personKey + " --> " + parentKey + "\n");
            }
        }
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public List<Person> getParents() {
        return parents;
    }
}
