package de.ws1718.ismla.client;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.ws1718.ismla.shared.TransliterationConfigs;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String name, AsyncCallback<TransliterationConfigs> callback);

	void getAccentMap(String path, AsyncCallback<HashMap<String, String>> callback);

	void tokenize(String input, AsyncCallback<List<String>> callback);

	void getRuToLatinMap(String path, AsyncCallback<HashMap<String, String>> callback);
}
