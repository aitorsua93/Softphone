package app.softphone.core.sip;

	import gov.nist.javax.sip.SipStackExt;
	import gov.nist.javax.sip.clientauthutils.*;

	import javax.sip.*;
	import javax.sip.address.*;
	import javax.sip.header.*;
	import javax.sip.message.*;
	import java.util.*;

	public class OperacionesSip {
	  private SipStackExt sipStack;
	  private HeaderFactory header;
	  private SipProvider udp;
	  private AddressFactory address;
	  private MessageFactory message;
	  private final String sipId = "8001";
	  private final String myIp = "192.168.1.101";
	  private final int myPort = 5060;
	  private final String myPw = "123";
	  private final String realm = "aitorCommunications";
	  private final String asteriskIp = "192.168.1.100";
	  private final int asteriskPort = 5060;
	  private final String tag = "fiewujgf489t6d23lkfd-dsfg8g125";

	  public static void main(String[] args) throws Exception {
	    new OperacionesSip().register();
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
		  AddressFactory address = sipFactory.createAddressFactory();
		  MessageFactory message = sipFactory.createMessageFactory();
		  } catch(Exception e) {
			  System.out.println(e.getMessage());
		  }
	  }
	  
	  public void register() {
		  try {
			  ListeningPoint udpPoint = sipStack.createListeningPoint(myIp, myPort, "udp");
	
			  MySIPListener listener = new MySIPListener();
	
			  udp = sipStack.createSipProvider(udpPoint);
			  udp.addSipListener(listener);
	
			  SipURI myRealmURI = address.createSipURI(sipId, realm);
			  Address fromAddress = address.createAddress(myRealmURI);
			  fromAddress.setDisplayName(sipId);
			  FromHeader fromHeader = header.createFromHeader(fromAddress, tag);
	
			  SipURI myURI = address.createSipURI(sipId, myIp);
			  myURI.setPort(myPort);
			  Address contactAddress = address.createAddress(myURI);
			  contactAddress.setDisplayName(sipId);
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
			  ExpiresHeader eh = header.createExpiresHeader(300);
			  request.addHeader(eh);
			  ClientTransaction transaction = udp.getNewClientTransaction(request);
			  transaction.sendRequest();
			  System.out.println("Sent request:");
			  System.out.println(request);
		  } catch(Exception e) {
			  	System.out.println(e.getMessage());
		}
	  }

	  private class MySIPListener implements SipListener {
	    @Override
	    public void processRequest(RequestEvent requestEvent) {}
	    @Override
	    public void processResponse(ResponseEvent event) {
	      try {
	        Response response = event.getResponse();
	        System.out.println("Response received:");
	        System.out.println(response);
	        if (response.getStatusCode() != 401) return;
	        ClientTransaction tid = event.getClientTransaction();
	        AccountManagerImpl manager = new AccountManagerImpl();
	        AuthenticationHelper helper = sipStack.getAuthenticationHelper(manager, header);
	        ClientTransaction transaction = helper.handleChallenge(response, tid, udp, 5);
	        transaction.sendRequest();
	        Request request = transaction.getRequest();
	        System.out.println("Sent request with authentication info:");
	        System.out.println(request);
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
	  }

	  private class AccountManagerImpl implements AccountManager {
	    @Override
	    public UserCredentials getCredentials(ClientTransaction clientTransaction, String s) {
	      return new UserCredentials() {
	        @Override
	        public String getUserName() { return sipId; }
	        @Override
	        public String getPassword() { return myPw; }
	        @Override
	        public String getSipDomain() { return realm; }
	      };
	    }
	  }
}
	

