package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import dataObjects.ValidTextField;
import dataObjects.comboBoxItem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {

	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Text informationText;
	@FXML
	private Button saveButton;
	@FXML
	private Button openFileButton;
	@FXML
	private ValidTextField serialNumberTextField;
	@FXML
	private ValidTextField dateTextField;
	@FXML
	private MenuButton dropDown;
	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private comboBoxItem Item1;
	@FXML
	private comboBoxItem Item2;
	@FXML
	private comboBoxItem Item3;
	@FXML
	private KeyCombination strgS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
	@FXML
	private KeyCombination easter = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);

	private String path;
	private List<String> allLines;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//comboBox.getItems().removeAll(comboBox.getItems());
		//comboBox.getItems().addAll(Item1.getItemName(), Item2.getItemName(), Item3.getItemName());
		//comboBox.setVisible(false);
	}

	public MainController() {
		Item1 = new comboBoxItem();
		Item2 = new comboBoxItem();
		Item3 = new comboBoxItem();

		Item1.setItemName("TheFirstItem");
		Item2.setItemName("2222222");
		Item3.setItemName("LastItem");
	}

	@FXML
	private void save() {
		// data in text field has been written
		if (dateTextField.isValid() && serialNumberTextField.isValid()) {
			// a file had to be selected previously
			if (allLines != null && path != null) {
				FileChooser chooser = new FileChooser();
				chooser.setTitle("Save current ECF file");
				chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
				chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ECF", "*.ecf"));
				File file = chooser.showSaveDialog(new Stage());
				if (file != null) {
					Translator translator = new Translator();
					String dateTextFieldValue = dateTextField.getText();
					String serialNumberTextFieldValue = serialNumberTextField.getText();

					// parsing i possible because validity of date text field was checked
					int dateAsInt = Integer.parseInt(dateTextFieldValue.replace(".", ""));
					allLines = translator.translateAndInsertDate(allLines, dateAsInt);
					allLines = translator.translateAndInsertSerialNumber(allLines, serialNumberTextFieldValue);
					ReadAndWriteFile writer = new ReadAndWriteFile();
					writer.writeTheFile(file.getAbsolutePath(), allLines);
					informationText.setText("Successfully saved file");
				} else {
					informationText.setText("Pleas enter a filename to save a file");
				}
			} else {
				informationText.setText("Please select a file first");
			}
		} else {
			informationText.setText("Please insert correct Date and Serial Number");
			return;
		}
	}

	@FXML
	private void openAFile() {
		FileChooser fileChooser = new FileChooser();
		// select current chooser opening on file directory
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		ReadAndWriteFile reader = new ReadAndWriteFile();
		// file chooser in new window
		File file = fileChooser.showOpenDialog(new Stage());
		// file has to be there and end with ecf
		if (file != null && file.getName().endsWith("ecf")) {
			informationText.setText(file.getName());
			path = file.getAbsolutePath();
			allLines = reader.readInputFile(path);
			// discard current selection because it did not meat correct definitions
		} else {
			informationText.setText("Please select a \"ecf\" file");
		}
	}

	@FXML
	private void comboBoxSelector() {
		String temp = comboBox.getSelectionModel().getSelectedItem();
		informationText.setText(temp);
	}

	@FXML
	private void checkSerialNumber(KeyEvent event) {
		String serialNumberTextFieldValue = serialNumberTextField.getText();
		if (serialNumberTextFieldValue.length() <= 10) {
			serialNumberTextField.nowValid();
		} else {
			serialNumberTextField.nowInValid();
		}
	}

	/*
	 * Depending on outcome of question. if 0 before numbers are not relevant then
	 * it may be enough to only parse the date and no lenght check
	 */
	@FXML
	private void checkDate(KeyEvent event) {
		String dateString = dateTextField.getText();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		formatter.setLenient(false);

		// lenght 10 to make sure the date 1.1.2017 is not accepted since 01.01.2017
		// would be right
		if (dateString.length() == 10) {
			// XX.XX.XXXX all chars at x need to be a digit to be a valid date
			if (isDigit(dateString.charAt(0)) && isDigit(dateString.charAt(1)) && isDigit(dateString.charAt(3)) && isDigit(dateString.charAt(4)) && isDigit(dateString.charAt(6)) && isDigit(dateString.charAt(7)) && isDigit(dateString.charAt(8))
					&& isDigit(dateString.charAt(9))) {
				// "." need to be at these positions to be a valid date
				if (dateString.charAt(2) == '.' && dateString.charAt(5) == '.') {
					try {
						Date date = formatter.parse(dateString);
						dateTextField.nowValid();
						return;
					} catch (ParseException e) {
						dateTextField.nowInValid();
						return;
					}
				} else {
					dateTextField.nowInValid();
					return;
				}
			} else {
				dateTextField.nowInValid();
				return;
			}
		} else {
			dateTextField.nowInValid();
			return;
		}
	}

	@FXML
	private void hotKeyListener(KeyEvent event) {
		// strg+s is pressed OR button is highlighted (tab) and enter
		if (strgS.match(event) || (event.getTarget() == saveButton && event.getCode() == KeyCode.ENTER)) {
			this.save();
		}

		if (event.getTarget() == openFileButton && event.getCode() == KeyCode.ENTER) {
			this.openAFile();
		}

		if (easter.match(event)) {
			// Parent root;
			try {
				// root =
				// FXMLLoader.load(getClass().getClassLoader().getResource("/popUp.fxml"));
				Parent root = FXMLLoader.load(getClass().getResource("/popUp.fxml"));
				Stage stage = new Stage();
				stage.setResizable(false);
				stage.setScene(new Scene(root, 180, 210));
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private boolean isDigit(char c) {
		if (c >= '0' && c <= '9') {
			return true;
		} else {
			return false;
		}
	}
}
