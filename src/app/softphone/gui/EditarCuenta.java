package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.cuentas.OperacionesCuenta;

@SuppressWarnings("serial")
public class EditarCuenta extends JDialog{
	
	JLabel usuarioLabel, servidorLabel, passwordLabel, nombreLabel;
	JTextField usuarioText, servidorText, nombreText;
	JPasswordField passwordText;
	JPanel panelDatos, panelBotones;
	JButton aceptarCue, cancelarCue;
	Cuenta nuevaCuenta;
	OperacionesCuenta op = new OperacionesCuenta();

	public EditarCuenta(Cuenta edCuenta,DefaultListModel<String> ln, int index) {
		crearPanelDatos(edCuenta);
		crearPanelBotones(edCuenta,ln,index);
		crearVentana();
	}
	
	public void crearPanelDatos(Cuenta edCuenta) {
		panelDatos = new JPanel();

		panelDatos.setLayout(new GridLayout(5,5,10,10));
		
		usuarioLabel = new JLabel("Usuario:");
		panelDatos.add(usuarioLabel);
		
		usuarioText = new JTextField();
		usuarioText.setText(edCuenta.getUsuario());
		panelDatos.add(usuarioText);
		
		servidorLabel = new JLabel("Servidor de registro:");
		panelDatos.add(servidorLabel);
		
		servidorText = new JTextField();
		servidorText.setText(edCuenta.getServidor());
		panelDatos.add(servidorText);
	
		passwordLabel = new JLabel("Contrase�a:");
		panelDatos.add(passwordLabel);
		
		passwordText = new JPasswordField();
		passwordText.setText(edCuenta.getPassword());
		panelDatos.add(passwordText);
		
		nombreLabel = new JLabel("Nombre:");
		panelDatos.add(nombreLabel);
		
		nombreText = new JTextField();
		nombreText.setText(edCuenta.getNombre());
		panelDatos.add(nombreText);
	}
	

	public void crearPanelBotones(Cuenta edCuenta,DefaultListModel<String> ln, int index) {
		panelBotones = new JPanel(new FlowLayout());
		
		aceptarCue = new JButton();
		aceptarCue.setText("Aceptar");
		panelBotones.add(aceptarCue);
		ActionListener aceptarCueListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String usuario = usuarioText.getText();
				String servidor = servidorText.getText();
				String password = passwordText.getText();//Temporal hasta encontrar solucion
				String nombre = nombreText.getText();
				nuevaCuenta = new Cuenta(usuario,servidor,password,nombre);
				op.actualizar(nuevaCuenta, edCuenta.getNombre());
				ln.remove(index);
				ln.add(index, nombre + " <" + usuario + "@" + servidor + ">");
				dispose();
			}
		};
		aceptarCue.addActionListener(aceptarCueListener);
		
		cancelarCue = new JButton();
		cancelarCue.setText("Cancelar");
		panelBotones.add(cancelarCue);
		ActionListener cancelarCueListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		cancelarCue.addActionListener(cancelarCueListener);
	}
	
	public void crearVentana() {
		setTitle("Editar Cuenta");
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