package app.softphone.core.agenda;

import java.util.Comparator;

public class DepartamentoContactoComparator implements Comparator<Contacto> {

	@Override
	public int compare(Contacto c1, Contacto c2) {
		return c1.getDepartamento().compareTo(c2.getDepartamento());
	}

}
