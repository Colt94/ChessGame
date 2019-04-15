package chessGame;
import javax.swing.ImageIcon;

public class Bishop extends Unit{

	Bishop(int _color){
		unitType = "B";
		value = 10;
		color = _color;
		captured = false;
		
		if (color == 0) {
			img = new ImageIcon("BW.png");
		}
		else if (color == 1) {
			img = new ImageIcon("BB.png");
		}
	}
	
	public void clearMoves() {
		possibleMoves.clear();
	}
	
	public void setUnits(Unit[][] units)
	{
		this.units = units;
	}
	
	public void setAvailableMoves(int currentX, int currentY, int moveNum) 
	{
		//Down and Right
		for(int i = 1; i < currentX + 8; ++i)
		{
			if(!onBoard(new int[] {currentX + i, currentY + i}))
			{
				break;
			}
			if(units[currentY + i][currentX + i] != null && units[currentY + i][currentX + i].color == this.color)
			{
				break;
			}
			if(units[currentY + i][currentX + i] != null && units[currentY + i][currentX + i].color != this.color)
			{
				possibleMoves.add(new int[] {currentX + i, currentY + i});
				break;
			}
			possibleMoves.add(new int[] {currentX + i, currentY + i});
		}
		//Up and Right
		for(int i = 1; i < currentX + 8; ++i)
		{
			if(!onBoard(new int[] {currentX + i, currentY - i}))
			{
				break;
			}
			if(units[currentY - i][currentX + i] != null && units[currentY - i][currentX + i].color == this.color)
			{
				break;
			}
			if(units[currentY - i][currentX + i] != null && units[currentY - i][currentX + i].color != this.color)
			{
				possibleMoves.add(new int[] {currentX + i, currentY - i});
				break;
			}
			possibleMoves.add(new int[] {currentX + i, currentY - i});
		}
		//Down and Left
		for(int i = 1; i < currentX + 8; ++i)
		{
			if(!onBoard(new int[] {currentX - i, currentY + i}))
			{
				break;
			}
			if(units[currentY + i][currentX - i] != null && units[currentY + i][currentX - i].color == this.color)
			{
				break;
			}
			if(units[currentY + i][currentX - i] != null && units[currentY + i][currentX - i].color != this.color)
			{
				possibleMoves.add(new int[] {currentX - i, currentY + i});
				break;
			}
			possibleMoves.add(new int[] {currentX - i, currentY + i});
		}
		//Up and Left
		for(int i = 1; i < currentX + 8; ++i)
		{
			if(!onBoard(new int[] {currentX - i, currentY - i}))
			{
				break;
			}
			if(units[currentY - i][currentX - i] != null && units[currentY - i][currentX - i].color == this.color)
			{
				break;
			}
			if(units[currentY - i][currentX - i] != null && units[currentY - i][currentX - i].color != this.color)
			{
				possibleMoves.add(new int[] {currentX - i, currentY - i});
				break;
			}
			possibleMoves.add(new int[] {currentX - i, currentY - i});
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