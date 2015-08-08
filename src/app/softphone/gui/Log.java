package app.softphone.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import app.softphone.core.log.LogAppender;

@SuppressWarnings("serial")
public class Log extends JFrame{
	
	JTextArea textArea;
	JButton cerrar,vaciarLog;
	JPanel panelText, panelBotones;
	JScrollPane scroll;
	
	
	public Log() {
		setTitle("Log");
		setSize(1000,600);
		crearTextArea();
		LogAppender.logTextArea = textArea;
		crearBotones();
		add(panelText,BorderLayout.CENTER);
		add(panelBotones,BorderLayout.SOUTH);
	}

	
	public void crearTextArea() {
		panelText = new JPanel(new BorderLayout());
		textArea = new JTextArea(900,500);
		textArea.setEditable(false);
		scroll = new JScrollPane(textArea);
		textArea.setFont(new java.awt.Font("Consolas", 0, 11));
		panelText.add(scroll);
	}
	
	public void crearBotones() {
		panelBotones = new JPanel(new FlowLayout());
		vaciarLog = new JButton();
		vaciarLog.setText("Vaciar Log");
		panelBotones.add(vaciarLog);
		ActionListener vaciarListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		};
		vaciarLog.addActionListener(vaciarListener);
		cerrar = new JButton();
		cerrar.setText("Cerrar");
		panelBotones.add(cerrar);
		ActionListener cerrarListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		};
		cerrar.addActionListener(cerrarListener);
		
	}
	
	public JTextArea getTextArea() {
		return this.textArea;
	}
	
}
