package com.enonic.autotests.logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.testng.Reporter;


public class Logger implements ILogger {

	private static volatile Logger instance;
	
	private final int defaultLogLevel = Severity.INFO;

	protected static java.util.logging.Logger perfLogger;

	protected static Logger defaultLogger = new Logger();
	
	protected int severity;
	
	public static Logger getInstance() {
		if (instance == null) {
			synchronized (Logger.class) {
				if (instance == null) {
					instance = new Logger();
					instance.setSeverity(5);
				}

			}
		}
		return instance;
	}

	public class Entry implements IEntry {
		
		protected int severity;

		protected String message;

		protected Throwable exception;

		public Entry(int severity, String message, Throwable exception) {
			this.severity = severity;
			this.message = message;
			this.exception = exception;
		}

		public int getSeverity() {
			return severity;
		}

		public String getMessage() {
			return message;
		}

		public Throwable getException() {
			return exception;
		}

		public String toString() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
			Date time = Calendar.getInstance().getTime();
			StringBuffer buffer = new StringBuffer(dateFormat.format(time));
			
			buffer.append(severityToString(severity)).append(": ");

			if (message != null) {
				buffer.append(message);
			}

			if (exception != null) {
				if (message != null) {
					buffer.append(", ");
				}
				buffer.append(exception);
			}

			return buffer.toString();
		}
	}
	
	public class PerfFormatter extends java.util.logging.Formatter {
	    private String lineSeparator = (String) java.security.AccessController.doPrivileged(
	               new sun.security.action.GetPropertyAction("line.separator"));
		@Override
		public String format(java.util.logging.LogRecord record) {
			String message = formatMessage(record);
			SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
			Date time = Calendar.getInstance().getTime();
			StringBuffer sb = new StringBuffer(dateFormat.format(time));
			sb.append(record.getLevel().getLocalizedName());
			sb.append(": ");
			sb.append(message);
			sb.append(lineSeparator);
			return sb.toString();
		}
	}

	/*
	 * 
	 */
	public static String severityToString(int severity) {
		switch (severity) {
		case Severity.ERROR:
			return "ERROR";
		case Severity.WARNING:
			return "WARNING";
		case Severity.INFO:
			return "INFO";
		case Severity.PERFOMANCE:
			return "PERFOMANCE";
		case Severity.DEBUG:
			return "DEBUG";
		case Severity.ALL:
			return "ALL";
		}

		return null;
	}

	public static final String stackTrace(Throwable ex) {
		ByteArrayOutputStream ostr = new ByteArrayOutputStream();
		ex.printStackTrace(new PrintWriter(ostr, true));
		String text = ostr.toString();

		return text;
	}

	public static Logger getDefault() {
		return Logger.defaultLogger;
	}

	private Logger() {
		this.severity = defaultLogLevel;
		try {
			perfLogger = java.util.logging.Logger.getLogger("Perfomance");
			java.util.logging.FileHandler perfHandler = new java.util.logging.FileHandler("performance%u.output", true);
			perfHandler.setFormatter(new PerfFormatter());
			perfLogger.addHandler(perfHandler);
			
			perfLogger.setUseParentHandlers(false);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void log(IEntry entry) {		
		Reporter.log(entry.toString());
		System.out.println(entry.toString());
	}

	public void log(int severity, String message, Throwable exception) {
		if (this.severity < severity)
			return;
		
		if (severity == Severity.EXCEPTION){
			//Assert.fail(message+" SNAPSHOT: "+ TestUtils.saveScreenshot(screenshotFileName, driver));
		}
		else if (severity == Severity.ERROR){
			//log(new Entry(severity, message+" SNAPSHOT: "+  TestUtils.captureSnapshot(), exception));
		}else
			log(new Entry(severity, message, exception));
	}

	public void log(int severity, String message) {
		log(severity, message, null);
	}

	public void log(int severity, Throwable exception) {
		log(severity, null, exception);
	}

	public void error(String message, Throwable exception) {
		log(Severity.ERROR, message, exception);
	}

	public void warning(String message, Throwable exception) {
		log(Severity.WARNING, message, exception);
	}

	public void info(String message, Throwable exception) {
		log(Severity.INFO, message, exception);
	}
		
	private class PerformanceLevel extends Level{
		private static final long serialVersionUID = 1L;

		protected PerformanceLevel() {
			super("PERFORMANCE",1488);
		}		
	}
	private PerformanceLevel oLevel = new PerformanceLevel();
	
	public void perfomance(String message, long startTime, Throwable exception) {
		double time = System.currentTimeMillis() - startTime;
		log(Severity.PERFOMANCE, time/1000 + ", Sec takes to " + message, exception);
		perfLogger.log(oLevel, time/1000 + ", Sec takes to " + message);
	}

	public void perfomance(String message, Throwable exception) {
		log(Severity.PERFOMANCE, message, exception);
		perfLogger.log(oLevel, message);
	}

	public void debug(String message, Throwable exception) {
		log(Severity.DEBUG, message, exception);
	}

	public void error(String message) {
		error(message, null);
	}

	public void warning(String message) {
		warning(message, null);
	}

	public void info(String message) {
		info(message, null);
	}

	public void perfomance(String message, long startTime) {
		perfomance(message, startTime, null);
	}

	public void perfomance(String message) {
		perfomance(message, null);
	}

	public void debug(String message) {
		debug(message, null);
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public void exception(String message, Throwable exception){
		log(Severity.EXCEPTION, message, exception);
	}
	
	public void exception(String message){
		log(Severity.EXCEPTION, message, null);
	}

}
