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
import javax.swing.border.EmptyBorder;

public class menu {

	JPanel menuscreen;
	JLabel menuTitle;
	JButton b1,b2,b3;
	public menu() {
		
	        menuscreen = new JPanel();
	        menuscreen.setBackground(Color.lightGray);
	        menuscreen.setBorder(new EmptyBorder(10, 10, 10, 10));
            menuscreen.setLayout(new GridBagLayout());
	        
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTH;

           
            menuscreen.add(new JLabel("<html><h1><strong><i>Chess Game</i></strong></h1><hr></html>"), gbc);
            
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JPanel buttons = new JPanel(new GridBagLayout());
	        b1 = new JButton("Single Player");
	        b2 = new JButton("Two Player");
	        b3 = new JButton("Quit");
	        
	        b1.setActionCommand("1");
	        b2.setActionCommand("2");
	        b3.setActionCommand("3");
	        
	        buttons.add(b1,gbc);
	        buttons.add(b2,gbc);
	        buttons.add(b3,gbc);
	        
	        //gbc.weighty = 1;
	        menuscreen.add(buttons,gbc);
	        
	        //menuscreen.add(b1);
	        //menuscreen.add(b2);
	        //menuscreen.add(b3);
	        
	        
	}
	
}
