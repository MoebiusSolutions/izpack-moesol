package com.izforge.izpack.panels.process;

import java.util.Date;

/**
 * Represents a log of all process panel jobs that have been run. This is
 * implemented as an in-memory cache with a on-disk replica. Updates are
 * immediately written to disk.
 * 
 * @author rkenney
 */
public interface JobProgressLog {

	/**
	 * Logs the start of a job's execution. The disk replica is updated by this
	 * method.
	 */
	public void logJobStarting(String jobName, String softwareVersion);

	/**
	 * Logs the completion of a job's execution. The disk replica is updated by
	 * this method.
	 */
	public void logJobCompleted(String jobName, String softwareVersion);

	
	/**
	 * Returns the time that job was last started. Returns null if no job start
	 * was ever recorded.
	 */
	public Date getLastStartedTime(String jobName);


	/**
	 * Returns the software version that was running when the job was last
	 * started. Returns null if no job start was ever recorded.
	 */
	public String getLastStartedVersion(String jobName);


	/**
	 * Returns the time that job was last completed without error. Returns null
	 * if no job completion was ever recorded.
	 */
	public Date getLastCompletedTime(String jobName);

	/**
	 * Returns the software version that was running when the job was last
	 * completed without error. Returns null if no job completion was ever
	 * recorded.
	 */
	public String getLastCompletedVersion(String jobName);
}
