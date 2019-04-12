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
	@Override
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
			
		}
		else if (e.getActionCommand() == "2") {
			frame.remove(Menu.menuscreen);
			
			
			frame.add(Menu.menuscreen);
			
		}
		else if (e.getActionCommand() == "3") {
			System.out.print("Closing..");
			System.exit(0);
		}
	}

}
