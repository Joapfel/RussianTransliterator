package de.ws1718.ismla.server;

import de.ws1718.ismla.client.GreetingService;
import de.ws1718.ismla.shared.FieldVerifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	
	
	/**
	 * 
	 * @param text
	 * @return
	 * source: https://www.drillio.com/en/2011/java-remove-accent-diacritic/
	 */
	public static String removeAccents(String text) {
	    return text == null ? null :
	        Normalizer.normalize(text, Form.NFD)
	            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
	
	/**
	 * reads in the accented forms and returns a map of the form
	 * non-accented -> accented
	 */
	public HashMap<String, String> getAccentMap(String path){
		
		HashMap<String, String> rval;
		
		InputStream resourceStream = getServletContext().getResourceAsStream(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));

		// read file into map
		String originalToken;
		
		try {
			
			rval = new HashMap<>();
			
			while ((originalToken = reader.readLine()) != null) {
				String removedAccent = removeAccents(originalToken);
				rval.put(removedAccent, originalToken);
			}
			
			return rval;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new HashMap<>();
	}
	
	
	
	
	
	@Override
	public List<String> tokenize(String input) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	public String greetServer(String input) throws IllegalArgumentException {

		
		/**
		 * 
		 * map anschauen
		 * 
		 * 
		 */
		HashMap<String, String> nonAccToAccented = getAccentMap("/ru-all-forms.txt");
		
		for(String key : nonAccToAccented.keySet()){
			System.out.println(key + " -> " + nonAccToAccented.get(key));
		}
		
		
		
		
		/**
		 * 
		 * 
		 * tokenizer testen
		 * 
		 * 
		 */
		String ru = "Качество энциклопедии в целом измерить непросто. "
				+ "Одним из относительных показателей развитости отдельных языковых разделов, "
				+ "который было предложено использовать ещё в 2006 году, "
				+ "является так называемая «глубина». При расчёте «глубины» принимается во внимание соотношение "
				+ "между служебными страницами[5] "
				+ "и статьями в общем количестве страниц языкового раздела, а также среднее количество правок на каждую статью. "
				+ "Раздел Википедии на русском языке обладает наибольшей «глубиной» среди всех славянских разделов,"
				+ " имеющих более 100 тысяч статей, и находится по этому показателю на 12-м месте среди 52 крупнейших "
				+ "языковых разделов, имеющих более 100 тысяч статей.";
		
		
		List<String> russianTokenized = tokenize(ru);
		
		for(String token : russianTokenized){
//			System.out.println(token);
		}
		
		
		
		
		/**
		 * 
		 * 
		 * mit accent zusammenbauen
		 * 
		 * 
		 */
		StringBuilder sb = new StringBuilder();
		for(String token : russianTokenized){
			token = token.toLowerCase();
			if(nonAccToAccented.containsKey(token)){
				String tokenWithAccent = nonAccToAccented.get(token);
				sb.append(tokenWithAccent + " ");
			}else{
				sb.append(token + " ");
			}
		}
		
		
		
		
		
		
		
		
		
		
		

		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back
			// to
			// the client.
			throw new IllegalArgumentException("Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script
		// vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>"
				+ userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

}
