package tp.pr2.logic;

import tp.pr2.utils.Position;
import tp.pr2.utils.PosList;

/**
 * Surface class
 * <p>
 * A surface is a matrix of cells with X rows and Y columns.
 * 
 * @author David Calleja and Marta Pastor
 * @version 2.0
 */
public class Surface {

	private Cell[][] surface;
	private int rows;
	private int columns;

	private String board;
	private String ConsoleMsg = new String("");
	private String ErrMsg = new String("");

	/**
	 * Surface constructor
	 * 
	 * @param numRows
	 *            The number of rows that is going to have our surface
	 * @param numColumns
	 *            The number of columns that is going to have our surface
	 */
	public Surface(int numRows, int numColumns) {
		this.rows = numRows;
		this.columns = numColumns;

		surface = new Cell[rows][columns];

		// Initializes all positions of the surface to null:
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				surface[i][j] = null;
			}
		}

	}

	/**
	 * Accesor method that returns the cell located in a given position.
	 * 
	 * @param r
	 *            The row where is the cell we want to get
	 * @param c
	 *            The column where is the cell we want to get
	 * @return The cell that is in the given position (row and column)
	 */

	public boolean getCell(int r, int c) {
		boolean cellGot;
		if (r < rows && c < columns) {
			if (surface[r][c] == null) {
				cellGot = false;
			} else {
				cellGot = true;
			}
		} else {
			cellGot = false;
		}
		return cellGot;
	}

	/**
	 * Delete the cell from a given position.
	 * 
	 * @param r
	 *            The row where is located the cell we want to delete
	 * @param c
	 *            The column where is located the cell we want to delete
	 */
	public void deleteCell(int r, int c) {
		surface[r][c] = null;
	}

	/**
	 * Inserts a given cell in a given position.
	 * 
	 * @param r
	 *            The row where we want to insert the cell
	 * @param c
	 *            The column where we want to insert the cell
	 * @param cell
	 *            The cell we want to insert
	 * @return If the cell has been inserted or not
	 */
	public boolean insertCell(int r, int c, Cell cell) {
		boolean inserted = false;

		if (surface[r][c] == null) {
			surface[r][c] = cell;

			inserted = true;
		}
		return inserted;
	}

	/**
	 * Checks if a cell located in a given position has already move from
	 * another one in the current "step" movement.
	 * 
	 * @param posList
	 *            The auxiliar list containing all the cells already moved
	 * @param r
	 *            The row where is located the given cell
	 * @param c
	 *            The column where is located the given cell
	 * @return If a given cell has already moved in the current "step" movement
	 */
	public boolean hasMoved(PosList posList, int r, int c) {
		boolean moved = false;

		for (int i = 0; i < posList.length(); i++) {
			if (posList.get(i).getRow() == r && posList.get(i).getColumn() == c)
				moved = true;
		}

		return moved;
	}

	/*
	 * Sets the surface as we want to see it in the console.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		board = "" + System.getProperty("line.separator");

		// Draw the board whatever the value of the cell is:
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (surface[i][j] == null) {
					board = board + " | - | ";
				} else {
					board = board + " |" + surface[i][j] + "| ";
				}
			}
			// Line finished:
			board = board + System.getProperty("line.separator");
		}
		// Board finished:
		board = board + System.getProperty("line.separator");

		return board;
	}

	/**
	 * Creates a posList with all the positions in the board which we will use
	 * later to select randomly the INI_NUM_CELLS initial positions.
	 * 
	 * @param r
	 *            The number of rows that has our surface
	 * @param c
	 *            The number of columns that has our surface
	 * @return A posList with all the board positions ordered.
	 */
	public PosList allBoardPositions(int r, int c) {
		PosList allPosList = new PosList();

		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				Position newPos = new Position(i, j);
				allPosList.add(newPos, allPosList.length());
			}
		}

		return allPosList;
	}

	/**
	 * Creates a posList with the positions where the cell can move - the cell
	 * can move to a position if it is neighbour and if it is free of cells.
	 * 
	 * @param r
	 *            The row where is located the given cell
	 * @param c
	 *            The column where is located the given cell
	 * @return A posList with all the free neighbour positions ordered.
	 */
	public PosList freeNeighbourPositions(int r, int c) {
		PosList freePosList = new PosList();

		for (int i = r - 1; i <= r + 1; i++) {
			if (0 <= i && i < this.rows) {
				for (int j = c - 1; j <= c + 1; j++) {
					if (0 <= j && j < this.columns && (i != r || j != c)) {
						Position pos = new Position(i, j);
						freePosList.add(pos, freePosList.length());
					}
				}
			}
		}

		return freePosList;
	}

	/**
	 * Returns the position coords (r, c) kept in the n position of the posList.
	 * 
	 * @param posList
	 *            A given posList
	 * @param n
	 *            The index of the posList element we want to get
	 * @return The position coords (r, c) kept in the n position of the posList
	 */
	public Position selectFirstPositions(PosList posList, int n) {
		return posList.get(n);
	}

	// Methods for assignment 2

	/**
	 * Executes the movement of the cell - it can die, eat, move...
	 * 
	 * @param pos
	 *            The position from where we want to move the cell
	 * @return The position where the cell has been moved
	 */
	public Position executeMovement(Position pos) {
		Position posAux;
		posAux = surface[pos.getRow()][pos.getColumn()].executeMovement(pos, this);
		return posAux;
	}

	/**
	 * Returns if a cell is edible or not.
	 * 
	 * @param pos
	 *            The position where the cell is located
	 * @return True if is edible (simple) or false if is not (complex)
	 */
	public boolean edible(Position pos) {
		if (surface[pos.getRow()][pos.getColumn()].isEdible()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Add a new instruction to the string that is going to show all the
	 * instructions executed.
	 * 
	 * @param message
	 *            The string where we are keeping the message that is going to
	 *            be printed in console.
	 */
	public void addNewMessageToConsoleMsg(String message) {
		this.ConsoleMsg += message;
	}

	/**
	 * Add a new error to the string that is going to show all the errors found.
	 * 
	 * @param errMsg
	 *            The string where we are keeping the errors that is going to be
	 *            printed in console.
	 */
	public void addNewMessageToErrMsg(String errMsg) {
		this.ErrMsg += errMsg;
	}

	/**
	 * Clears the message string.
	 */
	public void clearConsoleMsg() {
		this.ConsoleMsg = "";
	}

	/**
	 * Clears the error string.
	 */
	public void clearErrMsg() {
		this.ErrMsg = "";
	}

	/**
	 * Returns the complete message string.
	 * 
	 * @return The string with all the instructions executed in that step.
	 */
	public String getConsoleMsg() {
		return this.ConsoleMsg;
	}

	/**
	 * Returns the complete error string.
	 * 
	 * @return The string with all the errors found in that step.
	 */
	public String getErrMsg() {
		return this.ErrMsg;
	}

}
