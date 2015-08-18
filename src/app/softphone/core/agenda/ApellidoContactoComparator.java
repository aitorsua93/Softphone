package app.softphone.core.agenda;

import java.util.Comparator;

public class ApellidoContactoComparator implements Comparator<Contacto> {

	@Override
	public int compare(Contacto c1, Contacto c2) {
		return c1.getApellido().compareTo(c2.getApellido());
	}

}
