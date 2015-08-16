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
	
	JLabel opDesvio, datosDesvio, uriLabel, timeLabel;
	JPanel opciones, datos;
	
	
	public Desvio(JCheckBox desvioSiempre, JCheckBox desvioTimeout, JTextField uriDesvio, JTextField timeDesvio) {
		panelOpciones(desvioSiempre,desvioTimeout);
		panelDatos(uriDesvio,timeDesvio);
		setLayout(new BorderLayout());
		add(opciones, BorderLayout.NORTH);
		add(datos, BorderLayout.CENTER);
	}
	
	 public void panelOpciones(JCheckBox desvioSiempre, JCheckBox desvioTimeout) {
		 opciones = new JPanel();
		 opciones.setLayout(new BoxLayout(opciones,BoxLayout.Y_AXIS));
		 opciones.add(Box.createRigidArea(new Dimension (30,20)));
		 opDesvio = new JLabel("Opciones desvio");
		 opciones.add(opDesvio);
		 opciones.add(Box.createRigidArea(new Dimension (30,20)));
		 opciones.add(desvioSiempre);
		 opciones.add(Box.createRigidArea(new Dimension (30,20)));
		 opciones.add(desvioTimeout);
		 opciones.add(Box.createRigidArea(new Dimension (30,40)));
		 datosDesvio = new JLabel("Datos desvio");
		 opciones.add(datosDesvio);
	 }

	 public void panelDatos(JTextField uriDesvio, JTextField timeDesvio) {
		 datos = new JPanel(new FlowLayout());
		 datos.add(Box.createRigidArea(new Dimension (30,40)));
		 uriLabel = new JLabel("URI desvio:");
		 datos.add(uriLabel);
		 uriDesvio.setMaximumSize(uriDesvio.getPreferredSize());
		 datos.add(uriDesvio);
		 datos.add(Box.createRigidArea(new Dimension (10,10)));
		 timeLabel = new JLabel("Timeout desvio:");
		 datos.add(timeLabel);
		 timeDesvio.setMaximumSize(timeDesvio.getPreferredSize());
		 datos.add(timeDesvio);
	 }
}
