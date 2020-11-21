package org.osgi.test.junit5.listener;

import org.osgi.framework.ServiceEvent;

public interface ServiceDelegate {

	void serviceUnregisterEvent(ServiceEvent event);

	void serviceModifiedEvent(ServiceEvent event);

	void serviceModifiedEndmatchEvent(ServiceEvent event);

	void serviceRegisterEvent(ServiceEvent event);

	default void serviceChanged(ServiceEvent event) {
		switch (event.getType()) {
			case ServiceEvent.REGISTERED :
				serviceRegisterEvent(event);
			case ServiceEvent.MODIFIED :
				serviceModifiedEvent(event);
			case ServiceEvent.MODIFIED_ENDMATCH :
				serviceModifiedEndmatchEvent(event);
			case ServiceEvent.UNREGISTERING :
				serviceUnregisterEvent(event);
			default :
		}
	}

}
