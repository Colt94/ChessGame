package chessGame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.*;
import java.lang.Thread.*;
import java.net.ServerSocket;

import javax.imageio.*;
import javax.swing.*;

class MenuActionListener implements ActionListener {

	JFrame frame;
	menu Menu;
	DisplayGraphics graphics;
	ClientServerMenu csmenu;
	DifficultyMenu Dmenu;
	Server srv = null;
	Client c;
	JTextField jf;
	JPanel textfield;
	
	MenuActionListener(JFrame f, menu m, DisplayGraphics g) {
		this.frame = f;
		this.Menu = m;
		this.graphics = g;
		
		Menu.b1.addActionListener(this);
		Menu.b2.addActionListener(this);
		Menu.b3.addActionListener(this);
		
		csmenu = new ClientServerMenu();
		Dmenu = new DifficultyMenu();
	}
	
	//***This is where the chess game is executed****
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand() == "1") {
			//take menu off of frame
			frame.remove(Menu.menuscreen);
			
			frame.add(Dmenu.menuscreen);
			frame.revalidate();
			frame.setVisible(true);
			
			Dmenu.b1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
					frame.remove(Dmenu.menuscreen);
					Board brd = new Board(2);
					
					frame.add(brd.checkDisplay);
			        frame.add(brd.pane);
			        frame.add(brd.activePlayer);
			        frame.add(brd.capPane);
			        frame.add(graphics);
					
			        frame.setVisible(true);
				}
				
				
			});
			
			Dmenu.b2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
					frame.remove(Dmenu.menuscreen);
					Board brd = new Board(3);
					
					frame.add(brd.checkDisplay);
			        frame.add(brd.pane);
			        frame.add(brd.activePlayer);
			        frame.add(brd.capPane);
			        frame.add(graphics);
					
			        frame.setVisible(true);
				}
				
				
			});
			
			Dmenu.b3.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
					frame.remove(Dmenu.menuscreen);
					Board brd = new Board(4);
					
					frame.add(brd.checkDisplay);
			        frame.add(brd.pane);
			        frame.add(brd.activePlayer);
			        frame.add(brd.capPane);
			        frame.add(graphics);
					
			        frame.setVisible(true);
				}
				
				
			});
			
		}
		else if (e.getActionCommand() == "2") {
			
			frame.getContentPane();
			frame.remove(Menu.menuscreen); 
			frame.add(csmenu.menuscreen);
			frame.revalidate();
			frame.setVisible(true);
			
			csmenu.b1.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					srv = new Server(111, 2);
					try {
						srv.run();
						//srv.ServerHandler();
						(new Thread(new ServerHandler(srv))).start();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				
			});
			csmenu.b2.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					jf = new JTextField(30);
					jf.setPreferredSize( new Dimension( 100, 50 ) );
					frame.remove(csmenu.menuscreen);
					textfield = new JPanel();
					JLabel add = new JLabel("Enter IP address");
					textfield.add(add);
					textfield.add(jf);
					
					frame.add(textfield);
					frame.revalidate();
					frame.setVisible(true);
					
					jf.addActionListener(new ActionListener(){

						public void actionPerformed(ActionEvent arg0) {
							String address = jf.getText();
							c = new Client("",address,111);
							try {
								c.run();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
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
							c.setColor(colorInt);
							RemoteBoard brd = new RemoteBoard(colorInt,c);
							
							frame.remove(textfield);
							frame.revalidate();
							frame.add(brd.colordisplay);
							frame.add(brd.checkDisplay);
					        frame.add(brd.pane);
					        frame.add(brd.activePlayer);
					        frame.add(brd.capPane);
					        frame.add(graphics);
					        //frame.add(brd.colordisplay);
					        frame.setVisible(true);
					        
					        System.out.print("Waiting on other player..\n");
					        c.ClientRead(); // block untill both players are connected
					        		
					        (new Thread(new HandleBoardState(brd,c))).start();
						}		
				});
						
				}
			});
			
		}
		else if (e.getActionCommand() == "3") {
			System.out.print("Closing..");
			System.exit(0);
		}
	}

}


class ServerHandler implements Runnable {
	
	Server srv;
	
	public ServerHandler(Server srv) {
		this.srv = srv;
	}
	
	public void run() {
		while(true) {
		
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
			int data1 = srv.in1.available();
			int data2 = srv.in2.available();
			if(data1 != 0) {
				//String[] values = CSV.split(",");
				System.out.print("Data read from client1\n");
				String move1 = srv.in1.readUTF();
				srv.out2.writeUTF(move1); //send move to other client
				srv.out2.flush();
				System.out.print("Client1 move sent to client2\n");
				
				String c = srv.in2.readUTF();
			
				System.out.print("Client2 comfirmed\n");
				
				srv.out1.writeUTF("ok"); //send ok back to calling client
				srv.out1.flush();
				System.out.print("Sent ok to client1\n");
			}
			
			else if (data2 != 0) {
				//String[] values = CSV.split(",");
				String move2 = srv.in2.readUTF();
				System.out.printf("Data read %s from client2\n", move2);
				srv.out1.writeUTF(move2);
				srv.out1.flush();
				System.out.printf("%s sent to client1\n", move2);
				
				
				String c = srv.in1.readUTF();
				
				System.out.print("Client1 comfirmed\n");
				
				srv.out2.writeUTF("ok");
				srv.out2.flush();
				System.out.print("Sent ok to Client2");
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
			
}


class HandleBoardState implements Runnable {

	RemoteBoard brd;
	Client client;

	public HandleBoardState(RemoteBoard brd, Client client) {
		this.brd = brd;
		this.client = client;
	}
	public void run() {
		while(true) {
			//System.out.print("f");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(brd.moves % 2 != client.color) {
				
				System.out.print("About to read move\n");
				String move = client.ClientRead();
				System.out.printf("%s Recieved\n", move);
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
					System.out.print("Confirm sent\n");
					brd.moves++;
					brd.updatePlayer();
				}
			}
			
			
		}
		
	}
	
}

