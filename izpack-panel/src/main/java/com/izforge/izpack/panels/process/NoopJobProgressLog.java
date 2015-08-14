package com.izforge.izpack.panels.process;

import java.util.Date;

/**
 * A no-op implementation of {@link JobProgressLog}
 * 
 * @author rkenney
 */
public class NoopJobProgressLog implements JobProgressLog {

	@Override
	public void logJobStarting(String jobName, String softwareVersion) {}

	@Override
	public void logJobCompleted(String jobName, String softwareVersion) {}

	@Override
	public Date getLastStartedTime(String jobName) {
		return null;
	}

	@Override
	public String getLastStartedVersion(String jobName) {
		return null;
	}

	@Override
	public Date getLastCompletedTime(String jobName) {
		return null;
	}

	@Override
	public String getLastCompletedVersion(String jobName) {
		return null;
	}
}
