package maze;

//Methods that should be implemented by cell classes.

public interface CellMethods {

	// Update a cells object as a player moves with it and interacts with objects.
	void updateObject(String objectUpdate);

	// Updates a cells walls as a player interacts with them, possibly breaking
	// them.
	void breakWall(String wallToBreak);

}
