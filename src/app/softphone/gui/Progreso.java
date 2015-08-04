package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.Timer;

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
		final Timer t = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((Timer)e.getSource()).stop();
				dispose();
			}
	    });
		t.start();
		
	}
	
	public void crearVentana() {
		setTitle("Registrando...");
		setSize(350,100);
		setResizable(false);
		add(progress, BorderLayout.CENTER);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.NORTH);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.WEST);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.EAST);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.SOUTH);
		setModal(true);
	}

}
