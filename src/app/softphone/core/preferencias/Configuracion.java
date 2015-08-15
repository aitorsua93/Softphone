package app.softphone.core.preferencias;

public class Configuracion {

	String desvioSiempre, desvioTimeout, URIdesvio, timeoutDesvio, capturaOpcion, secCaptura;
	
	public Configuracion() {
		
	}
	
	public Configuracion(String desvioSiempre, String desvioTimeout, String URIdesvio, String timeoutDesvio,
			String capturaOpcion, String secCaptura) {
		this.desvioSiempre = desvioSiempre;
		this.desvioTimeout = desvioTimeout;
		this.URIdesvio = URIdesvio;
		this.timeoutDesvio = timeoutDesvio;
		this.capturaOpcion = capturaOpcion;
		this.secCaptura = secCaptura;
	}
	
	public String getDesvioSiempre() {
		return this.desvioSiempre;
	}
	
	public String getDesvioTimeout() {
		return this.desvioTimeout;
	}
	
	public String getURIdesvio() {
		return this.URIdesvio;
	}
	
	public String getTimeoutDesvio() {
		return this.timeoutDesvio;
	}
	
	public String getCapturaOpcion() {
		return this.capturaOpcion;
	}
	
	public String getSecCaptura() {
		return this.secCaptura;
	}
	
	public void setDesvioSiempre(String desvioSiempre) {
		this.desvioSiempre = desvioSiempre;
	}
	
	public void setDesvioTimeout(String desvioTimeout) {
		this.desvioTimeout = desvioTimeout;
	}
	
	public void setURIdesvio(String URIdesvio) {
		this.URIdesvio = URIdesvio;
	}
	
	public void setTimeoutDesvio(String timeoutDesvio) {
		this.timeoutDesvio = timeoutDesvio;
	}
	
	public void setCapturaOpcion(String capturaOpcion) {
		this.capturaOpcion = capturaOpcion;
	}
	
	public void setSecCaptura(String secCaptura) {
		this.secCaptura = secCaptura;
	}
}
