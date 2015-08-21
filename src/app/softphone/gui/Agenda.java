package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import app.softphone.core.agenda.Contacto;
import app.softphone.core.agenda.OperacionesAgenda;
import app.softphone.core.preferencias.OperacionesPreferencias;

@SuppressWarnings("serial")
public class Agenda extends JPanel {
	
	JPanel panelLista, panelBotones;
	JList<Contacto> listaContactos;
	AgendaListModel listModel;
	List<Contacto> lc;
	JButton anadirCont, eliminarCont, editarCont;
	JComboBox<String> filtro;
	OperacionesAgenda opAgenda = new OperacionesAgenda();
	OperacionesPreferencias opPre = new OperacionesPreferencias();
	
	public Agenda(JTextField llamar) {
		crearPanelLista();
		crearPanelBotones(llamar);
		add(panelLista, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
	}
	
	public void crearPanelLista() {
		lc = new ArrayList<Contacto>();
		listModel = new AgendaListModel();
		panelLista = new JPanel();
		listaContactos = new JList<Contacto>(listModel);
		lc = opAgenda.buscarContactos();
		for (int i=0;i<lc.size();i++) {
			listModel.addContacto(lc.get(i));
		}
		listModel.ordenarLista("nombre");
		listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaContactos.setLayoutOrientation(JList.VERTICAL);
		listaContactos.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(listaContactos);
		listScroller.setPreferredSize(new Dimension(300, 270));
		panelLista.add(listScroller, BorderLayout.CENTER);
	}
	
	
	public void crearPanelBotones(JTextField llamar) {
		panelBotones = new JPanel(new FlowLayout());
		//panelBotones.setLayout(new BoxLayout(panelBotones,BoxLayout.X_AXIS));
		anadirCont = new JButton();
		anadirCont.setText("Añadir");
		panelBotones.add(anadirCont);
		ActionListener anadirContListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnadirContacto anCont = new AnadirContacto(listModel,filtro.getSelectedItem().toString());
				anCont.setLocationRelativeTo(panelLista);
				anCont.setVisible(true);
			}
		};
		anadirCont.addActionListener(anadirContListener);
		
		editarCont = new JButton();
		editarCont.setText("Editar");
		editarCont.setEnabled(false);
		panelBotones.add(editarCont);
		ActionListener editarContListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Contacto c = listModel.getContacto(listaContactos.getSelectedIndex());
				EditarContacto ed = new EditarContacto(c,listModel, filtro.getSelectedItem().toString(),listaContactos.getSelectedIndex());
				ed.setLocationRelativeTo(panelLista);
				ed.setVisible(true);
			}
		};
		editarCont.addActionListener(editarContListener);
		
		eliminarCont = new JButton();
		eliminarCont.setText("Borrar");
		eliminarCont.setEnabled(false);
		panelBotones.add(eliminarCont);
		ActionListener eliminarContListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Contacto c = listModel.getContacto(listaContactos.getSelectedIndex());
				opAgenda.borrar(c);
				listModel.eliminarPersona(listaContactos.getSelectedIndex());
			}
		};
		eliminarCont.addActionListener(eliminarContListener);
		
		DefaultComboBoxModel<String> cm = new DefaultComboBoxModel<String>();
		cm.addElement("nombre"); cm.addElement("apellido"); cm.addElement("departamento");
		filtro = new JComboBox<String>(cm);
		panelBotones.add(filtro);
		ActionListener filtroListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listModel.ordenarLista(filtro.getSelectedItem().toString());
			}
		};
		filtro.addActionListener(filtroListener);
		
		ListSelectionListener listaListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (listaContactos.getSelectedIndex() == -1) {
					editarCont.setEnabled(false);
					eliminarCont.setEnabled(false);
			    } else {
			    	editarCont.setEnabled(true);
			    	eliminarCont.setEnabled(true);
			    	llamar.setText(listModel.getContacto(listaContactos.getSelectedIndex()).getNumero());
			    }
			}
		};
		listaContactos.addListSelectionListener(listaListener);
		
		
	}
	
	

}
