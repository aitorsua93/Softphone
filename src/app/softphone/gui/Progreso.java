package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public class Progreso extends JDialog{
	
	JProgressBar progress;
	
	public static void main(String[] args) {
		Progreso p = new Progreso();
		p.setLocationRelativeTo(null);
		p.setVisible(true);
	}
	
	public Progreso() {
		crearProgreso();
		crearVentana();
	}
	
	
	public void crearProgreso() {
		progress = new JProgressBar();
		progress.setVisible(true);
		progress.setIndeterminate(true);
		progress.setPreferredSize(new Dimension(100,50));
		progress.setMaximumSize(progress.getPreferredSize());
	}
	
	public void crearVentana() {
		setTitle("Registrando...");
		setSize(350,100);
		add(progress, BorderLayout.CENTER);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.NORTH);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.WEST);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.EAST);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.SOUTH);
		setModal(true);
	}

}
