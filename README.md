# Family Tree PlantUML Generator

## Overview
This Java project was created as part of a university assignment. It reads family data from a CSV file, processes it, and generates a visual family tree diagram in PlantUML format. The program supports multiple validation checks for realistic family relationships, including lifespan, age constraints, and parent-child relationships.

## Features
- **CSV File Parsing**: Reads family member data from a CSV file.
- **Error Checking**: Validates relationships with custom exceptions like `NegativeLifespanException`, `AmbiguousPersonException`, and `ParentingAgeException`.
- **PlantUML Integration**: Converts the data into PlantUML syntax and generates a visual diagram of the family tree.
- **Customizable Output**: Allows you to set the output directory and file name for the generated diagram.

## Project Structure
- **Main Class**: Manages the workflow, from reading the CSV to generating the UML file.
- **Person Class**: Represents each individual in the family tree and provides methods for CSV parsing and PlantUML syntax generation.
- **PlantUMLRunner**: Runs PlantUML to generate the diagram file from the PlantUML syntax.

## Usage
1. Set the `filePath` variable in `Main` to the location of your CSV file containing the family data.
2. Specify the `outputDir` and `outputName` for the generated PlantUML file.
3. Run the `Main` class to generate the UML diagram.
4. View the resulting diagram in a compatible PlantUML viewer.

### Example CSV Format
The CSV file should include columns for each person’s name, birth and death years, and parent-child relationships, for example:
```csv
name,birthYear,deathYear,parent1,parent2
Alice,1920,1990,,
Bob,1945,,
Carol,1970,,
```

## Requirements

- Java 8 or higher
- PlantUML jar file (set in runner.setPlantUmlFilePath in Main)

## Contact
If you’d like to reach out, feel free to connect with me:
- [LinkedIn](https://www.linkedin.com/in/nadiia-rybak-5092b8336)
- [Email](mailto:nvdiv5@gmail.com)

Thanks for visiting!
