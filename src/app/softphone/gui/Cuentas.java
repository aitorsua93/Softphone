package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.cuentas.OperacionesCuenta;

@SuppressWarnings("serial")
public class Cuentas extends JPanel {

	JPanel panelLista, panelBotones;
	OperacionesCuenta op = new OperacionesCuenta();
	JList<String> lista;
	List<Cuenta> lc;
	DefaultListModel<String> ln;
	JButton crCuenta, edCuenta, elCuenta; 
	
	public Cuentas() {
		crearPanelLista();
		crearPanelBotones();
		add(panelLista, BorderLayout.WEST);
		add(panelBotones, BorderLayout.CENTER);
	}
	
	public void crearPanelLista() {
		lc = new ArrayList<Cuenta>();
		ln = new DefaultListModel<String>();
 		panelLista = new JPanel();
		lc = op.buscarCuentas();
		for (int i=0;i<lc.size();i++) {
			ln.addElement(lc.get(i).getNombre() + " <" + lc.get(i).getUsuario() + "@" + lc.get(i).getServidor() + ">");
		}
		JList<String> lista = new JList<String>(ln);
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lista.setLayoutOrientation(JList.VERTICAL);
		lista.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(lista);
		listScroller.setPreferredSize(new Dimension(250, 320));
		panelLista.add(listScroller, BorderLayout.CENTER);
	}

	public void crearPanelBotones() {
		panelBotones = new JPanel();
		panelBotones.setLayout(new BoxLayout(panelBotones,BoxLayout.Y_AXIS));
		crCuenta = new JButton();
		crCuenta.setText("Crear Cuenta");
		panelBotones.add(crCuenta);
		panelBotones.add(Box.createRigidArea(new Dimension (10,20)));
		edCuenta = new JButton();
		edCuenta.setText("Editar");
		panelBotones.add(edCuenta);
		panelBotones.add(Box.createRigidArea(new Dimension (10,20)));
		elCuenta = new JButton();
		elCuenta.setText("Eliminar");
		panelBotones.add(elCuenta);
	}
}
