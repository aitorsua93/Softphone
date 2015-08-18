package app.softphone.core.agenda;

import java.util.Comparator;

public class NombreContactoComparator implements Comparator<Contacto> {

	@Override
	public int compare(Contacto c1, Contacto c2) {
		return c1.getNombre().compareTo(c2.getNombre());
	}

}
