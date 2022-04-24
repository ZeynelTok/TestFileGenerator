/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testfilegenerator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.util.converter.IntegerStringConverter;

/**
 *
 * @author TokZ
 */
public class WizardController {

    @FXML
    private ListView<String> accountNumberTable;

    @FXML
    private Button addAccountButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button backButton;

    @FXML
    private Button browseButton;

    @FXML
    private Label complexity1Text;

    @FXML
    private Label complexity2Text;

    @FXML
    private Label complexity3Text;

    @FXML
    private Slider complexitySlider;

    @FXML
    private Slider numberOfTransactionsSlider;

    @FXML
    private TextField daysOfDataText;

    @FXML
    private Button finalGenerateButton;

    @FXML
    private RadioButton manuallyInsertButton;

    @FXML
    private Button nextButton;

    @FXML
    private TextField numberOfAccountsText;

    @FXML
    private TextField numberOfFilesText;

    @FXML
    private TextField outputPathText;

    @FXML
    private Pane pane1;

    @FXML
    private Pane pane2;

    @FXML
    private Pane pane3;

    @FXML
    private Pane pane4;

    @FXML
    private Pane pane5;

    @FXML
    private Pane pane6;

    @FXML
    private RadioButton randomlyGenerateButton;

    @FXML
    private Label randomlyGenerateNote;

    @FXML
    private Button removeAccountButton;

    @FXML
    private TextField sizeOrAmountText;

    @FXML
    private StackPane stackPane;

    @FXML
    private ChoiceBox<String> swiftFormatSelector;

    @FXML
    private RadioButton transactionCountButton;

    @FXML
    private TextField accountTextBox;

    @FXML
    private Label accountNumbersText;

    @FXML
    private ComboBox<String> currencies;

    @FXML
    private ChoiceBox<String> currencies2;

    @FXML
    private ChoiceBox<String> openingBalCOrD;

    @FXML
    private ChoiceBox<String> closingBalCOrD;

    @FXML
    private TextField openingBalanceAmount;

    @FXML
    private TextField closingBalanceAmount;

    @FXML
    private DatePicker openingBalanceDate;

    @FXML
    private DatePicker closingBalanceDate;

    @FXML
    private RadioButton balanceRandomButton;

    @FXML
    private DatePicker txnDateFrom;

    @FXML
    private DatePicker txnDateTo;

    @FXML
    private TextField txnValueFrom;

    @FXML
    private TextField txnValueTo;
    @FXML
    private RadioButton autoTxnValue;

    @FXML
    private RadioButton generateDoubleSides;

    @FXML
    private TextField senderBicBox;

    @FXML
    private TextField receiverBicBox;

    @FXML
    private RadioButton sBAuto;

    @FXML
    private RadioButton rBAuto;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label summarySwiftFormat;

    @FXML
    private Label summaryAccounts;

    @FXML
    private Label summaryDays;

    @FXML
    private Label summaryFiles;

    @FXML
    private Label summaryTxns;

    @FXML
    private Label summaryComplexity;

    @FXML
    private Label summaryTotalFiles;

    @FXML
    private Label summaryTotalTxns;

    @FXML
    private Label summaryEstimate;
    
        @FXML
    private Label summaryDoubleSided;

    @FXML
    private Label txnValueBetweenPrompt;
    private final List<Pane> steps = new ArrayList<>();
    ObservableList<String> accounts = FXCollections.observableArrayList();
    ObservableList<String> observableCurrencies;

    private int currentStep = 0;

    Instant start;

    @FXML
    public void initialize() throws Exception {
        addSteps();
        stackPane.getChildren().clear();
        stackPane.getChildren().add(steps.get(currentStep));
        initialiseSelectors();
        if (currentStep == 0) {
            backButton.setDisable(true);
        }
        finalGenerateButton.setVisible(false);
    }

    public void addSteps() throws IOException, InterruptedException {
        steps.add(pane1);
        steps.add(pane2);
        steps.add(pane3);
        steps.add(pane4);
        steps.add(pane5);
        steps.add(pane6);
    }

    public Pane getPane() {
        return pane1;
    }

    public void initialiseSelectors() {
        //Add values to the below observable list to add new SWIFT formats
        ObservableList<String> observableFormats = FXCollections.observableArrayList("MT940");
        swiftFormatSelector.setItems(observableFormats);

//        Set values for size dropdown
//        ObservableList<String> observableSizes = FXCollections.observableArrayList("b", "Kb", "Mb", "Gb");
//        sizeChoice.setItems(observableSizes);
        //Toggle Group for file size or transaction count selectors
//        ToggleGroup group = new ToggleGroup();
//        fileSizeButton.setToggleGroup(group);
//        fileSizeButton.setSelected(true);
//        transactionCountButton.setToggleGroup(group);
        //Second toggle group for account information spage
        ToggleGroup group2 = new ToggleGroup();
        randomlyGenerateButton.setToggleGroup(group2);
        randomlyGenerateButton.setSelected(true);
        manuallyInsertButton.setToggleGroup(group2);
        accountSelectorSwitch();

        //Limits text fields to integers only
        UnaryOperator<Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };

        UnaryOperator<Change> filter2 = change2 -> {
            String text = change2.getText();

            if (text.matches("^\\d*\\.?\\d{0,2}$")) {
                return change2;
            }

            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        TextFormatter<String> textFormatter2 = new TextFormatter<>(filter);
        TextFormatter<String> textFormatter3 = new TextFormatter<>(filter);
        TextFormatter<String> textFormatter4 = new TextFormatter<>(filter);
        TextFormatter<String> textFormatter5 = new TextFormatter<>(filter2);
        TextFormatter<String> textFormatter6 = new TextFormatter<>(filter2);
        TextFormatter<String> textFormatter7 = new TextFormatter<>(filter2);
        TextFormatter<String> textFormatter8 = new TextFormatter<>(filter2);
        numberOfFilesText.setTextFormatter(textFormatter);
        sizeOrAmountText.setTextFormatter(textFormatter2);
        numberOfAccountsText.setTextFormatter(textFormatter3);
        daysOfDataText.setTextFormatter(textFormatter4);
        txnValueFrom.setTextFormatter(textFormatter5);
        txnValueTo.setTextFormatter(textFormatter6);
        openingBalanceAmount.setTextFormatter(textFormatter7);
        closingBalanceAmount.setTextFormatter(textFormatter8);
        //Slider defaults
        complexity2Text.setVisible(false);
        complexity3Text.setVisible(false);

        //Auto Generate Bal Defaults
        balanceRandomButton.setSelected(true);
        autoTxnValue.setSelected(true);
        txnValueFrom.setDisable(true);
        txnValueTo.setDisable(true);
        balanceGenerateSwitch();

        populateCurrencies();

        ObservableList<String> observableCorD = FXCollections.observableArrayList("C", "D");
        openingBalCOrD.setItems(observableCorD);
        closingBalCOrD.setItems(observableCorD);

        sBAuto.setSelected(true);
        rBAuto.setSelected(true);
        senderBicBox.setDisable(true);
        receiverBicBox.setDisable(true);

    }

    public void bicSelectors() {
        if (sBAuto.isSelected()) {
            senderBicBox.setDisable(true);
        }
        if (!sBAuto.isSelected()) {
            senderBicBox.setDisable(false);
        }
        if (rBAuto.isSelected()) {
            receiverBicBox.setDisable(true);
        }
        if (!rBAuto.isSelected()) {
            receiverBicBox.setDisable(false);
        }
    }

    public void populateCurrencies() {

        observableCurrencies = FXCollections.observableArrayList("USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "HKD",
                "NZD", "SEK", "KRW", "SGD", "NOK", "MXN", "INR", "RUB", "ZAR", "TRY", "BRL", "TWD", "DKK", "PLN", "THB", "IDR");
// Too many ISO 4217 currencies, list is cluttered and not in alphabetical order. Using 25 most traded currencies in the world instead
//        Set<Currency> currenciesList = Currency.getAvailableCurrencies();
//        for (Currency currency : currenciesList){
//            observableCurrencies.add(currency.toString());
//        }
        currencies.setItems(observableCurrencies);
    }

    public void balanceGenerateSwitch() {
        if (balanceRandomButton.isSelected()) {
            openingBalCOrD.setDisable(true);
            openingBalanceAmount.setDisable(true);
            openingBalanceDate.setDisable(true);
            currencies.setDisable(true);
            closingBalCOrD.setDisable(true);
            closingBalanceAmount.setDisable(true);
            txnValueBetweenPrompt.setVisible(false);
            autoTxnValue.setVisible(true);
        }
        if (!balanceRandomButton.isSelected()) {
            openingBalCOrD.setDisable(false);
            openingBalanceAmount.setDisable(false);
            openingBalanceDate.setDisable(false);
            currencies.setDisable(false);
            closingBalCOrD.setDisable(false);
            closingBalanceAmount.setDisable(false);
            txnValueBetweenPrompt.setVisible(true);
            txnValueFrom.setDisable(true);
            txnValueTo.setDisable(true);
            autoTxnValue.setVisible(false);
        }

    }

    public boolean validated(int currentStep) {
        Pane checkPane;
        if (currentStep == 0) {
            checkPane = pane1;
        } else if (currentStep == 1) {
            checkPane = pane2;
        } else if (currentStep == 2) {
            checkPane = pane3;
        } else if (currentStep == 3) {
            checkPane = pane4;
        } else if (currentStep == 4) {
            checkPane = pane5;
        } else if (currentStep == 5) {
            checkPane = pane6;
        } else {
            checkPane = pane1;
        }

        for (Node node : checkPane.getChildren()) {
            if (node instanceof TextField) {
                if (!((TextField) node).isDisabled()) {
                    if (!((TextField) node).getId().equals("accountTextBox")) {
                        if (((TextField) node).getText().isEmpty()) {
                            return false;
                        }
                    }
                }

            }
            if (node instanceof ListView) {
                if (!((ListView) node).isDisabled()) {
                    if (((ListView) node).getItems().isEmpty()) {
                        return false;
                    }
                    if (((ListView) node).getItems().size() < accounts.size()) {
                        return false;
                    }

                }

            }
            if (node instanceof ChoiceBox) {
                if (!((ChoiceBox) node).isDisabled()) {
                    if (((ChoiceBox) node).getSelectionModel().isEmpty()) {
                        return false;
                    }
                }

            }
            if (node instanceof ComboBox) {
                if (!((ComboBox) node).isDisabled()) {
                    if (((ComboBox) node).getSelectionModel().isEmpty()) {
                        return false;
                    }
                }

            }
            if (node instanceof DatePicker) {
                if (!((DatePicker) node).isDisabled()) {
                    if (((DatePicker) node).getValue() == null) {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    public void nextAction() throws IOException {
        if (validated(currentStep)) {
            currentStep++;
            stackPane.getChildren().clear();
            stackPane.getChildren().add(steps.get(currentStep));
            if (currentStep > 0) {
                backButton.setDisable(false);
            }
            if (currentStep == steps.size() - 1) {
                nextButton.setDisable(true);
                finalGenerateButton.setVisible(true);
            }
            if (currentStep == 5) {
                summaryPage();
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Required Fields Empty");
            alert.setContentText("Ensure that all the required fields are populated. & if manual accounts are selected that the number of accounts entered equal the number of accounts to be generated"
                    + "\nPlease try again.");
            alert.showAndWait();
        }

    }

    public void backAction() throws IOException {
        currentStep--;
        stackPane.getChildren().clear();
        stackPane.getChildren().add(steps.get(currentStep));
        if (currentStep == 0) {
            backButton.setDisable(true);
        }
        if (currentStep <= steps.size() - 1) {
            nextButton.setDisable(false);
            finalGenerateButton.setVisible(false);
        }
    }

    public void summaryPage() {
        summarySwiftFormat.setText(summarySwiftFormat.getText().split(":")[0] + ": " + swiftFormatSelector.getValue());
        summaryDays.setText(summaryDays.getText().split(":")[0] + ": " + daysOfDataText.getText());
        summaryAccounts.setText(summaryAccounts.getText().split(":")[0] + ": " + numberOfAccountsText.getText());
        summaryFiles.setText(summaryFiles.getText().split(":")[0] + ": " + numberOfFilesText.getText());
        summaryTxns.setText(summaryTxns.getText().split(":")[0] + ": " + sizeOrAmountText.getText());
        summaryComplexity.setText(summaryComplexity.getText().split(":")[0] + ": " + complexitySlider.getValue());
        summaryDoubleSided.setText(summaryDoubleSided.getText().split(":")[0] + ": " + generateDoubleSides.isSelected());
        int totalFiles = (Integer.parseInt(daysOfDataText.getText())
                *Integer.parseInt(numberOfAccountsText.getText())*Integer.parseInt(numberOfFilesText.getText()));
        summaryTotalFiles.setText(summaryTotalFiles.getText().split(":")[0] + ": "+ totalFiles);    
        int totalTxns = Integer.parseInt(daysOfDataText.getText())*Integer.parseInt(numberOfAccountsText.getText())*Integer.parseInt(numberOfFilesText.getText())*Integer.parseInt(sizeOrAmountText.getText());
        summaryTotalTxns.setText(summaryTotalTxns.getText().split(":")[0] + ": " + totalTxns);
        String estimatedTime = "";
        if (totalFiles<5000){
            estimatedTime = "Instant";
        }
        else if (totalFiles<25000){
            estimatedTime = "Under 1 minute";
        }
        else if (totalFiles<100000){
            estimatedTime = "Under 5 minutes";
        }
        else if (totalFiles<250000){
            estimatedTime = "Under 10 minutes";
        }
        else if (totalFiles<500000){
            estimatedTime = "Under 30 minutes";
        }
        else {
            estimatedTime = "Under an hour";
        }
        summaryEstimate.setText(summaryEstimate.getText().split(":")[0] + ": " + estimatedTime);
        
        
    }

    public void browseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(browseButton.getScene().getWindow());
        if (selectedDirectory.exists()) {
            outputPathText.setText(selectedDirectory.getAbsolutePath());
        }

    }

    public void accountSelectorSwitch() {
        if (randomlyGenerateButton.isSelected()) {
            randomlyGenerateNote.setVisible(true);
            accountNumberTable.setVisible(false);
            accountNumberTable.setDisable(true);
            addAccountButton.setVisible(false);
            removeAccountButton.setVisible(false);
            accountTextBox.setVisible(false);
            accountTextBox.setDisable(true);
            accountNumbersText.setVisible(false);
        } else {
            randomlyGenerateNote.setVisible(false);
            accountNumberTable.setVisible(true);
            accountNumberTable.setDisable(false);
            addAccountButton.setVisible(true);
            removeAccountButton.setVisible(true);
            accountTextBox.setVisible(true);
            accountTextBox.setDisable(false);
            accountNumbersText.setVisible(true);
        }
    }

    public void txnAutoSwitch() {
        if (autoTxnValue.isSelected()) {
            txnValueFrom.setDisable(true);
            txnValueTo.setDisable(true);
        }
        if (!autoTxnValue.isSelected()) {
            txnValueFrom.setDisable(false);
            txnValueTo.setDisable(false);
        }
    }

    public void addAccount() {
        if (accountTextBox.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Required Fields Empty");
            alert.setContentText("The account number cannot be empty");
            alert.showAndWait();
        } else if (accounts.size() == Integer.parseInt(numberOfAccountsText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Maximum Accounts reached");
            alert.setContentText("You cannot add more accounts than the number of accounts selected in the previous stage");
            alert.showAndWait();
        } else {
            accounts.add(accountTextBox.getText());
            accountNumberTable.setItems(accounts);
            System.out.println(accounts);
        }

    }

    public void removeAccount() {
        accounts.remove(accountNumberTable.getSelectionModel().getSelectedItem());
    }

    public void transactionCountSelection() {
        sizeOrAmountText.textProperty().bind(numberOfTransactionsSlider.valueProperty().asString("%.0f"));
    }

    public void complexitySelection() {
        switch ((int) complexitySlider.getValue()) {
            case 1 -> {
                complexity1Text.setVisible(true);
                complexity2Text.setVisible(false);
                complexity3Text.setVisible(false);
            }
            case 2 -> {
                complexity1Text.setVisible(false);
                complexity2Text.setVisible(true);
                complexity3Text.setVisible(false);
            }
            case 3 -> {
                complexity1Text.setVisible(false);
                complexity2Text.setVisible(false);
                complexity3Text.setVisible(true);
            }
        }

    }

    @FXML
    void generate() {
        boolean doubleSided = generateDoubleSides.isSelected();
        String swiftFormat = swiftFormatSelector.getValue();
        String outputPath = outputPathText.getText();
        int daysOfData = Integer.parseInt(daysOfDataText.getText());
        int numberOfAccounts = Integer.parseInt(numberOfAccountsText.getText());
        int numberOfFiles = Integer.parseInt(numberOfFilesText.getText());
        String sizeOrAmountSelected;

        int sizeOrAmountValue = Integer.parseInt(sizeOrAmountText.getText());
        final boolean randomlyGenerateAccounts = randomlyGenerateButton.isSelected();
        final boolean autoGenerateBal = balanceRandomButton.isSelected();
        String openingBalCOrDValue = "";
        BigDecimal openingBalanceAmountValue = BigDecimal.ZERO;
        String currency = "";
        LocalDate openingBalanceDateValue = LocalDate.now();
        String closingBalCorDValue = "";
        BigDecimal closingBalanceAmountValue = BigDecimal.ZERO;
        LocalDate closingBalanceDateValue = LocalDate.now();
        final boolean autoSenderBic = sBAuto.isSelected();
        final boolean autoReceiverBic = rBAuto.isSelected();

        String senderBic = "";
        String receiverBic = "";

        if (!autoSenderBic) {
            senderBic = senderBicBox.getText();
        }
        if (!autoReceiverBic) {
            receiverBic = receiverBicBox.getText();
        }
        if (!autoGenerateBal) {
            openingBalCOrDValue = openingBalCOrD.getValue();
            openingBalanceAmountValue = BigDecimal.valueOf(Double.valueOf(openingBalanceAmount.getText()));
            currency = currencies.getValue();
            openingBalanceDateValue = openingBalanceDate.getValue();
            closingBalCorDValue = closingBalCOrD.getValue();
            closingBalanceAmountValue = BigDecimal.valueOf(Double.valueOf(closingBalanceAmount.getText()));
            closingBalanceDateValue = closingBalanceDate.getValue();
        }
        BigDecimal transactionValueFrom = BigDecimal.ZERO;
        BigDecimal transactionValueTo = BigDecimal.ZERO;
        final boolean autoTxnValueSelected = autoTxnValue.isSelected();
        if (!autoTxnValueSelected && autoGenerateBal) {
            transactionValueFrom = BigDecimal.valueOf(Double.valueOf(txnValueFrom.getText()));
            transactionValueTo = BigDecimal.valueOf(Double.valueOf(txnValueTo.getText()));
        }
        final double complexity = complexitySlider.getValue();

        final int sizeOrAmountValueF = sizeOrAmountValue;
        final ObservableList<String> accountsF = accounts;
        final ObservableList<String> observableCurrenciesF = observableCurrencies;
        final String openingBalCOrDValueF = openingBalCOrDValue;
        final BigDecimal openingBalanceAmountValueF = openingBalanceAmountValue;
        final String currencyF = currency;
        final String closingBalCorDValueF = closingBalCorDValue;
        final BigDecimal closingBalanceAmountValueF = closingBalanceAmountValue;
        final LocalDate openingBalanceDateValueF = openingBalanceDateValue;
        final LocalDate closingBalanceDateValueF = closingBalanceDateValue;
        final BigDecimal transactionValueFromF = transactionValueFrom;
        final BigDecimal transactionValueToF = transactionValueTo;
        final String senderBicF = senderBic;
        final String receiverBicF = receiverBic;

        progressBar.setProgress(-1.0);
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                start = Instant.now();
                Generator.generate(swiftFormat, outputPath, daysOfData, numberOfAccounts, numberOfFiles,
                        sizeOrAmountValueF, accountsF, observableCurrenciesF, randomlyGenerateAccounts, openingBalCOrDValueF,
                        openingBalanceAmountValueF, currencyF, autoGenerateBal, closingBalCorDValueF, closingBalanceAmountValueF,
                        openingBalanceDateValueF, closingBalanceDateValueF, transactionValueFromF,
                        transactionValueToF, complexity, autoTxnValueSelected, doubleSided, autoSenderBic, autoReceiverBic, senderBicF, receiverBicF
                );
                return null;
            }
        };

        Thread t = new Thread(task);
        t.start();
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                progressBar.setProgress(100);
                Instant end = Instant.now();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("File Generation Successful");
                alert.setHeaderText(null);
                Duration d = Duration.between(start, end);
                alert.setContentText("Files have been generated in " + humanReadableFormat(d));
                alert.showAndWait();
            }
        });
    }

    public static String humanReadableFormat(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }
}
