package app.softphone.core.sip;

import gov.nist.javax.sip.SipStackExt;
import gov.nist.javax.sip.clientauthutils.*;

import javax.sip.*;
import javax.sip.address.*;
import javax.sip.header.*;
import javax.sip.message.*;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.cuentas.EstadoCuenta.Estado;
import app.softphone.core.cuentas.OperacionesCuenta;

import java.util.*;

public class OperacionesSip  implements SipListener {
	  SipStackExt sipStack;
	  HeaderFactory header;
	  SipProvider udp;
	  AddressFactory address;
	  MessageFactory message;
	  String sipId;
	  private final String myIp = "192.168.1.101";
	  private final int myPort = 5060;
	  String myPw;
	  String asteriskIp;
	  private final int asteriskPort = 5060;
	  private final String tag = "fiewujgf489t6d23lkfd-dsfg8g125";
	  OperacionesCuenta op = new OperacionesCuenta();
	  Cuenta cuentaGlob;

	  public static void main(String[] args) throws Exception {
	    //OperacionesSip os = new OperacionesSip();
	    //os.register();
	  }

	  
	  public OperacionesSip() {
		  try {
		  SipFactory sipFactory = SipFactory.getInstance();
		  sipFactory.setPathName("gov.nist");
		  Properties properties = new Properties();

		  properties.setProperty("javax.sip.STACK_NAME", "softphone");
		    //properties.setProperty("javax.sip.OUTBOUND_PROXY", asteriskIp+":"+asteriskPort+"/udp");
		  this.sipStack = (SipStackExt) sipFactory.createSipStack(properties);	
		  header = sipFactory.createHeaderFactory();
		  address = sipFactory.createAddressFactory();
		  message = sipFactory.createMessageFactory();
		  ListeningPoint udpPoint = sipStack.createListeningPoint(myIp, myPort, "udp");
		  udp = sipStack.createSipProvider(udpPoint);
		  udp.addSipListener(this);
		  TimerTask registerTask = new TimerTask() 
		     { 
		         public void run()  
		         { 
		            List<Cuenta> lc = new ArrayList<Cuenta>();
		            lc = op.buscarCuentas();
		            for (int i=0;i<lc.size();i++) {
		            	register(lc.get(i),3600);
		            	try {
							Thread.sleep (400);
						} catch (Exception ex) {
							System.out.println(ex.getMessage());
						}
		            }
		         } 
		     }; 
		  Timer timer = new Timer();
		  timer.scheduleAtFixedRate(registerTask, 0, 500000);
		  } catch(Exception e) {
			  System.out.println(e.getMessage());
		  }
	  }
	  
	  public void register(Cuenta cuenta, int expires) {
		  try {
			  cuentaGlob = cuenta;
			  sipId = cuenta.getUsuario();
			  asteriskIp = cuenta.getServidor();
			  myPw = cuenta.getPassword(); 
			  SipURI myRealmURI = address.createSipURI(sipId, asteriskIp);
			  Address fromAddress = address.createAddress(myRealmURI);
			  FromHeader fromHeader = header.createFromHeader(fromAddress, tag);
			  SipURI myURI = address.createSipURI(sipId, myIp);
			  myURI.setPort(myPort);
			  Address contactAddress = address.createAddress(myURI);
			  ContactHeader contactHeader = header.createContactHeader(contactAddress);
	
			  MaxForwardsHeader maxForwards = header.createMaxForwardsHeader(5);
	
			  List<ViaHeader> viaHeaders = new ArrayList<>();
			  CallIdHeader callIdHeader = udp.getNewCallId();
			  long seq = 1;
			  CSeqHeader cSeqHeader = header.createCSeqHeader(seq++, Request.REGISTER);
			  ToHeader toHeader = header.createToHeader(fromAddress, null);
			  URI requestURI = address.createURI("sip:"+asteriskIp+":"+asteriskPort+";maddr="+asteriskIp);
		    
	
			  Request request = message.createRequest(requestURI, Request.REGISTER, callIdHeader,
		            cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			  request.addHeader(contactHeader);
			  ExpiresHeader eh = header.createExpiresHeader(expires);
			  request.addHeader(eh);
			  ClientTransaction transaction = udp.getNewClientTransaction(request);
			  transaction.sendRequest();
			  System.out.println("Sent request:");
			  System.out.println(request);
		  } catch(Exception e) {
			  	System.out.println(e.getMessage());
		}
	  }


	  public void subscribe(Cuenta cuenta) {
		  try {
			  sipId = cuenta.getUsuario();
			  asteriskIp = cuenta.getServidor();
			  myPw = cuenta.getPassword();
			  SipURI myRealmURI = address.createSipURI(sipId, asteriskIp);
			  Address fromAddress = address.createAddress(myRealmURI);
			  FromHeader fromHeader = header.createFromHeader(fromAddress, tag);
			  SipURI myURI = address.createSipURI(sipId, myIp);
			  myURI.setPort(myPort);
			  Address contactAddress = address.createAddress(myURI);
			  ContactHeader contactHeader = header.createContactHeader(contactAddress);
	
			  System.out.println("111111111111111111111111111111111111111111111111111111");
			  
			  MaxForwardsHeader maxForwards = header.createMaxForwardsHeader(5);
	
			  List<ViaHeader> viaHeaders = new ArrayList<>();
			  CallIdHeader callIdHeader = udp.getNewCallId();
			  long seq = 1;
			  CSeqHeader cSeqHeader = header.createCSeqHeader(seq++, Request.SUBSCRIBE);
			  ToHeader toHeader = header.createToHeader(fromAddress, null);
			  URI requestURI = address.createURI("sip:asterisk@"+asteriskIp+":"+asteriskPort+";maddr="+asteriskIp);
		    
			  System.out.println("2222222222222222222222222222222222222222222222222222222");
			  
			  Request request = message.createRequest(requestURI, Request.SUBSCRIBE, callIdHeader,
		            cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			  System.out.println("333333333333333333333333333333333333333333333333333333333333333");
			  request.addHeader(contactHeader);
			  EventHeader event = header.createEventHeader("message-summary");
			  request.addHeader(event);
			  ExpiresHeader eh = header.createExpiresHeader(0);
			  request.addHeader(eh);
			  ClientTransaction transaction = udp.getNewClientTransaction(request);
			  transaction.sendRequest();
			  System.out.println("4444444444444444444444444444444444444444444444444444444444444444");
			  System.out.println("Sent request:");
			  System.out.println(request);
		  } catch(Exception e) {
			 System.out.println(e.getMessage());
		  }
	  }
	  
	    @Override
	    public void processRequest(RequestEvent requestEvent) {
	    	
	    }
	    
	 
	    @Override
	    public void processResponse(ResponseEvent event) {
	      try {
	        Response response = event.getResponse();
	        System.out.println("Response received:");
	        System.out.println(response);
	        if (response.getStatusCode() == 401) {
		        ClientTransaction tid = event.getClientTransaction();
		        AccountManagerImpl manager = new AccountManagerImpl();
		        AuthenticationHelper helper = sipStack.getAuthenticationHelper(manager, header);
		        ClientTransaction transaction = helper.handleChallenge(response, tid, udp, 5);
		        transaction.sendRequest();
		        Request request = transaction.getRequest();
		        System.out.println("Sent request with authentication info:");
		        System.out.println(request);
	        }
	        
	        if (response.getStatusCode() == 200) {
	        	String[] cSeq = response.getHeader("CSeq").toString().split("\\s+");
	        	if (cSeq[2].equals("REGISTER")) {
	        		cuentaGlob.setEstado(Estado.REGISTRADO);
	        		op.actualizar(cuentaGlob, cuentaGlob.getNombre());
	        	}
	        }
	      } catch (SipException e) { e.printStackTrace(); }
	    }

	    @Override
	    public void processTimeout(TimeoutEvent timeoutEvent) {}
	    @Override
	    public void processIOException(IOExceptionEvent exceptionEvent) {}
	    @Override
	    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {}
	    @Override
	    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {}
	 

	  private class AccountManagerImpl implements AccountManager {
	    @Override
	    public UserCredentials getCredentials(ClientTransaction clientTransaction, String s) {
	      return new UserCredentials() {
	        @Override
	        public String getUserName() { return sipId; }
	        @Override
	        public String getPassword() { return myPw; }
	        @Override
	        public String getSipDomain() { return asteriskIp; }
	      };
	    }
	  }
}
	

