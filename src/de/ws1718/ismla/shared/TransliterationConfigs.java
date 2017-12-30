package de.ws1718.ismla.shared;

import java.io.Serializable;
import java.util.List;

public class TransliterationConfigs implements Serializable{

	private List<String> russianPlain;
	private List<String> russianAccented;
	private List<String> latinPlain;
	private List<String> latinAccented;
		
	public TransliterationConfigs() {

	}

	public TransliterationConfigs(List<String> russianPlain, List<String> russianAccented, List<String> latinPlain,
			List<String> latinAccented) {
		super();
		this.russianPlain = russianPlain;
		this.russianAccented = russianAccented;
		this.latinPlain = latinPlain;
		this.latinAccented = latinAccented;
	}

	public List<String> getRussianPlain() {
		return russianPlain;
	}

	public void setRussianPlain(List<String> russianPlain) {
		this.russianPlain = russianPlain;
	}

	public List<String> getRussianAccented() {
		return russianAccented;
	}

	public void setRussianAccented(List<String> russianAccented) {
		this.russianAccented = russianAccented;
	}

	public List<String> getLatinPlain() {
		return latinPlain;
	}

	public void setLatinPlain(List<String> latinPlain) {
		this.latinPlain = latinPlain;
	}

	public List<String> getLatinAccented() {
		return latinAccented;
	}

	public void setLatinAccented(List<String> latinAccented) {
		this.latinAccented = latinAccented;
	}
	
	
	
	
}
