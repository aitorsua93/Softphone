package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.softphone.core.sip.OperacionesSip;

@SuppressWarnings("serial")
public class RecibirLlamada extends JDialog {
	
	JPanel panelBotones, panelLabels;
	JButton respBut, rechBut;
	JLabel tituloLabel, usuarioLabel, sipLabel;
	
	public static void main(String[] args) {
		//RecibirLlamada rl = new RecibirLlamada();
		//rl.setVisible(true);
	}
	
	public RecibirLlamada(String usuario ,String sip, OperacionesSip opSip) {
		crearLabels(usuario,sip);
		crearBotones(opSip);
		add(panelLabels, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
		setTitle("Recibiendo llamada...");
		setSize(400,200);
		setModal(true);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	public void crearLabels(String usuario, String sip) {
		panelLabels = new JPanel();
		panelLabels.setLayout(new BoxLayout(panelLabels, BoxLayout.Y_AXIS));
		tituloLabel = new JLabel("Recibiendo llamada de:");
		tituloLabel.setFont(new java.awt.Font("Calibri", 1, 20));
		usuarioLabel = new JLabel(usuario);
		usuarioLabel.setFont(new java.awt.Font("Calibri", 1, 18));
		sipLabel = new JLabel(sip);
		sipLabel.setFont(new java.awt.Font("Calibri", 1, 18));
		
		panelLabels.add(Box.createRigidArea(new Dimension (10,10)));
		panelLabels.add(tituloLabel);
		panelLabels.add(Box.createRigidArea(new Dimension (10,10)));
		panelLabels.add(usuarioLabel);
		panelLabels.add(Box.createRigidArea(new Dimension (10,10)));
		panelLabels.add(sipLabel);
		panelLabels.add(Box.createRigidArea(new Dimension (10,10)));
	}
	
	public void crearBotones(OperacionesSip opSip) {
		panelBotones = new JPanel(new FlowLayout());
		
		respBut = new JButton();
		respBut.setText("Responder");
		panelBotones.add(respBut);
		ActionListener respListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		};
		respBut.addActionListener(respListener);
		
		rechBut = new JButton();
		rechBut.setText("Rechazar");
		panelBotones.add(rechBut);
		ActionListener rechListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				opSip.call(1, null);
				dispose();
			}
		};
		rechBut.addActionListener(rechListener);
		
		//panelBotones.add(Box.createRigidArea(new Dimension (10,20)));
		//panelBotones.add(Box.createRigidArea(new Dimension (10,20)));
	}
}

