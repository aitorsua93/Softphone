package app.softphone.gui;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Cuentas extends JPanel {

	JPanel panelSelec;
	
	public static void main(String[] args) {
		Cuentas preferencias = new Cuentas();
		preferencias.setVisible(true);
		
	}
	
	public Cuentas() {
		crearVentana();
	}
	
	public void crearPanelSelec() {
		panelSelec = new JPanel();
		
	}
	
	public void crearVentana() {
		setSize(500,500);
	}
	
}
