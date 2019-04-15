package chessGame;

import javax.swing.*;

public class Pawn extends Unit{

	Pawn(int _color) {
		unitType = "P";
		value = 4;
		color = _color;
		captured = false;

		if (color == 0) {
			img = new ImageIcon("PW.png");
		}
		else if (color == 1) {
			img = new ImageIcon("PB.png");
		}
	}
	
	

	public void setUnits(Unit[][] units)
	{
		this.units = units;
	}

	public void setAvailableMoves(int currentX, int currentY, int moveNum)
	{
		//boolean isFirstTurn = isFirstMove(moveNum);
		if (color == 0)
		{
			if (isFirstTurn)
			{
				int[] twoUp = {currentX, currentY - 2};
				int[] oneUp = {currentX, currentY - 1};
				int[] upLeft = {currentX - 1, currentY - 1};
				int[] upRight = {currentX + 1, currentY - 1};

				if(onBoard(twoUp) && units[currentY - 1][currentX] == null && units[currentY - 2][currentX] == null)
				{
					possibleMoves.add(twoUp);
				}
				if(onBoard(oneUp) && units[currentY - 1][currentX] == null)
				{
					possibleMoves.add(oneUp);
				}
				if (onBoard(upLeft) && units[currentY - 1][currentX - 1] != null && units[currentY - 1][currentX - 1].color != this.color)
				{
					possibleMoves.add(upLeft);
				}
				if (onBoard(upRight) && units[currentY - 1][currentX + 1] != null && units[currentY - 1][currentX + 1].color != this.color)
				{
					possibleMoves.add(upRight);
				}
			}
			else
			{
				int[] oneUp = {currentX, currentY - 1};
				int[] upLeft = {currentX - 1, currentY - 1};
				int[] upRight = {currentX + 1, currentY - 1};

				if (onBoard(oneUp) && units[currentY - 1][currentX] == null)
				{
					possibleMoves.add(oneUp);
				}
				if (onBoard(upLeft) && units[currentY - 1][currentX - 1] != null && units[currentY - 1][currentX - 1].color != this.color)
				{
					possibleMoves.add(upLeft);
				}
				if (onBoard(upRight) && units[currentY - 1][currentX + 1] != null && units[currentY - 1][currentX + 1].color != this.color)
				{
					possibleMoves.add(upRight);
				}
			}
		}
		else
		{
			if (isFirstTurn)
			{
				int[] twoDown = {currentX, currentY + 2};
				int[] oneDown = {currentX, currentY + 1};
				int[] downLeft = {currentX - 1, currentY + 1};
				int[] downRight = {currentX + 1, currentY + 1};

				if(onBoard(twoDown) && units[currentY + 1][currentX] == null && units[currentY + 2][currentX] == null)
				{
					possibleMoves.add(twoDown);
				}
				if(onBoard(oneDown) && units[currentY + 1][currentX] == null)
				{
					possibleMoves.add(oneDown);
				}
				if (onBoard(downLeft) && units[currentY + 1][currentX - 1] != null && units[currentY + 1][currentX - 1].color != this.color)
				{
					possibleMoves.add(downLeft);
				}
				if (onBoard(downRight) && units[currentY + 1][currentX + 1] != null && units[currentY + 1][currentX + 1].color != this.color)
				{
					possibleMoves.add(downRight);
				}
			}
			else
			{
				int[] oneDown = {currentX, currentY + 1};
				int[] downLeft = {currentX - 1, currentY + 1};
				int[] downRight = {currentX + 1, currentY + 1};
				if (onBoard(oneDown) && units[currentY + 1][currentX] == null)
				{
					possibleMoves.add(oneDown);
				}
				if (onBoard(downLeft) && units[currentY + 1][currentX - 1] != null && units[currentY + 1][currentX - 1].color != this.color)
				{
					possibleMoves.add(downLeft);
				}
				if (onBoard(downRight) && units[currentY + 1][currentX + 1] != null && units[currentY + 1][currentX + 1].color != this.color)
				{
					possibleMoves.add(downRight);
				}
			}
		}
		removeFriendlyMoves();
	}

	public boolean checkMove(int desiredX, int desiredY)
	{
		int[] possibleMove = new int[2];
		int possibleX = 0;
		int possibleY = 0;
		for(int i = 0; i < possibleMoves.size(); i++)
		{
			possibleMove = possibleMoves.get(i);
			possibleX = possibleMove[0];
			possibleY = possibleMove[1];
			if ((desiredX == possibleX) && (desiredY == possibleY))
			{
				possibleMoves.clear();
				return true;
			}
		}
		possibleMoves.clear();
		return false;
	}
}
