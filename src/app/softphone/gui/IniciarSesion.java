
package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.sip.OperacionesSip;


@SuppressWarnings("serial")
public class IniciarSesion extends JDialog {
		
	JLabel usuarioLabel, servidorLabel, passwordLabel, nombreLabel, interfazLabel;
	JTextField usuarioText, servidorText, nombreText;
	JComboBox<String> elegirInt;
	JPasswordField passwordText;
	JPanel panelDatos, panelBotones;
	JButton aceptarCue, cancelarCue;
	Cuenta cuenta;
	OperacionesSip opSip;


	public static DefaultComboBoxModel<String> elegirInterface() {
		DefaultComboBoxModel<String> cb = new DefaultComboBoxModel<String>(); 
		try {   
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
		        NetworkInterface i = (NetworkInterface) en.nextElement();
		        for (Enumeration<InetAddress> en2 = i.getInetAddresses(); en2.hasMoreElements();) {
		            InetAddress addr = (InetAddress) en2.nextElement();
		            if (!addr.isLoopbackAddress()) {
		                if (addr instanceof Inet4Address) {
		                	String in = i.getName();
		                	String address = addr.toString();
		                	cb.addElement(in + ": " + address.substring(1, address.length()));
		                }
		            }
		        }
		    }
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return cb;
	}
	

	public IniciarSesion(JMenuItem in) {
		crearPanelDatos();
		crearPanelBotones(in);
		crearVentana();
	}
		
	public void crearPanelDatos() {
		panelDatos = new JPanel();

		panelDatos.setLayout(new GridLayout(6,6,10,10));
		
		nombreLabel = new JLabel("Nombre:");
		panelDatos.add(nombreLabel);
		nombreText = new JTextField();
		panelDatos.add(nombreText);
		
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
		
		interfazLabel = new JLabel("Interfaz de red:");
		panelDatos.add(interfazLabel);
		DefaultComboBoxModel<String> cb = elegirInterface();
		elegirInt = new JComboBox<String>(cb);
		panelDatos.add(elegirInt);
	}
		

	public void crearPanelBotones(JMenuItem in) {
		panelBotones = new JPanel(new FlowLayout());
			
		aceptarCue = new JButton();
		aceptarCue.setText("Aceptar");
		panelBotones.add(aceptarCue);
		ActionListener aceptarCueListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String usuario = usuarioText.getText();
				String servidor = servidorText.getText();
				String password = passwordText.getText();
				String nombre = nombreText.getText();
				if (usuario.equals("") || servidor.equals("") || password.equals("") || usuario.equals("")) {
					JOptionPane.showMessageDialog(panelDatos, "Hay que rellenar todos los campos","Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				cuenta = new Cuenta(usuario,servidor,password,nombre);
				String ip = elegirInt.getSelectedItem().toString();
				String[] ipA = ip.split("\\s+");
				String ipEleg = ipA[1];
				Progreso p = new Progreso();
				opSip = new OperacionesSip(ipEleg,cuenta);
				p.setLocationRelativeTo(panelDatos);
				p.setVisible(true);
				if (opSip.getRegistro()) {
					in.setEnabled(false);
					dispose();
				} else {
					JOptionPane.showMessageDialog(panelDatos, "Se ha producido un error en el registro, compruebe sus datos","Error", JOptionPane.ERROR_MESSAGE);
					opSip.removeListener();
					return;
				}
			}
		};
		aceptarCue.addActionListener(aceptarCueListener);

		cancelarCue = new JButton();
		cancelarCue.setText("Salir");
		panelBotones.add(cancelarCue);
		ActionListener cancelarCueListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		cancelarCue.addActionListener(cancelarCueListener);
	}
	
	
	public OperacionesSip getOpSip() {
		return this.opSip;
	}
	
	public Cuenta getCuenta() {
		return this.cuenta;
	}
		
	public void crearVentana() {
		setTitle("Iniciar Sesion...");
		setModal(true);
		setSize(400,300);
		add(panelDatos, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.NORTH);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.WEST);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.EAST);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
	}
}

