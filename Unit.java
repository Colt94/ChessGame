package chessGame;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public abstract class Unit {
	String unitType;
	int color;		//0 == white, 1 == black
	int value;
	boolean captured;
	ImageIcon img;
	List<int[]> possibleMoves = new ArrayList<>();
	Unit[][] units;
	boolean isFirstTurn = true;
	
	public void clearMoves() {
		possibleMoves.clear();
	}

	//Creates the set of possible moves
	public abstract void setAvailableMoves(int currentX, int currentY, int moveNum);
	//Checks if the move is in the set of possible moves
	public abstract boolean checkMove(int desiredX, int desiredY);
	public abstract void setUnits(Unit[][] units);

	protected boolean onBoard(int[] coordinates)
	{
		int x = coordinates[0];
		int y = coordinates[1];
		if (x >= 0 && x <= 7 && y >= 0 && y <= 7)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	protected boolean isFirstMove(int moveNum)
	{
		if(moveNum == 0 || moveNum == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected void removeFriendlyMoves()
	{
		int [] moves;
		for (int i = possibleMoves.size() - 1; i >= 0; --i)
		{
			moves = possibleMoves.get(i);
			if (units[moves[1]][moves[0]] != null)
			{
				if (units[moves[1]][moves[0]].color == this.color)
				{
					possibleMoves.remove(i);
				}
			}
		}
	}
}
