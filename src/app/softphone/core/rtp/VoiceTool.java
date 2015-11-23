package app.softphone.core.rtp;

import java.net.InetAddress;
import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Codec;
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

import com.ibm.media.codec.audio.gsm.JavaEncoder;
import com.ibm.media.codec.audio.gsm.Packetizer;
import com.ibm.media.codec.audio.rc.RCModule;
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
			AudioFormat df = new AudioFormat(AudioFormat.LINEAR,8000,16,1);
			Vector devices = CaptureDeviceManager.getDeviceList(df);
			//SOLO HAY UNO CON ESTAS CARACTERISTICAS JAVASOUND
			//COGE LOS DATOS DEL jmf.properties -> IMPOSIBLE PORTABILIDAD POR EL MOMENTO.
			CaptureDeviceInfo di = (CaptureDeviceInfo) devices.elementAt(0);
			DataSource daso = Manager.createDataSource(di.getLocator());
			myProcessor = Manager.createProcessor(daso);
			myProcessor.configure();
			//ESPERAR 10 ms A QUE SE CONFIGURE EL PROCESSOR.
			while (myProcessor.getState() != Processor.Configured) {
				Thread.sleep(10);
			}
			myProcessor.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW_RTP));
			TrackControl track[] = myProcessor.getTrackControls();
			switch (fmt) {
				case 3: af = new AudioFormat(AudioFormat.GSM_RTP,8000,16,1);
				case 4: af = new AudioFormat(AudioFormat.G723_RTP,8000,16,1);
			}
			//Siempre coge formato GSM.
			af = new AudioFormat(AudioFormat.GSM_RTP,8000,16,1);
			System.out.println("0");
			track[0].setFormat(af);
			
			// PERMITE CAMBIAR EL TAMAÑO DE LOS PAQUETES RTP PARA EVITAR EL DELAY ENTRE EMISOR Y RECEPTOR. ¡¡SOLO EN LA PRIMERA LLAMADA!!. DESPUÉS EL DELAY ENTRE EMISOR Y RECEPTOR ES DE MAS O MENOS 1s
			RCModule rcm = new RCModule();
			JavaEncoder je = new JavaEncoder();
			Packetizer p = new Packetizer();
			p.setPacketSize(33);
			Codec[] c = {rcm,je,p};
			track[0].setCodecChain(c);
			// 
			
			System.out.println("1");
			myProcessor.realize();
			System.out.println("2");
			// NO SALIR DEL BUCLE HASTA QUE PROCESSOR ESTE EN EL ESTADO REALIZED.
			while(myProcessor.getState() != Processor.Realized) {
				System.out.println("Bucle");
			}

			System.out.println("3");
			DataSource ds = myProcessor.getDataOutput();
			System.out.println("4");
			myVoiceSessionManager = new RTPSessionMgr();
			myVoiceSessionManager.addReceiveStreamListener(this);
			System.out.println("5");
			SessionAddress senderAddr = new SessionAddress();
			myVoiceSessionManager.initSession(senderAddr, null, 0.05, 0.25);
			System.out.println("6");
			InetAddress destAddr = InetAddress.getByName(peerIP);
			SessionAddress localAddr = new SessionAddress(InetAddress.getByName(myIP), recvPort, InetAddress.getByName(myIP), recvPort+1);
			System.out.println("7");
			SessionAddress remoteAddr = new SessionAddress(destAddr, peerPort, destAddr, peerPort+1);
			
			//METODO QUE PROVOCA EL DELAY DE 5s DESDE QUE SE ESTABLECE LA LLAMADA HASTA QUE SOFTPHONE EMPIEZA A ENVIAR PAQUETES RTP. ¡¡ENCONTRAR SOLUCION!!
			myVoiceSessionManager.startSession(localAddr, localAddr, remoteAddr, null);
			
			System.out.println("8");
			ss = myVoiceSessionManager.createSendStream(ds, 0);
			System.out.println("9");
			ss.start();
			System.out.println("10");
			myProcessor.start();
			System.out.println("11");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void stopMedia() {
		
		try {
			/*player.stop();
			player.deallocate();
			player.close();*/
			
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
