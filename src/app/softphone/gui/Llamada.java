package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.sip.OperacionesSip;

@SuppressWarnings("serial")
public class Llamada extends JPanel {
	
	JLabel destLabel, destCompLabel, statusLabel, cronometro;
	JPanel panelLabels, panelBotLlam;
	JButton colgarLlam, ponEsper, Linea1, Linea2;
	Thread crono;
	
	public Llamada(OperacionesSip opSip, Cuenta cuenta, String destinatario, JTabbedPane pestanas) {
		cronometro = new JLabel("00:00:00");
		cronometro.setFont(new java.awt.Font("Calibri", 1, 15));
		Cronometro cron = new Cronometro(cronometro);
		crearLabels(destinatario,cuenta,cronometro);
		crearBotonesLlam(pestanas);
		add(panelLabels, BorderLayout.WEST);
		add(panelBotLlam, BorderLayout.SOUTH);
		crono = new Thread(cron);
		crono.start();
	}
	
	public void crearLabels(String destinatario, Cuenta c, JLabel cronometro) {
		panelLabels = new JPanel();
		panelLabels.setLayout(new BoxLayout(panelLabels, BoxLayout.Y_AXIS));
		destLabel = new JLabel(destinatario);
		destLabel.setFont(new java.awt.Font("Calibri", 1, 20));
		destCompLabel = new JLabel(c.getUsuario() + "@" + c.getServidor());
		destCompLabel.setFont(new java.awt.Font("Calibri", 1, 15));
		statusLabel = new JLabel("Llamando...");
		statusLabel.setFont(new java.awt.Font("Calibri", 1, 15));
		
		panelLabels.add(Box.createRigidArea(new Dimension (10,10)));
		panelLabels.add(destLabel);
		panelLabels.add(Box.createRigidArea(new Dimension (10,10)));
		panelLabels.add(destCompLabel);
		panelLabels.add(Box.createRigidArea(new Dimension (10,10)));
		panelLabels.add(statusLabel);
		panelLabels.add(Box.createRigidArea(new Dimension (10,10)));
		panelLabels.add(cronometro);
		panelLabels.add(Box.createRigidArea(new Dimension (10,40)));
	}
	
	public void crearBotonesLlam(JTabbedPane pestanas) {
		panelBotLlam = new JPanel(new GridLayout(3,3,10,10));
		
		colgarLlam = new JButton();
		colgarLlam.setText("Colgar");
		panelBotLlam.add(colgarLlam);
		ActionListener colgarListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Mandar peticion
				 pestanas.remove(3);
			}
		};
		colgarLlam.addActionListener(colgarListener);
		
		ponEsper = new JButton();
		ponEsper.setText("Poner Espera");
		panelBotLlam.add(ponEsper);
		
		Linea1 = new JButton();
		Linea1.setText("Linea 1");
		panelBotLlam.add(Linea1);
		
		Linea2 = new JButton();
		Linea2.setText("Linea2");
		panelBotLlam.add(Linea2);
		
		panelBotLlam.add(Box.createRigidArea(new Dimension (10,20)));
		panelBotLlam.add(Box.createRigidArea(new Dimension (10,20)));
	}
	


	
}
