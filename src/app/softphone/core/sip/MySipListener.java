package app.softphone.core.sip;

import gov.nist.javax.sip.clientauthutils.AccountManager;
import gov.nist.javax.sip.clientauthutils.UserCredentials;

import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class MySipListener implements SipListener {

	Inicializar ini;
	
	public MySipListener() {
		
	}

	@Override
	public void processDialogTerminated(DialogTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processIOException(IOExceptionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processRequest(RequestEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processResponse(ResponseEvent event) {
	     /* try {
	          Response response = event.getResponse();
	          System.out.println("Response received:");
	          System.out.println(response);
	          //if (response.getStatusCode() != 401) return;
	          ClientTransaction tid = event.getClientTransaction();
	         // AccountManagerImpl manager = new AccountManagerImpl();
	          //AuthenticationHelper helper = ini.sipStack.getAuthenticationHelper(manager, header);
	          ClientTransaction transaction = helper.handleChallenge(response, tid, udp, 5);
	          transaction.sendRequest();
	          Request request = transaction.getRequest();
	          System.out.println("Sent request with authentication info:");
	          System.out.println(request);
	        } catch (SipException e) { e.printStackTrace(); }*/
		
	}

	@Override
	public void processTimeout(TimeoutEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/*
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
	*/
}
