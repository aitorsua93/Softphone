package app.softphone.core.sip;

import gov.nist.javax.sip.SipStackExt;

import java.util.Properties;

import javax.sip.SipFactory;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;

public class Inicializar {

	  public SipStackExt sipStack;
	  public HeaderFactory header;
	  public AddressFactory address;
	  public MessageFactory message;
	
	public Inicializar(){
		SipFactory sipFactory = SipFactory.getInstance();
	    sipFactory.setPathName("gov.nist");
	    Properties properties = new Properties();

	    properties.setProperty("javax.sip.STACK_NAME", "softphone");
	    //properties.setProperty("javax.sip.OUTBOUND_PROXY", asteriskIp+":"+asteriskPort+"/udp");

	    try {
		    this.sipStack = (SipStackExt) sipFactory.createSipStack(properties);
		    header = sipFactory.createHeaderFactory();
		    address = sipFactory.createAddressFactory();
		    message = sipFactory.createMessageFactory();
	    } catch (Exception e) {
	    	System.out.println("Error INICIALIZAR");
	    }
	}
	
	public void leerConfig() {
		
	}
}
