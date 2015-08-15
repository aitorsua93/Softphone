package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.preferencias.Configuracion;
import app.softphone.core.preferencias.OperacionesPreferencias;
import app.softphone.core.sip.OperacionesSip;

@SuppressWarnings("serial")
public class Preferencias extends JDialog {
	
	JTabbedPane pestanas;
	JPanel cuenta, desvio, captura, panelBotones;
	JButton aceptarPre, cancelarPre;
	

	public Preferencias(OperacionesSip opSip, Cuenta c, JMenuItem in) {
		OperacionesPreferencias opPre = new OperacionesPreferencias();
		Configuracion conf = new Configuracion();
		conf = opPre.obtenerPreferencias();
		crearPestanas(opSip,c,in,conf);
		crearPanelBotones();
		crearVentana();
	}

	public void crearPestanas(OperacionesSip opSip, Cuenta c, JMenuItem in, Configuracion conf) {
		pestanas = new JTabbedPane();
		cuenta = new Cuentas(opSip,c,in);
		desvio = new Desvio(conf);
		captura = new Captura(conf);
		pestanas.addTab("Cuentas", cuenta);
		pestanas.addTab("Desvio de Llamada", desvio);
		pestanas.addTab("Captura de Llamada", captura);
	}
	
	public void crearPanelBotones() {
		panelBotones = new JPanel(new FlowLayout());
		
		aceptarPre = new JButton();
		aceptarPre.setText("Aceptar");
		panelBotones.add(aceptarPre);
		ActionListener aceptarPreListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Configuracion config = new Configuracion();
				/*if () {
					
				}*/
				dispose();
			}
		};
		aceptarPre.addActionListener(aceptarPreListener);
		
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
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500,500);
		setModal(true);
		add(pestanas, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
	}
	
}
