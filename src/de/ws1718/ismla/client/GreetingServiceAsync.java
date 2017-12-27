package de.ws1718.ismla.client;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void getAccentMap(String path, AsyncCallback<HashMap<String, String>> callback);

	void tokenize(String input, AsyncCallback<List<String>> callback);
}
