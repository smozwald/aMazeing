package maze;

//Methods that should be implemented by classes which represent players in the maze.

public interface PlayerMethods {

	// Method to set a new position for the player.
	void setNewPosition(String dirToMove);

	// Method to take an object in a cell.
	void takeObject(String objectTaken);

	// Method to return an array of ints showing position in a mae.
	int[] getPosition();

}
