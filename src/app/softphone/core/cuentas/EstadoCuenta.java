package app.softphone.core.cuentas;

public class EstadoCuenta {

	public enum Estado {REGISTRADO("Registrado"), NO_REGISTRADO("No registrado(Comprobar credenciales)");

	private String descr;
	
	Estado(String descr) {
		this.descr = descr;
	}
	
	public String getDescr() {
		return descr;
	}
}
	
}
