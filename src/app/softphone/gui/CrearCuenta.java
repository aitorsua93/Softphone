package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.cuentas.EstadoCuenta.Estado;
import app.softphone.core.cuentas.OperacionesCuenta;
import app.softphone.core.sip.OperacionesSip;


@SuppressWarnings("serial")
public class CrearCuenta extends JDialog {
	
	JLabel usuarioLabel, servidorLabel, passwordLabel, nombreLabel;
	JTextField usuarioText, servidorText, nombreText;
	JPasswordField passwordText;
	JPanel panelDatos, panelBotones;
	JButton aceptarCue, cancelarCue;
	Cuenta nuevaCuenta;
	DefaultListModel<String> ln = new DefaultListModel<String>();;
	OperacionesCuenta op = new OperacionesCuenta();

	public CrearCuenta(DefaultListModel<String> ln, OperacionesSip opSip, JComboBox<String> cuentaBox) {
		crearPanelDatos();
		crearPanelBotones(ln,opSip,cuentaBox);
		crearVentana();
	}

	public CrearCuenta(OperacionesSip opSip, JComboBox<String> cuentaBox) {
		crearPanelDatos();
		crearPanelBotones(ln,opSip, cuentaBox);
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
	

	public void crearPanelBotones(DefaultListModel<String> ln, OperacionesSip opSip, JComboBox<String> cuentaBox) {
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
				if (usuario.equals("") || servidor.equals("") || password.equals("") || usuario.equals("")) {
					JOptionPane.showMessageDialog(panelDatos, "Hay que rellenar todos los campos","Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				nuevaCuenta = new Cuenta(usuario,servidor,password,nombre,Estado.NO_REGISTRADO);
				if (op.existeCuenta(nuevaCuenta,null)) {
					JOptionPane.showMessageDialog(panelDatos, "El nombre de la cuenta es repetido o ya hay una cuenta asociada a ese usuario y servidor","Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				op.crear(nuevaCuenta);
				dispose();
				opSip.register(nuevaCuenta,3600);
				Progreso p = new Progreso();
				p.setLocationRelativeTo(panelBotones);
				p.setVisible(true);
				nuevaCuenta = op.buscarCuenta(nuevaCuenta.getNombre());
				ln.addElement(nombre + " | <" + usuario + "@" + servidor + "> | " + nuevaCuenta.getEstado().getDescr());
				//Actualizar ComboBox
				List<Cuenta> lc = new ArrayList<Cuenta>();
				lc = op.buscarCuentas();
				DefaultComboBoxModel<String> cuentas = new DefaultComboBoxModel<String>();
				for (int i=0;i<lc.size();i++) {
					if (lc.get(i).getEstado().equals(Estado.REGISTRADO)) {
						cuentas.addElement(lc.get(i).getNombre() + " <" + lc.get(i).getUsuario() + "@" + lc.get(i).getServidor() + ">");
					}
				}
				cuentaBox.setModel(cuentas);
				
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
