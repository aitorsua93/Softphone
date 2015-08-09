package app.softphone.core.rtp;

import java.net.InetAddress;
import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.Player;
import javax.media.Processor;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;

import com.sun.media.rtp.RTPSessionMgr;

public class VoiceTool implements ReceiveStreamListener {

	private RTPSessionMgr myVoiceSessionManager = null;
	private Processor myProcessor = null;
	private SendStream ss = null;
	private ReceiveStream rs = null;
	private Player player = null;
	private AudioFormat af = null;
	
	
	public void startMedia(String peerIP, int peerPort, int recvPort, int fmt, String myIP) {
		
		try {
			
			AudioFormat df = new AudioFormat(AudioFormat.LINEAR,8000,8,1);
			Vector devices = CaptureDeviceManager.getDeviceList(df);
			CaptureDeviceInfo di = (CaptureDeviceInfo) devices.elementAt(0);
			DataSource daso = Manager.createDataSource(di.getLocator());
			
			myProcessor = Manager.createProcessor(daso);
			myProcessor.configure();
			while (myProcessor.getState() != Processor.Configured) {
				Thread.sleep(10);
			}
			myProcessor.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW_RTP));
			TrackControl track[] = myProcessor.getTrackControls();
			switch (fmt) {
				case 3: af = new AudioFormat(AudioFormat.GSM_RTP,8000,4,1);
				case 4: af = new AudioFormat(AudioFormat.G723_RTP,8000,4,1);
			}
			track[0].setFormat(af);
			myProcessor.realize();
			while(myProcessor.getState() != Processor.Realized) {}
			DataSource ds = myProcessor.getDataOutput();
			
			myVoiceSessionManager = new RTPSessionMgr();
			
			myVoiceSessionManager.addReceiveStreamListener(this);
			
			SessionAddress senderAddr = new SessionAddress();
			myVoiceSessionManager.initSession(senderAddr, null, 0.05, 0.25);
			
			InetAddress destAddr = InetAddress.getByName(peerIP);
			SessionAddress localAddr = new SessionAddress(InetAddress.getByName(myIP), recvPort, InetAddress.getByName(myIP), recvPort+1);
			SessionAddress remoteAddr = new SessionAddress(destAddr, peerPort, destAddr, peerPort+1);
			myVoiceSessionManager.startSession(localAddr, localAddr, remoteAddr, null);
			
			ss = myVoiceSessionManager.createSendStream(ds, 0);
			
			ss.start();
			myProcessor.start();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void stopMedia() {
		
		try {
			player.stop();
			player.deallocate();
			player.close();
			
			ss.stop();
			
			myProcessor.stop();
			myProcessor.deallocate();
			myProcessor.close();
			
			myVoiceSessionManager.closeSession("terminated");
			myVoiceSessionManager.dispose();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void update(ReceiveStreamEvent event) {
		
		if(event instanceof NewReceiveStreamEvent) {
			rs = event.getReceiveStream();
			DataSource myDS = rs.getDataSource();
			try {
				player = Manager.createRealizedPlayer(myDS);
			} catch(Exception e) {
				e.printStackTrace();
			}
			player.start();
		}
		
	}

}
