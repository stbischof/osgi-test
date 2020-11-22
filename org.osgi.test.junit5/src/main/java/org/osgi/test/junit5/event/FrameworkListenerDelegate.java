package org.osgi.test.junit5.event;

import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

public interface FrameworkListenerDelegate extends FrameworkListener {

	void frameworkWarning(FrameworkEvent event);

	void frameworkWaitTimeout(FrameworkEvent event);

	void frameworkStoppedUpdate(FrameworkEvent event);

	void frameworkSpoppedBootclassPathModified(FrameworkEvent event);

	void frameworkStopped(FrameworkEvent event);

	void frameworkStartlevelChanged(FrameworkEvent event);

	void frameworkStarted(FrameworkEvent event);

	void frameworkPackagesRefreshed(FrameworkEvent event);

	void frameworkInfo(FrameworkEvent event);

	void frameworkError(FrameworkEvent event);

	@Override
	default void frameworkEvent(FrameworkEvent event) {
		switch (event.getType()) {
			case FrameworkEvent.ERROR :
				frameworkError(event);
			case FrameworkEvent.INFO :
				frameworkInfo(event);
			case FrameworkEvent.PACKAGES_REFRESHED :
				frameworkPackagesRefreshed(event);
			case FrameworkEvent.STARTED :
				frameworkStarted(event);
			case FrameworkEvent.STARTLEVEL_CHANGED :
				frameworkStartlevelChanged(event);
			case FrameworkEvent.STOPPED :
				frameworkStopped(event);
			case FrameworkEvent.STOPPED_BOOTCLASSPATH_MODIFIED :
				frameworkSpoppedBootclassPathModified(event);
			case FrameworkEvent.STOPPED_UPDATE :
				frameworkStoppedUpdate(event);
			case FrameworkEvent.WAIT_TIMEDOUT :
				frameworkWaitTimeout(event);
			case FrameworkEvent.WARNING :
				frameworkWarning(event);
			default :
		}
	}

}
