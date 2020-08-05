package org.osgi.test.common.annotation.config;

public enum CmAction {
	/*
	 * Do not check if Configuration exist, create a new Configuration.
	 */
	CREATE,
	/*
	 * Get the Configuration from ConfigAdmin if exist, or create an
	 * Configuration.
	 */
	GET_OR_CREATE
}
