package de.ws1718.ismla.server;

import de.ws1718.ismla.client.GreetingService;
import de.ws1718.ismla.shared.TransliterationConfigs;

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
	public HashMap<String, String> getRuToLatinMap(String path) {
		// TODO Auto-generated method stub
		HashMap<String, String> rval;
		
		InputStream resourceStream = getServletContext().getResourceAsStream(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));
		
		String line;
		try {
			
			rval = new HashMap<>();
			while ((line = reader.readLine()) != null && !line.isEmpty()) {
				
				String[] ruLatinTable = line.split(" ");
	
				String ru = ruLatinTable[0];
				String latin = ruLatinTable[1];
				
				if(!rval.containsKey(ru)){
					rval.put(ru, latin);
				}
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
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < input.length(); i++){
			
			String curChar = input.charAt(i) + "";
			
			if(!Character.isLetter(input.charAt(i)) && !curChar.equals("\u0301")){
				sb.append(" " + input.charAt(i) + " ");
			}else{
				sb.append(input.charAt(i));
			}
		}
		
		List<String> rval = new ArrayList<>();
		String[] arr = sb.toString().split("\\s+");
		for(String s : arr){
			rval.add(s);
		}
		return rval;
	}
	
	
//	@Override
//	public List<String> tokenize(String input) {
//		// TODO Auto-generated method stub
//		List<String> rval = new ArrayList<>();
//		String[] arr = input.split("\\s+");
//		for(String s : arr){
//			StringBuilder sb = new StringBuilder();
//			for(int i = 0; i < s.length(); i++){
//				//non alphabetic
//				if(!Character.isLetter(s.charAt(i))){
//					rval.add(s.charAt(i) + "");
//				}else{
//					sb.append(s.charAt(i));
//				}
//			}
////			rval.add(s);
//			rval.add(sb.toString());
//		}
//		return rval;
//	}
	
	// \u0301

	public TransliterationConfigs greetServer(String input) throws IllegalArgumentException {

		TransliterationConfigs rval;
		
		HashMap<String, String> ruLatinTable = getRuToLatinMap("/ru-latin.txt");

		HashMap<String, String> nonAccToAccented = getAccentMap("/ru-all-forms.txt");		
		
		List<String> russianTokenized = tokenize(input);

		
		//build with accent
		List<String> russianAccented = new ArrayList<>();
		
		for(String token : russianTokenized){
			token = token.toLowerCase();
			if(nonAccToAccented.containsKey(token)){
				String tokenWithAccent = nonAccToAccented.get(token);
				russianAccented.add(tokenWithAccent);
			}else{
				russianAccented.add(token);
			}
		}
		
	
		
		//transliterate
		List<String> transliterated = new ArrayList<>();
		
		for(String russianToken : russianAccented){
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < russianToken.length(); i++){
				String c = russianToken.charAt(i) + "";
				if(ruLatinTable.containsKey(c)){
					sb.append(ruLatinTable.get(c));
					
					//ё needs to be accented 
					if(c.equals("ё")){
						sb.append("\u0301");
					}
					
				}else{
//					sb.append("#");
					sb.append(c);
				}
			}
			
			transliterated.add(sb.toString());
		}
		
		
		
		
		List<String> adaptedPronounciation = new ArrayList<>();
		
		//adapt pronounciation
		for(String s : transliterated){
			
			if(s.contains("\u0301")){
				
				int stressSize = 0;
				
				if(s.matches(".*j.\u0301.*")){
					stressSize = 3;
				}else{
					stressSize = 2;
				}
				
				String arr[] = s.split("j?.\u0301");
				
				String beforeStress = "";
				String afterStress = "";
				
				if(arr.length == 1) {
					beforeStress = arr[0];
				}else if(arr.length == 2){
					beforeStress = arr[0];
					afterStress = arr[1];
				}
				else continue;
				
				
				
				//je becomes ji in any syllable before the stress
				if(beforeStress.contains("je")){
					beforeStress.replaceAll("je", "ji");
				}
				
				//o becomes a immediately before the stress, and ə otherwise
				boolean firstVowel = true;
				String tmpBeforeStress = "";
				for(int i = beforeStress.length()-1; i >= 0; i--){
					String cur = beforeStress.charAt(i) + "";
					
					if(firstVowel){
						
						switch (cur) {
						case "a": firstVowel = false;
							break;
						case "e": firstVowel = false;
							break;
						case "i": firstVowel = false;
							break;
						case "o": firstVowel = false;
							StringBuilder sb = new StringBuilder();
							sb.append(beforeStress.substring(0, i));
							sb.append("a");
							sb.append(beforeStress.substring(i+1, beforeStress.length()));
							tmpBeforeStress = sb.toString();
							break;
						case "u": firstVowel = false;
							break;

						default:
							break;
							
						}
					}
				}
				if(tmpBeforeStress != null && tmpBeforeStress.length() > 0){
					beforeStress = tmpBeforeStress;
				}
				beforeStress = beforeStress.replaceAll("o", "ə");
				afterStress = afterStress.replaceAll("o", "ə");
				
				//ja becomes jə at the end of a word, and ji otherwise
				if(afterStress.endsWith("ja")){
					StringBuilder sb = new StringBuilder();
					sb.append(afterStress.substring(0, afterStress.length()-2));
					sb.append("jə");
					
					afterStress = sb.toString();
				}
				beforeStress = beforeStress.replaceAll("ja", "ji");
				afterStress = afterStress.replaceAll("ja", "ji");
				
				//a becomes ə at the end of a word
				if(afterStress.endsWith("a")){
					StringBuilder sb = new StringBuilder();
					sb.append(afterStress.substring(0, afterStress.length()-1));
					sb.append("ə");
					
					afterStress = sb.toString();
				}
				
				String stress = "#";
				if(!afterStress.equals("") && afterStress != null){
					
					stress = s.substring(beforeStress.length(), beforeStress.length() + stressSize);
					
				}else{
					stress = s.substring(beforeStress.length());
				}
				
				StringBuilder sb = new StringBuilder();
				sb.append(beforeStress);
				sb.append(stress);
				sb.append(afterStress);
				
				adaptedPronounciation.add(sb.toString());
				
				
			//without stress there is no reference for vowel adaption
			}else{
				adaptedPronounciation.add(s);
			}
		}		
		
		
		
		
		List<String> russianPlain = new ArrayList<>();
		for(String s : russianAccented){
			russianPlain.add(removeAccents(s));
		}
		
		List<String> latinPlain = new ArrayList<>();
		for(String s : adaptedPronounciation){
			latinPlain.add(removeAccents(s));
		}


		rval = new TransliterationConfigs(russianPlain, russianAccented, latinPlain, adaptedPronounciation);
		return rval;
	}

}
