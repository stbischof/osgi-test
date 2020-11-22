package org.osgi.test.junit5.event;

import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public interface ServiceListenerDelegate extends ServiceListener {

	void serviceUnregisterEvent(ServiceEvent event);

	void serviceModifiedEvent(ServiceEvent event);

	void serviceModifiedEndmatchEvent(ServiceEvent event);

	void serviceRegisterEvent(ServiceEvent event);

	@Override
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
