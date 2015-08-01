package app.softphone.core.sip;

import java.util.ArrayList;
import java.util.List;

import javax.sip.ClientTransaction;
import javax.sip.ListeningPoint;
import javax.sip.SipProvider;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.*;
import javax.sip.message.Request;

import app.softphone.core.cuentas.Cuenta;

public class Register {

	private SipProvider udp;
	private final String realm = "aitorCommunication";
	private final String tag = "nkslhjfr89ew5yfoqiwehr938dewq-fdwfef23u";
	private final String myIP = "192.168.1.101";
	
	public Register(Cuenta cuenta, Inicializar ini) {
		crearListenPoint(ini);
		enviarRegister(cuenta,ini);
	}
	
	public void crearListenPoint(Inicializar ini) {
		try {
		 ListeningPoint udpPoint = ini.sipStack.createListeningPoint(myIP, 5060, "udp");
		 MySipListener listener = new MySipListener();
		 udp = ini.sipStack.createSipProvider(udpPoint);
		 udp.addSipListener(listener);
		} catch(Exception e){
			System.out.println("ERROR CREAR LISTENING POINT CON SIPLISTENER");
		}
	}
	
	public void enviarRegister(Cuenta cuenta, Inicializar ini) {
		try {
		    SipURI myRealmURI = ini.address.createSipURI(cuenta.getNombre(), realm);
		    Address fromAddress = ini.address.createAddress(myRealmURI);
		    fromAddress.setDisplayName(cuenta.getNombre());
		    FromHeader fromHeader = ini.header.createFromHeader(fromAddress, tag);
	
		    SipURI myURI = ini.address.createSipURI(cuenta.getNombre(), myIP);
		    myURI.setPort(5060);
		    Address contactAddress = ini.address.createAddress(myURI);
		    contactAddress.setDisplayName(cuenta.getNombre());
		    ContactHeader contactHeader = ini.header.createContactHeader(contactAddress);
	
		    MaxForwardsHeader maxForwards = ini.header.createMaxForwardsHeader(5);
	
		    List<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
		    CallIdHeader callIdHeader = udp.getNewCallId();
		    long seq = 1;
		    CSeqHeader cSeqHeader = ini.header.createCSeqHeader(seq++, Request.REGISTER);
		    ToHeader toHeader = ini.header.createToHeader(fromAddress, null);
		    URI requestURI = ini.address.createURI("sip:"+cuenta.getServidor()+":"+"5060"+";maddr="+cuenta.getServidor());
		    
	
		    Request request = ini.message.createRequest(requestURI, Request.REGISTER, callIdHeader,
		            cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
		    request.addHeader(contactHeader);
		    ExpiresHeader eh = ini.header.createExpiresHeader(3600);
		    request.addHeader(eh);
		    ClientTransaction transaction = udp.getNewClientTransaction(request);
		    transaction.sendRequest();
		    System.out.println("Sent request:");
		    System.out.println(request);
		} catch(Exception e) {
			System.out.println("ERROR ENVIAR REGISTER");
		}
	}
}
