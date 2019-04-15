package chessGame;

import java.util.*;
import java.lang.*;

public class AI {
	
	int depth;
	int color;
	int realColor;
	int [] bestMove;
	/*int aiPrevIMax;
	int aiPrevJMax;
	int aiPrevIMin;
	int aiPrevJMin;
	int movedIMax;
	int movedJMax;
	int movedIMin;
	int movedJMin;*/
	int megaMoveValue = -9999999;
	int [] megaMove = new int[2];
	int [] initPos = new int[2];
	//int numPossibleMovesMax;
	//int numPossibleMovesMin;
	//Unit aiMover;
	Stack<Unit> captured = new Stack<Unit>();
	//boolean maxWasCaptured = false;
	//boolean minWasCaptured = false;
	
	Unit[][] boardstate = new Unit[8][8];
	
	AI(int depth, int color) {
		this.depth = depth;
		this.color = 1;
		if (color == 0)
		{
			realColor = 1;
		}
		else
		{
			realColor = 0;
		}
	}
	
	public void setBoardState(Unit[][] state) {
		boardstate = unitDeepCopy(state);
	}
	
	public Unit[][] getBoardstate()
	{
		return boardstate;
	}
	
	public int MiniMax(int dep, boolean maxPlayer) {
		int value;
		int aiPrevI;
		int aiPrevJ;
		int movedI;
		int movedJ;
		int numPossibleMoves;
		Unit aiMover;
		boolean wasCaptured = false;
		
		if (dep == 0)
		{
			if (depth % 2 == 0) {
				return value = utilityFunction(color);
			}
			else {
				return value = utilityFunction(realColor);
			}
		}
		if (maxPlayer)
		{
			value = -9999999;
			megaMoveValue = value;
			for (int i = 0; i < 8; ++i)
			{
				for(int j = 0; j < 8; ++j)
				{
					if(boardstate[i][j] != null)
					{
						if(boardstate[i][j].color == color)
						{
							setFirstTurn();
							boardstate[i][j].setUnits(boardstate);
							boardstate[i][j].setAvailableMoves(j, i, color);
							numPossibleMoves = boardstate[i][j].possibleMoves.size();
							aiMover = boardstate[i][j];
							for(int k = 0; k < numPossibleMoves; ++k)
							{
								//System.out.println(numPossibleMoves);
								setFirstTurn();
								boardstate[i][j].setUnits(boardstate);
								boardstate[i][j].setAvailableMoves(j, i, color);
								//aiMover = boardstate[i][j];
								aiPrevI = i;
								aiPrevJ = j;
								movedI = aiMover.possibleMoves.get(k)[1];
								movedJ = aiMover.possibleMoves.get(k)[0];
								if (boardstate[movedI][movedJ] != null)
								{
									wasCaptured = true;
								}
								aiPlace(movedI, movedJ, aiPrevI, aiPrevJ, aiMover);
								setFirstTurn();
								value = Math.max(utilityFunction(color), MiniMax(dep - 1, false));
								setFirstTurn();
								aiMover = boardstate[movedI][movedJ];
								if (value > megaMoveValue && dep == depth)
								{
									megaMoveValue = value;
									megaMove[0] = movedI;
									megaMove[1] = movedJ;
									initPos[0] = aiPrevI;
									initPos[1] = aiPrevJ;
								}
								aiPrevI = movedI;
								aiPrevJ = movedJ;
								aiPlace(i, j, aiPrevI, aiPrevJ, aiMover);
								setFirstTurn();
								if (wasCaptured)
								{
									boardstate[aiPrevI][aiPrevJ] = captured.pop();
									wasCaptured = false;
								}
							}
							aiMover.possibleMoves.clear();
						}
					}
				}
			}
			return value;
		}
		else
		{
			value = 9999999;
			for (int i = 0; i < 8; ++i)
			{
				for(int j = 0; j < 8; ++j)
				{
					if(boardstate[i][j] != null)
					{
						if(boardstate[i][j].color == realColor)
						{
							setFirstTurn();
							boardstate[i][j].setUnits(boardstate);
							boardstate[i][j].setAvailableMoves(j, i, realColor);
							numPossibleMoves = boardstate[i][j].possibleMoves.size();
							aiMover = boardstate[i][j];
							for(int k = 0; k < numPossibleMoves; ++k)
							{
								setFirstTurn();
								boardstate[i][j].setUnits(boardstate);
								boardstate[i][j].setAvailableMoves(j, i, realColor);
								//aiMover = boardstate[i][j];
								aiPrevI = i;
								aiPrevJ = j;
								movedI = aiMover.possibleMoves.get(k)[1];
								movedJ = aiMover.possibleMoves.get(k)[0];
								if (boardstate[movedI][movedJ] != null)
								{
									wasCaptured = true;
								}
								aiPlace(movedI, movedJ, aiPrevI, aiPrevJ, aiMover);
								setFirstTurn();
								value = Math.min(utilityFunction(color), MiniMax(dep - 1, true));
								setFirstTurn();
								aiMover = boardstate[movedI][movedJ];
								aiPrevI = movedI;
								aiPrevJ = movedJ;
								aiPlace(i, j, aiPrevI, aiPrevJ, aiMover);
								setFirstTurn();
								if (wasCaptured)
								{
									boardstate[aiPrevI][aiPrevJ] = captured.pop();
									wasCaptured = false;
								}
							}
							aiMover.possibleMoves.clear();
						}
					}
				}
			}
			return value;
		}
	}
	
	public int utilityFunction(int c) {
		
		int score = 0;
		//color = (moves % 2);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (boardstate[i][j] != null)
				{
					if (boardstate[i][j].color == c) {
						score = score + boardstate[i][j].value;
						if (i == 1)
						{
							score = score + 1;
						}
						else if (i == 2)
						{
							score = score + 2;
						}
						else if (i == 3)
						{
							score = score + 3;
						}
					}
					else
					{
						score = score - boardstate[i][j].value;
						if (i == 6)
						{
							score = score - 1;
						}
						else if (i == 5)
						{
							score = score - 2;
						}
						else if (i == 4)
						{
							score = score - 3;
						}
					}
				}
			}
			
		}
		return score;
	}
	
	void setFirstTurn()
	{
		for (int i = 0; i < 8; ++i)
		{
			for (int j = 0; j < 8; ++j)
			{
				if (boardstate[i][j] != null)
				{
					boardstate[i][j].isFirstTurn = false;
				}
			}
		}
		
		for (int i = 0; i < 8; ++i)
		{
			if (boardstate[1][i] != null)
			{
				if (boardstate[1][i].color == 1 && boardstate[1][i].unitType.equals("P"))
				{
					boardstate[1][i].isFirstTurn = true;
				}
			}
			if (boardstate[6][i] != null)
			{
				if (boardstate[6][i].color == 0 && boardstate[6][i].unitType.equals("P"))
				{
					boardstate[6][i].isFirstTurn = true;
				}
			}
		}
	}
	
	void aiPlace(int i, int j, int aiPrevI, int aiPrevJ, Unit aiMover) {
		if (boardstate[i][j] != null) {
			captured.push(boardstate[i][j]);
		}
		boardstate[i][j] = aiMover;
		boardstate[aiPrevI][aiPrevJ] = null;
	}
	
	Unit[][] unitDeepCopy(Unit[][] input)
	{
		Unit[][] result = new Unit[input.length][];
		
		for (int i = 0; i < input.length; ++i)
		{
			result[i] = input[i].clone();
		}
		return result;
	}

}

