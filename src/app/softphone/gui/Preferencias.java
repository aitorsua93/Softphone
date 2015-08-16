package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
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
	JCheckBox desvioSiempre, desvioTimeout, activarCaptura;;
	JTextField uriDesvio, timeDesvio, secCaptura;
	OperacionesPreferencias opPre = new OperacionesPreferencias();
	

	public Preferencias(OperacionesSip opSip, Cuenta c, JMenuItem in) {
		Configuracion conf = new Configuracion();
		conf = opPre.obtenerPreferencias();
		crearPestanas(opSip,c,in,conf);
		crearPanelBotones();
		crearVentana();
	}

	public void crearPestanas(OperacionesSip opSip, Cuenta c, JMenuItem in, Configuracion conf) {
		pestanas = new JTabbedPane();
		cuenta = new Cuentas(opSip,c,in);
		
		//Obtener configuracion y pasarla a los componentes
		desvioSiempre = new JCheckBox("Siempre desviar las llamadas al equipo indicado");
		if (conf.getDesvioSiempre().equals("Si")) {
			 desvioSiempre.setSelected(true);
		 } else if (conf.getDesvioSiempre().equals("No")) {
			 desvioSiempre.setSelected(false);
		 }
		desvioTimeout = new JCheckBox("Desviar las llamadas despues del timeout indicado");
		if (conf.getDesvioTimeout().equals("Si")) {
			 desvioTimeout.setSelected(true);
		 } else if (conf.getDesvioTimeout().equals("No")) {
			 desvioTimeout.setSelected(false);
		 }
		uriDesvio = new JTextField(15);
		uriDesvio.setText(conf.getURIdesvio());
		timeDesvio = new JTextField(3);
		timeDesvio.setText(conf.getTimeoutDesvio());
		desvio = new Desvio(desvioSiempre,desvioTimeout,uriDesvio,timeDesvio);
		
		activarCaptura = new JCheckBox("Activar captura de llamadas");
		if (conf.getCapturaOpcion().equals("Si")) {
			activarCaptura.setSelected(true);
		} else if (conf.getCapturaOpcion().equals("No")) {
			activarCaptura.setSelected(false);
		}
		secCaptura = new JTextField(5);
		secCaptura.setText(conf.getSecCaptura());
		captura = new Captura(activarCaptura,secCaptura);
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
				if (desvioSiempre.isSelected()) {
					config.setDesvioSiempre("Si");
				} else {
					config.setDesvioSiempre("No");
				}
				if (desvioTimeout.isSelected()) {
					config.setDesvioTimeout("Si");
				} else {
					config.setDesvioTimeout("No");
				}
				config.setURIdesvio(uriDesvio.getText());
				config.setTimeoutDesvio(timeDesvio.getText());
				if (activarCaptura.isSelected()) {
					config.setCapturaOpcion("Si");
				} else {
					config.setCapturaOpcion("No");
				}
				config.setSecCaptura(secCaptura.getText());
				opPre.actualizar(config);
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
