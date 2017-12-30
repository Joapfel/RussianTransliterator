package de.ws1718.ismla.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.sun.java.swing.plaf.windows.WindowsOptionPaneUI;

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
		
		inputArea.getElement().addClassName("defaultBorder");
		outputArea.setEnabled(false);
		
		dropdownLanguage.setEnabled(false);
		dropdownStress.setEnabled(false);
		dropdownLanguage.getElement().addClassName("notActiveColor");
		dropdownStress.getElement().addClassName("notActiveColor");
		dropdownLanguage.getElement().addClassName("redBorder");
		dropdownStress.getElement().addClassName("redBorder");
		
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
		
		inputArea.getElement().removeClassName("greenBorder");
		inputArea.getElement().addClassName("redBorder");
		inputArea.setEnabled(false);
		
		outputArea.setText("");
		
		dropdownLanguage.setEnabled(false);
		dropdownStress.setEnabled(false);
		
		dropdownLanguage.getElement().removeClassName("greenBorder");
		dropdownStress.getElement().removeClassName("greenBorder");
		dropdownLanguage.getElement().addClassName("redBorder");
		dropdownStress.getElement().addClassName("redBorder");
		
		dropdownLanguage.getElement().removeClassName("activeColor");
		dropdownStress.getElement().removeClassName("activeColor");
		dropdownLanguage.getElement().addClassName("notActiveColor");
		dropdownStress.getElement().addClassName("notActiveColor");

		greetingService.greetServer(inputArea.getText(), new AsyncCallback<TransliterationConfigs>() {

			@Override
			public void onSuccess(TransliterationConfigs result) {
				
				//design
				loading.setVisible(false);
				
				inputArea.setEnabled(true);
				inputArea.getElement().addClassName("greenBorder");

				dropdownLanguage.setEnabled(true);
				dropdownStress.setEnabled(true);
				
				dropdownLanguage.getElement().addClassName("activeColor");
				dropdownStress.getElement().addClassName("activeColor");
				dropdownLanguage.getElement().removeClassName("redBorder");
				dropdownStress.getElement().removeClassName("redBorder");
				dropdownLanguage.getElement().addClassName("greenBorder");
				dropdownStress.getElement().addClassName("greenBorder");

				
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
