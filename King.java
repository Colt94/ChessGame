package chessGame;
import javax.swing.ImageIcon;

public class King extends Unit{

	King(int _color){
		unitType = "K";
		value = 10000;
		color = _color;
		captured = false;
		
		if (color == 0) {
			img = new ImageIcon("KW.png");
		}
		else if (color == 1) {
			img = new ImageIcon("KB.png");
		}
	}
	
	
	public void setUnits(Unit[][] units)
	{
		this.units = units;
	}

	public void setAvailableMoves(int currentX, int currentY, int moveNum) 
	{
		int[] up = {currentX, currentY + 1};
		int[] upLeft = {currentX - 1, currentY + 1};
		int[] left = {currentX - 1, currentY};
		int[] leftDown = {currentX - 1, currentY - 1};
		int[] down = {currentX, currentY - 1};
		int[] downRight = {currentX + 1, currentY - 1};
		int[] right = {currentX + 1, currentY};
		int[] rightUp = {currentX + 1, currentY + 1};
		
		if(onBoard(up))
		{
			possibleMoves.add(up);
		}
		if(onBoard(upLeft))
		{
			possibleMoves.add(upLeft);
		}
		if(onBoard(left))
		{
			possibleMoves.add(left);
		}
		if(onBoard(leftDown))
		{
			possibleMoves.add(leftDown);
		}
		if(onBoard(down))
		{
			possibleMoves.add(down);
		}
		if(onBoard(downRight))
		{
			possibleMoves.add(downRight);
		}
		if(onBoard(right))
		{
			possibleMoves.add(right);
		}
		if(onBoard(rightUp))
		{
			possibleMoves.add(rightUp);
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