package app.softphone.core.sdp;

public class SdpInfo {
	
	private String IpAddress;
	private int aport;
	private int aformat;

	
	public SdpInfo() {
		IpAddress = "";
		aport = 0;
		aformat = 0;
	}
	
	public String getIpAddress() {
		return IpAddress;
	}
	
	public int getAPort() {
		return aport;
	}
	
	public int getAFormat() {
		return aformat;
	}
	
	public void setIpAddress(String IP) {
		IpAddress = IP;
	}
	
	public void setAPort(int AP) {
		aport = AP;
	}
	
	public void setAFormat(int AF) {
		aformat = AF;
	}

}
