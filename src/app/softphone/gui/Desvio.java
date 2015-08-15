package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import app.softphone.core.preferencias.Configuracion;

@SuppressWarnings("serial")
public class Desvio extends JPanel {
	
	JCheckBox desvioSiempre, desvioTimeout;
	JLabel opDesvio, datosDesvio, uriLabel, timeLabel;
	JTextField uriDesvio, timeDesvio;
	JPanel opciones, datos;
	Configuracion conf;
	
	public Desvio(Configuracion conf) {
		this.conf = conf;
		panelOpciones();
		panelDatos();
		setLayout(new BorderLayout());
		add(opciones, BorderLayout.NORTH);
		add(datos, BorderLayout.CENTER);
	}
	
	 public void panelOpciones() {
		 opciones = new JPanel();
		 opciones.setLayout(new BoxLayout(opciones,BoxLayout.Y_AXIS));
		 opciones.add(Box.createRigidArea(new Dimension (30,20)));
		 opDesvio = new JLabel("Opciones desvio");
		 opciones.add(opDesvio);
		 opciones.add(Box.createRigidArea(new Dimension (30,20)));
		 desvioSiempre = new JCheckBox("Siempre desviar las llamadas al equipo indicado");
		 opciones.add(desvioSiempre);
		 if (conf.getDesvioSiempre().equals("Si")) {
			 desvioSiempre.setSelected(true);
		 } else if (conf.getDesvioSiempre().equals("No")) {
			 desvioSiempre.setSelected(false);
		 }
		 opciones.add(Box.createRigidArea(new Dimension (30,20)));
		 desvioTimeout = new JCheckBox("Desviar las llamadas despues del timeout indicado");
		 opciones.add(desvioTimeout);
		 if (conf.getDesvioTimeout().equals("Si")) {
			 desvioTimeout.setSelected(true);
		 } else if (conf.getDesvioTimeout().equals("No")) {
			 desvioTimeout.setSelected(false);
		 }
		 opciones.add(Box.createRigidArea(new Dimension (30,40)));
		 datosDesvio = new JLabel("Datos desvio");
		 opciones.add(datosDesvio);
	 }

	 public void panelDatos() {
		 datos = new JPanel(new FlowLayout());
		 datos.add(Box.createRigidArea(new Dimension (30,40)));
		 uriLabel = new JLabel("URI desvio:");
		 datos.add(uriLabel);
		 uriDesvio = new JTextField(15);
		 uriDesvio.setMaximumSize(uriDesvio.getPreferredSize());
		 uriDesvio.setText(conf.getURIdesvio());
		 datos.add(uriDesvio);
		 datos.add(Box.createRigidArea(new Dimension (10,10)));
		 timeLabel = new JLabel("Timeout desvio:");
		 datos.add(timeLabel);
		 timeDesvio = new JTextField(3);
		 timeDesvio.setMaximumSize(timeDesvio.getPreferredSize());
		 timeDesvio.setText(conf.getTimeoutDesvio());
		 datos.add(timeDesvio);
	 }
	 
	 public JCheckBox getDesvioSiempre() {
		 return this.desvioSiempre;
	 }
	 
	 public JCheckBox getDesvioTimeout() {
		 return this.desvioTimeout;
	 }
	 
	 public JTextField getUriDesvio() {
		 return this.uriDesvio;
	 }
	 
	 public JTextField getTimeoutDesvio() {
		 return this.timeDesvio;
	 }
	
}
