package maze;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

//Class creates a matrix of cells. The relationship between these cells is defined by the row and column values provided to the maze. It calls methods from both the cell and player class which dictates interactions between the two. It ensures the maze is correctly formatted, and that cells align properly.

public class Maze implements MazeMethods {

	// Initial maze details. Will control cells within the maze.
	int maxRow; // Maximum row in the maze. Used to define how many cells will be included in
				// array.
	int maxCol; // As above, but for columns.
	Cell[][] cells; // An array of cells of size maxRow * maxCol
	String mazeFormat = "Xcoordinate,Ycoordinate,northWall,southWall,eastWall,westWall,Object"; // Checks if text file
																								// format is valid for
																								// input.

	public Maze(String inputFileName) {

		try (Scanner input = new Scanner(new FileReader(inputFileName))) {
			ArrayList<String> lines = new ArrayList<String>();
			while (input.hasNextLine()) {
				lines.add(input.nextLine());
			}

			if (!lines.get(0).equals(mazeFormat)) {
				throw new IllegalArgumentException(
						" Maze file must include 'Xcoordinate,Ycoordinate,northWall,southWall,eastWall,westWall,Object' as first line and follow this format.");
			}

			// Convert arrayList to array. Use last line in array to determine number of
			// cells.

			String[] fileLines = lines.toArray(new String[0]);
			String[] finalLine = fileLines[fileLines.length - 1].split(",");
			this.maxRow = Integer.parseInt(finalLine[0].trim()) + 1;
			this.maxCol = Integer.parseInt(finalLine[1].trim()) + 1;

			// Create cells from fileLines.
			this.cells = new Cell[this.maxRow][this.maxCol];
			for (int i = 1; i < fileLines.length; i++) {

				// split cell into component parts and parse to cells constructor.
				String[] thisLine = fileLines[i].split(",");
				int thisLineRow = Integer.parseInt(thisLine[0].trim());
				int thisLineCol = Integer.parseInt(thisLine[1].trim());
				cells[thisLineRow][thisLineCol] = new Cell(thisLine[2], thisLine[3], thisLine[4], thisLine[5],
						thisLine[6]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File not found.");
		} catch (IllegalArgumentException e) {
			System.err.println("File format incorrect." + e);
		}
	}

	public void showMaze() {

		// Will show the current maze, looping through the cells and outputting their
		// properties as a text interface.
		// Start at highest row number as this will be printed first. Columns progress
		// as normal.
		System.out.println(""); // Seperate from previous text

		for (int i = this.maxRow - 1; i >= 0; i--) {
			if (i == this.maxRow - 1) {
				writeLine(i, true); // Will ensure the top row of the maze is properly displayed).
			} else {
				writeLine(i, false); // For all other rows.
			}
		}

	}

	private void writeLine(int cRow, boolean printTop) {
		// Formats each line for showMaze method. cRow is for current row being written.
		// The top line should only be written for the topmost row.
		try {
			if (printTop == true) {
				for (int j = 0; j < this.maxCol; j++) {
					if (!cells[cRow][j].north.equals("wall")) {
						throw new IllegalArgumentException("Cell: " + cRow + ", " + j);
					}
					if (j == 0) {
						System.out.print("\n");
					}
					System.out.print("+" + cells[cRow][j].drawWall("north"));
					if (j == this.maxCol - 1) {
						System.out.print("+");
					}
				}

			}
			for (int k = 0; k < 2; k++) {
				// Loop through middle and bottom of new printed line.
				for (int j = 0; j < this.maxCol; j++) {
					Cell thisCell = cells[cRow][j];
					if (j == 0) {
						System.out.print("\n");
						if (!thisCell.west.equals("wall")) {
							throw new IllegalArgumentException("Cell: " + cRow + ", " + j);
						}
					}
					if (k == 0) {
						System.out.print(thisCell.drawWall("west") + " " + thisCell.drawObject() + " ");
						if (j == this.maxCol - 1) {
							if (!thisCell.east.equals("wall")) {
								throw new IllegalArgumentException("Cell: " + cRow + ", " + j);
							}
							System.out.print(thisCell.drawWall("east"));
						}

					} else if (k == 1) {
						if (cRow == 0) {
							if (!thisCell.south.equals("wall")) {
								throw new IllegalArgumentException("Cell: " + cRow + ", " + j);
							}
						}
						System.out.print("+" + thisCell.drawWall("south"));
						if (j == this.maxCol - 1) {
							System.out.print("+");
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			// Checks all sides of maze are bounded.
			System.err.println("Maze unbounded. Check formatting of maze file." + e);
		}
	}

	public int[] findCell(String containedObj) {
		// Finds the cell containing an object (generally used for start to establish
		// player location).
		try {
			boolean objectFound = false; // Used to check if the object is found in any cell in the maze.
			for (int i = 0; i < this.maxRow; i++) {
				for (int j = 0; j < this.maxCol; j++) {
					if (cells[i][j].getObject().equals(containedObj)) {
						int[] objPos = new int[2];
						objPos[0] = i;
						objPos[1] = j;
						System.out.println(containedObj + " found at " + i + " " + j + ".");
						return objPos;
					}
				}
			}
			if (!objectFound) {
				throw new IllegalArgumentException("No " + containedObj + " found in maze.");
			}
		} catch (IllegalArgumentException e) {
			System.err.println(e);
		}
		return null;
	}

	public boolean validMove(String inputRaw) {
		String input = inputRaw.toLowerCase(); // convert to lower case to test string.
		// Checks firstly that the input is one of the four directions which the player
		// can move in, and secondly if the player is able to move.
		if (input.equals("w") || input.equals("a") || input.equals("s") || input.equals("d") || input.equals("z")
				|| input.equals("q")) {
			return true;
		} else {
			System.out.println("Please enter a valid movement direction (w/z(up), a/q(left), s(down), or d(right).");
			return false;
		}
	}

	public void playerMove(String dirRaw, Player player) {

		int[] playerCell = player.getPosition();
		int pRow = playerCell[0];
		int pCol = playerCell[1];
		try {
			String dir = dirRaw.toLowerCase(); // as above, convert values to lower case.
			// Cell currently containing the player.
			Cell oldCell = cells[pRow][pCol];

			// Cell the player is to move into. plus the walls to be interacted with.
			Cell newCell;
			String oldWall = null;
			String newWall = null;
			String oldDir = null;
			String newDir = null;
			switch (dir) {
			case "w":
			case "z":
				newCell = cells[pRow + 1][pCol];
				oldWall = oldCell.north;
				newWall = newCell.south;
				oldDir = "north";
				newDir = "south";
				break;
			case "a":
			case "q":
				newCell = cells[pRow][pCol - 1];
				oldWall = oldCell.west;
				newWall = newCell.east;
				oldDir = "west";
				newDir = "east";
				break;
			case "d":
				newCell = cells[pRow][pCol + 1];
				oldWall = oldCell.east;
				newWall = newCell.west;
				oldDir = "east";
				newDir = "west";
				break;
			case "s":
				newCell = cells[pRow - 1][pCol];
				oldWall = oldCell.south;
				newWall = newCell.north;
				oldDir = "south";
				newDir = "north";
				break;
			default:
				newCell = oldCell; // e.g. on game start when no valid direction exists.
				break;
			}

			if (newCell != oldCell) {
				// If the two adjoining walls are not equal, an exception is thrown as the file
				// is incorrect.
				if (oldWall.equals(newWall)) {
					if (confirmMove(oldWall, player.getHammer(), player.getKey(), oldCell, newCell, oldDir, newDir)) {
						player.setNewPosition(oldDir);

						// retrieve object from cell as player moves into it.
						String object = newCell.getObject();
						if (!object.equals("no")) {
							player.takeObject(object);
						}
						oldCell.updateObject("no");
						newCell.updateObject("player");
					} else {

					}
				} else {
					throw new Exception("Wall values are not equal between cells. Check maze format.");
				}
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("You are trying to move into a cell that doesn't exist! Try another direction.");
		} catch (Exception e) {
			System.err.println(e);
		}

	}

	private boolean confirmMove(String wall, boolean hammer, boolean key, Cell thisCell, Cell nextCell, String thisDir,
			String nextDir) {

		// confirms the player is able to move through cells. This will depend on the
		// wall type, as well as whether they posses a hammer or key object.
		switch (wall) {
		case "no":
			System.out.println("You have passed through an open space. Nice work!");
			return true;
		case "breakable":
			if (hammer) {
				System.out.println("You have broken a wall. Nice work!");
				thisCell.breakWall(thisDir);
				nextCell.breakWall(nextDir);
				return true;
			} else {
				System.out.println("You struggle valiantly against the wall with your fists. Find a hammer!");
				return false;
			}

		case "fake":
			System.out.println("You have passed through a false wall. Nice work!");
			return true;
		case "door":
			if (key) {
				System.out.println("You have opened a door. Nice work!");
				thisCell.breakWall(thisDir);
				nextCell.breakWall(nextDir);
				return true;
			} else {
				System.out.println("You attempt to poke your finger through the keyhole. Find a key!");
				return false;
			}
		case "wall":
			System.out.println("You fearlessly smash your face against the wall. You remain where you are.");
			return false;
		default:
			return false;
		}
	}

}
