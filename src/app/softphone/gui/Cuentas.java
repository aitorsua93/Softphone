package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;




import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.sip.OperacionesSip;

@SuppressWarnings("serial")
public class Cuentas extends JPanel {

	JPanel panelDetalles, panelBotones;
	JLabel nombreCuenta, usuarioCuenta, servidorCuenta;
	JButton edCuenta, elCuenta;
	
	public Cuentas(OperacionesSip opSip, Cuenta c, JMenuItem in) {
		crearPanelDetalles(opSip,c);
		crearPanelBotones(opSip,c,in);
		add(panelDetalles, BorderLayout.NORTH);
		add(panelBotones, BorderLayout.CENTER);
	}
	
	public void crearPanelDetalles(OperacionesSip opSip, Cuenta c) {
 		panelDetalles = new JPanel();
 		if (c != null) {
	 		nombreCuenta = new JLabel("Nombre de la cuenta: " + c.getNombre());
	 		nombreCuenta.setFont(new java.awt.Font("Bold", 1, 16));
	 		usuarioCuenta = new JLabel("Usuario de la cuenta: " + c.getUsuario());
	 		usuarioCuenta.setFont(new java.awt.Font("Bold", 1, 16));
	 		servidorCuenta = new JLabel("Servidor de la cuenta: " + c.getServidor());
	 		servidorCuenta.setFont(new java.awt.Font("Bold", 1, 16));
 		} else {
	 		nombreCuenta = new JLabel("No se ha iniciado sesion");
	 		nombreCuenta.setFont(new java.awt.Font("Bold", 1, 16));
	 		usuarioCuenta = new JLabel("");
	 		usuarioCuenta.setFont(new java.awt.Font("Bold", 1, 16));
	 		servidorCuenta = new JLabel("");
	 		servidorCuenta.setFont(new java.awt.Font("Bold", 1, 16));
 		}
		panelDetalles.setLayout(new BoxLayout(panelDetalles,BoxLayout.Y_AXIS));
		panelDetalles.add(Box.createRigidArea(new Dimension (10,20)));
		panelDetalles.add(nombreCuenta);
		panelDetalles.add(Box.createRigidArea(new Dimension (10,20)));
		panelDetalles.add(usuarioCuenta);
		panelDetalles.add(Box.createRigidArea(new Dimension (10,20)));
		panelDetalles.add(servidorCuenta);
		panelDetalles.add(Box.createRigidArea(new Dimension (10,20)));
	}

	
	public void crearPanelBotones(OperacionesSip opSip, Cuenta c, JMenuItem in) {
		panelBotones = new JPanel();
		elCuenta = new JButton();
		elCuenta.setText("Cerrar Sesion");
		panelBotones.add(elCuenta, BorderLayout.SOUTH);
		
		if (usuarioCuenta.getText().equals("")) {
			elCuenta.setVisible(false);
		} else {
			elCuenta.setVisible(true);
		}
		
		ActionListener elCuentaListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				opSip.register(0);
				in.setEnabled(true);
				nombreCuenta.setText("No se ha iniciado sesion");
				usuarioCuenta.setText("");
				servidorCuenta.setText("");
				elCuenta.setVisible(false);
				opSip.removeListener();
			}
		};
		elCuenta.addActionListener(elCuentaListener);
	}
}
