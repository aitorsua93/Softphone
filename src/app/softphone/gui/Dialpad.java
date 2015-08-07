package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class Dialpad extends JPanel implements ActionListener {
	
	String [] butName = {"1","2","3","4","5","6","7","8","9","*","0","#"};
	JButton[] buttons  = new JButton[12];
	JTextField llamarField;
	JPanel botones;
	
	public Dialpad(JTextField llamarText) {
		llamarField = llamarText;
		crearBotones();
		add(botones, BorderLayout.CENTER);
		//add(Box.createRigidArea(new Dimension (0,20)), BorderLayout.NORTH);
		//add(Box.createRigidArea(new Dimension (20,20)), BorderLayout.SOUTH);
		//add(Box.createRigidArea(new Dimension (10,10)), BorderLayout.WEST);
		//add(Box.createRigidArea(new Dimension (10,10)), BorderLayout.EAST);
	}
	
	public void crearBotones() {
		botones = new JPanel();
		botones.setPreferredSize(new Dimension(200,200));
		botones.setLayout(new GridLayout(4,3,10,10));
		for (int i=0;i<butName.length;i++) {
			buttons[i] = new JButton(butName[i]);
			buttons[i].addActionListener(this);
			botones.add(buttons[i]);
		}
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		String textoField = llamarField.getText();
		switch (e.getActionCommand()){
		case "1":
			llamarField.setText(textoField + "1");
			break;
		case "2":
			llamarField.setText(textoField + "2");
			break;
		case "3":
			llamarField.setText(textoField + "3");
			break;
		case "4":
			llamarField.setText(textoField + "4");
			break;
		case "5":
			llamarField.setText(textoField + "5");
			break;
		case "6":
			llamarField.setText(textoField + "6");
			break;
		case "7":
			llamarField.setText(textoField + "7");
			break;
		case "8":
			llamarField.setText(textoField + "8");
			break;
		case "9":
			llamarField.setText(textoField + "9");
			break;
		case "*":
			llamarField.setText(textoField + "*");
			break;
		case "0":
			llamarField.setText(textoField + "0");
			break;
		case "#":
			llamarField.setText(textoField + "#");
			break;
		}
		
	}

}
