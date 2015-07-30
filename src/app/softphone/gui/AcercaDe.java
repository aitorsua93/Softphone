package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class AcercaDe extends JDialog {

	JPanel panelText, panelBut;
	JLabel titulo, descr, copy;
	JButton okBut;
	
	public static void main (String[] args) {
		AcercaDe acercade = new AcercaDe();
		acercade.setLocationRelativeTo(null);
		acercade.setVisible(true);
	}
	
	public AcercaDe() {
		crearPanelTexto();
		crearPanelBoton();
		crearDialogo();
		
	}

	public void crearPanelTexto() {
		panelText = new JPanel();
		titulo = new JLabel("Softphone", JLabel.CENTER);
		titulo.setFont(new Font("Calibri",1,30));
		descr = new JLabel("Teléfono a través de de la red  que usa el protocolo estándar SIP (rfc 3261)", JLabel.CENTER);
		descr.setFont(new Font("Calibri",1,11));
		copy = new JLabel("(C) Aitor Communications, 2015", JLabel.CENTER);
		copy.setFont(new Font("Calibri",1,9));
		
		//panelText.setLayout(new BoxLayout(panelText,BoxLayout.Y_AXIS));
		panelText.add(titulo, BorderLayout.NORTH);
		panelText.add(descr, BorderLayout.CENTER);
		panelText.add(copy, BorderLayout.SOUTH);
		
	}
	
	public void crearPanelBoton() {
		panelBut = new JPanel();
		okBut = new JButton();
		okBut.setText("OK");
		//panelBut.setLayout(new BoxLayout(panelBut,BoxLayout.Y_AXIS));
		panelBut.add(okBut, BorderLayout.CENTER);
		ActionListener okListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		okBut.addActionListener(okListener);
	}
	
	public void crearDialogo() {
		setTitle("Acerca De...");
		setSize(400,220);
		add(panelText,BorderLayout.CENTER);
		add(panelBut, BorderLayout.SOUTH);
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}
