package maze;

//This class is used to create cell objects. They collectively should make up a larger maze object, with the spatial relations between the cells defined instead in that class.

public class Cell implements CellMethods {

	// Set variables. Directions will describe the wall of the cell, object will
	// describe what is contained within it.
	String north;
	String south;
	String east;
	String west;
	private String object;

	// Possible object and wall values constant.
	String[] OBJECT_VALUES = { "no", "start", "end", "hammer", "key", "player", "trophy" };
	String[] WALL_VALUES = { "no", "wall", "breakable", "fake", "door" };

	// Constructor for line in maze file.
	Cell(String n, String s, String e, String w, String o) {
		boolean validCell = checkValue(n, s, e, w, o);
		if (validCell == true) {
			this.north = n;
			this.south = s;
			this.east = e;
			this.west = w;
			this.object = o;
		} else {
			throw new IllegalArgumentException("Invalid objects or wall values in cells.");
		}
	}

	private boolean checkValue(String n, String s, String e, String w, String o) {
		// Checks each wall is within the list of possible wall values, and object is in
		// possible object values. Initially, each value is set to false, and will be
		// updated if the value passed is found to be a valid value in the arrays
		// showing acceptable wall or object values.
		boolean nValid = false;
		boolean sValid = false;
		boolean eValid = false;
		boolean wValid = false;
		int totalWalls = 0;
		boolean oValid = false;
		// in the "baseMaze.txt" file, start and end are formatted with S and E instead
		// of start and end( which is considered the correct format).
		// To ensure compatibility, these values are converted to start and end for the
		// purposes of setting objects.
		if (o.equals("S")) {
			o = "start";
		}
		if (o.equals("E")) {
			o = "end";
		}
		for (int i = 0; i < WALL_VALUES.length; i++) {
			if (n.equals(WALL_VALUES[i])) {
				nValid = true;
				totalWalls++;
			}
			if (s.equals(WALL_VALUES[i])) {
				sValid = true;
				totalWalls++;
			}
			if (e.equals(WALL_VALUES[i])) {
				eValid = true;
				totalWalls++;
			}
			if (w.equals(WALL_VALUES[i])) {
				wValid = true;
				totalWalls++;
			}
			if (totalWalls == 4) {
				break;
			}
		}

		for (int i = 0; i < OBJECT_VALUES.length; i++) {
			if (o.equals(OBJECT_VALUES[i])) {
				oValid = true;
			}

		}

		if (!nValid || !sValid || !eValid || !wValid || !oValid) {
			return false;
		} else {
			return true;
		}

	}

	String drawWall(String dir) {
		// Returns a string visually showing a cell wall element in text format. What
		// will be drawn depends on which direction has been passed into the program.
		if (dir.equals("north")) {
			return drawHoriType(this.north);
		} else if (dir.equals("south")) {
			return drawHoriType(this.south);
		} else if (dir.equals("west")) {
			return drawVertType(this.west);
		} else if (dir.equals("east")) {
			return drawVertType(this.east);
		} else {
			return "---";
		}

	}

	private String drawHoriType(String type) {
		// Extends on drawWall function and reduces code by combining if else statement
		// for north or south walls.
		if (type.equals("wall")) {
			return "---";
		} else if (type.equals("breakable")) {
			return "~~~";
		} else if (type.equals("fake")) {
			return "---";
		} else if (type.equals("door")) {
			return "-o-";
		} else {
			return "   ";
		}
	}

	private String drawVertType(String type) {
		// As with drawHoriType but for east and west walls.
		if (type.equals("wall")) {
			return "|";
		} else if (type.equals("breakable")) {
			return "%";
		} else if (type.equals("fake")) {
			return "|";
		} else if (type.equals("door")) {
			return "[";
		} else {
			return " ";
		}
	}

	String drawObject() {

		// Draws the object in a cell, allowing it to be output to the console.
		if (this.object.equals("hammer")) {
			return " ";
		} else if (this.object.equals("start")) {
			return "S";
		} else if (this.object.equals("key")) {
			return " ";
		} else if (this.object.equals("end")) {
			return "E";
		} else if (this.object.equals("player")) {
			return "P";
		} else if (this.object.equals("trophy")) {
			return " ";
		} else {
			return " ";
		}
	}

	public void updateObject(String newObj) {
		// Updates a cell, based on a new string object containing a cell being passed.
		this.object = newObj;
	}

	public void breakWall(String dir) {
		// Remove a wall in the cell, depending on the direction submitted to the
		// method.
		switch (dir) {
		case "north":
			this.north = "no";
			break;
		case "south":
			this.south = "no";
			break;
		case "west":
			this.west = "no";
			break;
		case "east":
			this.east = "no";
			break;
		default:
			break;
		}
	}

	String getObject() {
		// return the object currently in the cell.
		return this.object;
	}

}
