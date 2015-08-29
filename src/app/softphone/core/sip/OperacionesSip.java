package app.softphone.core.sip;

import gov.nist.javax.sip.SipStackExt;
import gov.nist.javax.sip.clientauthutils.*;

import javax.sip.*;
import javax.sip.address.*;
import javax.sip.header.*;
import javax.sip.message.*;
import javax.swing.JOptionPane;










import org.apache.log4j.Logger;

import app.softphone.core.agenda.Contacto;
import app.softphone.core.agenda.OperacionesAgenda;
import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.preferencias.Configuracion;
import app.softphone.core.preferencias.OperacionesPreferencias;
import app.softphone.core.rtp.TonesTool;
import app.softphone.core.rtp.VoiceTool;
import app.softphone.core.sdp.SdpInfo;
import app.softphone.core.sdp.SdpManager;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class OperacionesSip  implements SipListener {
	  SipStackExt sipStack;
	  HeaderFactory header;
	  SipProvider udp;
	  AddressFactory address;
	  MessageFactory message;
	  ClientTransaction myClientTransaction;
	  ServerTransaction myServerTransaction;
	  Dialog myDialog;
	  ListeningPoint udpPoint;
	  ContactHeader contactHeader;
	  AuthorizationHeader ah;
	  Address fromAddress;
	  SdpManager mySdpManager;
	  SdpInfo offerInfo;
	  SdpInfo answerInfo;
	  VoiceTool myVoiceTool;
	  TonesTool myAlertTool, myRingTool, myBusyTool;
	  String sipId;
	  String myIp;
	  String sipLlam;
	  String usuarioLlam;
	  int myPort = 5060;
	  int audioPort = 40000;
	  int audioFormat = 3;
	  String myPw;
	  String asteriskIp;
	  String tag;
	  long seq = 1;
	  int asteriskPort = 5060;
	  Cuenta cuentaGlob;
	  int status, status2;
	  boolean registro;
	  
	  static final int YES=0;
	  static final int NO=1;

	  static final int REGISTER=-1;
	  static final int IDLE=0;
	  static final int WAIT_PROV=1;
	  static final int WAIT_FINAL=2;
	  static final int ESTABLISHED=4;
	  static final int RINGING=5;
	  static final int WAIT_ACK=6;
	  static final int PICK_UP=7;

	  static Logger log = Logger.getLogger("softphone");
	  OperacionesPreferencias opPre = new OperacionesPreferencias();
	  OperacionesAgenda opAgenda = new OperacionesAgenda();
	  private SecureRandom random = new SecureRandom();
	  
	  
	  public OperacionesSip(String myIp, Cuenta c) {
		  this.myIp = myIp;
		  this.sipId = c.getUsuario();
		  this.asteriskIp = c.getServidor();
		  this.myPw = c.getPassword();
		  try {
			  mySdpManager=new SdpManager();
		      myVoiceTool=new VoiceTool();
		      answerInfo=new SdpInfo();
		      offerInfo=new SdpInfo();
		      
		      myAlertTool=new TonesTool();
		      myRingTool=new TonesTool();
		      myBusyTool=new TonesTool();
		      
		      String toneRing = "file://" + new File("resources/ring 4.wav").getAbsolutePath();
		      String toneBusy = "file://" + new File("resources/ocupado.wav").getAbsolutePath();
		      
		      TimerTask tonesTask = new TimerTask() { 
			         public void run() { 
			        	 myRingTool.prepareTone(toneRing);
			        	 myBusyTool.prepareTone(toneBusy);
			        	//myAlertTool.prepareTone("file://c:\\alert.wav");
			         } 
			     }; 
			  Timer timerT = new Timer();
			  timerT.schedule(tonesTask, 6000);
		      
			  SipFactory sipFactory = SipFactory.getInstance();
			  sipFactory.setPathName("gov.nist");
			  Properties properties = new Properties();
	
			  properties.setProperty("javax.sip.STACK_NAME", "softphone");
			    //properties.setProperty("javax.sip.OUTBOUND_PROXY", asteriskIp+":"+asteriskPort+"/udp");
			  this.sipStack = (SipStackExt) sipFactory.createSipStack(properties);	
			  header = sipFactory.createHeaderFactory();
			  address = sipFactory.createAddressFactory();
			  message = sipFactory.createMessageFactory();
			  udpPoint = sipStack.createListeningPoint(myIp, myPort, "udp");
			  udp = sipStack.createSipProvider(udpPoint);
			  udp.addSipListener(this);
			  
			  SipURI myRealmURI = address.createSipURI(sipId, asteriskIp);
			  fromAddress = address.createAddress(myRealmURI);
			  
			  SipURI myURI = address.createSipURI(sipId, myIp);
			  myURI.setPort(myPort);
			  Address contactAddress = address.createAddress(myURI);
			  contactHeader = header.createContactHeader(contactAddress);
			  
			  TimerTask registerTask = new TimerTask() { 
			         public void run() { 
			            register(3600);
			            try {
							Thread.sleep (400);
						} catch (Exception ex) {
							System.out.println(ex.getMessage());
						} 
			         } 
			     }; 
			  Timer timerR = new Timer();
			  timerR.scheduleAtFixedRate(registerTask, 0, 500000);
		  } catch(Exception e) {
			  log.error(e.getMessage());
		  }
		  status = IDLE;
	  }
	  
	  
	  public String getSipLlam() {
		  return this.sipLlam;
	  }
	  
	  public String getUsuarioLlam() {
		  return this.usuarioLlam;
	  }
	  
	  public String nextTag() {
		  return new BigInteger(32, random).toString(64);
	  }
		  
	  public int getStatus() {
		  return this.status;
	  }
	  
	  public int getStatus2() {
		  return this.status2;
	  }
	  
	  public boolean getRegistro() {
		  return this.registro;
	  }
	 
	  public void removeListener() {
		  try {
			  udp.removeSipListener(this);
			  sipStack.deleteSipProvider(udp);
			  sipStack.deleteListeningPoint(udpPoint);
		  } catch(Exception e) {
			  log.error(e.getMessage());
		  }
	  }
	  
	  public void register(int expires) {
		  try {
			  if (status == IDLE) {
				  String tag = nextTag();
				  FromHeader fromHeader = header.createFromHeader(fromAddress, tag);
		
				  MaxForwardsHeader maxForwards = header.createMaxForwardsHeader(5);
		
				  List<ViaHeader> viaHeaders = new ArrayList<>();
				  CallIdHeader callIdHeader = udp.getNewCallId();
				  CSeqHeader cSeqHeader = header.createCSeqHeader(seq+3, Request.REGISTER);
				  ToHeader toHeader = header.createToHeader(fromAddress, null);
				  URI requestURI = address.createURI("sip:" + asteriskIp + ":" + asteriskPort);
			    
		
				  Request request = message.createRequest(requestURI, Request.REGISTER, callIdHeader,
			            cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
				  request.addHeader(contactHeader);
				  ExpiresHeader eh = header.createExpiresHeader(expires);
				  request.addHeader(eh);
				  myClientTransaction = udp.getNewClientTransaction(request);
				  myClientTransaction.sendRequest();
				  status = REGISTER;
				  log.info("Sent REGISTER request:\n" + request.toString());
			  }
		  } catch(Exception e) {
			  	log.error(e.getMessage());
		}
	  }

	  public void call(int type, String destination) {

		  try {
			  switch (status) {
			  	case IDLE:
			  		if (type == YES) {
			  			SipURI dest = address.createSipURI(destination, asteriskIp);
			  			Address toAddress = address.createAddress(dest);
			  			ToHeader myToHeader = header.createToHeader(toAddress, null);
			  			
			  			String tag = nextTag();
			  			FromHeader fromHeader = header.createFromHeader(fromAddress, tag);
			  			
			  			ViaHeader myViaHeader = header.createViaHeader(myIp, myPort, "udp", null);
			  			ArrayList<ViaHeader> myViaHeaders = new ArrayList<ViaHeader>();
			  			myViaHeaders.add(myViaHeader);
			  			
			  			MaxForwardsHeader myMaxForwardsHeader = header.createMaxForwardsHeader(70);
			  			CSeqHeader myCSeqHeader = header.createCSeqHeader(seq+3,"INVITE");
			  			CallIdHeader myCallIdHeader = udp.getNewCallId();
			  			URI myRequestURI = address.createURI("sip:" + destination + "@" + asteriskIp + ":" + asteriskPort);
			  			
			  			Request myRequest = message.createRequest(myRequestURI, "INVITE", myCallIdHeader, myCSeqHeader, fromHeader, 
			  					myToHeader, myViaHeaders, myMaxForwardsHeader);
			  			myRequest.addHeader(contactHeader);
			  			
			  			offerInfo = new SdpInfo();
			  			offerInfo.setIpAddress(myIp);
			  			offerInfo.setAPort(audioPort);
			  			offerInfo.setAFormat(audioFormat);
			  			
			  			ContentTypeHeader contentTypeHeader = header.createContentTypeHeader("application", "sdp");
			  			byte[] content = mySdpManager.createSdp(offerInfo);
			  			myRequest.setContent(content, contentTypeHeader);
			  			
			  			myClientTransaction = udp.getNewClientTransaction(myRequest);
			  			String bid = myClientTransaction.getBranchId();
			  			
			  			myClientTransaction.sendRequest();
			  			log.info("Send INVITE request:\n" + myRequest.toString());
			  			myDialog = myClientTransaction.getDialog();
			  			status = WAIT_PROV;
			  		}
			  		break;
			  		
			  	case WAIT_FINAL:
			  		if (type == NO) {
			  			status = IDLE;
			  			Request myCancelRequest = myClientTransaction.createCancel();
			  			ClientTransaction myCancelClientTransaction = udp.getNewClientTransaction(myCancelRequest);
			  			myCancelRequest.addHeader(ah);
			  			myCancelClientTransaction.sendRequest();
			  			log.info("Send CANCEL request:\n" + myCancelRequest.toString());
			  			//myRingTool.stopTone();
			  		}
			  		break;
			  		
			  	case ESTABLISHED:
			  		if (type == NO) {
			  			Request myBye = myDialog.createRequest("BYE");
			  			String uri = ((ToHeader)myBye.getHeader("To")).getAddress().getURI().toString();
		        		URI requestBye = address.createURI(uri + ":" + asteriskPort);
		        		myBye.setRequestURI(requestBye);
			  			myBye.addHeader(contactHeader);
			  			myClientTransaction = udp.getNewClientTransaction(myBye);
			  			myDialog.sendRequest(myClientTransaction);
			  			log.info("Send BYE request:\n" + myBye.toString());
			  			
			  			myVoiceTool.stopMedia();
			  			
			  			status = IDLE;
			  		}
			  		break;
			  		
			  	case RINGING:
			  		if (type == NO) {
			  			Request originalRequest = myServerTransaction.getRequest();
			  			Response myResponse  = message.createResponse(486,originalRequest);
			  			myServerTransaction.sendResponse(myResponse);
			  			status = IDLE;
			  			
			  			log.info("Send BUSY response:\n" + myResponse.toString());
			  			
			  			myRingTool.stopTone();
			  			break;
			  		} else if (type == YES) {
			  			Request originalRequest = myServerTransaction.getRequest();
			  			Response myResponse = message.createResponse(200, originalRequest);
			  			ToHeader myToHeader = (ToHeader) myResponse.getHeader("To");
			  			myToHeader.setTag(tag);
			  			myResponse.addHeader(contactHeader);
			  			
			  			myRingTool.stopTone();
			  			
			  			ContentTypeHeader contentTypeHeader = header.createContentTypeHeader("application", "sdp");
			  			byte[] content = mySdpManager.createSdp(answerInfo);
			  			myResponse.setContent(content, contentTypeHeader);
			  			
			  			myVoiceTool.startMedia(offerInfo.getIpAddress(), offerInfo.getAPort(), answerInfo.getAPort(), offerInfo.getAFormat(), myIp);
			  			
			  			myServerTransaction.sendResponse(myResponse);
			  			myDialog = myServerTransaction.getDialog();
			  			
			  			status = WAIT_ACK;
			  			
			  			log.info("Send 200 OK response:\n" + myResponse.toString());
			  			
			  			break;
			  		}
			  }
		  } catch(Exception e) {
			  log.error(e.getMessage());
		  }
		  
	  }
	  
	  public void callPickUp(String destination) {
		  try {
			  SipURI dest = address.createSipURI(destination, asteriskIp);
			  Address toAddress = address.createAddress(dest);
			  ToHeader myToHeader = header.createToHeader(toAddress, null);
			  
			  String tag = nextTag();
			  FromHeader fromHeader = header.createFromHeader(fromAddress, tag);
			  ViaHeader myViaHeader = header.createViaHeader(myIp, myPort, "udp", null);
			  ArrayList<ViaHeader> myViaHeaders = new ArrayList<ViaHeader>();
			  myViaHeaders.add(myViaHeader);
	  			
			  MaxForwardsHeader myMaxForwardsHeader = header.createMaxForwardsHeader(70);
			  CSeqHeader myCSeqHeader = header.createCSeqHeader(seq+3,"SUBSCRIBE");
			  CallIdHeader myCallIdHeader = udp.getNewCallId();
			  URI myRequestURI = address.createURI("sip:" + destination + "@" + asteriskIp + ":" + asteriskPort);
			  
			  Request myRequest = message.createRequest(myRequestURI, "SUBSCRIBE", myCallIdHeader, myCSeqHeader, fromHeader, 
	  					myToHeader, myViaHeaders, myMaxForwardsHeader);
			  myRequest.addHeader(contactHeader);
			  EventHeader eventHeader = header.createEventHeader("dialog");
			  myRequest.addHeader(eventHeader);
			  ExpiresHeader expiresHeader = header.createExpiresHeader(0);
			  myRequest.addHeader(expiresHeader);
			  
			  myClientTransaction = udp.getNewClientTransaction(myRequest);
			  myClientTransaction.sendRequest();
			  
			  System.out.println(myRequest.toString());
			  
			  status = PICK_UP;
			  
		  } catch(Exception e) {
			  e.printStackTrace();
		  }
	  }
	  
	  @Override
	  public void processRequest(RequestEvent requestReceivedEvent) {
		  Request myRequest = requestReceivedEvent.getRequest();
		  log.info("Request received\n" + myRequest.toString());
		  String method = myRequest.getMethod();
		  if (!method.equals("CANCEL")) {
			  myServerTransaction = requestReceivedEvent.getServerTransaction();
		  }
		  try {
			  switch(status) {
			  
			  	case IDLE:
			  		if (method.equals("INVITE")) {
			  			if (myServerTransaction == null) {
			  				myServerTransaction = udp.getNewServerTransaction(myRequest);
			  			}
			  			
			  			ContactHeader ch = (ContactHeader) myRequest.getHeader("Contact");
			  			sipLlam = ch.getAddress().toString();
			  			sipLlam = sipLlam.substring(5, sipLlam.length()-1);
			  			String[] user = sipLlam.split("@");
			  			usuarioLlam = user[0];
			  			Contacto c = opAgenda.buscarContactoNumero(user[0]);
			  			if (c.getNumero() != null) {
			  				usuarioLlam = c.getApellido() + ", " + c.getNombre();
			  				sipLlam = c.getDepartamento();
			  			}
			  			myRingTool.playTone();
			  			
			  			byte[] cont = (byte[]) myRequest.getContent();
			  			offerInfo = mySdpManager.getSdp(cont);
			  			
			  			answerInfo.setIpAddress(myIp);
			  			answerInfo.setAPort(audioPort);
			  			answerInfo.setAFormat(audioFormat);
			  			
			  			Configuracion conf = opPre.obtenerPreferencias();
			  			
			  			if (conf.getDesvioSiempre().equals("Si")) {
			  				Response myResponseTrying = message.createResponse(100, myRequest);
			  				Response myResponseMoved = message.createResponse(302, myRequest);
			  				
			  				myResponseTrying.addHeader(contactHeader);
			  				tag = nextTag();
			  				ToHeader toHeaderTrying = (ToHeader) myResponseTrying.getHeader("To");
			  				ToHeader toHeaderMoved = (ToHeader) myResponseMoved.getHeader("To");
			  				toHeaderTrying.setTag(tag);
			  				toHeaderMoved.setTag(tag);
			  				String[] moved = conf.getURIdesvio().split("@");
			  				SipURI myURI = address.createSipURI(moved[0], moved[1]);
			  				Address contactAddressMoved = address.createAddress(myURI);
			  				ContactHeader contactMoved = header.createContactHeader(contactAddressMoved);
			  				myResponseMoved.addHeader(contactMoved);
			  				myServerTransaction.sendResponse(myResponseTrying);
			  				myServerTransaction.sendResponse(myResponseMoved);
			  				myDialog = myServerTransaction.getDialog();
			  				
			  				log.info("Sent TRYING response:\n" + myResponseTrying.toString());
			  				log.info("Sent MOVED TEMPORARILY response:\n" + myResponseMoved.toString());
			  				
			  				status =IDLE;
			  			} else {
				  			Response myResponse = message.createResponse(180, myRequest);
				  			myResponse.addHeader(contactHeader);
				  			ToHeader myToHeader = (ToHeader) myResponse.getHeader("To");
				  			tag = nextTag();
				  			myToHeader.setTag(tag);
				  			myServerTransaction.sendResponse(myResponse);
				  			myDialog = myServerTransaction.getDialog();
				  			
				  			log.info("Send RINGING response:\n" + myResponse.toString());
				  			
				  			status = RINGING;
				  			
				  			if (conf.getDesvioTimeout().equals("Si")) {
				  				new Timer().schedule(new DesvioTimerTask(myRequest,conf), (Integer.parseInt(conf.getTimeoutDesvio())*1000));
				  			}
			  			}
			  		}
			  		break;
				  
			  	case ESTABLISHED:
			  		if (method.equals("BYE")) {
			  			Response myResponse = message.createResponse(200, myRequest);
			  			myResponse.addHeader(contactHeader);
			  			status = IDLE;
			  			myServerTransaction.sendResponse(myResponse);
			  			log.info("Send response:\n" + myResponse.toString());
			  			
			  			myVoiceTool.stopMedia();
			  			
			  		}/* else if(method.equals("INVITE")){
			  			if (myServerTransaction == null) {
			  				myServerTransaction = udp.getNewServerTransaction(myRequest);
			  			}
			  			
			  			byte[] cont = (byte[]) myRequest.getContent();
			  			offerInfo = mySdpManager.getSdp(cont);
			  			
			  			answerInfo.setIpAddress(myIp);
			  			answerInfo.setAPort(audioPort);
			  			answerInfo.setAFormat(audioFormat);
			  			
			  			Response myResponse = message.createResponse(200, myRequest);
			  			myResponse.addHeader(contactHeader);
			  			
			  			ContentTypeHeader contentTypeHeader = header.createContentTypeHeader("application", "sdp");
			  			byte[] content = mySdpManager.createSdp(answerInfo);
			  			myResponse.setContent(content, contentTypeHeader);
			  			
			  			//Hay que parar el anterior media
			  			myVoiceTool.stopMedia();
			  			myVoiceTool.startMedia(offerInfo.getIpAddress(),offerInfo.getAPort(),answerInfo.getAPort(),offerInfo.getAFormat(),myIp);
			  			
			  			myServerTransaction.sendResponse(myResponse);
			  			myDialog = myServerTransaction.getDialog();
			  			
			  			log.info("Send Response:\n" + myResponse.toString());
			  			
			  			status = ESTABLISHED;
			  		}*/
			  		break;
			  		
			  
			  	case RINGING:
			  		if (method.equals("CANCEL")) {
			  			ServerTransaction myCancelServerTransaction  = requestReceivedEvent.getServerTransaction();
			  			Request originalRequest = myServerTransaction.getRequest();
			  			Response myResponse = message.createResponse(487, originalRequest);
			  			myServerTransaction.sendResponse(myResponse);
			  			Response myCancelResponse = message.createResponse(200, myRequest);
			  			myCancelServerTransaction.sendResponse(myCancelResponse);
			  			
			  			myRingTool.stopTone();
			  			
			  			log.info("Send Response:\n" + myResponse.toString());
			  			log.info("Send Response:\n"+ myCancelResponse.toString());
			  			
			  			status = IDLE;
			  		}
			  		break;
			  	
			  	case WAIT_ACK:
			  		if (method.equals("ACK")) {
			  			status = ESTABLISHED;
			  		}
			  		break;
			  }
			  
		  } catch(Exception e) {
			  e.printStackTrace();
		  }
	  }
	    
	 
	    
	  @Override
	  public void processResponse(ResponseEvent responseReceivedEvent) {
		  try {
			  Response myResponse = responseReceivedEvent.getResponse();
			  log.info("Response received:\n" + myResponse.toString());
			  ClientTransaction thisClientTransaction = responseReceivedEvent.getClientTransaction();
			  	
			  if (!thisClientTransaction.equals(myClientTransaction)) {
					 return;
			  }
				  
			  int myStatusCode = myResponse.getStatusCode();
			  CSeqHeader originalCSeq = (CSeqHeader) myClientTransaction.getRequest().getHeader(CSeqHeader.NAME);
			  long numseq = originalCSeq.getSeqNumber();
			  
			  	if (myStatusCode == 401) {
			  		AccountManagerImpl manager = new AccountManagerImpl();
				    AuthenticationHelper helper = sipStack.getAuthenticationHelper(manager, header);
				    myClientTransaction = helper.handleChallenge(myResponse, thisClientTransaction, udp, 5);
				    Request request = myClientTransaction.getRequest();
				    ah = (AuthorizationHeader)request.getHeader("Authorization");
				    ((SipURI) request.getRequestURI()).removeParameter("maddr");
				    myClientTransaction.sendRequest();
				    log.info("Sent request with authentication info:\n" + request.toString());
				    return;
			  	}
			  	
			    switch (status) {
			    
			     	case REGISTER: 
			        	if (myStatusCode == 200) {
					        registro = true;
					    } else {
					        registro = false;
					    }
					    status = IDLE;
					    break;
					        
			    	case IDLE:
			    		
			    		break;
			    		
			        case WAIT_PROV:
			        	if (myStatusCode < 200) {
			        		status = WAIT_FINAL;
			        		myDialog = thisClientTransaction.getDialog();
			        		//myRingTool.playTone();
			        	} else if (myStatusCode < 300) {
			        		myDialog = thisClientTransaction.getDialog();
			        		Request myAck = myDialog.createAck(numseq);
			        		String uri = ((ToHeader)myAck.getHeader("To")).getAddress().getURI().toString();
			        		URI requestAck = address.createURI(uri + ":" + asteriskPort);
			        		myAck.setRequestURI(requestAck);
			        		myAck.addHeader(contactHeader);
			        		myDialog.sendAck(myAck);
			        		log.info("Send ACK:\n" + myAck.toString());
			        		//myRingTool.stopTone();
			        		status = ESTABLISHED;
			        			
			        		byte[] cont = (byte[]) myResponse.getContent();
			        		answerInfo = mySdpManager.getSdp(cont);
			        		
			        		//myVoiceTool.startMedia(answerInfo.getIpAddress(), answerInfo.getAPort(), offerInfo.getAPort(),answerInfo.getAFormat(), myIp);
			        		
			        	} else {
			        		status = IDLE;
			        		Request myAck  = myDialog.createAck(numseq);
			        		String uri = ((ToHeader)myAck.getHeader("To")).getAddress().getURI().toString();
			        		URI requestAck = address.createURI(uri + ":" + asteriskPort);
			        		myAck.setRequestURI(requestAck);
			        		myAck.addHeader(contactHeader);
			        		myDialog.sendAck(myAck);
			        		log.info("Send ACK:\n" + myAck.toString());
			        		//myRingTool.stopTone();
			        	}
			        	break;
			        	
			        case WAIT_FINAL:
			        	if (myStatusCode < 200) {
			        		status = WAIT_FINAL;
			        		myDialog = thisClientTransaction.getDialog();
			        		//myRingTool.playTone();
			        	} else if (myStatusCode < 300) {
			        		status = ESTABLISHED;
			        		myDialog = thisClientTransaction.getDialog();
			        		Request myAck = myDialog.createAck(numseq);
			        		String uri = ((ToHeader)myAck.getHeader("To")).getAddress().getURI().toString();
			        		URI requestAck = address.createURI(uri + ":" + asteriskPort);
			        		myAck.setRequestURI(requestAck);
			        		myAck.addHeader(contactHeader);
			        		myDialog.sendAck(myAck);
			        		log.info("Send ACK:\n" + myAck.toString());
			        		//myRingTool.stopTone();
			        			
			        		byte[] cont = (byte[]) myResponse.getContent();
			        		answerInfo = mySdpManager.getSdp(cont);
			        			
			        		myVoiceTool.startMedia(answerInfo.getIpAddress(), answerInfo.getAPort(), offerInfo.getAPort(), answerInfo.getAFormat(), myIp);
			        		log.info("Llamada establecida\n\n");
			        	} else {
			        		status = IDLE;
			        		myBusyTool.playTone();
			        		Thread.sleep(2000);
			        		myBusyTool.stopTone();
			        		//myRingTool.stopTone();
			        		
			        	}
			        	break;
			     }
			  } catch (Exception e) { 
			     e.printStackTrace(); 
			  }
	    }

	    @Override
	    public void processTimeout(TimeoutEvent timeoutEvent) {
	    	switch (status) {
	    		case REGISTER:
	    			registro = false;
	    			log.info("Timeout exceeded:\n" + timeoutEvent.getTimeout().toString());
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
	 

	    private class DesvioTimerTask extends TimerTask {
	        Request request;
	        Configuracion conf;
	        public DesvioTimerTask (Request request, Configuracion conf){
	                this.request = request;
	                this.conf = conf;
	              }
	        public void run() {
	          try{
	        	  if (status == RINGING) {
	  				Response myResponseMoved = message.createResponse(302, request);
	  				
	  				ToHeader toHeaderMoved = (ToHeader) myResponseMoved.getHeader("To");
	  				toHeaderMoved.setTag(tag);
	  				String[] moved = conf.getURIdesvio().split("@");
	  				SipURI myURI = address.createSipURI(moved[0], moved[1]);
	  				Address contactAddressMoved = address.createAddress(myURI);
	  				ContactHeader contactMoved = header.createContactHeader(contactAddressMoved);
	  				myResponseMoved.addHeader(contactMoved);
	  				myServerTransaction.sendResponse(myResponseMoved);
	  				myDialog = myServerTransaction.getDialog();
	  				
	  				log.info("Sent MOVED TEMPORARILY response:\n" + myResponseMoved.toString());
	  				
	  				myRingTool.stopTone();
	  				
	  				status = IDLE;
	        	  }
	          }catch (Exception ex){
	            ex.printStackTrace();
	        }
	    }
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
	

