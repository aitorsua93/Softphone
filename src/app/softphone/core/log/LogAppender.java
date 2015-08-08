package app.softphone.core.log;

import javax.swing.JTextArea;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

public class LogAppender extends WriterAppender {
	
	public static JTextArea logTextArea; 
	
    @Override
    public void append(LoggingEvent loggingEvent){

        final String logMessage = this.layout.format(loggingEvent);

        logTextArea.append(logMessage);
        
        logTextArea.setCaretPosition(logTextArea.getText().length() - 1);
    }
	
}
