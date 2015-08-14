package com.izforge.izpack.panels.process;

import static org.junit.Assert.assertEquals;
import jacle.common.lang.JavaUtil;
import jacle.common.time.CommonDateFormats;
import jacle.commontest.JUnitFiles;

import java.io.File;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.izforge.izpack.core.os.TimeProvider;

public class JobProgressLogImplTest {

	JUnitFiles files = new JUnitFiles(JavaUtil.I.getClass());

	@Before
	public void setUp() throws Exception {
		files.before();
	}
	
	@Test
	public void test() throws Exception {
		// Setup
		MockTimeProvider mockTime = new MockTimeProvider();
		File file = files.getFile("my/mock/log/file.txt");
		JobProgressLog log = new JobProgressLogImpl(file, mockTime);
		
		// Execute
		int i = 1;
		mockTime.setTime(toDate("2015-12-11 10:09:%02d", i));
		log.logJobStarting("StartedAndCompleted", "v"+Integer.toString(i++));
		mockTime.setTime(toDate("2015-12-11 10:09:%02d", i));
		log.logJobCompleted("StartedAndCompleted", "v"+Integer.toString(i++)); // The version shouldn't change on completion, but let's verify anyway
		mockTime.setTime(toDate("2015-12-11 10:09:%02d", i));
		log.logJobStarting("StartedOnly", "v"+Integer.toString(i++));
		mockTime.setTime(toDate("2015-12-11 10:09:%02d", i));
		log.logJobCompleted("CompletedOnly", "v"+Integer.toString(i++));
		mockTime.setTime(toDate("2015-12-11 10:09:%02d", i));
		log.logJobStarting("Restarted", "v"+Integer.toString(i++));
		mockTime.setTime(toDate("2015-12-11 10:09:%02d", i));
		log.logJobStarting("Restarted", "v"+Integer.toString(i++));
		mockTime.setTime(toDate("2015-12-11 10:09:%02d", i));
		log.logJobCompleted("Recompleted", "v"+Integer.toString(i++));
		mockTime.setTime(toDate("2015-12-11 10:09:%02d", i));
		log.logJobCompleted("Recompleted", "v"+Integer.toString(i++));
		mockTime.setTime(toDate("2015-12-11 10:09:%02d", i));
		log.logJobStarting("StartedCompletedStarted", "v"+Integer.toString(i++));
		mockTime.setTime(toDate("2015-12-11 10:09:%02d", i));
		log.logJobCompleted("StartedCompletedStarted", "v"+Integer.toString(i++));
		mockTime.setTime(toDate("2015-12-11 10:09:%02d", i));
		log.logJobStarting("StartedCompletedStarted", "v"+Integer.toString(i++));
		
		// Build verification task
		final JobProgressLog logToVerify[] = {null};
		Runnable verifyTask = new Runnable() {
			@Override
			public void run() {
				JobProgressLog log = logToVerify[0];
				// Verify
				String name = "NeverStartedOrCompleted";
				assertEquals((Date) null, log.getLastStartedTime(name));
				assertEquals((Date) null, log.getLastCompletedTime(name));
				assertEquals((String) null, log.getLastStartedVersion(name));
				assertEquals((String) null, log.getLastCompletedVersion(name));
				name = "StartedAndCompleted";
				assertEquals(toDate("2015-12-11 10:09:01"), log.getLastStartedTime(name));
				assertEquals(toDate("2015-12-11 10:09:02"), log.getLastCompletedTime(name));
				assertEquals("v1", log.getLastStartedVersion(name));
				assertEquals("v2", log.getLastCompletedVersion(name));
				name = "StartedOnly";
				assertEquals(toDate("2015-12-11 10:09:03"), log.getLastStartedTime(name));
				assertEquals((Date) null, log.getLastCompletedTime(name));
				assertEquals("v3", log.getLastStartedVersion(name));
				assertEquals((String) null, log.getLastCompletedVersion(name));
				name = "CompletedOnly";
				assertEquals((Date) null, log.getLastStartedTime(name));
				assertEquals(toDate("2015-12-11 10:09:04"), log.getLastCompletedTime(name));
				assertEquals((String) null, log.getLastStartedVersion(name));
				assertEquals("v4", log.getLastCompletedVersion(name));
				name = "Restarted";
				assertEquals(toDate("2015-12-11 10:09:06"), log.getLastStartedTime(name));
				assertEquals((Date) null, log.getLastCompletedTime(name));
				assertEquals("v6", log.getLastStartedVersion(name));
				assertEquals((String) null, log.getLastCompletedVersion(name));
				name = "Recompleted";
				assertEquals((Date) null, log.getLastStartedTime(name));
				assertEquals(toDate("2015-12-11 10:09:08"), log.getLastCompletedTime(name));
				assertEquals((String) null, log.getLastStartedVersion(name));
				assertEquals("v8", log.getLastCompletedVersion(name));
				name = "StartedCompletedStarted";
				assertEquals(toDate("2015-12-11 10:09:11"), log.getLastStartedTime(name));
				assertEquals(toDate("2015-12-11 10:09:10"), log.getLastCompletedTime(name));
				assertEquals("v11", log.getLastStartedVersion(name));
				assertEquals("v10", log.getLastCompletedVersion(name));
			}
		};
		
		// Run verification against in-memory cache
		logToVerify[0] = log;
		verifyTask.run();

		// Run verification against disk copy
		logToVerify[0] = new JobProgressLogImpl(file);
		verifyTask.run();
	}

	private static class MockTimeProvider implements TimeProvider {

		private Date time;

		public MockTimeProvider() {
			setTime(new Date());
		}
		
		public void setTime(Date time) {
			this.time = time;
		}
		
		@Override
		public Date getNow() {
			return this.time;
		}
		
	}
	
	private Date toDate(String format) {
		return CommonDateFormats.parseFromSecs(format);
	}

	private Date toDate(String format, Object... args) {
		return CommonDateFormats.parseFromSecs(String.format(format, args));
	}
}