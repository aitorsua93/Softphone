package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import app.softphone.core.agenda.Contacto;
import app.softphone.core.agenda.OperacionesAgenda;

@SuppressWarnings("serial")
public class AnadirContacto extends JDialog{
	
	JLabel nombreLabel, apellidoLabel, numeroLabel, deptLabel;
	JTextField nombreText, apellidoText, numeroText, deptText;
	JPanel panelDatos, panelBotones;
	JButton aceptar, cancelar;
	Contacto nuevoContacto;
	OperacionesAgenda opAgenda = new OperacionesAgenda();

	public AnadirContacto(AgendaListModel list, String filtro) {
		setTitle("Añadir Contacto");
		setModal(true);
		setSize(400,250);
		crearPanelDatos();
		crearPanelBotones(list,filtro);
		add(panelDatos, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.NORTH);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.WEST);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.EAST);
		setResizable(false);
	}
	
	public void crearPanelDatos() {
		panelDatos = new JPanel();
		
		panelDatos.setLayout(new GridLayout(5,5,10,10));
		
		nombreLabel = new JLabel("Nombre:");
		panelDatos.add(nombreLabel);
		
		nombreText = new JTextField();
		panelDatos.add(nombreText);
		
		apellidoLabel = new JLabel("Apellido:");
		panelDatos.add(apellidoLabel);
		
		apellidoText = new JTextField();
		panelDatos.add(apellidoText);
		
		numeroLabel = new JLabel("Numero:");
		panelDatos.add(numeroLabel);
		
		numeroText = new JTextField();
		panelDatos.add(numeroText);
		
		deptLabel = new JLabel("Departamento:");
		panelDatos.add(deptLabel);
		
		deptText = new JTextField();
		panelDatos.add(deptText);
	}
	
	public void crearPanelBotones(AgendaListModel list, String filtro) {
		panelBotones = new JPanel(new FlowLayout());
		
		aceptar = new JButton();
		aceptar.setText("Aceptar");
		panelBotones.add(aceptar);
		ActionListener aceptarListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombre = nombreText.getText();
				String apellido = apellidoText.getText();
				String numero = numeroText.getText();
				String departamento = deptText.getText();
				if (nombre.equals("") || apellido.equals("") || numero.equals("") || departamento.equals("")) {
					JOptionPane.showMessageDialog(panelDatos, "Hay que rellenar todos los campos","Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Contacto c = new Contacto(nombre,apellido,numero,departamento);
				opAgenda.crear(c);
				list.addContacto(c);
				list.ordenarLista(filtro);
				dispose();
			}
		};
		aceptar.addActionListener(aceptarListener);
		
		cancelar = new JButton();
		cancelar.setText("Cancelar");
		panelBotones.add(cancelar);
		ActionListener cancelarListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		cancelar.addActionListener(cancelarListener);
	}
	
}
