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
public class Captura extends JPanel {
	
	JPanel opciones, datos;
	JCheckBox activarCaptura;
	JLabel opcionesLabel, datosLabel, secLabel;
	JTextField secCaptura;
	
	public Captura() {
		crearOpciones();
		crearDatos();
		setLayout(new BorderLayout());
		add(opciones, BorderLayout.NORTH);
		add(datos, BorderLayout.CENTER);
	}
	
	public void crearOpciones() {
		opciones = new JPanel();
		opciones.setLayout(new BoxLayout(opciones,BoxLayout.Y_AXIS));
		opciones.add(Box.createRigidArea(new Dimension (30,20)));
		opcionesLabel = new JLabel ("Opciones captura de llamadas");
		opciones.add(opcionesLabel);
		opciones.add(Box.createRigidArea(new Dimension (30,20)));
		activarCaptura = new JCheckBox("Activar captura de llamadas");
		opciones.add(activarCaptura);
		opciones.add(Box.createRigidArea(new Dimension (30,40)));
		datosLabel = new JLabel("Datos captura de llamadas");
		opciones.add(datosLabel);
		opciones.add(Box.createRigidArea(new Dimension (30,20)));
	}

	public void crearDatos() {
		datos = new JPanel(new FlowLayout(FlowLayout.LEADING));
		datos.add(Box.createRigidArea(new Dimension (30,40)));
		secLabel = new JLabel("Secuencia para captura:");
		datos.add(secLabel);
		secCaptura = new JTextField(5);
		secCaptura.setMaximumSize(secCaptura.getPreferredSize());
		datos.add(secCaptura);
	}
}
