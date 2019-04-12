package chessGame;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.lang.*;

public class AI {
	
	int depth;
	int color;
	int realColor;
	int [] bestMove;
	int aiPrevIMax;
	int aiPrevJMax;
	int aiPrevIMin;
	int aiPrevJMin;
	int movedIMax;
	int movedJMax;
	int movedIMin;
	int movedJMin;
	int megaMoveValue = -9999999;
	int [] megaMove = new int[2];
	int [] initPos = new int[2];
	int numPossibleMovesMax;
	int numPossibleMovesMin;
	Unit mover;
	Unit captured;
	boolean maxWasCaptured = false;
	boolean minWasCaptured = false;
	
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
	
	public int MiniMax(int dep, boolean maxPlayer) {
		int value;
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
							boardstate[i][j].setUnits(boardstate);
							boardstate[i][j].setAvailableMoves(j, i, color);
							setFirstTurn();
							numPossibleMovesMax = boardstate[i][j].possibleMoves.size();
							mover = boardstate[i][j];
							for(int k = 0; k < numPossibleMovesMax; ++k)
							{
								mover = boardstate[i][j];
								aiPrevIMax = i;
								aiPrevJMax = j;
								movedIMax = mover.possibleMoves.get(k)[1];
								movedJMax = mover.possibleMoves.get(k)[0];
								if (boardstate[movedIMax][movedJMax] != null)
								{
									maxWasCaptured = true;
								}
								aiPlace(movedIMax, movedJMax, aiPrevIMax, aiPrevJMax);
								setFirstTurn();
								value = Math.max(utilityFunction(color), MiniMax(dep - 1, false));
								mover = boardstate[movedIMax][movedJMax];
								if (value > megaMoveValue)
								{
									megaMoveValue = value;
									megaMove[0] = mover.possibleMoves.get(k)[1];
									megaMove[1] = mover.possibleMoves.get(k)[0];
									initPos[0] = aiPrevIMax;
									initPos[1] = aiPrevJMax;
								}
								aiPrevIMax = mover.possibleMoves.get(k)[1];
								aiPrevJMax = mover.possibleMoves.get(k)[0];
								aiPlace(i, j, aiPrevIMax, aiPrevJMax);
								setFirstTurn();
								if (maxWasCaptured)
								{
									boardstate[aiPrevIMax][aiPrevJMax] = captured;
									maxWasCaptured = false;
								}
							}
							mover.possibleMoves.clear();
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
							boardstate[i][j].setUnits(boardstate);
							boardstate[i][j].setAvailableMoves(j, i, realColor);
							setFirstTurn();
							numPossibleMovesMin = boardstate[i][j].possibleMoves.size();
							mover = boardstate[i][j];
							for(int k = 0; k < numPossibleMovesMin; ++k)
							{
								mover = boardstate[i][j];
								aiPrevIMin = i;
								aiPrevJMin = j;
								movedIMin = mover.possibleMoves.get(k)[1];
								movedJMin = mover.possibleMoves.get(k)[0];
								if (boardstate[movedIMin][movedJMin] != null)
								{
									minWasCaptured = true;
								}
								aiPlace(movedIMin, movedJMin, aiPrevIMin, aiPrevJMin);
								setFirstTurn();
								value = Math.min(utilityFunction(color), MiniMax(dep - 1, false));
								mover = boardstate[movedIMin][movedJMin];
								aiPrevIMin = mover.possibleMoves.get(k)[1];
								aiPrevJMin = mover.possibleMoves.get(k)[0];
								aiPlace(i, j, aiPrevIMin, aiPrevJMin);
								setFirstTurn();
								if (minWasCaptured)
								{
									boardstate[aiPrevIMin][aiPrevJMin] = captured;
									minWasCaptured = false;
								}
							}
							mover.possibleMoves.clear();
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
					if(boardstate[i][j].color == c) {
						score = score + boardstate[i][j].value;
					}
					else
					{
						score = score - boardstate[i][j].value;
					}
				}
			}
			
		}
		return 0;
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
	
	void aiPlace(int i, int j, int aiPrevI, int aiPrevJ) {
		if (boardstate[i][j] != null) {
			captured = boardstate[i][j];
		}
		boardstate[i][j] = mover;
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

