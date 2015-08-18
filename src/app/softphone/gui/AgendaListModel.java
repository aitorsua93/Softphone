package app.softphone.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;

import app.softphone.core.agenda.ApellidoContactoComparator;
import app.softphone.core.agenda.Contacto;
import app.softphone.core.agenda.DepartamentoContactoComparator;
import app.softphone.core.agenda.NombreContactoComparator;

@SuppressWarnings("serial")
public class AgendaListModel extends DefaultListModel {
	
	private List<Contacto> lista = new ArrayList<Contacto>();

	
	public void addContacto(Contacto c) {
		lista.add(c);
		this.fireIntervalAdded(this, 0, getSize());
	}
	
	public void eliminarPersona(int index) {
		lista.remove(index);
		this.fireIntervalRemoved(this, 0, getSize());
	}
	
	public void ordenarLista(String filtro) {
		if (filtro.equals("nombre")) {
			Collections.sort(lista, new NombreContactoComparator());
		} else if (filtro.equals("apellido")) {
			Collections.sort(lista, new ApellidoContactoComparator());
		} else if (filtro.equals("departamento")) {
			Collections.sort(lista, new DepartamentoContactoComparator());
		}
		this.fireContentsChanged(this, 0, getSize());
	}
	
	public Contacto getContacto(int index) {
		return lista.get(index);
	}
	
	@Override
	public Object getElementAt(int index) {
		Contacto c = lista.get(index);
		String l = c.getApellido() + ", " + c.getNombre() + " | " + c.getDepartamento();
		return l;
	}

	@Override
	public int getSize() {
		return lista.size();
	}

}
