package chessGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.*;
import java.util.Timer;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

import javax.imageio.*;
import javax.swing.*;

public class Board implements ActionListener{
	//Gameboard and pieces
	Unit[][] units = new Unit[8][8];
	JButton[][] squares = new JButton[8][8];
	JPanel pane;

	//Turn notification
	JPanel activePlayer;
	JLabel player;
	JLabel Wcheck;
	JLabel Bcheck;
	JPanel checkDisplay;
	JLabel selfChecked;
	JLabel checkLabel;
	
	//AI ai
	int depth;
	AI bruce;

	//Capture zone
	Unit[] captured = new Unit[32];
	JButton[] captureZone = new JButton[32];
	JPanel capPane = new JPanel();
	int cappedUnits = 0;
	
	//AI turns
	int AI = 0;
	boolean activeAI = false;
	
	List<int[]> wSquares = new ArrayList<>();
	List<int[]> BSquares = new ArrayList<>();
	
	//Ifcheck bits
	int WhiteIsCheck = 0;
	int BlackIsCheck = 0;
	
	//check position
	int[] checkPos = new int[2];
	
	//AI position list
	List<PositionPairs> AIarray = new ArrayList<PositionPairs>();
	
	//Moving pieces
	boolean moving = false;
	Unit mover;
	int prevI;
	int prevJ;
	int moves = 0;
	
	//End game screen state
	boolean parentBoard;
	JPanel checkmatePanel;
	JLabel checkmateText;
	boolean gameEnd = false;
	

	//Constructor sets up initial board state and game screen
	Board(int depth, boolean _activeAI) {

		activeAI = _activeAI;
		
		this.depth = depth;
		bruce = new AI(depth, 1);
		//Set up capture zone
		capPane.setBounds(650, 200, 300, 600);
		capPane.setLayout(new GridLayout(8, 4));
		for (int i = 0; i < 32; i++) {
			captureZone[i] = new JButton();
			captureZone[i].setBackground(Color.DARK_GRAY);
			capPane.add(captureZone[i]);
		}

		//Player indicator
		player = new JLabel("White's Turn");
		activePlayer = new JPanel();
		activePlayer.setBounds(700, 50, 100, 100);
		activePlayer.add(player);
		
		//check indicator
		checkDisplay = new JPanel();
		checkDisplay.setBounds(650, 100, 200, 20);
		checkLabel = new JLabel("");
		checkDisplay.add(checkLabel);
		checkmatePanel = new JPanel();
		checkmatePanel.setBounds(200, 800, 200, 20);
		checkmateText = new JLabel("");
		checkmatePanel.add(checkmateText);
		
		//Spawn black pieces
		units[0][0] = new Rook(1);
		units[0][1] = new Knight(1);
		units[0][2] = new Bishop(1);
		units[0][3] = new King(1);
		units[0][4] = new Queen(1);
		units[0][5] = new Bishop(1);
		units[0][6] = new Knight(1);
		units[0][7] = new Rook(1);
		for(int j = 0; j < 8; j++) {
			units[1][j] = new Pawn(1);
		}

		//Spawn white pieces
		for(int j = 0; j < 8; j++) {
			units[6][j] = new Pawn(0);
		}
		units[7][0] = new Rook(0);
		units[7][1] = new Knight(0);
		units[7][2] = new Bishop(0);
		units[7][3] = new King(0);
		units[7][4] = new Queen(0);
		units[7][5] = new Bishop(0);
		units[7][6] = new Knight(0);
		units[7][7] = new Rook(0);

		//Make button grid/panel
		pane = new JPanel();
		pane.setBounds(00, 00, 600, 600);
		pane.setLayout(new GridLayout(8, 8));
		int count = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (units[i][j] != null) {
					squares[i][j] = new JButton(units[i][j].img);
				}
				else {
					squares[i][j] = new JButton();
				}
				squares[i][j].addActionListener(this);

				if (count % 2 == 0) {
					squares[i][j].setBackground(Color.WHITE);
				}
				else {
					squares[i][j].setBackground(Color.BLACK);
				}

				String action = Integer.toString(i) + Integer.toString(j);
				squares[i][j].setActionCommand(action);
				pane.add(squares[i][j]);
				count++;
			}
			count--;
		}
	}

	JPanel getBoard() {
		return pane;
	}

	JPanel getActivePlayer() {
		return activePlayer;
	}

	String squareUnitType(int i, int j) {
		String out = "";
		if (units[i][j] != null) {
			out = units[i][j].unitType;
		}
		return out;
	}

	void moveUnit(int i, int j) {
		mover = units[i][j];
		squares[i][j].setIcon(new ImageIcon("blank.png"));
		units[i][j] = null;
		prevI = i;
		prevJ = j;
	}

	void placeUnit(int i, int j) {
		boolean unitCapped = false;
		//Moves enemy unit to captured zone
		if (units[i][j] != null) {
			units[i][j].captured = true;
			captured[cappedUnits] = units[i][j];
		    captureZone[cappedUnits].setIcon(squares[i][j].getIcon());
			cappedUnits++;
			unitCapped = true;
		}

		units[i][j] = mover;
		squares[i][j].setIcon(units[i][j].img);
		//Make sure player does not move own king into check
		updateCheck();
		if ((mover.color == 0 && WhiteIsCheck == 1) || (mover.color == 1 && BlackIsCheck == 1)) {
			units[prevI][prevJ] = mover;
			squares[prevI][prevJ].setIcon(mover.img);
			
			if (unitCapped) {
				cappedUnits--;
				units[i][j] = captured[cappedUnits];
				units[i][j].captured = false;
				squares[i][j].setIcon(captureZone[cappedUnits].getIcon());
				captureZone[cappedUnits].setIcon(new ImageIcon("blank.png"));
				captured[cappedUnits] = null;
			}
			else {
				if (prevI != i || prevJ != j) {
					units[i][j] = null;
					squares[i][j].setIcon(new ImageIcon("blank.png"));
				}
			}
		}
		updateCheck();
		
		mover = null;
	}

	//Prints button coordinates to console when clicked
	public void actionPerformed(ActionEvent arg0) {
		int i = Integer.parseInt(arg0.getActionCommand().substring(0, 1));
		int j = Integer.parseInt(arg0.getActionCommand().substring(1));

		if (!moving && units[i][j] != null) {
			//Prevent players from selecting other player's pieces
			if (units[i][j].color == moves % 2) {
				moving = true;
				moveUnit(i, j);
				mover.setUnits(units);
				mover.setAvailableMoves(j, i, moves);
				for (int m = 0; m < mover.possibleMoves.size(); m++) {
					if(squares[mover.possibleMoves.get(m)[1]][mover.possibleMoves.get(m)[0]].getBackground() == Color.WHITE) {
						wSquares.add(new int[] {mover.possibleMoves.get(m)[1], mover.possibleMoves.get(m)[0]});
					}
					else if (squares[mover.possibleMoves.get(m)[1]][mover.possibleMoves.get(m)[0]].getBackground() == Color.BLACK){
						BSquares.add(new int[] {mover.possibleMoves.get(m)[1], mover.possibleMoves.get(m)[0]});
					}
					squares[mover.possibleMoves.get(m)[1]][mover.possibleMoves.get(m)[0]].setBackground(Color.blue);
				}
			}
		}
		else if (moving) {
			//check if valid spot
			if ((units[i][j] == null || units[i][j].color != mover.color) && mover.checkMove(j, i)) {
				
				placeUnit(i, j);
				
				for (int m = 0; m < wSquares.size(); m++) {
					squares[wSquares.get(m)[0]][wSquares.get(m)[1]].setBackground(Color.WHITE);
				}
				for (int m = 0; m < BSquares.size(); m++) {
					squares[BSquares.get(m)[0]][BSquares.get(m)[1]].setBackground(Color.BLACK);
				}
				//mover.clearMoves();
				wSquares.clear();
				BSquares.clear();
			
				if (units[i][j] != null) {
					units[i][j].isFirstTurn = false;
					AI = 1;
					moves++;
					updatePlayer();
				}
				moving = false;

			}
			else {
				placeUnit(prevI, prevJ);
				for (int m = 0; m < wSquares.size(); m++) {
					squares[wSquares.get(m)[0]][wSquares.get(m)[1]].setBackground(Color.WHITE);
				}
				for (int m = 0; m < BSquares.size(); m++) {
					squares[BSquares.get(m)[0]][BSquares.get(m)[1]].setBackground(Color.BLACK);
				}
				//mover.clearMoves();
				wSquares.clear();
				BSquares.clear();
				moving = false;
			}
			
			if (WhiteIsCheck == 1) {
				if (checkCheckmate(0)) {
					updateGameEnd(1);
				}
			}
			if (BlackIsCheck == 1) {
				if(checkCheckmate(1)) {
					updateGameEnd(0);
				}
			}

		}
		if (AI == 1 && activeAI == true) {
			generateMove();
		}
		
		if (!gameEnd) {
			updateCheck();
			if (WhiteIsCheck == 1) {
				checkLabel.setText("WHITE IS IN CHECK");
				//System.out.println("White check");
				
			}
			else if (BlackIsCheck == 1) {
				checkLabel.setText("BLACK IS IN CHECK");
				//System.out.println("Black check");
			}
			else if (BlackIsCheck == 0 && WhiteIsCheck == 0) {
				checkLabel.setText("");
				//System.out.println("Black uncheck");
			}
		}		
		
	}

	public void updatePlayer() {
		if (moves % 2 == 0) {
			player.setText("White's Turn");
		}
		else if (moves % 2 == 1) {
			player.setText("Black's Turn");
			}
	}
	
	public void updateGameEnd(int winner) {
		if (winner == 1) {
			//black wins
		//	checkmateText.setText("White in checkmate, black wins");
			System.out.println("Black wins");
			checkLabel.setText("White in checkmate, black wins");
		}
		if (winner == 0) {
			//white wins
			//checkmateText.setText("Black in checkmate, white wins");
			System.out.println("Black wins");
			checkLabel.setText("Black in checkmate, white wins");
		}
		AI = 0;
		gameEnd = true;
	}
	
	public void updateCheck() {
		WhiteIsCheck = 0;
		BlackIsCheck = 0;
		
		ArrayList<Unit> allMoves = new ArrayList<Unit>();
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < 8; b++) {
				if (units[a][b] != null) {
					units[a][b].setUnits(units);
					units[a][b].clearMoves();
					units[a][b].setAvailableMoves(b, a, moves);
					allMoves.add(units[a][b]);
				}
			}
		}
		
		for (int w = 0; w < allMoves.size(); w++) {
			for (int k = 0; k < allMoves.get(w).possibleMoves.size(); k++) {
				int y = allMoves.get(w).possibleMoves.get(k)[0];
				int x = allMoves.get(w).possibleMoves.get(k)[1];
				if (units[x][y] != null) {
					if (units[x][y].unitType == "K" && units[x][y].color != allMoves.get(w).color) {
						if (units[x][y].color == 0) {
							WhiteIsCheck = 1;
						}
						if (units[x][y].color == 1) {
							BlackIsCheck = 1;
						}
					}
				}
			}
		}	
	}
	
	public boolean checkCheckmate(int kingColor) {
		boolean checkmate = true;
		
		Board boardCopy = new Board(depth,false);
		boardCopy.units = copyBoard(this.units);
		boardCopy.moves = this.moves;
		
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				
				if (boardCopy.units[i][j] != null) {
					if (boardCopy.units[i][j].color == kingColor) {
						Unit move = boardCopy.units[i][j];
						move.clearMoves();
						move.setAvailableMoves(j,  i,  moves);
					
						List<int[]> myMoves = move.possibleMoves;
						for(int k = 0; k < myMoves.size(); k++) {
							boardCopy.units = copyBoard(this.units);
							boardCopy.moves = this.moves;
							int x = myMoves.get(k)[1];
							int y = myMoves.get(k)[0];
							boardCopy.moveUnit(i,  j);
							boardCopy.placeUnit(x,  y);
							boardCopy.updateCheck();
							if (kingColor == 0 && boardCopy.WhiteIsCheck == 0 && checkmate == true) {
								checkmate = false;
							}
							if (kingColor == 1 && boardCopy.BlackIsCheck == 0 && checkmate == true) {
								checkmate = false;
							}
						}
					}
				}
			}
		}
		
		if (checkmate) {
			System.out.println(kingColor + " CHECKMATE");
		}
		else {
			System.out.println(kingColor + " CHECK");
		}
		
		return checkmate;
	}
	
	public void generateMove() {
		//AI will choose a random move from a list of valid moves
		
				bruce.setBoardState(units);
				bruce.MiniMax(depth, true);
				
				mover = units[bruce.initPos[0]][bruce.initPos[1]];
				int i1 = bruce.megaMove[0];
				int j1 = bruce.megaMove[1];
				
					//mover.checkMove(0, 0);
					//mover.clearMoves();
					placeUnit(i1,j1);
					units[bruce.initPos[0]][bruce.initPos[1]] = null;
					squares[bruce.initPos[0]][bruce.initPos[1]].setIcon(new ImageIcon("blank.png"));
					
					if (moves % 2 == 0) {
						updateCheck();
					}
					else if (moves % 2 == 1) {
						updateCheck();
					}
					
					moves++;
					updatePlayer();
					units[i1][j1].isFirstTurn = false;
					//break;
				
				//Moves.clear();
			//}
			//else {
				//placeUnit(prevI, prevJ);
			//}
			
	
		//}
		//set turn back to player
		AI = 0;
	}
	

	public Unit[][] getUnits()
	{
		return units;
	}

	//This is for silly stuff
	class Helper extends TimerTask{
		public void run() {
			squares[prevI][prevJ].setIcon(units[prevI][prevJ].img);
		}
	}
	
	public Unit[][] copyBoard(Unit[][] in){
		Unit[][] out = new Unit[8][];
		for(int i = 0; i < 8; i++) {
			out[i] = in[i].clone();
		}
		return out;
	}
}
