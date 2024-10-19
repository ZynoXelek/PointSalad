# PointSalad
Home Exam of the Software Engineering course at LTU

## Maven to compile, run, test and clean

**Commands**:
- `mvn compile`: To compile the source code of the project in the `target/` folder
- `mvn test`: To run unit tests for the project. The results are displayed in the console, and written in the `target/surefire-reports` directory.
- `mvn exec:java`: To run the project. This executes the main class specified in the `exec-maven-plugin` configuration.
- `mvn clean`: To clean the repository by deleting the `target/` folder.

Additionally, you can run these commands:
- `mvn clean install`: To clean and install the project, ensuring that all dependencies are correctly downloaded and included.
- `mvn package`: To package the compiled code in a jar file. It goes through `compile` and `test` first. The resulting `.jar` file is located in the `target/` directory.
