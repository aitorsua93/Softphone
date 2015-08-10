package app.softphone.core.sip;

import gov.nist.javax.sip.SipStackExt;
import gov.nist.javax.sip.clientauthutils.*;

import javax.sip.*;
import javax.sip.address.*;
import javax.sip.header.*;
import javax.sip.message.*;

import org.apache.log4j.Logger;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.rtp.TonesTool;
import app.softphone.core.rtp.VoiceTool;
import app.softphone.core.sdp.SdpInfo;
import app.softphone.core.sdp.SdpManager;

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
	  Address fromAddress;
	  SdpManager mySdpManager;
	  SdpInfo offerInfo;
	  SdpInfo answerInfo;
	  VoiceTool myVoiceTool;
	  TonesTool myAlertTool;
	  TonesTool myRingTool;
	  String sipId;
	  String myIp;
	  int myPort = 5060;
	  int audioPort = 4000;
	  int audioFormat = 3;
	  String myPw;
	  String asteriskIp;
	  long seq = 1;
	  int asteriskPort = 5060;
	  Cuenta cuentaGlob;
	  int status;
	  boolean registro;
	  
	  static final int YES=0;
	  static final int NO=1;

	  static final int REGISTER=0;
	  static final int IDLE=0;
	  static final int WAIT_PROV=1;
	  static final int WAIT_FINAL=2;
	  static final int ESTABLISHED=4;
	  static final int RINGING=5;
	  static final int WAIT_ACK=6;

	  static Logger log = Logger.getLogger("softphone");
	 
	  private SecureRandom random = new SecureRandom();

	  public String nextTag() {
	    return new BigInteger(32, random).toString(64);
	  }
	  
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
		      
		      //myAlertTool.prepareTone("file://c:\\alert.wav");
		      //myRingTool.prepareTone("file://c:\\ring.wav");
		      
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
			  
			  TimerTask registerTask = new TimerTask() 
			     { 
			         public void run()  
			         { 
			            register(3600);
			            try {
							Thread.sleep (400);
						} catch (Exception ex) {
							log.error(ex.getMessage());
						} 
			         } 
			     }; 
			  Timer timer = new Timer();
			  timer.scheduleAtFixedRate(registerTask, 0, 500000);
		  } catch(Exception e) {
			  log.error(e.getMessage());
		  }
		  status = IDLE;
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
				  URI requestURI = address.createURI("sip:"+asteriskIp+":"+asteriskPort+";maddr="+asteriskIp);
			    
		
				  Request request = message.createRequest(requestURI, Request.REGISTER, callIdHeader,
			            cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
				  request.addHeader(contactHeader);
				  ExpiresHeader eh = header.createExpiresHeader(expires);
				  request.addHeader(eh);
				  myClientTransaction = udp.getNewClientTransaction(request);
				  myClientTransaction.sendRequest();
				  status = REGISTER;
				  log.info("Sent request:\n" + request.toString());
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
			  			Address toAddress = address.createAddress(destination);
			  			ToHeader myToHeader = header.createToHeader(toAddress, null);
			  			
			  			String tag = nextTag();
			  			FromHeader fromHeader = header.createFromHeader(fromAddress, tag);
			  			
			  			ViaHeader myViaHeader = header.createViaHeader(myIp, myPort, "udp", null);
			  			ArrayList<ViaHeader> myViaHeaders = new ArrayList<ViaHeader>();
			  			myViaHeaders.add(myViaHeader);
			  			
			  			MaxForwardsHeader myMaxForwardsHeader = header.createMaxForwardsHeader(70);
			  			CSeqHeader myCSeqHeader = header.createCSeqHeader(seq+3,"INVITE");
			  			CallIdHeader myCallIdHeader = udp.getNewCallId();
			  			URI myRequestURI = toAddress.getURI();
			  			
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
			  			myDialog = myClientTransaction.getDialog();
			  			status = WAIT_PROV;
			  			
			  			break;
			  		}
			  }
		  } catch(Exception e) {
			  log.error(e.getMessage());
		  }
		  
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
	  
	  @Override
	  public void processRequest(RequestEvent requestEvent) {
	    	
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
				        ClientTransaction transaction = helper.handleChallenge(myResponse, thisClientTransaction, udp, 5);
				        transaction.sendRequest();
				        Request request = transaction.getRequest();
				        log.info("Sent request with authentication info:\n" + request.toString());
			        }
			        
			        switch (status) {
			        	case REGISTER: 
					        String[] expires = myResponse.getExpires().toString().split("\\s+");
					        if (!(expires[1].equals("0")) ) {
					        	if (myStatusCode == 200) {
					        		registro = true;
					        	}
					        }
					        if (myStatusCode == 403) {
					        	registro = false;
					        }
					        
					        status = IDLE;
					        break;
					        
			        	case WAIT_PROV:
			        		if (myStatusCode < 200) {
			        			status = WAIT_FINAL;
			        			myDialog = thisClientTransaction.getDialog();
			        			//myRingTool.playTone();
			        		} else if (myStatusCode < 300) {
			        			myDialog = thisClientTransaction.getDialog();
			        			Request myAck = myDialog.createAck(numseq);
			        			myAck.addHeader(contactHeader);
			        			myDialog.sendAck(myAck);
			        			//myRingTool.stopTone();
			        			status = ESTABLISHED;
			        			
			        			byte[] cont = (byte[]) myResponse.getContent();
			        			answerInfo = mySdpManager.getSdp(cont);
			        			
			        			myVoiceTool.startMedia(answerInfo.getIpAddress(), answerInfo.getAPort(), offerInfo.getAPort(),answerInfo.getAFormat(), myIp);
			        		} else {
			        			status = IDLE;
			        			Request myAck  = myDialog.createAck(numseq);
			        			myAck.addFirst(contactHeader);
			        			myDialog.sendAck(myAck);
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
	

