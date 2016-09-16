package xan_code;

import javax.swing.JTextArea;

public class Logger {
	static JTextArea LOG;
	
	public Logger(JTextArea log) {
		LOG = log;
	}
	
	public Logger() {} //This is so we can initialize elsewhere
	
	public static void Append(String txt) {
		LOG.append(txt);
		LOG.setCaretPosition(LOG.getDocument().getLength());
	}
	
	public static void AppendLn(String txt) {
		LOG.append(txt+"\n");
		LOG.setCaretPosition(LOG.getDocument().getLength());
	}
	
	public static void AppendLn() {
		LOG.append("\n");
		LOG.setCaretPosition(LOG.getDocument().getLength());
	}
}
