package chessGame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ClientServerMenu {

	JPanel menuscreen;
	JLabel menuTitle;
	JButton b1,b2;
	
	public ClientServerMenu() {
		
        menuscreen = new JPanel();
        menuscreen.setBackground(Color.lightGray);
        menuscreen.setBorder(new EmptyBorder(10, 10, 10, 10));
        menuscreen.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;

       
        //menuscreen.add(new JLabel("<html><h1><strong><i>Chess Game</i></strong></h1><hr></html>"), gbc);
        
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel buttons = new JPanel(new GridBagLayout());
        b1 = new JButton("Play as Server");
        b2 = new JButton("Play as Client");
        
        b1.setActionCommand("1");
        b2.setActionCommand("2");
        
        buttons.add(b1,gbc);
        buttons.add(b2,gbc);
        
        //gbc.weighty = 1;
        menuscreen.add(buttons,gbc);
        
        //menuscreen.add(b1);
        //menuscreen.add(b2);
        //menuscreen.add(b3);
        
        
}
}
