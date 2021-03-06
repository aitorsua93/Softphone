package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.preferencias.Configuracion;
import app.softphone.core.preferencias.OperacionesPreferencias;
import app.softphone.core.sip.OperacionesSip;


public class Principal implements ActionListener{
	JFrame ventana;
	JMenuBar menuPrin;
	JTabbedPane pestanas;
	JPanel zonaLlamar;
	JTextField llamarField;
	JButton llamarBut;
	OperacionesSip opSip;
	Cuenta cuenta;
	IniciarSesion is;
	JMenuItem iniciarSesion;
	String sipLlam, usuarioLlam;
	static final int IDLE=0;
	static final int WAIT_PROV=1;
	static final int WAIT_FINAL=2;
	static final int ESTABLISHED=4;
	static final int RINGING=5;
	static final int WAIT_ACK=6;
	int status;
	Log l = new Log();
	OperacionesPreferencias opPre = new OperacionesPreferencias();
	RecibirLlamada rl = null;
	
	
	public static void main(String[] args) {
		new Principal();
	}
	
	
	public Principal () {
		crearMenu();
		crearZonaLlamar();
		crearPestanas();
		crearVentana();
		Timer timer = new Timer(100,this);
		timer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (!(iniciarSesion.isEnabled())) {
			  opSip = is.getOpSip();
			  status = opSip.getStatus();
			  //System.out.println(status);
			  
			  switch(status) {
			  	case IDLE:
			  		if (pestanas.getTabCount() == 4) {
			  			pestanas.remove(3);
			  		}
			  		rl = null;
			  		break;
			  	case RINGING:
			  		sipLlam = opSip.getSipLlam();
			  		usuarioLlam = opSip.getUsuarioLlam();
			  		Cuenta c = is.getCuenta();
			  		if (rl == null) {
				  		rl = new RecibirLlamada(usuarioLlam,sipLlam,opSip,c,pestanas);
				  		rl.setVisible(true);
			  		}
			  		break;
			  }
		  }
	}
	
	public void crearMenu() {
		menuPrin = new JMenuBar();
		JMenu opciones = new JMenu("Opciones");
		JMenu log = new JMenu("Log");
		JMenu ayuda = new JMenu("Ayuda");
		menuPrin.add(opciones);
		menuPrin.add(log);
		menuPrin.add(ayuda);
		
		
		//Item IniciarSesion
		iniciarSesion = new JMenuItem("Iniciar Sesion..."); 
		opciones.add(iniciarSesion);
		ActionListener iniciarSesionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				is = new IniciarSesion(iniciarSesion);
				is.setLocationRelativeTo(ventana);
				is.setVisible(true);
			}
		};
		iniciarSesion.addActionListener(iniciarSesionListener);
		
		//Item Preferencias
		JMenuItem preferencias = new JMenuItem("Preferencias"); 
		opciones.add(preferencias);
		ActionListener preferenciasListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (iniciarSesion.isEnabled()) {
					opSip = null;
					cuenta = null;
				} else {
					opSip = is.getOpSip();
					cuenta = is.getCuenta();
				}
				Preferencias preferencias = new Preferencias(opSip,cuenta,iniciarSesion);
				preferencias.setLocationRelativeTo(ventana);
				preferencias.setVisible(true);
			}
		};
		preferencias.addActionListener(preferenciasListener);
		
		//Item Log
		JMenuItem logV = new JMenuItem("Ver log"); 
		log.add(logV);
		ActionListener logVListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				l.setLocationRelativeTo(ventana);
				l.setVisible(true);
			}
		};
		logV.addActionListener(logVListener);
		
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
	
	public void crearZonaLlamar() {
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
				if (!destinatario.equals("") && !(iniciarSesion.isEnabled())) {
					Configuracion conf = opPre.obtenerPreferencias();
					String comparar = destinatario.substring(0, conf.getSecCaptura().length());
					if (conf.getCapturaOpcion().equals("Si") && conf.getSecCaptura().equals(comparar)) {
						String usuarioCaptura = destinatario.substring(conf.getSecCaptura().length(), destinatario.length());
						//Funcion que haga la captura de llamadas
						opSip.callPickUp(usuarioCaptura);
					} else {
						opSip = is.getOpSip();
						cuenta = is.getCuenta();
						JPanel llamada = new Llamada(opSip,cuenta,destinatario,pestanas);
						pestanas.addTab("Llamada",llamada);
						pestanas.setSelectedIndex(3);
						llamarField.setText("");
						opSip.call(0, destinatario);
					}
				}	
			}
		};
		llamarBut.addActionListener(llamarListener);
	}
	
	public void crearPestanas() {
		pestanas = new JTabbedPane();
		JPanel agenda = new Agenda(llamarField);
		JPanel historico = new JPanel();
		JPanel dialpad = new Dialpad(llamarField);
		pestanas.addTab("Agenda", agenda);
		pestanas.addTab("Historico", historico);
		pestanas.addTab("Dialpad", dialpad);
	}
	
	
	public void crearVentana() {
		ventana = new JFrame();
		ventana.setTitle("Softphone");
		ventana.setSize(370,460);
		ventana.setJMenuBar(menuPrin);
		ventana.add(zonaLlamar, BorderLayout.NORTH); // Colocar la zona de llamar en la zona de arriba de la ventana
		ventana.add(pestanas, BorderLayout.CENTER); // Colocar las pesta�as en centro de la ventana
		ventana.setLocationRelativeTo(null); // Poner pantalla en el centro de la pantalla
		ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Funcion para terminar la aplicacion al cerrar la ventana principal
		ventana.setResizable(false); // Evitar aumentar el tama�o de la ventana
		ventana.setVisible(true); 
	}


}
