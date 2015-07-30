package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Principal{
	JFrame ventana;
	JMenuBar menuPrin;
	JTabbedPane pestanas;
	JPanel zonaLlamar;
	JTextField llamarField;
	JButton llamarBut;
	
	public static void main (String[] args) {
		new Principal();
	}

	
	public Principal () {
		crearMenu();
		crearZonaLlamar();
		crearPestanas();
		crearVentana();
	}
	
	
	public void crearMenu() {
		menuPrin = new JMenuBar();
		JMenu opciones = new JMenu("Opciones");
		JMenu ayuda = new JMenu("Ayuda");
		menuPrin.add(opciones);
		menuPrin.add(ayuda);
		JMenuItem crearCuenta = new JMenuItem("Crear Cuenta...");
		JMenuItem preferencias = new JMenuItem("Preferencias");
		opciones.add(crearCuenta);
		opciones.add(preferencias);
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
	}
	
	public void crearZonaLlamar() {
		zonaLlamar = new JPanel();
		llamarField = new JTextField();
		llamarBut = new JButton();
		llamarBut.setText("Llamar");
		zonaLlamar.setLayout(new BoxLayout(zonaLlamar,BoxLayout.X_AXIS));
		zonaLlamar.add(Box.createRigidArea(new Dimension (5,0)));
		zonaLlamar.add(llamarField);
		zonaLlamar.add(Box.createRigidArea(new Dimension (10,0)));
		zonaLlamar.add(llamarBut);
		zonaLlamar.add(Box.createRigidArea(new Dimension (5,0)));
	}
	
	public void crearPestanas() {
		pestanas = new JTabbedPane();
		JPanel agenda = new JPanel();
		JPanel historico = new JPanel();
		JPanel dialpad = new JPanel();
		pestanas.addTab("Agenda", agenda);
		pestanas.addTab("Historico", historico);
		pestanas.addTab("Dialpad", dialpad);
	}
	
	public void crearVentana() {
		ventana = new JFrame();
		ventana.setTitle("Softphone");
		ventana.setSize(270,400);
		ventana.setJMenuBar(menuPrin);
		ventana.add(zonaLlamar, BorderLayout.NORTH);
		ventana.add(pestanas, BorderLayout.CENTER);
		ventana.setLocationRelativeTo(null);
		ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		ventana.setResizable(false);
		ventana.setVisible(true);
	}

}
