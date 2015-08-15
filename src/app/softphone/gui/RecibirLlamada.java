package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.sip.OperacionesSip;

@SuppressWarnings("serial")
public class RecibirLlamada extends JDialog implements ActionListener{
	
	JPanel panelBotones, panelLabels;
	JButton respBut, rechBut;
	JLabel tituloLabel, usuarioLabel, sipLabel;
	OperacionesSip opSip;
	int status;
	static final int IDLE=0;
	
	
	public RecibirLlamada(String usuario ,String sip, OperacionesSip opSip, Cuenta cuenta, JTabbedPane pestanas) {
		this.opSip = opSip;
		crearLabels(usuario,sip);
		crearBotones(opSip,sip,cuenta,pestanas);
		add(panelLabels, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
		setTitle("Recibiendo llamada...");
		setSize(400,200);
		setModal(true);
		setLocationRelativeTo(null);
		setResizable(false);
		Timer timer = new Timer(100,this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		status = opSip.getStatus();
		if (status == IDLE) {
			dispose();
		}
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
	
	public void crearBotones(OperacionesSip opSip, String sip, Cuenta cuenta, JTabbedPane pestanas) {
		panelBotones = new JPanel(new FlowLayout());
		
		respBut = new JButton();
		respBut.setText("Responder");
		panelBotones.add(respBut);
		ActionListener respListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel llamada = new Llamada(opSip,cuenta,sip,pestanas);
				pestanas.addTab("Llamada",llamada);
				pestanas.setSelectedIndex(3);
				opSip.call(0, sip);
				dispose();
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

