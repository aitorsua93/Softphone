package app.softphone.core.agenda;

public class Contacto {

	String nombre, numero;
	
	public Contacto() {
		
	}
	
	public Contacto(String nombre, String numero) {
		this.nombre = nombre;
		this.numero = numero;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getNumero() {
		return this.numero;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
}
