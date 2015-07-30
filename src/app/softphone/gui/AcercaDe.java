package app.softphone.gui;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class AcercaDe extends JDialog {





	public AcercaDe() {
		crearDialogo();
	}

	

	public void crearDialogo() {
		setTitle("Acerca De...");
		setSize(400,400);
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}
