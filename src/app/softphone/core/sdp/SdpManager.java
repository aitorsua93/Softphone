package app.softphone.core.sdp;

import java.util.Date;
import java.util.Vector;

import javax.sdp.*;

public class SdpManager {

	SdpFactory mySdpFactory;
	SdpInfo mySdpInfo;
	byte[] mySdpContent;
	
	public SdpManager() {
		mySdpFactory = SdpFactory.getInstance();
	}
	
	public byte[] createSdp(SdpInfo sdpInfo) {
		
		try {
			//Session Level description lines
			Version myVersion = mySdpFactory.createVersion(0);
			long ss = SdpFactory.getNtpTime(new Date());
			Origin myOrigin = mySdpFactory.createOrigin("-",ss,ss,"IN","IP4",sdpInfo.getIpAddress());
			SessionName mySessionName = mySdpFactory.createSessionName("-");
			Connection myConnection = mySdpFactory.createConnection("IN","IP4",sdpInfo.getIpAddress());
			
			//Time description lines
			Time myTime = mySdpFactory.createTime();
			Vector<Time> myTimeVector = new Vector<Time>();
			myTimeVector.add(myTime);
			
			//Media description lines
			int[] aaf = new int[1];
			aaf[0] = sdpInfo.getAFormat();
			MediaDescription myAudioDescription = mySdpFactory.createMediaDescription("audio", sdpInfo.getAPort(), 1, "RTP/AVP", aaf);
			Vector<MediaDescription> myMediaDescriptionVector = new Vector<MediaDescription>();
			myMediaDescriptionVector.add(myAudioDescription);
			
			SessionDescription mySdp = mySdpFactory.createSessionDescription();
			
			mySdp.setVersion(myVersion);
			mySdp.setOrigin(myOrigin);
			mySdp.setSessionName(mySessionName);
			mySdp.setConnection(myConnection);
			mySdp.setTimeDescriptions(myTimeVector);
			mySdp.setMediaDescriptions(myMediaDescriptionVector);
			
			mySdpContent = mySdp.toString().getBytes();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return mySdpContent;
	}
	
	
	public SdpInfo getSdp(byte[] content) {
		
		try {
			String s = new String(content);
			SessionDescription recSdp = mySdpFactory.createSessionDescription(s);
			
			String myPeerIp = recSdp.getConnection().getAddress();
			
			String myPeerName = recSdp.getOrigin().getUsername();
			Vector recMediaDescriptionVector = recSdp.getMediaDescriptions(false);
			
				MediaDescription myAudioDescription = (MediaDescription) recMediaDescriptionVector.elementAt(0);
				Media myAudio = myAudioDescription.getMedia();
				int myAudioPort = myAudio.getMediaPort();
				Vector audioFormats = myAudio.getMediaFormats(false);
				
				int myAudioMediaFormat = Integer.parseInt(audioFormats.elementAt(0).toString());
				
			mySdpInfo = new SdpInfo();
			
			mySdpInfo.setIpAddress(myPeerIp);
			mySdpInfo.setAPort(myAudioPort);
			mySdpInfo.setAFormat(myAudioMediaFormat);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return mySdpInfo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
