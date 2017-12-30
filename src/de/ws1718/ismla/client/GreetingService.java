package de.ws1718.ismla.client;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.ws1718.ismla.shared.TransliterationConfigs;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	TransliterationConfigs greetServer(String name) throws IllegalArgumentException;
	HashMap<String, String> getAccentMap(String path);
	List<String> tokenize(String input);
	HashMap<String, String> getRuToLatinMap(String path);
}
