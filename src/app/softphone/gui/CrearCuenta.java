package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class CrearCuenta extends JDialog {
	
	JLabel usuarioLabel, servidorLabel, passwordLabel, nombreLabel;
	JTextField usuarioText, servidorText, nombreText;
	JPasswordField passwordText;
	JPanel panelDatos, panelBotones;
	JButton aceptar, cancelar;
	
	public static void main (String[] args) {
		CrearCuenta crearCuenta = new CrearCuenta();
		crearCuenta.setLocationRelativeTo(null);
		crearCuenta.setVisible(true);
	}

	public CrearCuenta() {
		crearPanelDatos();
		crearPanelBotones();
		crearVentana();
	}
	
	public void crearPanelDatos() {
		panelDatos = new JPanel();

		panelDatos.setLayout(new GridLayout(5,5,10,10));
		
		usuarioLabel = new JLabel("Usuario:");
		panelDatos.add(usuarioLabel);
		
		usuarioText = new JTextField();
		panelDatos.add(usuarioText);
		
		servidorLabel = new JLabel("Servidor de registro:");
		panelDatos.add(servidorLabel);
		
		servidorText = new JTextField();
		panelDatos.add(servidorText);
	
		passwordLabel = new JLabel("Contraseña:");
		panelDatos.add(passwordLabel);
		passwordText = new JPasswordField();
		panelDatos.add(passwordText);
		
		nombreLabel = new JLabel("Nombre:");
		panelDatos.add(nombreLabel);
		nombreText = new JTextField();
		panelDatos.add(nombreText);
	}
	

	public void crearPanelBotones() {
		panelBotones = new JPanel(new FlowLayout());
		
		aceptar = new JButton();
		aceptar.setText("Aceptar");
		panelBotones.add(aceptar);
		
		cancelar = new JButton();
		cancelar.setText("Cancelar");
		panelBotones.add(cancelar);
		ActionListener cancelarListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		cancelar.addActionListener(cancelarListener);
	}
	
	public void crearVentana() {
		setTitle("Crear Cuenta");
		setModal(true);
		setSize(400,250);
		add(panelDatos, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.NORTH);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.WEST);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.EAST);
		setResizable(false);
	}
}
