package app.softphone.core.rtp;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.protocol.DataSource;

public class TonesTool implements ControllerListener {
	
	private Player player = null;
	private DataSource dsource;
	private Boolean end = true;

	
	
	public synchronized void controllerUpdate(ControllerEvent cEvent) {
		
		if (cEvent instanceof EndOfMediaEvent) {
			if (!end) {
				player.start();
			}
		}
	}

	public void prepareTone(String filename) {
		
		try {
			MediaLocator ml = new MediaLocator(filename);
			dsource = Manager.createDataSource(ml);
			player = Manager.createPlayer(dsource);
			player.addControllerListener(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void playTone() {
		
		try {
			end = false;
			player.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void stopTone() {
		
		try {
			
		} catch(Exception e) {
			end = true;
			notify();
			player.stop();
		}
	}
		
}
