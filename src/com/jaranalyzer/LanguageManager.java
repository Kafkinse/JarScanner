package com.jaranalyzer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class LanguageManager {

	public enum Language {
		TR("tr"), EN("en"), RU("ru");

		private final String code;

		Language(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

		public static Language fromCode(String code) {
			if (code == null) return RU;
			for (Language lang : values()) {
				if (lang.code.equalsIgnoreCase(code)) return lang;
			}
			return RU;
		}

		public String getDisplayName() {
			switch (this) {
				case TR: return "Türkçe";
				case EN: return "English";
				case RU: return "Русский";
				default: return "English";
			}
		}
	}

	private static Language currentLanguage = Language.RU;
	private static ResourceBundle bundle;

	static {
		setLanguage(Language.RU);
	}

	public static void setLanguage(Language language) {
		currentLanguage = language;
		loadBundle();
		applySwingStrings();
	}

	/**
	 * Translates the strings Swing supplies itself.
	 *
	 * <p>Dialog buttons and the file chooser are not built from application text —
	 * a look and feel ships its own words for "Yes", "Cancel", "Look in", and picks
	 * them by the JVM's default locale, which has nothing to do with the language
	 * the user chose in this window. Left alone, a Turkish confirmation ends in
	 * English Yes/No buttons. Setting them here rather than at each of the several
	 * dozen call sites means a dialog added later is translated by default.
	 */
	public static void applySwingStrings() {
		boolean tr = currentLanguage == Language.TR;
		boolean ru = currentLanguage == Language.RU;
		javax.swing.UIManager.put("OptionPane.yesButtonText", tr ? "Evet" : ru ? "Да" : "Yes");
		javax.swing.UIManager.put("OptionPane.noButtonText", tr ? "Hayır" : ru ? "Нет" : "No");
		javax.swing.UIManager.put("OptionPane.cancelButtonText", tr ? "İptal" : ru ? "Отмена" : "Cancel");
		javax.swing.UIManager.put("OptionPane.okButtonText", tr ? "Tamam" : ru ? "ОК" : "OK");
		javax.swing.UIManager.put("OptionPane.titleText", tr ? "Mesaj" : ru ? "Сообщение" : "Message");
		javax.swing.UIManager.put("OptionPane.messageDialogTitle", tr ? "Mesaj" : ru ? "Сообщение" : "Message");
		javax.swing.UIManager.put("OptionPane.inputDialogTitle", tr ? "Giriş" : ru ? "Ввод" : "Input");

		javax.swing.UIManager.put("FileChooser.openDialogTitleText", tr ? "Aç" : ru ? "Открыть" : "Open");
		javax.swing.UIManager.put("FileChooser.saveDialogTitleText", tr ? "Kaydet" : ru ? "Сохранить" : "Save");
		javax.swing.UIManager.put("FileChooser.openButtonText", tr ? "Aç" : ru ? "Открыть" : "Open");
		javax.swing.UIManager.put("FileChooser.saveButtonText", tr ? "Kaydet" : ru ? "Сохранить" : "Save");
		javax.swing.UIManager.put("FileChooser.cancelButtonText", tr ? "İptal" : ru ? "Отмена" : "Cancel");
		javax.swing.UIManager.put("FileChooser.updateButtonText", tr ? "Güncelle" : ru ? "Обновить" : "Update");
		javax.swing.UIManager.put("FileChooser.helpButtonText", tr ? "Yardım" : ru ? "Справка" : "Help");
		javax.swing.UIManager.put("FileChooser.directoryOpenButtonText", tr ? "Aç" : ru ? "Открыть" : "Open");
		javax.swing.UIManager.put("FileChooser.lookInLabelText", tr ? "Konum:" : ru ? "Папка:" : "Look in:");
		javax.swing.UIManager.put("FileChooser.saveInLabelText", tr ? "Konum:" : ru ? "Папка:" : "Save in:");
		javax.swing.UIManager.put("FileChooser.fileNameLabelText", tr ? "Dosya adı:" : ru ? "Имя файла:" : "File name:");
		javax.swing.UIManager.put("FileChooser.filesOfTypeLabelText", tr ? "Dosya türü:" : ru ? "Тип файла:" : "Files of type:");
		javax.swing.UIManager.put("FileChooser.acceptAllFileFilterText",
				tr ? "Tüm dosyalar" : ru ? "Все файлы" : "All files");
		javax.swing.UIManager.put("FileChooser.upFolderToolTipText",
				tr ? "Bir üst klasör" : ru ? "На уровень выше" : "Up one level");
		javax.swing.UIManager.put("FileChooser.homeFolderToolTipText",
				tr ? "Masaüstü" : ru ? "Домой" : "Home");
		javax.swing.UIManager.put("FileChooser.newFolderToolTipText",
				tr ? "Yeni klasör" : ru ? "Создать папку" : "Create new folder");
		javax.swing.UIManager.put("FileChooser.listViewButtonToolTipText",
				tr ? "Liste" : ru ? "Список" : "List");
		javax.swing.UIManager.put("FileChooser.detailsViewButtonToolTipText",
				tr ? "Ayrıntılar" : ru ? "Подробности" : "Details");
		javax.swing.UIManager.put("FileChooser.newFolderButtonText",
				tr ? "Yeni klasör" : ru ? "Новая папка" : "New folder");
		javax.swing.UIManager.put("FileChooser.renameFileButtonText",
				tr ? "Yeniden adlandır" : ru ? "Переименовать" : "Rename");
		javax.swing.UIManager.put("FileChooser.deleteFileButtonText", tr ? "Sil" : ru ? "Удалить" : "Delete");
		javax.swing.UIManager.put("FileChooser.filterLabelText", tr ? "Dosya türü:" : ru ? "Тип файла:" : "Files of type:");
		javax.swing.UIManager.put("FileChooser.fileNameHeaderText", tr ? "Ad" : ru ? "Имя" : "Name");
		javax.swing.UIManager.put("FileChooser.fileSizeHeaderText", tr ? "Boyut" : ru ? "Размер" : "Size");
		javax.swing.UIManager.put("FileChooser.fileTypeHeaderText", tr ? "Tür" : ru ? "Тип" : "Type");
		javax.swing.UIManager.put("FileChooser.fileDateHeaderText", tr ? "Değiştirilme" : ru ? "Изменён" : "Modified");
		javax.swing.UIManager.put("FileChooser.fileAttrHeaderText", tr ? "Öznitelik" : ru ? "Атрибуты" : "Attributes");
	}

	private static void loadBundle() {
		String resourceName = "/resources/messages_" + currentLanguage.getCode() + ".properties";
		try (InputStream is = LanguageManager.class.getResourceAsStream(resourceName)) {
			if (is != null) {
				Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
				bundle = new PropertyResourceBundle(reader);
			} else {
				bundle = null;
			}
		} catch (Exception e) {
			bundle = null;
		}
	}

	public static String getString(String key) {
		if (bundle != null && bundle.containsKey(key)) {
			return bundle.getString(key);
		}
		if (currentLanguage != Language.EN) {
			try (InputStream is = LanguageManager.class.getResourceAsStream("/resources/messages_en.properties")) {
				if (is != null) {
					ResourceBundle fallback = new PropertyResourceBundle(new InputStreamReader(is, StandardCharsets.UTF_8));
					if (fallback.containsKey(key)) return fallback.getString(key);
				}
			} catch (Exception ignored) { }
		}
		return key;
	}

	public static Language getCurrentLanguage() {
		return currentLanguage;
	}

	public static void toggleLanguage() {
		switch (currentLanguage) {
			case TR: setLanguage(Language.EN); break;
			case EN: setLanguage(Language.RU); break;
			case RU: setLanguage(Language.TR); break;
		}
	}
}
