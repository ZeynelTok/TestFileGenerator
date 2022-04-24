/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testfilegenerator;

import com.broadridge.testfilegenerator.users.Role;
import com.broadridge.testfilegenerator.users.User;
import com.broadridge.testfilegenerator.users.UsersUtils;
import static com.broadridge.testfilegenerator.users.UsersUtils.validatedUsers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author TokZ
 */
public class TFGController implements Initializable {

    //FXML VARIABLES
    @FXML
    private Label label;

    @FXML
    private Text loggedInUserText;

    @FXML
    private TextArea usersTextArea;

    @FXML
    private Button settingsButton;

    @FXML
    private MenuItem createNewFile;

    @FXML
    private MenuItem Preferences;

    @FXML
    private MenuItem close;

    @FXML
    private TabPane settingsPane;

    @FXML
    private Pane defaultPane;

    @FXML
    private Pane preferencesPane;

    @FXML
    private Pane guidePane;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn usersTableSelect;

    @FXML
    private TableColumn<User, String> usersTableFN;

    @FXML
    private TableColumn<User, String> usersTableLN;

    @FXML
    private TableColumn<User, String> usersTableUN;

    @FXML
    private TableColumn<User, Role> usersTableR;

    @FXML
    private Button applyPreferences;

    @FXML
    private Button cancelPreferences;

    @FXML
    private StackPane mainStackPane;

    @FXML
    private RadioButton preferencesDarkTheme;

    @FXML
    private RadioButton preferencesLightTheme;

    @FXML
    private ListView<String> fontList;
    @FXML
    private ListView<String> fontSizeList;

    @FXML
    private Pane aboutPane;

    @FXML
    private TextArea userGuide;

    Stage window;

    boolean settingsChanged = false;

    @FXML
    private Button editUserB;

    boolean usersUpdated = false;

    //LAUNCES THE MAIN PANE UPON PROGRAM LAUNCH
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UsersUtils.createUsers();
        authenticateUser();
        populateUsersTable();
        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(defaultPane);

    }

    //AUTHENTICATES USER DEPENDING ON THE LIST RETRIEVED FROM VALIDATED USERS THAT READS USERS.XML
    public void authenticateUser() {
        boolean valid = UsersUtils.validateUser();
        if (valid) {
            loggedInUserText.setText("Logged in as " + UsersUtils.loggedInUser.getFirstName() + " " + UsersUtils.loggedInUser.getLastName());
            //IF ITS THE FIRST LOGIN FOR THE USER, IT SETS THE PREFERENCES TO DEFAULT AS THIS NODE WON'T EXIST DURING FIRST LOGIN
            if (!UsersUtils.loggedInUser.getPreferences().isUserNode()) {
                UsersUtils.loggedInUser.setPreferences("theme", "light");
                UsersUtils.loggedInUser.setPreferences("finalfont", "12 \"System\"");
            }

        //CLOSE PROGRAM IF AUTHENTICATION FAILS
        } else {
            loggedInUserText.setText("Login Failed");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("User Authentication Failed");
            alert.setContentText("You are not authorised to use this application. Exiting now");

            alert.showAndWait();
            System.exit(0);
        }
    }

    //OPENS WIZARD THAT GOES TO WIZARDCONTROLLER
    public void createNewFile() throws Exception {
        openWizard();
    }

    public void openWizard() throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Wizard.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        window = new Stage();
        Scene scene = new Scene(root1);
        if (settingsChanged) {
            applySecondaryPreferences(scene);
        }

        window.setScene(scene);
        window.show();
        window.setResizable(false);
    }
    
    //OPENS SETTINGS DEPENDING ON USER ROLE
    public void openSettings() {
        usersTextArea.setVisible(false);
        if (settingsPane.isVisible()) {
            settingsPane.setVisible(false);
            mainStackPane.setVisible(true);
            mainStackPane.getChildren().clear();
            mainStackPane.getChildren().add(defaultPane);
        } else {
            mainStackPane.setVisible(false);
            mainStackPane.getChildren().clear();
            settingsPane.setVisible(true);

        }
        if (UsersUtils.loggedInUser.getRole() != Role.User) {
            usersTable.setEditable(true);
            editUserB.setVisible(true);
        } else {
            usersTable.setEditable(false);
            editUserB.setVisible(false);
        }
    }

    //POPULATES THE USERS TABLE WITH THE USERS GATHERED FROM USERS.XML
    public void populateUsersTable() {

        ObservableList<User> observableUsers = FXCollections.observableArrayList(validatedUsers);
        usersTableFN.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        usersTableLN.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        usersTableUN.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        usersTableR.setCellValueFactory(new PropertyValueFactory<User, Role>("role"));
        usersTable.setItems(observableUsers);

    }

    //FUNCTIONALITY TO EDIT USERS THAT OPENS UP USERS.XML FOR THE USER TO EDIT THE VALID USERS
    public void editUsers() throws IOException {
        if (!usersTextArea.isVisible()) {
            usersTextArea.setVisible(true);
            usersTextArea.clear();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File("users.xml")));
                String line;
                while ((line = reader.readLine()) != null) {
                    usersTextArea.appendText(line);
                    usersTextArea.appendText("\n");
                    usersUpdated = true;
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(TFGController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (usersUpdated) {
                UsersUtils.updateUsers(usersTextArea.getText());
                usersUpdated = false;
                UsersUtils.createUsers();
                populateUsersTable();
            }
            usersTextArea.setVisible(false);
            openSettings();
        }

    }

    //WHEN LOAD PREFERENCES IS CLICKED, IT SETS THE PREFERENCES TO THOSE SAVED WITHIN THE USERS PREFERENCES NODE
    public void setInitialPreferences() {
        if (UsersUtils.loggedInUser.getPreferences().get("theme", null).equals("dark")) {
            mainStackPane.getScene().getStylesheets().add("styles/dark-theme.css");
        } else if (UsersUtils.loggedInUser.getPreferences().get("theme", null).equals("light")) {
            mainStackPane.getScene().getStylesheets().remove("styles/dark-theme.css");
        }
        String finalFont = UsersUtils.loggedInUser.getPreferences().get("finalfont", null);
        mainStackPane.getScene().getRoot().setStyle(finalFont);
        String font = finalFont.split("\"")[1];
        String fontSize = finalFont.split("\\s+")[1];

        if (font.isEmpty()) {
            fontList.getSelectionModel().select(6);
            fontList.getFocusModel().focus(6);
        } else {
            if (font.equals("System")) {
                font = "System (Default)";
            }
            fontList.getSelectionModel().select(font);
            int x = fontList.getSelectionModel().getSelectedIndex();
            fontList.getFocusModel().focus(x);
        }

        if (fontSize.isEmpty()) {
            fontSizeList.getSelectionModel().select(4);
            fontSizeList.getFocusModel().focus(4);
        } else {
            if (fontSize.equals("12")) {
                fontSize = "12 (Default)";
            }
            fontSizeList.getSelectionModel().select(fontSize);
            int x = fontSizeList.getSelectionModel().getSelectedIndex();
            fontSizeList.getFocusModel().focus(x);
        }

        switch (UsersUtils.loggedInUser.getPreferences().get("theme", null)) {
            case "dark":
                preferencesDarkTheme.setSelected(true);
                break;
            case "light":
                preferencesLightTheme.setSelected(true);
                break;
            default:
                preferencesLightTheme.setSelected(true);
        };

    }

    //VIEW THE PREFERENCES PANE 
    public void viewPreferences() {
        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(preferencesPane);

        String finalFont = UsersUtils.loggedInUser.getPreferences().get("finalfont", null);
        String font = finalFont.split("\"")[1];
        String fontSize = finalFont.split("\\s+")[1];

        ObservableList<String> fonts = FXCollections.observableArrayList("Arial", "Calibri", "Futura", "Garamond", "Helvetica", "Rockwell",
                "System (Default)", "Times New Roman", "Verdana");
        ObservableList<String> fontSizes = FXCollections.observableArrayList("8", "9", "10", "11", "12 (Default)", "13", "14");
        fontList.setItems(fonts);
        fontSizeList.setItems(fontSizes);

        ToggleGroup group = new ToggleGroup();
        preferencesLightTheme.setToggleGroup(group);
        preferencesDarkTheme.setToggleGroup(group);
        preferencesLightTheme.setSelected(true);
        fontList.getSelectionModel().select(6);
        fontList.getFocusModel().focus(6);
        fontSizeList.getSelectionModel().select(4);
        fontSizeList.getFocusModel().focus(4);

    }

    //ABOUT SECTION
    public void viewAbout() {
        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(aboutPane);
    }

    //GUIDE SECTION
    public void viewGuide() {
        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(guidePane);
        if (UsersUtils.loggedInUser.getRole() == Role.Developer) {
            userGuide.setEditable(true);
        } else {
            userGuide.setEditable(false);
        }
    }

    //BUTTON THAT CLOSES THE GUIDE & ABOUT AND RETURNS TO MAIN SCREEN
    public void close() {
        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(defaultPane);
    }

    //APPLIES THE USER PREFERENCES AND SAVES THEM TO THE USERS PREFERENCES
    public void applyPreferences() {
        if (preferencesDarkTheme.isSelected()) {
            preferencesDarkTheme.getScene().getStylesheets().add("styles/dark-theme.css");
            UsersUtils.loggedInUser.setPreferences("theme", "dark");

        } else {
            preferencesDarkTheme.getScene().getStylesheets().remove("styles/dark-theme.css");
            UsersUtils.loggedInUser.setPreferences("theme", "light");
        }
        String font = fontList.getSelectionModel().getSelectedItem();
        String fontSize = fontSizeList.getSelectionModel().getSelectedItem();
        if (font.equals("System (Default)")) {
            font = "System";
        }
        if (fontSize.equals("12 (Default)")) {
            fontSize = "12";
        }

        String finalFont = "-fx-font: " + fontSize + " \"" + font + "\"";
        fontList.getScene().getRoot().setStyle(finalFont);
        UsersUtils.loggedInUser.setPreferences("finalfont", finalFont);
        settingsChanged = true;
    }

    //METHOD THAT APPLIES THE USER PREFERENCES TO THE WIZARD SCREEN UPON WIZARD LAUNCH TO ENSURE PREFERENCES ARE SHOWN THROUGHOUT THE PROGRAM
    public void applySecondaryPreferences(Scene scene) {
        if (preferencesDarkTheme.isSelected()) {
            scene.getStylesheets().add("styles/dark-theme.css");

        } else {
            scene.getStylesheets().remove("styles/dark-theme.css");
        }
        String font = fontList.getSelectionModel().getSelectedItem();
        String fontSize = fontSizeList.getSelectionModel().getSelectedItem();
        if (font.equals("System (Default)")) {
            font = "System";
        }
        if (fontSize.equals("12 (Default)")) {
            fontSize = "12";
        }

        String finalFont = "-fx-font: " + fontSize + " \"" + font + "\"";
        scene.getRoot().setStyle(finalFont);

    }

    //CANCEL PREFERENCES TO RETURN TO MAIN SCREEN
    public void cancelPreferences() {
        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(defaultPane);
    }

    //EXITS PROGRAM
    public void exitProgram() throws BackingStoreException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            System.exit(0);
        }
    }

}
