package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.cuentas.EstadoCuenta.Estado;
import app.softphone.core.cuentas.OperacionesCuenta;
import app.softphone.core.sip.OperacionesSip;

@SuppressWarnings("serial")
public class Cuentas extends JPanel {

	JPanel panelLista, panelBotones;
	OperacionesCuenta op = new OperacionesCuenta();
	JList<String> lista;
	List<Cuenta> lc;
	DefaultListModel<String> ln;
	JButton crCuenta, edCuenta, elCuenta; 
	
	public Cuentas(OperacionesSip opSip) {
		crearPanelLista(opSip);
		//crearPanelBotones();
		add(panelLista, BorderLayout.WEST);
		add(panelBotones, BorderLayout.CENTER);
	}
	
	public void crearPanelLista(OperacionesSip opSip) {
		lc = new ArrayList<Cuenta>();
		ln = new DefaultListModel<String>();
 		panelLista = new JPanel();
		lc = op.buscarCuentas();
		for (int i=0;i<lc.size();i++) {
			ln.addElement(lc.get(i).getNombre() + " | <" + lc.get(i).getUsuario() + "@" + lc.get(i).getServidor() + "> | " + lc.get(i).getEstado().getDescr());
		}
		JList<String> lista = new JList<String>(ln);
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lista.setLayoutOrientation(JList.VERTICAL);
		lista.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(lista);
		listScroller.setPreferredSize(new Dimension(250, 320));
		panelLista.add(listScroller, BorderLayout.CENTER);
		crearPanelBotones(lista,ln,opSip);
	}

	
	public void crearPanelBotones(JList<String> list, DefaultListModel<String> ln, OperacionesSip opSip) {
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
		elCuenta.setEnabled(false);
		if (list.getSelectedIndex() == -1) {
			edCuenta.setEnabled(false);
			elCuenta.setEnabled(false);
	    }
		
		ActionListener crCuentaListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CrearCuenta crear = new CrearCuenta(ln,opSip);
				crear.setLocationRelativeTo(panelLista);
				crear.setVisible(true);
			}
		};
		crCuenta.addActionListener(crCuentaListener);
		ActionListener edCuentaListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String c = list.getSelectedValue();
				int index = list.getSelectedIndex();
				String[] nombre = c.split("\\s+");
				Cuenta cuentaEd = op.buscarCuenta(nombre[0]);
				EditarCuenta editar = new EditarCuenta(cuentaEd,ln,index,opSip);
				editar.setLocationRelativeTo(panelLista);
				editar.setVisible(true);
			}
		};
		edCuenta.addActionListener(edCuentaListener);
		
		ActionListener elCuentaListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String c = list.getSelectedValue();
				int index = list.getSelectedIndex();
				String[] nombre = c.split("\\s+");
				ln.remove(index);
				Cuenta cuenta = op.buscarCuenta(nombre[0]);
				op.borrar(nombre[0]);
				if (cuenta.getEstado().equals(Estado.REGISTRADO)) {
					opSip.register(cuenta,0);
					//opSip.subscribe(cuenta);
				}
			}
		};
		elCuenta.addActionListener(elCuentaListener);
		
		ListSelectionListener listaListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (list.getSelectedIndex() == -1) {
					edCuenta.setEnabled(false);
					elCuenta.setEnabled(false);
			    } else {
			    	edCuenta.setEnabled(true);
			    	elCuenta.setEnabled(true);
			    }
			}
		};
		list.addListSelectionListener(listaListener);
	}
}
