package com.izforge.izpack.panels.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.api.adaptator.impl.XMLElementImpl;
import com.izforge.izpack.api.adaptator.impl.XMLParser;
import com.izforge.izpack.api.adaptator.impl.XMLWriter;
import com.izforge.izpack.core.os.DefaultTimeProvider;
import com.izforge.izpack.core.os.TimeProvider;

/**
 * Represents a log of all process panel jobs that have been run. This is
 * implemented as an in-memory cache with a on-disk replica. Updates are
 * immediately written to disk.
 * 
 * @author rkenney
 */
public class JobProgressLogImpl implements JobProgressLog {

	static class LogEntry {
		public String jobName;
		public Date lastStartedTime;
		public String lastStartedVersion;
		public Date lastCompletedTime;
		public String lastCompletedVersion;
	}

	static class Fields {
		public static final String JobProgressLog = JobProgressLogImpl.class.getSimpleName();
		public static final String LogEntry = LogEntry.class.getSimpleName();
		public static final String jobName = "jobName";
		public static final String lastStartedTime = "lastStartedTime";
		public static final String lastStartedVersion = "lastStartedVersion";
		public static final String lastCompletedTime = "lastCompletedTime";
		public static final String lastCompletedVersion = "lastCompletedVersion";
	}

	File logFile;
	private Map<String, LogEntry> logMap = new LinkedHashMap<String, LogEntry>();
	private TimeProvider time;

	public JobProgressLogImpl(File logFile) {
		this(logFile, new DefaultTimeProvider());
	}

	public JobProgressLogImpl(File logFile, TimeProvider timeProvider) {
		this.logFile = logFile;
		this.time = timeProvider;
		loadFromDisk();
	}

	@Override
	public void logJobStarting(String jobName, String softwareVersion) {
		LogEntry entry = getLogEntry(jobName);
		entry.lastStartedTime = time.getNow();
		entry.lastStartedVersion = softwareVersion;
		saveToDisk();
	}

	@Override
	public void logJobCompleted(String jobName, String softwareVersion) {
		LogEntry entry = getLogEntry(jobName);
		entry.lastCompletedTime = time.getNow();
		entry.lastCompletedVersion = softwareVersion;
		saveToDisk();
	}
	
	@Override
	public Date getLastStartedTime(String jobName) {
		LogEntry entry = logMap.get(jobName);
		return entry == null ? null : entry.lastStartedTime;
	}

	@Override
	public String getLastStartedVersion(String jobName) {
		LogEntry entry = logMap.get(jobName);
		return entry == null ? null : entry.lastStartedVersion;
	}

	@Override
	public Date getLastCompletedTime(String jobName) {
		LogEntry entry = logMap.get(jobName);
		return entry == null ? null : entry.lastCompletedTime;
	}

	@Override
	public String getLastCompletedVersion(String jobName) {
		LogEntry entry = logMap.get(jobName);
		return entry == null ? null : entry.lastCompletedVersion;
	}

	private LogEntry getLogEntry(String jobName) {
		LogEntry logEntry = logMap.get(jobName);
		if (logEntry == null) {
			logEntry = new LogEntry();
			logEntry.jobName = jobName;
			logMap.put(jobName, logEntry);
		}
		return logEntry;
	}

	private void saveToDisk() {
		if (!logFile.exists()) {
			createParentDir(logFile);
		}
		XMLElementImpl xmlRoot = new XMLElementImpl(Fields.JobProgressLog);
		for (LogEntry entry : logMap.values()) {
			XMLElementImpl xmlEntry = new XMLElementImpl(Fields.LogEntry);
			addOptionalAttribute(xmlEntry, Fields.jobName, entry.jobName);
			addOptionalAttribute(xmlEntry, Fields.lastStartedTime, entry.lastStartedTime);
			addOptionalAttribute(xmlEntry, Fields.lastStartedVersion, entry.lastStartedVersion);
			addOptionalAttribute(xmlEntry, Fields.lastCompletedTime, entry.lastCompletedTime);
			addOptionalAttribute(xmlEntry, Fields.lastCompletedVersion, entry.lastCompletedVersion);
			xmlRoot.addChild(xmlEntry);
		} 
		writeXml(xmlRoot, logFile);
	}

	void loadFromDisk() {
		logMap = new LinkedHashMap<String, LogEntry>();
		if (!logFile.exists()) {
			return;
		}
		
		IXMLElement xml = parseXml(logFile);
		List<IXMLElement> children = xml.getChildrenNamed(Fields.LogEntry);
		for (IXMLElement child : children) {
			LogEntry logEntry = new LogEntry();
			logEntry.jobName = child.getAttribute(Fields.jobName);
			logEntry.lastStartedTime = getAttributeAsDate(child, Fields.lastStartedTime);
			logEntry.lastStartedVersion = child.getAttribute(Fields.lastStartedVersion);
			logEntry.lastCompletedTime = getAttributeAsDate(child, Fields.lastCompletedTime);
			logEntry.lastCompletedVersion = child.getAttribute(Fields.lastCompletedVersion);
			logMap.put(logEntry.jobName, logEntry);
		}
	}

	private static IXMLElement parseXml(File file) {
		try {
			FileInputStream stream = new FileInputStream(file);
			try {
				XMLParser parser = new XMLParser();
				return parser.parse(stream);
			} finally {
				stream.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(String.format("Failed to parse xml file [%s]", file));
		}
	}

	private static void writeXml(XMLElementImpl xmlRoot, File file) {
		try {
			FileOutputStream stream = new FileOutputStream(file);
			XMLWriter writer = new XMLWriter(stream);
			try {
				writer.write(xmlRoot);
			} finally {
				stream.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(String.format("Failed to write xml file [%s]", file));
		}
	}

	/**
	 * Parses the specified attribute as a UTC-millis timestamp and returns it
	 * as a Date object. Return null if the attribute is not defined.
	 */
	private Date getAttributeAsDate(IXMLElement element, String attributeName) {
		String value = element.getAttribute(attributeName);
		if (value == null || value.length() < 1) {
			return null;
		}
		long millis = Long.parseLong(value);
		return new Date(millis);
	}

	private void addOptionalAttribute(XMLElementImpl xmlElement, String attributeName, String value) {
		if (value == null) {
			return;
		}
		xmlElement.setAttribute(attributeName, value);
	}

	private void addOptionalAttribute(XMLElementImpl xmlElement, String attributeName, Date value) {
		if (value == null) {
			return;
		}
		xmlElement.setAttribute(attributeName, Long.toString(value.getTime()));
	}

	/**
	 * Creates the parent directory of the specified file, throwing a
	 * {@link RuntimeException} if this is not possible. Simply returns
	 * if the parent directory already exists.
	 */
	private static void createParentDir(File file) {
		File dir = file.getParentFile();
		if (dir.exists() && dir.isDirectory()) {
			return;
		}
		if (dir.exists() && !dir.isDirectory()) {
			throw new RuntimeException(String.format("Cannot create parent directories of [%s]", file));
		}
		dir.mkdirs();
		if (!dir.exists() || !dir.isDirectory()) {
			throw new RuntimeException(String.format("Failed to create parent directories of [%s]", file));
		}
	}
}
