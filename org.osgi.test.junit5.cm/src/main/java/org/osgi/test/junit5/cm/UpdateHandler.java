package org.osgi.test.junit5.cm;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.common.dictionary.Dictionaries;

public class UpdateHandler implements ConfigurationListener {

	private Map<String, CountDownLatch>	updateMap	= new HashMap<String, CountDownLatch>();
	private Map<String, CountDownLatch>	deleteMap	= new HashMap<String, CountDownLatch>();

	public boolean update(Configuration configuration, Dictionary<String, Object> dictionary, long timeout)
		throws InterruptedException, IOException {
		String pid = configuration.getPid();
		CountDownLatch latch = createCountdownLatchUpdate(configuration.getPid());
		configuration.update(dictionary);
		boolean ok = latch.await(timeout, TimeUnit.MILLISECONDS);
		return ok;
	}

	public boolean delete(Configuration configuration, long timeout) throws InterruptedException, IOException {
		String pid = configuration.getPid();
		CountDownLatch latch = createCountdownLatchDelete(configuration.getPid());
		configuration.delete();

		boolean ok = latch.await(timeout, TimeUnit.MILLISECONDS);
		return ok;

	}

	private CountDownLatch createCountdownLatchUpdate(String pid) {

		CountDownLatch countDownLatch = new CountDownLatch(1);
		updateMap.put(pid, countDownLatch);
		return countDownLatch;
	}

	private CountDownLatch createCountdownLatchDelete(String pid) {

		CountDownLatch countDownLatch = new CountDownLatch(1);
		deleteMap.put(pid, countDownLatch);
		return countDownLatch;
	}

	@Override
	public void configurationEvent(ConfigurationEvent event) {

		String pid = event.getPid();

		if (event.getType() == ConfigurationEvent.CM_UPDATED) {

			CountDownLatch countDownLatch = updateMap.get(pid);
			if (countDownLatch != null) {
				updateMap.remove(pid);
				countDownLatch.countDown();
			}
		} else if (event.getType() == ConfigurationEvent.CM_DELETED) {

			CountDownLatch countDownLatch = deleteMap.get(pid);
			if (countDownLatch != null) {
				deleteMap.remove(pid);
				countDownLatch.countDown();
			}
		}
	}

	public void checkOldAndUpdate(Configuration configBefore, Configuration configuration,
		Dictionary<String, Object> dictionary) throws InterruptedException, IOException {
		if (configuration != null) {
			if (dictionary != null && !notSet(dictionary)) {
				// has relevant Properties to update
				update(configuration, dictionary, 1000);
			} else if (configBefore == null) {
				// is new created Configuration. must be updated
				update(configuration, Dictionaries.dictionaryOf(), 1000);
			}
		}
	}

	private static boolean notSet(Dictionary<String, Object> dictionary) {
		if (dictionary.size() == 1) {
			if (dictionary.keys()
				.nextElement()
				.equals(WithConfiguration.NOT_SET)) {
				return true;
			}
		}
		return false;
	}

}
