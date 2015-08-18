package app.softphone.core.agenda;

public class Contacto {

	String nombre, apellido, numero, departamento;
	
	public Contacto() {
		
	}
	
	public Contacto(String nombre, String apellido, String numero, String departamento) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.numero = numero;
		this.departamento = departamento;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getApellido() {
		return this.apellido;
	}
	
	public String getNumero() {
		return this.numero;
	}
	
	public String getDepartamento() {
		return this.departamento;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
}
