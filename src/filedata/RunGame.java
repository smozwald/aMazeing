package filedata;

//The following java packages are imported, with their uses detailed below.
import java.io.BufferedWriter; //To append strings to high score file.
import java.io.FileWriter; //To allow for writing of files.
import java.io.IOException; //When working with outputting to high score table, able to throw exception if I/O error occurs.
import java.nio.file.*; //Allow for checking whether maze files exist, and importing them into the maze constructor method.
import java.util.Scanner; //Allow for user input into system.

import maze.Maze;
import maze.Player;

//This class runs the game, creates mazes, allows for player input and manipulation of classes in maze package.
//Additionally saves games to high score table.
//Is seperated from the maze/class/player package, allowing for encapsulation and protection of data.

public class RunGame {

	public static void run() {
		// Method to run the maze. Sequentially moves through each step in a maze's
		// construction, its playing and the conclusion.

		// Scanner used for user to input their name, the maze name, as well as moves in
		// the maze.
		try (Scanner scan = new Scanner(System.in)) {

			// User is first prompted to choose their maze.
			// Once a desired maze name is input, the program checks whether it is valid in
			// a while, and if so will return a true boolean.
			boolean validMaze = false;
			String inputMaze = "";
			while (!validMaze) {
				System.out.println("Enter maze name");
				inputMaze = scan.nextLine();
				// Check that file exists.
				if (Files.exists(Paths.get(inputMaze + ".txt"))) {
					validMaze = true;
				} else {
					System.out.println(
							"Please enter the name of a valid maze. Do not include file type (i.e. '.txt.') in name.");
				}
			}

			// Once a valid maze name is established, a new maze is constructed and shown in
			// the console.
			Maze maze = new Maze(inputMaze + ".txt");
			maze.showMaze();

			// Insert player into the maze, again using scanner prompts. Once user enters
			// name, the mazes start position will determine other player attributes.
			System.out.println("\nEnter player name");
			String playerName = scan.nextLine();
			int[] startPos = maze.findCell("start");
			Player player = new Player(startPos[0], startPos[1], playerName);
			maze.playerMove("", player);
			maze.showMaze();

			// Begin game, continue asking for player moves while unsolved = false.
			// Unsolved is related to the "end" object, and when the player reaches this the
			// while loop ends, as does the game.
			boolean solved = false;
			while (!solved) {
				// Lines output showing current moves taken, and whether the player possesses
				// they key or hammer object.
				System.out.println("\nCurrent moves taken: " + player.getMoves());
				String currentObjs = "Current objects: ";
				if (player.getHammer()) {
					currentObjs = currentObjs + "Hammer. ";
				}
				if (player.getKey()) {
					currentObjs = currentObjs + "Key. ";
				}
				System.out.println(currentObjs);

				// Prompt player to make next move. Test for move validity.
				System.out.println("Make your move!");
				String thisMove = scan.nextLine();
				if (maze.validMove(thisMove)) {
					maze.playerMove(thisMove, player);
					maze.showMaze();
					if (player.getSolved()) {
						solved = true;
					}
				} else {
					System.out.println("Please try again.");
				}
			}

			// Congratulate player on finishing maze, and write score to high score file.
			System.out.println("\nCongratulations. You have solved the Maze! You completed the maze in "
					+ player.getMoves() + " moves.");
			highScoreWriter(inputMaze, playerName, player.getMoves());

			// Finally, ask player if they would like to play again.
			System.out.println("Would you like to play again? Type 'yes' if so.");
			String response = scan.nextLine();
			if (!response.toLowerCase().equals("yes")) {
				System.out.println("Ok. Bye! (Exitting program)"); // Do not rerun if no response.
			} else {
				run(); // Rerun the program if yes response.
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void highScoreWriter(String mazeName, String playerName, int moves) throws IOException {
		// This method will firstly create a new high score file, and subsequently
		// append a players score at the conclusion of a maze.

		String highScoreFile = "High_Scores.txt";
		if (Files.notExists(Paths.get(highScoreFile))) {

			// If file does not yet exist, this will create a new text file and fill in the
			// first line, showing the format of the list.
			Files.createFile(Paths.get(highScoreFile));
			BufferedWriter scoreFile = new BufferedWriter(new FileWriter(highScoreFile));
			scoreFile.append("PLAYERNAME, MAZENAME, MOVESTAKEN");
			scoreFile.close();

		}

		// Write new score to high score file.
		BufferedWriter scoreFile = new BufferedWriter(new FileWriter(highScoreFile, true));
		scoreFile.newLine(); // Forces new score to appear on new line rather than as one line.
		scoreFile.append(playerName + "," + mazeName + "," + moves);
		scoreFile.close();
		System.out.println("Score added to high score file.");

	}

	public static void main(String[] args) {
		// Main method to run maze. It is good practice to reduce the code here, and
		// thus it references another method which actually runs the program.
		// First line introduces user to maze game.
		System.out.println(
				"Welcome to Samuel Oswald's maze game program! \nThe objective of a maze is to progress from the start cell 'S' to the end cell 'E'. \n Along the way, you may find useful objects such as keys, hammers and trophies, hidden in the maze. \nYou will also have to navigate around and through walls. \nWalls are visuallized according to type, these being: \n 'normal' | or --- \n 'breakable' % or ~~~ \n 'door' ] or -o-. \nFake walls may also exist! To move, use W(up),A(left),S(down),D(right) (or Z,Q,S,D for Azerty users). \nGood luck!");

		run();
	}

}
