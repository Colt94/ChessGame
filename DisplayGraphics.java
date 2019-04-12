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


public class DisplayGraphics extends Canvas{
	
	//All paint/draw/image stuff goes here
	public void paint(Graphics g) {  
		
		//IMAGE EXAMPLE
		File path = new File("board.jpg");
		Image img = null;
		try {
			img = ImageIO.read(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageObserver imgob = null;
		g.drawImage(img,  0,  0,  imgob);
		
		/*
		//VARIOUS EXAMPLES
        g.drawString("Hello",40,40);  
        setBackground(Color.WHITE);  
        g.fillRect(130, 30,100, 80);  
        g.drawOval(30,130,50, 60);  
        setForeground(Color.RED);  
        g.fillOval(130,130,50, 60);  
        g.drawArc(30, 200, 40,50,90,60);  
        g.fillArc(30, 130, 40,50,180,40);
        */
    }  
	
	
       	public static void main(String[] args) { 
       	DisplayGraphics m=new DisplayGraphics();
        JFrame f=new JFrame("Chess Game");
        f.setSize(1000, 1000); 
        
        
        //create menu object
        menu mnu = new menu();
        
        //add menu screen to frame
        f.add(mnu.menuscreen);
        f.setVisible(true);
        
        //creates an action listener class for menu buttons
        //Chess game currently is executed inside this class
        MenuActionListener menufunction = new MenuActionListener(f,mnu,m);
        
        //Board brd = new Board();
                
        
        //JPanel must be added before DisplayGraphics
        //f.add(brd.pane);
        //f.add(brd.activePlayer);
        //f.add(brd.capPane);
        //f.add(m);
        
        
        //Only setVisible after everything has been added
        //f.setVisible(true);  
        
    }

       	
}
