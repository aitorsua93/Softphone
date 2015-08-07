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

import java.math.BigInteger;
import java.security.SecureRandom;
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
	  long seq = 1;
	  private final int asteriskPort = 5060;
	  //private final String tag = "fiewujgf489t6d23lkfd-dsfg8g125";
	  OperacionesCuenta op = new OperacionesCuenta();
	  Cuenta cuentaGlob;
	  int status;
	  
	  static final int YES=0;
	  static final int NO=1;

	  static final int REGISTER=0;
	  static final int IDLE=0;
	  static final int WAIT_PROV=1;
	  static final int WAIT_FINAL=2;
	  static final int ESTABLISHED=4;
	  static final int RINGING=5;
	  static final int WAIT_ACK=6;

	  
	  
	  private SecureRandom random = new SecureRandom();

	  public String nextTag() {
	    return new BigInteger(32, random).toString(64);
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
		  status = IDLE;
	  }
	  
	  public void register(Cuenta cuenta, int expires) {
		  try {
			  cuentaGlob = cuenta;
			  sipId = cuenta.getUsuario();
			  asteriskIp = cuenta.getServidor();
			  myPw = cuenta.getPassword();
			  status = REGISTER;
			  String tag = nextTag();

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
			  CSeqHeader cSeqHeader = header.createCSeqHeader(seq+3, Request.REGISTER);
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

	  public void call(int type, String destination, Cuenta cuenta) {
		  try {
			  cuentaGlob = cuenta;
			  sipId = cuenta.getUsuario();
			  asteriskIp = cuenta.getServidor();
			  myPw = cuenta.getPassword(); 
			  
			  switch (status) {
			  	case IDLE:
			  		if (type == YES) {
			  			break;
			  		}
			  }
		  } catch(Exception e) {
			  System.out.println(e.getMessage());
		  }
		  
	  }

	  /*public void subscribe(Cuenta cuenta) {
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
			  
			  MaxForwardsHeader maxForwards = header.createMaxForwardsHeader(5);
	
			  List<ViaHeader> viaHeaders = new ArrayList<>();
			  CallIdHeader callIdHeader = udp.getNewCallId();
			  long seq = 1;
			  CSeqHeader cSeqHeader = header.createCSeqHeader(seq++, Request.SUBSCRIBE);
			  ToHeader toHeader = header.createToHeader(fromAddress, null);
			  URI requestURI = address.createURI("sip:asterisk@"+asteriskIp+":"+asteriskPort+";maddr="+asteriskIp);
		    
			  Request request = message.createRequest(requestURI, Request.SUBSCRIBE, callIdHeader,
		            cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
			  request.addHeader(contactHeader);
			  EventHeader event = header.createEventHeader("message-summary");
			  request.addHeader(event);
			  ExpiresHeader eh = header.createExpiresHeader(0);
			  request.addHeader(eh);
			  ClientTransaction transaction = udp.getNewClientTransaction(request);
			  transaction.sendRequest();
			  System.out.println("Sent request:");
			  System.out.println(request);
		  } catch(Exception e) {
			 System.out.println(e.getMessage());
		  }
	  }*/
	  
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
			        
			        switch (status) {
			        	case REGISTER: 
					        String[] expires = response.getExpires().toString().split("\\s+");
					        if (!(expires[1].equals("0")) ) {
					        	if (response.getStatusCode() == 200) {
					        		cuentaGlob.setEstado(Estado.REGISTRADO);
					        		op.actualizar(cuentaGlob, cuentaGlob.getNombre());
					        	}
					        }
					        if (response.getStatusCode() == 403) {
					        	cuentaGlob.setEstado(Estado.NO_REGISTRADO);
				        		op.actualizar(cuentaGlob, cuentaGlob.getNombre());
					        }
					        
					        status = IDLE;
					        break;
			  }
			      } catch (SipException e) { 
			    	  e.printStackTrace(); 
			      }
	    }

	    @Override
	    public void processTimeout(TimeoutEvent timeoutEvent) {
	    	switch (status) {
	    		case REGISTER:
	        		status = IDLE;
	        		break;
	        }
	    }
	   
	    @Override
	    public void processIOException(IOExceptionEvent exceptionEvent) {
	    	//System.out.println("process IOException");
	    }
	    
	    @Override
	    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
	    	//System.out.println("process Transaction Terminated");
	    }
	    
	    @Override
	    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
	    	//System.out.println("process Dialog Terminated");
	    }
	 

	  private class AccountManagerImpl implements AccountManager {
	    @Override
	    public UserCredentials getCredentials(ClientTransaction clientTransaction, String s) {
	      return new UserCredentials() {
	        @Override
	        public String getUserName() { 
	        	return sipId; 
	        }
	        
	        @Override
	        public String getPassword() { 
	        	return myPw; 
	        }
	        
	        @Override
	        public String getSipDomain() { 
	        	return asteriskIp; 
	        }
	      };
	    }
	  }
}
	

