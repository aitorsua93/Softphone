package app.softphone.gui;

import javax.swing.JLabel;

public class Cronometro implements Runnable {

	JLabel cronometro;
	
	public Cronometro(JLabel cronometro) {
		this.cronometro = cronometro;
	}
	
	
	@Override
	public void run() {
		 Integer horas = 0 , minutos = 0, segundos = 0;
	     String hor="", min="", seg="";
	     try {
	    	 while (true) {
	    		 Thread.sleep(1000);
	             segundos += 1;
	             if (segundos == 60) {
	            	 segundos = 0;
	                 minutos += 1;
	                 if(minutos == 60) {
	                	 minutos = 0;
	                     horas++;
	                 }
	             }
	             
	             if(horas < 10) {
	            	 hor = "0" + horas.toString();
	             }
	             else {
	            	 hor = horas.toString();
	             }
	             if( minutos < 10 ) {
	            	min = "0" + minutos.toString();
	             }
	             else {
	                min = minutos.toString();
	             }
	             if( segundos < 10 ) {
	            	 seg = "0" + segundos.toString();
	             }
	             else {
	            	 seg = segundos.toString();
	             }
	             
	             cronometro.setText(hor + ":" + min + ":" + seg);            
	    	 }
	          
	        } catch(Exception e){
	        	
	        }
		
	}

}
