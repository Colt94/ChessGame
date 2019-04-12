package chessGame;
import javax.swing.ImageIcon;

public class Rook extends Unit {
	
	Rook(int _color){
		unitType = "R";
		int value = 3;
		color = _color;
		captured = false;
		
		if (color == 0) {
			img = new ImageIcon("RW.png");
		}
		else if (color == 1) {
			img = new ImageIcon("RB.png");
		}
	}
	
	public void setUnits(Unit[][] units)
	{
		this.units = units;
	}

	public void setAvailableMoves(int currentX, int currentY, int moveNum) 
	{
		//Horizontal and Vertical Movement
		for(int i = currentX - 1; i >= 0; --i)
		{
			if(units[currentY][i] != null && units[currentY][i].color == this.color)
			{
				break;
			}
			if(units[currentY][i] != null && units[currentY][i].color != this.color)
			{
				possibleMoves.add(new int[] {i, currentY});
				break;
			}
			possibleMoves.add(new int[] {i, currentY});
		}
		for(int i = currentX + 1; i <= 7; ++i)
		{
			if(units[currentY][i] != null && units[currentY][i].color == this.color)
			{
				break;
			}
			if(units[currentY][i] != null && units[currentY][i].color != this.color)
			{
				possibleMoves.add(new int[] {i, currentY});
				break;
			}
			possibleMoves.add(new int[] {i, currentY});
		}
		for(int i = currentY - 1; i >= 0; --i)
		{
			if(units[i][currentX] != null && units[i][currentX].color == this.color)
			{
				break;
			}
			if(units[i][currentX] != null && units[i][currentX].color != this.color)
			{
				possibleMoves.add(new int[] {currentX, i});
				break;
			}
			possibleMoves.add(new int[] {currentX, i});
		}
		for(int i = currentY + 1; i <= 7; ++i)
		{
			if(units[i][currentX] != null && units[i][currentX].color == this.color)
			{
				break;
			}
			if(units[i][currentX] != null && units[i][currentX].color != this.color)
			{
				possibleMoves.add(new int[] {currentX, i});
				break;
			}
			possibleMoves.add(new int[] {currentX, i});
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