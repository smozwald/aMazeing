package maze;

//Key functionality that should be implemented by maze classes.

public interface MazeMethods {

	// Method to show the maze, whether in a text output or in a gui.
	void showMaze();

	// Method to find certain cells within a maze, i.e. the start position.
	int[] findCell(String toFind);

	// Method to allow player to move within a maze.
	void playerMove(String direction, Player activePlayer);
}
