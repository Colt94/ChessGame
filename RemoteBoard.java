package chessGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.util.List;

import javax.imageio.*;
import javax.swing.*;

public class RemoteBoard implements ActionListener {
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
	
	JPanel colordisplay;
	JLabel yourColor;
	
	boolean waiting;
	
	int color;
	
	//client object
	Client client;
	boolean WrequestMove, BrequestMove, WmoveRec, BmoveRec,WmoveSent, BmoveSent = false;  

	//Capture zone
	Unit[] captured = new Unit[32];
	JButton[] captureZone = new JButton[32];
	JPanel capPane = new JPanel();
	int cappedUnits = 0;
	
	//AI turns
	int AI = 0;
	AI bruce = new AI(2, 1);
	
	
	//Ifcheck bits
	int WhiteIsCheck = 0;
	int BlackIsCheck = 0;
	
	//check position
	int[][] checkPos = new int[2][2];
	
	//AI position list
	List<PositionPairs> AIarray = new ArrayList<PositionPairs>();
	
	//Moving pieces
	boolean moving = false;
	Unit mover;
	int prevI;
	int prevJ;
	int moves = 0;

	//Constructor sets up initial board state and game screen
	RemoteBoard(int color, Client client) {

		this.color = color;
		this.client = client;
		
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
		
		if (color == 0) {
			yourColor = new JLabel("Your color: White");
		}
		else {
			yourColor = new JLabel("Your color: Black");
		}
		colordisplay = new JPanel();
		colordisplay.setBounds(200, 900, 100, 100);
		colordisplay.add(yourColor);
		colordisplay.setVisible(true);
		
		//check indicator
		checkDisplay = new JPanel();
		checkDisplay.setBounds(700, 100, 100, 100);
		
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
		//This bit is for silly stuff
		Timer timer = new Timer();
		//timer.schedule(new Helper(), 800);

		//Moves enemy unit to captured zone
		if (units[i][j] != null) {
		//	if(units[i][j].unitType == "K") {
				
			//}
			//else {
			units[i][j].captured = true;
			captured[cappedUnits] = units[i][j];
		    captureZone[cappedUnits].setIcon(squares[i][j].getIcon());
			cappedUnits++;
		}

		units[i][j] = mover;
		squares[i][j].setIcon(units[i][j].img);
		//squares[i][j].setIcon(new ImageIcon("capturesmall.gif"));
		mover = null;
	}

	//Prints button coordinates to console when clicked
	public void actionPerformed(ActionEvent arg0) {
		int i = Integer.parseInt(arg0.getActionCommand().substring(0, 1));
		int j = Integer.parseInt(arg0.getActionCommand().substring(1));

		if (!moving && units[i][j] != null && moves % 2 == color) {
			//Prevent players from selecting other player's pieces
			if (units[i][j].color == moves % 2) {
				moving = true;
				moveUnit(i, j);
				mover.setUnits(units);
				mover.setAvailableMoves(j, i, moves);
			}
		}
		else if (moving) {
			//check if valid spot
			if ((units[i][j] == null || units[i][j].color != mover.color) && mover.checkMove(j, i)) {
				
				placeUnit(i, j);
				//check if the player's move puts them OUT OF check
				if(moves % 2 == 0 && WhiteIsCheck == 1) {
					int x = checkPos[0][0];
					int y = checkPos[1][1];
					if(!testCheck(x,y,1)) {
						WhiteIsCheck = 0;
						checkDisplay.setVisible(false);
						checkDisplay.remove(Wcheck);
						checkDisplay.revalidate();
					}
				}
					//send move to server
				String oldX = Integer.toString(prevI);
				String oldY = Integer.toString(prevJ);
				String newX = Integer.toString(i);
				String newY = Integer.toString(j);
				System.out.print("TESTSTSTSTS");
				String move = oldX + "," + oldY + "," + newX + "," + newY;
				
				
				/*
				else if(moves % 2 == 1 && BlackIsCheck == 1) {
					int x = checkPos[0][0];
					int y = checkPos[1][1];
					if(!testCheck(x,y,0)) {
						WhiteIsCheck = 0;
						checkDisplay.setVisible(false);
						checkDisplay.remove(Bcheck);
						checkDisplay.revalidate();
					}
				}
				*/
				//check if move puts other player IN check
				if(moves % 2 == 0) {
					if(testCheck(i,j,0)) {
						System.out.print("CHECK");
						Bcheck = new JLabel("BLACK IN CHECK");
						checkDisplay.add(Bcheck);
						checkDisplay.setVisible(true);
						BlackIsCheck = 1;
						checkPos[0][0] = i;
						checkPos[1][1] = j;
					}
				}
				/*
				else {
					if(testCheck(i,j,1)) {
						System.out.print("CHECK");
						Wcheck = new JLabel("WHITE IN CHECK");
						checkDisplay.add(Wcheck);
						checkDisplay.setVisible(true);
						WhiteIsCheck = 1;
						checkPos[0][0] = i;
						checkPos[1][1] = j;
					}
				}
				*/
				
				moving = false;
				//moves++;
				updatePlayer();
				units[i][j].isFirstTurn = false;
				
				client.ClientWrite(move);
				
	
				String ok = "";
				int data = 0;
				try {
					
					//wait for comfirmation from server
					//data = client.dataAvailable();
				
					//while(data == 0) {
						//data = client.dataAvailable();
					//}
				
					//wait for move from other player to be sent back
					//data = client.dataAvailable();
					/*
					if(color == 0) {
						WmoveSent = true;
					}
					else {
						BmoveSent = true;
					}
					if (color == 0) {
						WrequestMove = true;
					}
					else {
						BrequestMove = true;
					}
					
					*/
					//while (data == 0) {
						//data = client.dataAvailable();
					//}
					//waiting = true;
					while(true){ //wait for ok from sever. This comfirms the other player received the move, and updated their board
						data = client.dataAvailable();
						if(data != 0){
							String c = client.ClientRead();
							if(c.compareTo("ok")== 0) {
								break;
							}
						}
					}
					moves++;
					//waiting = false;
						
					/*
					moveBack = client.ClientRead();
					String[] values = moveBack.split(",");
					oldX = values[0];
					oldY = values[1];
					newX = values[2];
					newY = values[3];
					moveUnit(Integer.parseInt(oldX),Integer.parseInt(oldY));
					mover.setUnits(units);
					placeUnit(Integer.parseInt(newX), Integer.parseInt(newY));
						*/
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
				AI = 1;
			}
			else {
				placeUnit(prevI, prevJ);
				moving = false;
			}

		}
		if (AI == 1) {
			//generateMove();
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
	
	public boolean testCheck(int i, int j, int c) {
		units[i][j].setAvailableMoves(j,i, moves);
		for (int k = 0; k < units[i][j].possibleMoves.size(); k++) {
			int y = units[i][j].possibleMoves.get(k)[0];
			int x = units[i][j].possibleMoves.get(k)[1];
			if(units[x][y] != null) {
				if(units[x][y].unitType == "K" && units[x][y].color != c) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean testCheckMate(int c) {
		
		int i;
		int j;
		for(i=0; i < 8; i++) {
			for (j=0; j < 8; j++) {
				if (units[i][j].color == c) {
					
				}
			}
		}
		
		return true;
	}
	
	public boolean testOutofCheck(int i, int j, int c) {
		
		
		return true;
	}
	
	public void generateMove() {
		//AI will choose a random move from a list of valid moves
		/*
		AIarray.clear();
		//int moved = 0;
		int i,j,i1,j1 = 0;
		List<int[]> Moves;
		//get all current piece positions for AI
		//int index = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (units[x][y] != null && units[x][y].color == 1) {
					PositionPairs p = new PositionPairs(x,y);
					AIarray.add(p);
					//AIarray.get(index).i = x;
					//AIarray.get(index).j = y;
					//index++;
				}
			}
		}
	
		Random rand = new Random();
		
		// loops through all current AI positions and find a piece that has
		// at least one move available 
		for (int k = 0; k < AIarray.size(); k++) {
			i = AIarray.get(k).i;
			j = AIarray.get(k).j;
			moveUnit(i,j);
			mover.setUnits(units);
			mover.setAvailableMoves(j,i, moves);
			
			Moves = mover.possibleMoves;
		
			System.out.printf("%s\n", mover.unitType);
			System.out.printf("from (%d,%d) to ", i,j);
			
			if (Moves.size() > 0) {
				
				//Select random move
				
				//if 1 item we choose that item
				if (Moves.size() - 1 == 0) {
					j1 = Moves.get(0)[0];
					i1 = Moves.get(0)[1];
				}
				//random chorice
				else {
					int randMove = rand.nextInt(Moves.size() - 1);
					j1 = Moves.get(randMove)[0];
					i1 = Moves.get(randMove)[1];
				}
				System.out.printf("(%d, %d)\n", i1, j1);
			*/
				bruce.setBoardState(units);
				bruce.MiniMax(2, true);
				
				mover = units[bruce.initPos[0]][bruce.initPos[1]];
				int i1 = bruce.megaMove[0];
				int j1 = bruce.megaMove[1];
				
					//mover.checkMove(0, 0);
					//mover.clearMoves();
					placeUnit(i1,j1);
					units[bruce.initPos[0]][bruce.initPos[1]] = null;
					squares[bruce.initPos[0]][bruce.initPos[1]].setIcon(new ImageIcon("blank.png"));
					
					
					//check if move gets player OUT OF check
					if(moves % 2 == 1 && BlackIsCheck == 1) {
						int x = checkPos[0][0];
						int y = checkPos[1][1];
						if(!testCheck(x,y,0)) {
							WhiteIsCheck = 0;
							checkDisplay.setVisible(false);
							checkDisplay.remove(Bcheck);
							checkDisplay.revalidate();
						}
					}
					/*
					if(testCheck(i1,j1,1)) {
						System.out.print("CHECK");
						Wcheck = new JLabel("WHITE IN CHECK");
						checkDisplay.add(Wcheck);
						checkDisplay.setVisible(true);
						WhiteIsCheck = 1;
						checkPos[0][0] = i;
						checkPos[1][1] = j;
					}
					*/
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
}
