package chessGame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DifficultyMenu {
	
	JPanel menuscreen;
	JLabel menuTitle;
	JButton b1,b2,b3;
	public DifficultyMenu() {
		
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
	        b1 = new JButton("Difficulty Level 1");
	        b2 = new JButton("Difficulty Level 2");
	        b3 = new JButton("Difficulty Level 3");
	        
	        
	        buttons.add(b1,gbc);
	        buttons.add(b2,gbc);
	        buttons.add(b3,gbc);
	        
	        //gbc.weighty = 1;
	        menuscreen.add(buttons,gbc);
	        
	        
	        
	}
}
