package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
public class EditarContacto extends JDialog {
	
	JLabel nombreLabel, apellidoLabel, numeroLabel, deptLabel;
	JTextField nombreText, apellidoText, numeroText, deptText;
	JPanel panelDatos, panelBotones;
	JButton aceptar, cancelar;
	OperacionesAgenda opAgenda = new OperacionesAgenda();

	public EditarContacto(Contacto c, AgendaListModel list, String filtro, int index) {
		setTitle("Editar Contacto");
		setModal(true);
		setSize(400,250);
		crearPanelDatos(c);
		crearPanelBotones(c,list,filtro,index);
		add(panelDatos, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.NORTH);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.WEST);
		add(Box.createRigidArea(new Dimension (30,20)), BorderLayout.EAST);
		setResizable(false);
	}
	
	public void crearPanelDatos(Contacto c) {
		panelDatos = new JPanel();
		
		panelDatos.setLayout(new GridLayout(5,5,10,10));
		
		nombreLabel = new JLabel("Nombre:");
		panelDatos.add(nombreLabel);
		
		nombreText = new JTextField();
		nombreText.setText(c.getNombre());
		panelDatos.add(nombreText);
		
		apellidoLabel = new JLabel("Apellido:");
		panelDatos.add(apellidoLabel);
		
		apellidoText = new JTextField();
		apellidoText.setText(c.getApellido());
		panelDatos.add(apellidoText);
		
		numeroLabel = new JLabel("Numero:");
		panelDatos.add(numeroLabel);
		
		numeroText = new JTextField();
		numeroText.setText(c.getNumero());
		panelDatos.add(numeroText);
		
		deptLabel = new JLabel("Departamento:");
		panelDatos.add(deptLabel);
		
		deptText = new JTextField();
		deptText.setText(c.getDepartamento());
		panelDatos.add(deptText);
	}
	
	public void crearPanelBotones(Contacto c, AgendaListModel list, String filtro, int index) {
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
				Contacto cAct = new Contacto(nombre,apellido,numero,departamento);
				opAgenda.actualizar(cAct,c);
				list.eliminarPersona(index);
				list.addContacto(cAct);
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
