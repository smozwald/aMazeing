package maze;

//This class is used to construct player objects. Player objects have a position within the maze, and keep track of a players progress within the maze.

public class Player implements PlayerMethods {

	// Initialize player values
	private int rowPos; // Current player row value
	private int colPos; // Current player column value
	private int movesTaken; // Total moves taken by player
	private String name; // Player Name
	private boolean hammer; // Does the player have the hammer item?
	private boolean key; // Does the player have the key item?
	private boolean solved; // Has the player solved the maze?

	// constructor, taking the start position found in the maze and the player name.
	public Player(int rowStart, int colStart, String newName) {
		this.rowPos = rowStart;
		this.colPos = colStart;
		this.name = newName;
		this.movesTaken = 0;
		this.hammer = false;
		this.key = false;
	}

	public void takeObject(String object) {
		// Will update the object booleans based on strings identifying object. This may
		// extended if new possible objects are added to cells.
		switch (object) {
		case "key":
			this.key = true;
			System.out.println("You have found a key, giving you the incredible power of opening doors!");
			break;
		case "hammer":
			this.hammer = true;
			System.out.println("You have found a hammer, giving you the strength to destroy brittle walls!");
			break;
		case "end":
			solved = true;
			break;
		case "trophy":
			movesTaken = movesTaken - 3;
			System.out.println("You found a magic trophy which makes your total moves 3 less than before.");
			break;
		default:
			System.err.println("No object with name " + object + " found!");
			break;
		}
	}

	public int[] getPosition() {
		int[] playerPos = new int[2];
		playerPos[0] = this.rowPos;
		playerPos[1] = this.colPos;
		return playerPos;
	}

	public String getName() {
		return this.name;
	}

	public int getMoves() {
		return this.movesTaken;
	}

	public boolean getHammer() {
		// will return true if player has retrieved hammer object.
		return hammer;
	}

	public boolean getKey() {
		// Will return true if player has retrieved key object.
		return key;
	}

	public boolean getSolved() {
		// Will return true if player has reached "end" object and achieved solved
		// status.
		return solved;
	}

	public void setNewPosition(String dir) {
		// Sets new position once a move possibility is confirmed.
		this.movesTaken++;
		switch (dir) {
		case "north":
			this.rowPos = this.rowPos + 1;
			break;
		case "south":
			this.rowPos = this.rowPos - 1;
			break;
		case "west":
			this.colPos = this.colPos - 1;
			break;
		case "east":
			this.colPos = this.colPos + 1;
			break;
		}
	}

}
