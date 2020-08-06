/*
 * Copyright (c) OSGi Alliance (2020). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osgi.test.junit5.cm;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class ConfigAdminUtil {

	public static Configuration getConfigsByServicePid(ConfigurationAdmin ca, String pid) throws Exception {
		return getConfigsByServicePid(ca, pid, 0l);
	}

	public static Configuration getConfigsByServicePid(ConfigurationAdmin ca, String pid, long timeout)
		throws Exception {
		return getConfigsByPid(ca, Constants.SERVICE_PID, pid, timeout);
	}

	public static Configuration getConfigsByFactoryServicePid(ConfigurationAdmin ca, String pid, String name,
		long timeout) throws Exception {
		return getConfigsByPid(ca, Constants.SERVICE_PID, pid + "~" + name, timeout);
	}

	public static Configuration getConfigsByFactoryServicePid(ConfigurationAdmin ca, String pid, String name)
		throws Exception {
		return getConfigsByFactoryServicePid(ca, pid, name, 0l);
	}

	private static Configuration getConfigsByPid(ConfigurationAdmin ca, String pid_key, String value, long timeout)
		throws Exception {
		String filter = String.format("(%s=%s)", pid_key, value);
		Configuration[] configurations = null;
		final Instant endTime = Instant.now()
			.plusMillis(timeout);
		do {

			configurations = ca.listConfigurations(filter);
			Thread.sleep(50l);
		} while (configurations == null && !endTime.isBefore(Instant.now()));

		if (configurations == null || configurations.length == 0) {
			return null;
		} else {
			return configurations[0];
		}
	}

	static List<Configuration> getAllConfigurations(ConfigurationAdmin ca) throws IOException, InvalidSyntaxException {

		Configuration[] cs = ca.listConfigurations(null);
		if (cs == null) {
			return Collections.EMPTY_LIST;
		}

		return Stream.of(cs)
			.collect(Collectors.toList());
	}

	static List<ConfigurationCopy> cloneConfigurations(List<Configuration> configurations) {
		return configurations.stream()
			.map((c) -> {
				return ConfigurationCopy.of(c);
			})
			.collect(Collectors.toList());

	}

	static void resetConfig(UpdateHandler timeoutListener, ConfigurationAdmin ca, List<ConfigurationCopy> copys)
		throws Exception {

		List<ConfigurationCopy> leftOvers = new ArrayList<ConfigurationCopy>(copys);
		List<Configuration> configurations = ConfigAdminUtil.getAllConfigurations(ca);

		configurations.stream()
			.forEach((conf) -> {
				boolean match = copys.stream()
					.anyMatch((copy) -> {
							if (Objects.equals(conf.getPid(), copy.getPid())) {
							try {
										timeoutListener.update(conf, copy.getProperties(), 3000);
							} catch (IOException e) {
								throw new UncheckedIOException(e);
									} catch (InterruptedException e) {
										throw new RuntimeException(e);
							}
							leftOvers.remove(copy);
							return true;
						} else {
							return false;
						}
					});
				try {
						if (!match) {
							String pid = conf.getPid();
							timeoutListener.delete(conf, 3000);

						}
				} catch (IOException e) {
					throw new UncheckedIOException(e);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
				}
			});
		leftOvers.stream()
			.forEach((copy) -> {
				try {
					Configuration conf = null;
					if (copy.getFactoryPid() != null) {
							String name = copy.getPid()
								.substring(copy.getFactoryPid()
									.length() + 1);
							conf = ca.getFactoryConfiguration(copy.getFactoryPid(), name, copy.getBundleLocation());
					} else {
							conf = ca.getConfiguration(copy.getPid(), copy.getBundleLocation());
						}

						try {
							timeoutListener.update(conf, copy.getProperties(), 3000);

						} catch (IOException e) {
							throw new UncheckedIOException(e);
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
					}

				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			});

			// TODO: wait for async activating/deactivating of Services
		}

}
