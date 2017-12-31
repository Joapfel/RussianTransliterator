package de.ws1718.ismla.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import de.ws1718.ismla.shared.TransliterationConfigs;

public class TransliterationUiBinder extends Composite {

	private final static String RUSSIAN = "Russian";
	private final static String LATIN = "Latin";
	private final static String PLAIN = "Plain";
	private final static String ACCENTED = "Accented";
	
	private List<String> russianPlain;
	private List<String> russianAccented;
	private List<String> latinPlain;
	private List<String> latinAccented;

	private static TransliterationUiBinderUiBinder uiBinder = GWT.create(TransliterationUiBinderUiBinder.class);

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	interface TransliterationUiBinderUiBinder extends UiBinder<Widget, TransliterationUiBinder> {
	}

	public TransliterationUiBinder() {
		initWidget(uiBinder.createAndBindUi(this));
		
		outputArea.setEnabled(false);
		dropdownLanguage.setEnabled(false);
		dropdownStress.setEnabled(false);

		dropdownLanguage.getElement().addClassName("notActiveDropdown");
		dropdownStress.getElement().addClassName("notActiveDropdown");
		
		loading.setVisible(false);
	}
	


	@UiField
	TextArea inputArea;
	@UiField
	TextArea outputArea;
	@UiField
	ListBox dropdownLanguage;
	@UiField
	ListBox dropdownStress;
	@UiField
	HTMLPanel loading;

	@UiHandler("dropdownLanguage")
	public void onLanguageChange(ChangeEvent event) {
		String language = dropdownLanguage.getSelectedItemText();
		String stress = dropdownStress.getSelectedItemText();

		outputArea.setText("");
		
		if (language.equals(RUSSIAN)) {
			if (stress.equals(PLAIN)) {
				StringBuilder sb = new StringBuilder();
				for (String s : russianPlain) {
					sb.append(s + " ");
				}
				outputArea.setText(sb.toString());
			} else if (stress.equals(ACCENTED)) {
				StringBuilder sb = new StringBuilder();
				for (String s : russianAccented) {
					sb.append(s + " ");
				}
				outputArea.setText(sb.toString());
			}
		} else if (language.equals(LATIN)) {
			if (stress.equals(PLAIN)) {
				StringBuilder sb = new StringBuilder();
				for (String s : latinPlain) {
					sb.append(s + " ");
				}
				outputArea.setText(sb.toString());
			} else if (stress.equals(ACCENTED)) {
				StringBuilder sb = new StringBuilder();
				for (String s : latinAccented) {
					sb.append(s + " ");
				}
				outputArea.setText(sb.toString());
			}
		}
	}

	@UiHandler("dropdownStress")
	public void onStressChange(ChangeEvent event) {
		String language = dropdownLanguage.getSelectedItemText();
		String stress = dropdownStress.getSelectedItemText();

		outputArea.setText("");

		if (language.equals(RUSSIAN)) {
			if (stress.equals(PLAIN)) {
				StringBuilder sb = new StringBuilder();
				for (String s : russianPlain) {
					sb.append(s + " ");
				}
				outputArea.setText(sb.toString());
			} else if (stress.equals(ACCENTED)) {
				StringBuilder sb = new StringBuilder();
				for (String s : russianAccented) {
					sb.append(s + " ");
				}
				outputArea.setText(sb.toString());
			}
		} else if (language.equals(LATIN)) {
			if (stress.equals(PLAIN)) {
				StringBuilder sb = new StringBuilder();
				for (String s : latinPlain) {
					sb.append(s + " ");
				}
				outputArea.setText(sb.toString());
			} else if (stress.equals(ACCENTED)) {
				StringBuilder sb = new StringBuilder();
				for (String s : latinAccented) {
					sb.append(s + " ");
				}
				outputArea.setText(sb.toString());
			}
		}
	}
	
	@UiHandler("inputArea")
	void onKeyUp(KeyUpEvent up){
		
		loading.setVisible(true);
		
		inputArea.setEnabled(false);
		
		outputArea.setText("");
		
		dropdownLanguage.setEnabled(false);
		dropdownStress.setEnabled(false);
		
		dropdownLanguage.getElement().removeClassName("activeDropdown");
		dropdownStress.getElement().removeClassName("activeDropdown");
		dropdownLanguage.getElement().addClassName("notActiveDropdown");
		dropdownStress.getElement().addClassName("notActiveDropdown");
		

		greetingService.greetServer(inputArea.getText(), new AsyncCallback<TransliterationConfigs>() {

			@Override
			public void onSuccess(TransliterationConfigs result) {
				
				//design
				loading.setVisible(false);
				
				inputArea.setEnabled(true);
				dropdownLanguage.setEnabled(true);
				dropdownStress.setEnabled(true);
				
				dropdownLanguage.getElement().removeClassName("notActiveDropdown");
				dropdownStress.getElement().removeClassName("notActiveDropdown");
				dropdownLanguage.getElement().addClassName("activeDropdown");
				dropdownStress.getElement().addClassName("activeDropdown");

				
				//content
				russianPlain = result.getRussianPlain();
				russianAccented = result.getRussianAccented();
				latinPlain = result.getLatinPlain();
				latinAccented = result.getLatinAccented();


				StringBuilder sb = new StringBuilder();
				for (String s : latinAccented) {
					sb.append(s + " ");
				}
				outputArea.setText(sb.toString());
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("failed");
			}
		});

	}

}
