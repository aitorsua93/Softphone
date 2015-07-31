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

@SuppressWarnings("serial")
public class Desvio extends JPanel {
	
	JCheckBox desvioSiempre, desvioTimeout;
	JLabel opDesvio, datosDesvio, uriLabel, timeLabel;
	JTextField uriDesvio, timeDesvio;
	JPanel opciones, datos;
	
	public Desvio() {
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
		 opciones.add(Box.createRigidArea(new Dimension (30,20)));
		 desvioTimeout = new JCheckBox("Desviar las llamadas despues del timeout indicado");
		 opciones.add(desvioTimeout);
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
		 datos.add(uriDesvio);
		 datos.add(Box.createRigidArea(new Dimension (10,10)));
		 timeLabel = new JLabel("Timeout desvio:");
		 datos.add(timeLabel);
		 timeDesvio = new JTextField(3);
		 timeDesvio.setMaximumSize(timeDesvio.getPreferredSize());
		 datos.add(timeDesvio);
	 }
	
}
