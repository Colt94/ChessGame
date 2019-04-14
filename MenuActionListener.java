package chessGame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

class MenuActionListener implements ActionListener {

	JFrame frame;
	menu Menu;
	DisplayGraphics graphics;
	
	MenuActionListener(JFrame f, menu m, DisplayGraphics g) {
		this.frame = f;
		this.Menu = m;
		this.graphics = g;
		
		Menu.b1.addActionListener(this);
		Menu.b2.addActionListener(this);
		Menu.b3.addActionListener(this);
	}
	
	//***This is where the chess game is executed****
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand() == "1") {
			//take menu off of frame
			frame.remove(Menu.menuscreen);
			
			Board brd = new Board();
			
			 //JPanel must be added before DisplayGraphics
			frame.add(brd.checkDisplay);
	        frame.add(brd.pane);
	        frame.add(brd.activePlayer);
	        frame.add(brd.capPane);
	        frame.add(graphics);
	        
	        
	        //Only setVisible after everything has been added
	        frame.setVisible(true);
	        
	        while(true){
	        	
	        }
			
		}
		else if (e.getActionCommand() == "2") {
			frame.remove(Menu.menuscreen);
			Scanner myObj = new Scanner(System.in);  // Create a Scanner object
			System.out.println("Server(s) or Client(c)?");
			Server srv = null;
			
			String play = myObj.nextLine();  // Read user input

			System.out.print(play);
			if (play.compareTo("s") == 0) {
				srv = new Server(111, 2);
				try {
					srv.run();
					srv.ServerHandler();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else {
				Client c = new Client("Client1", "127.0.0.1", 111);
				try {
					c.run();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//String color = c.ClientRead();
				
				int data = 0;
				try {
					data = c.dataAvailable();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				while(data == 0) {
					try {
						data = c.dataAvailable();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				String color = c.ClientRead();
				int colorInt = Integer.parseInt(color);
				
				RemoteBoard brd = new RemoteBoard(colorInt,c);
				frame.add(brd.colordisplay);
				frame.add(brd.checkDisplay);
		        frame.add(brd.pane);
		        frame.add(brd.activePlayer);
		        frame.add(brd.capPane);
		        frame.add(graphics);
		        //frame.add(brd.colordisplay);
		        frame.setVisible(true);
		        
		        (new Thread(new player(brd,c))).start();
			}
			
			
			
			
			
			
			
			
			
			
			/*
			ClientServerMenu scMenu = new ClientServerMenu();
			frame.add(scMenu.menuscreen);
			frame.setVisible(true);
			
			scMenu.b1.setAction(null ); {
				
			
			};
			//frame.add(Menu.menuscreen);
			*/
		}
		else if (e.getActionCommand() == "3") {
			System.out.print("Closing..");
			System.exit(0);
		}
	}

}

class player implements Runnable {

	RemoteBoard brd;
	Client client;
	
	public player(RemoteBoard brd, Client client) {
		this.brd = brd;
		this.client = client;
	}
	public void run() {
		while(true) {
			
			if(brd.moves % 2 != brd.color) {
				int data = 0;
				try {
					data = client.dataAvailable();
				
				while(data == 0) {
					data = client.dataAvailable();
					
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				String move = client.ClientRead();
				if(move.length() > 2){
					String[] values = move.split(",");
					String oldX = values[0];
					String oldY = values[1];
					String newX = values[2];
					String newY = values[3];
					brd.moveUnit(Integer.parseInt(oldX),Integer.parseInt(oldY));
					brd.mover.setUnits(brd.units);
					brd.placeUnit(Integer.parseInt(newX), Integer.parseInt(newY));
				
					brd.client.ClientWrite("confirm");
					brd.moves++;
					brd.updatePlayer();
				}
			}
			
			
		}
		
	}
	
}

