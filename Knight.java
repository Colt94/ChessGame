package chessGame;
import javax.swing.ImageIcon;

public class Knight extends Unit{

	Knight(int _color){
		unitType = "KN";
		value = 3;
		color = _color;
		captured = false;
		
		if (color == 0) {
			img = new ImageIcon("KNW.png");
		}
		else if (color == 1) {
			img = new ImageIcon("KNB.png");
		}
	}
	
	
	public void setUnits(Unit[][] units)
	{
		this.units = units;
	}
	
	public void setAvailableMoves(int currentX, int currentY, int moveNum) 
	{
		int[] twoLeftUp = {currentX - 2, currentY + 1};
		int[] twoLeftDown = {currentX -2, currentY - 1};
		int[] twoDownLeft = {currentX - 1, currentY - 2};
		int[] twoDownRight = {currentX + 1, currentY - 2};
		int[] twoRightDown = {currentX + 2, currentY - 1};
		int[] twoRightUp = {currentX + 2, currentY + 1};
		int[] twoUpRight = {currentX + 1, currentY + 2};
		int[] twoUpLeft = {currentX - 1, currentY + 2};
		
		if(onBoard(twoLeftUp))
		{
			possibleMoves.add(twoLeftUp);
		}
		if(onBoard(twoLeftDown))
		{
			possibleMoves.add(twoLeftDown);
		}
		if(onBoard(twoDownLeft))
		{
			possibleMoves.add(twoDownLeft);
		}
		if(onBoard(twoDownRight))
		{
			possibleMoves.add(twoDownRight);
		}
		if(onBoard(twoRightDown))
		{
			possibleMoves.add(twoRightDown);
		}
		if(onBoard(twoRightUp))
		{
			possibleMoves.add(twoRightUp);
		}
		if(onBoard(twoUpRight))
		{
			possibleMoves.add(twoUpRight);
		}
		if(onBoard(twoUpLeft))
		{
			possibleMoves.add(twoUpLeft);
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