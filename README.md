# PointSalad
This is my work (Enzo Brossier-Sécher) for the Home Exam 2024 of the Software Engineering course at LTU.

## Maven to compile, run, test, and clean

The project is built using **Apache Maven**.

**Commands**:
- `mvn compile`: To compile the source code of the project in the `target/` folder
- `mvn test`: To run unit tests for the project. The results are displayed in the console and written in the `target/surefire-reports` directory.
- `mvn exec:java`: To run the project. This executes the main class specified in the `exec-maven-plugin` configuration, which should be `main.Main`.
- `mvn clean`: To clean the repository by deleting the `target/` folder.

Additionally, you can run these commands:
- `mvn clean install`: To clean and install the project, ensuring that all dependencies are correctly downloaded and included.
- `mvn package`: To package the compiled code in a jar file. It goes through `compile` and `test` first. The resulting `.jar` file is located in the `target/` directory.

Passing arguments through the command line is not supported on my machine. `mvn exec:java -Dexec.mainClass="main.PointSalad" -Dexec.args="arg1 arg2 ..."` should be the right way to do it, but it does not work.

## Running the game

As mentioned above, the game can be launched through the `mvn exec:java` command. Now that the code uses Maven, we cannot add command args anymore, even though I added support for it. (At least it did not work on my computer)

Once you have launched the game, you must first choose the game you want to run. There is only PointSalad available for now. The selection is not case-sensitive, and you can skip it and use the default value written in the config file.

Then, you should decide whether you want to host a server or join a server.

### Hosting a server

When you host a server, the default port will be used, which is the one written in the config file.

You can decide how many players there should be. It should be in the range specified in the config file (by default, 2 to 6 players).

Then, you must decide how many of them are bots. You can launch the game with bots only.

At each step of the game, the current state of the game will be printed to allow the hosting terminal to track the advancement of the game.

### Joining a game

When you join a server, you must declare the IP and port to join.

You must enter a valid IP address, which is in the format 'X.X.X.X' with X being 0 to 255, or 'localhost'.

You can skip the port selection to use the default port specified in the config file.

At each step of the game, instructions will be printed in the terminal. You will have to enter your move during your drafting and flipping phases.
Once a player finishes their turn, every other player will see their hand.

Once the market is empty, the game will grant each player their score and declare the winning player.

## Modifying the code

If one would like to modify the PointSalad game.

They may first look at the config file, where some options are already customizable.

If they want to modify the way the network is handled, they should add two new classes, implementing **IClientConnection** and **IServer**, similarly to the existing classes **ClientConnection** and **Server**. They should then update the **PointSaladClient** and **PointSaladHost** classes or create their own to use this new Network.

## Extending the code

If one wants to add a new game mode, such as PointCity, they shall implement some new classes to do so.

1. Creating new cards

First, they should add their new Card class, which should implement the **ICard** interface. They should as well create their own CardFactory class, which should implement the **ICardFactory** interface.

2. Creating new criteria for scoring

Then, they may want to add their own criteria objects, either on cards or somewhere else. To do so, they should implement the **ICriterion** interface and create a factory to create the corresponding objects from a String representation. It should implement the **ICriterionFactory** interface.

3. Creating a new market

A new market may be created. It only requires implementing the **IMarket** interface.

4. Creating a new Scorer

Similarly, a new scorer can easily be added, as long as it implements the **IScorer** interface. It may want to delegate the work to the Criterion object.

5. Creating new phases

They should define new phases to the game. All of them should implement **IPhase** and have the logic to process the current state of the game, and get the next phase following this one. Since they are specific to this game, they may do assumptions or checks on the actual classes of the current **State** objects attributes.

6. Players

If one needs to implement a new type of player, it should at least be extending the **AbstractPlayer** class. It can also extend the already implemented **HumanPlayer** and **IAPlayer** classes.

7. States

If one needs to implement a new state object, they can do so by extending the already implementing **State**. This one should be enough complete to be generic though, at least according to the possible future extensions. The **StateManager** object is generic and should not have to be modified.

8. Adding the main game

Finally, to make the game playable, one would have to code the main class to launch the game (i.e., similar to the **PointSalad** class), which should most probably create the corresponding host or client object, respectively extending from the **AbstractHost** and **AbstractClient** classes. And they should then add the correct case to the **Main** class in its constructor.

## Adding new tests

To create new tests, one must respect the folder hierarchy and place their new testing class in the `src/test/` directory. If they are testing resources, they should add it in the `resources/` directory, else in the `java/` directory.

In order for their testing class to be correctly detected as a testing class, and therefore actually be tested with the `mvn test` command, one **must** name their testing class following this convention: `<Name>Test`. The class' name **must** finish by the **'Test'** keyword. For instance, `PointSaladDefaultBotLogicTest` is the name of the testing class for the default bot logic of the PointSalad game.

## Remarks about testing

The requirements are all tested in the same file, which is the `RequirementsTest` class. Additionally, I added unit testing for every criterion to be sure the scores of each player are correctly computed. I have also added unit testing for the bot logic to verify it correctly drafts the best criterion card when it can, and flips one of its point cards only if it could win points by doing so.

About the requirements unit-test, I did not know how to write a test ensuring that removed cards from the market are not shown to players (Requirement 3). I wanted to test that the designated method was of void type, but I could not.
