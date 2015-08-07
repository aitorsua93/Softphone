package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.cuentas.EstadoCuenta.Estado;
import app.softphone.core.cuentas.OperacionesCuenta;
import app.softphone.core.sip.OperacionesSip;


public class Principal{
	JFrame ventana;
	JMenuBar menuPrin;
	JTabbedPane pestanas;
	JPanel zonaLlamar;
	JTextField llamarField;
	JButton llamarBut;
	OperacionesSip opSip;
	JComboBox<String> cuentaBox;
	OperacionesCuenta opC = new OperacionesCuenta();
	JPanel elegirC; 
	
	public static void main (String[] args) {
		new Principal();
	}

	
	public Principal () {
		opSip = new OperacionesSip();
		crearMenu(opSip);
		crearZonaLlamar(opSip);
		crearPestanas();
		crearElegirCuenta();
		crearVentana();
	}
	
	
	public void crearMenu(OperacionesSip opSip) {
		menuPrin = new JMenuBar();
		JMenu opciones = new JMenu("Opciones");
		JMenu ayuda = new JMenu("Ayuda");
		menuPrin.add(opciones);
		menuPrin.add(ayuda);
		
		//Item Crear Cuenta con Listener para mostrar la ventana CrearCuenta
		JMenuItem crearCuenta = new JMenuItem("Crear Cuenta..."); 
		opciones.add(crearCuenta);
		ActionListener crearCuentaListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CrearCuenta crearCuenta = new CrearCuenta(opSip,cuentaBox);
				crearCuenta.setLocationRelativeTo(ventana);
				crearCuenta.setVisible(true);
			}
		};
		crearCuenta.addActionListener(crearCuentaListener);
		
		//Item Preferencias
		JMenuItem preferencias = new JMenuItem("Preferencias"); 
		opciones.add(preferencias);
		ActionListener preferenciasListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Preferencias preferencias = new Preferencias(opSip,cuentaBox);
				preferencias.setLocationRelativeTo(ventana);
				preferencias.setVisible(true);
			}
		};
		preferencias.addActionListener(preferenciasListener);
		
		//Item Acerca De con Listener para mostrar la ventana AcercaDe
		JMenuItem acercaDe = new JMenuItem("Acerca De"); 
		ayuda.add(acercaDe);
		ActionListener acercaDeListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AcercaDe acercaDe = new AcercaDe();
				acercaDe.setLocationRelativeTo(ventana);
				acercaDe.setVisible(true);
			}
		};
		acercaDe.addActionListener(acercaDeListener);
		
		//Item Salir con Listener para salir de la aplicacion
		JMenuItem salir = new JMenuItem("Salir"); 
		ayuda.add(salir);
		ActionListener salirListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		salir.addActionListener(salirListener);
	}
	
	public void crearZonaLlamar(OperacionesSip opSip) {
		zonaLlamar = new JPanel(); 
		llamarField = new JTextField();
		llamarBut = new JButton();
		llamarBut.setText("Llamar");
		zonaLlamar.setLayout(new BoxLayout(zonaLlamar,BoxLayout.X_AXIS)); // Colocar elementos entorno al eje de las X
		zonaLlamar.add(Box.createRigidArea(new Dimension (5,0)));
		zonaLlamar.add(llamarField);
		zonaLlamar.add(Box.createRigidArea(new Dimension (10,0)));
		zonaLlamar.add(llamarBut);
		zonaLlamar.add(Box.createRigidArea(new Dimension (5,0)));
		ActionListener llamarListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String destinatario = llamarField.getText();
				if (!destinatario.equals("")) {
					JPanel llamada = new JPanel();
					pestanas.insertTab("Llamada", null, llamada, null, 0);
					pestanas.setSelectedIndex(0);
					llamarField.setText("");
				}
			}
		};
		llamarBut.addActionListener(llamarListener);
	}
	
	public void crearPestanas() {
		pestanas = new JTabbedPane();
		JPanel agenda = new JPanel();
		JPanel historico = new JPanel();
		JPanel dialpad = new Dialpad(llamarField);
		pestanas.addTab("Agenda", agenda);
		pestanas.addTab("Historico", historico);
		pestanas.addTab("Dialpad", dialpad);
	}
	
	public void crearElegirCuenta() {
		elegirC = new JPanel();
		List<Cuenta> lc = new ArrayList<Cuenta>();
		lc = opC.buscarCuentas();
		DefaultComboBoxModel<String> cuentas = new DefaultComboBoxModel<String>();
		for (int i=0;i<lc.size();i++) {
			if (lc.get(i).getEstado().equals(Estado.REGISTRADO)) {
				cuentas.addElement(lc.get(i).getNombre() + " <" + lc.get(i).getUsuario() + "@" + lc.get(i).getServidor() + ">");
			}
		}
		cuentaBox = new JComboBox<String>(cuentas);
		elegirC.add(Box.createRigidArea(new Dimension (0,10)), BorderLayout.NORTH);
		elegirC.add(cuentaBox, BorderLayout.CENTER);
		elegirC.add(Box.createRigidArea(new Dimension (0,10)), BorderLayout.SOUTH);
		elegirC.add(Box.createRigidArea(new Dimension (0,5)), BorderLayout.WEST);
		elegirC.add(Box.createRigidArea(new Dimension (0,5)), BorderLayout.EAST);
	}
	
	public void crearVentana() {
		ventana = new JFrame();
		ventana.setTitle("Softphone");
		ventana.setSize(350,450);
		ventana.setJMenuBar(menuPrin);
		ventana.add(zonaLlamar, BorderLayout.NORTH); // Colocar la zona de llamar en la zona de arriba de la ventana
		ventana.add(pestanas, BorderLayout.CENTER); // Colocar las pesta�as en centro de la ventana
		ventana.add(elegirC, BorderLayout.SOUTH);
		ventana.setLocationRelativeTo(null); // Poner pantalla en el centro de la pantalla
		ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Funcion para terminar la aplicacion al cerrar la ventana principal
		ventana.setResizable(true); // Evitar aumentar el tama�o de la ventana
		ventana.setVisible(true); 
	}

}
