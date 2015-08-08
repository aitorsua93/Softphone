package app.softphone.core.cuentas;

import app.softphone.core.cuentas.EstadoCuenta.Estado;


public class Cuenta {
	
	String usuario, servidor, nombre;
	String password;
	Estado estado;
	
	
	public Cuenta() {
		
	}
	
	public Cuenta(String usuario, String servidor, String password, String nombre) {
		this.usuario = usuario;
		this.servidor = servidor;
		this.password = password;
		this.nombre = nombre;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public String getServidor() {
		return servidor;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Estado getEstado() {
		return estado;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setNombre (String nombre) {
		this.nombre = nombre;
	}
	
	public void setEstado (Estado estado) {
		this.estado= estado;
	}
}
