package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class Preferencias extends JDialog {
	
	JTabbedPane pestanas;
	JPanel cuenta, desvio, captura, panelBotones;
	JButton aceptarPre, cancelarPre;
	
	public static void main(String[] args) {
		Preferencias preferencias = new Preferencias();
		preferencias.setLocationRelativeTo(null);
		preferencias.setVisible(true);
		
	}
	
	public Preferencias() {
		crearPestanas();
		crearPanelBotones();
		crearVentana();
	}

	public void crearPestanas() {
		pestanas = new JTabbedPane();
		cuenta = new Cuentas();
		desvio = new Desvio();
		captura = new JPanel();
		pestanas.addTab("Cuentas", cuenta);
		pestanas.addTab("Desvio de Llamada", desvio);
		pestanas.addTab("Captura de Llamada", captura);
	}
	
	public void crearPanelBotones() {
		panelBotones = new JPanel(new FlowLayout());
		
		aceptarPre = new JButton();
		aceptarPre.setText("Aceptar");
		panelBotones.add(aceptarPre);
		
		cancelarPre = new JButton();
		cancelarPre.setText("Cancelar");
		panelBotones.add(cancelarPre);
		ActionListener cancelarPreListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		cancelarPre.addActionListener(cancelarPreListener);
		
	}
	
	public void crearVentana() {
		setTitle("Preferencias");
		setSize(500,500);
		setModal(true);
		add(pestanas, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
	}
	
}
