/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testfilegenerator;

import com.prowidesoftware.swift.model.SwiftBlock1;
import com.prowidesoftware.swift.model.field.Field20;
import com.prowidesoftware.swift.model.field.Field21;
import com.prowidesoftware.swift.model.field.Field25;
import com.prowidesoftware.swift.model.field.Field28C;
import com.prowidesoftware.swift.model.field.Field60F;
import com.prowidesoftware.swift.model.field.Field61;
import com.prowidesoftware.swift.model.field.Field62F;
import com.prowidesoftware.swift.model.field.Field64;
import com.prowidesoftware.swift.model.field.Field86;
import com.prowidesoftware.swift.model.mt.mt9xx.MT940;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author zeyne
 */
public class Generator {

    public void initialise() {
        //Pop out screen that will show progress etc

    }

    public static void generate(String swiftFormat, String outputPath, int daysOfData, int numberOfAccounts, int numberOfFiles,
            int sizeOrAmountValue, ObservableList<String> accounts, ObservableList<String> observableCurrencies, boolean randomlyGenerateAccounts, String openingBalCOrDValue,
            BigDecimal openingBalanceAmountValue, String currency, boolean autoGenerateBal, String closingBalCorDValue, BigDecimal closingBalanceAmountValue,
            LocalDate openingBalanceDateValue, LocalDate closingBalanceDateValue, BigDecimal transactionValueFrom,
            BigDecimal transactionValueTo, double complexity, boolean autoTxnValue, boolean doubleSided, boolean autoSenderBic, boolean autoReceiverBic, String senderBic, String receiverBic) throws IOException {

        //CURRENTLY ONLY SUPPORTS MT940 SO IF SWIFT FORMAT = MT940 IT GOES TO RELEVANT GENERATION METHOD
        createDayFolders(outputPath, daysOfData);
        if (swiftFormat.equals("MT940")) {
            generateMT940(swiftFormat, outputPath, daysOfData, numberOfAccounts, numberOfFiles,
                    sizeOrAmountValue, accounts, observableCurrencies, randomlyGenerateAccounts, openingBalCOrDValue,
                    openingBalanceAmountValue, currency, autoGenerateBal, closingBalCorDValue, closingBalanceAmountValue,
                    openingBalanceDateValue, closingBalanceDateValue, transactionValueFrom,
                    transactionValueTo, complexity, autoTxnValue, doubleSided, autoSenderBic, autoReceiverBic, senderBic, receiverBic);
        }

    }

    //METHOD TO GENERATE MT940'S
    public static void generateMT940(String swiftFormat, String outputPath, int daysOfData, int numberOfAccounts, int numberOfFiles,
            int sizeOrAmountValue, ObservableList<String> accounts, ObservableList<String> observableCurrencies, boolean randomlyGenerateAccounts, String openingBalCOrDValue,
            BigDecimal openingBalanceAmountValue, String currency, boolean autoGenerateBal, String closingBalCorDValue, BigDecimal closingBalanceAmountValue,
            LocalDate openingBalanceDateValue, LocalDate closingBalanceDateValue, BigDecimal transactionValueFrom,
            BigDecimal transactionValueTo, double complexity, boolean autoTxnValue, boolean doubleSided, boolean autoSenderBic, boolean autoReceiverBic, String senderBic, String receiverBic) throws IOException {

        Instant start = Instant.now();
        MT940[][][] allMessages = new MT940[numberOfAccounts + 1][daysOfData + 1][numberOfFiles + 1];
        for (int numAccounts = 1; numAccounts <= numberOfAccounts; numAccounts++) {
            Field60F firstOpeningBal = createFirstOpeningBal(numberOfFiles, numAccounts, allMessages, openingBalCOrDValue, openingBalanceDateValue, openingBalanceAmountValue, currency, observableCurrencies, autoGenerateBal);
            Field62F lastClosingBal = createLastClosingBal(firstOpeningBal, daysOfData, numberOfFiles, numAccounts, allMessages, closingBalCorDValue, closingBalanceDateValue, closingBalanceAmountValue, currency, observableCurrencies, autoGenerateBal);
            for (int day = 1; day <= daysOfData; day++) {
                for (int file = 1; file <= numberOfFiles; file++) {
                    MT940 m = new MT940();
                    SwiftBlock1 b1 = new SwiftBlock1();
                    b1.setSessionNumber("1234");
                    b1.setSequenceNumber("567890");
                    m.getSwiftMessage().setBlock1(b1);
                    if (!autoSenderBic) {
                        m.setSender(senderBic);
                    } else {
                        m.setSender("TESTBIC");
                    }
                    if (!autoReceiverBic) {
                        m.setReceiver(receiverBic);
                    } else {
                        m.setReceiver("TESTBIC");
                    }
                    m.addField(new Field20(createReference(file)));
                    allMessages[numAccounts][day][file] = m;
                    if (complexity == 3) {
                        m.addField(createRelatedReference(m, allMessages, numAccounts, day, file));
                    }
                    m.addField(new Field25(createAccount(allMessages, file, numAccounts, accounts, randomlyGenerateAccounts)));
                    m.addField(new Field28C(createStatSeqNo(day, file)));
                    m.addField(createOpBal(firstOpeningBal, day, file, numberOfFiles, numAccounts, allMessages, openingBalCOrDValue, openingBalanceDateValue, openingBalanceAmountValue, currency, observableCurrencies, autoGenerateBal));
                    allMessages[numAccounts][day][file] = m;
                    for (int numberOfStatements = 1; numberOfStatements <= sizeOrAmountValue; numberOfStatements++) {
                        m.addField((createStatement(m, allMessages, numAccounts, day, file, daysOfData, numberOfFiles, numberOfStatements, sizeOrAmountValue, transactionValueFrom, transactionValueTo, lastClosingBal, autoTxnValue, complexity, firstOpeningBal)));
                        if (complexity >= 2) {
                            m.addField(createStatementNarrative(m, allMessages, numAccounts, day, file, numberOfStatements));
                        }
                    }
                    allMessages[numAccounts][day][file] = m;
                    m.addField(createCloseBal(lastClosingBal, day, file, numAccounts, numberOfFiles, daysOfData, allMessages, closingBalCorDValue, closingBalanceDateValue, closingBalanceAmountValue, currency, observableCurrencies, autoGenerateBal));
                    allMessages[numAccounts][day][file] = m;
                    if (complexity == 3) {
                        m.addField(createAvailableBal(m));
                        m.addField(createFinalNarrative(m, allMessages, numAccounts, day, file));
                    }
                    allMessages[numAccounts][day][file] = m;
                }
                if (doubleSided) {
                    generateOppositeSide(allMessages, numAccounts, day, outputPath);
                }
            }
        }
        try {
            outputMessage(allMessages, outputPath);
        } catch (InterruptedException ex) {
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Thrown Exception");
            alert.setContentText("Program has thrown an exception.");
        }
    }

    //CREATES THE FOLDERS FOR EACH DAY
    public static void createDayFolders(String outputPath, int daysOfData) {
        //Create method that creates folders for each day
        for (int i = 1; i <= daysOfData; i++) {
            String path = outputPath + "\\Day" + String.valueOf(i);
            new File(path).mkdirs();
        }
    }

    //CREATES A SIMPLE REFERENCE FOR THE MT940
    public static String createReference(int file) {
        String reference = "REFFORFILE" + String.valueOf(file);
        return reference;

    }

    //CREATES THE ACCOUNTS THROUGHOUT THE GENERATION PROCESS DEPENDING ON RANDOM GENERATION OR USER INPUTS
    public static String createAccount(MT940[][][] allMessages, int file, int numAccounts, ObservableList<String> accounts, boolean randomlyGenerateAccounts) {
        String accountCode;
        if (file == 1) {
            if (!randomlyGenerateAccounts) {
                accountCode = accounts.get(numAccounts - 1);
            } else {
                Random rand = new Random();
                int intRandomNumber = rand.nextInt(99998999);
                intRandomNumber += 1000;
                accountCode = "RAND" + String.valueOf(intRandomNumber);
            }
        } else {
            accountCode = allMessages[numAccounts][1][1].getField25().getAccount();
        }

        return accountCode;
    }

    //RETURNS THE STATEMENT/SEQUENCE NUMBER FOR THE SWIFT MESSAGE
    public static String createStatSeqNo(int day, int file) {
        String StatSeqNo = day + "/" + file;
        return StatSeqNo;
    }

    //CREATES THE FIRST OPENING BALANCE DEPENDING ON USER INPUT OR RANDOM GENERATION
    public static Field60F createFirstOpeningBal(int numAccounts, int numberOfFiles, MT940[][][] allMessages, String openingBalCOrDValue, LocalDate openingBalanceDateValue, BigDecimal openingBalanceAmountValue, String currency, ObservableList<String> observableCurrencies, boolean autoGenerateOpeningBal) {
        Field60F f60f = new Field60F();
        if (autoGenerateOpeningBal) {
            String[] dOrC = new String[]{"C", "D"};
            int rnd = new Random().nextInt(dOrC.length);
            f60f.setComponent1(dOrC[rnd]);

            LocalDate startDate = LocalDate.of(2020, 1, 1); //start date
            long start = startDate.toEpochDay();
            LocalDate endDate = LocalDate.now(); //end date
            long end = endDate.toEpochDay();
            long randomEpochDay = ThreadLocalRandom.current().longs(start, end).findAny().getAsLong();
            LocalDate openingBalDate = LocalDate.ofEpochDay(randomEpochDay);
            Date d = Date.from(openingBalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            f60f.setComponent2(c);

            Random r = new Random();
            String openingBalCurrency = observableCurrencies.get(r.nextInt(observableCurrencies.size()));
            f60f.setComponent3(openingBalCurrency);

            float openingBalAmount;
            Random rand = new Random();
            openingBalAmount = rand.nextFloat(999999);

            f60f.setComponent4(BigDecimal.valueOf(openingBalAmount).setScale(2, BigDecimal.ROUND_HALF_UP));

        } else {
            f60f.setComponent1(openingBalCOrDValue);

            Date d = Date.from(openingBalanceDateValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            f60f.setComponent2(c);
            f60f.setComponent3(currency);
            f60f.setComponent4(openingBalanceAmountValue);

        }
        return f60f;
    }

    //CREATES THE LAST CLOSING BALANCE DEPENDING ON USER INPUT OR RANDOM GENERATION
    public static Field62F createLastClosingBal(Field60F firstOpeningBal, int daysOfData, int numberOfFiles, int numAccounts, MT940[][][] allMessages, String closingBalCOrDValue, LocalDate closingBalanceDateValue, BigDecimal closingBalanceAmountValue, String currency, ObservableList<String> observableCurrencies, boolean autoGenerateClosingBal) {
        Field62F f62f = new Field62F();
        if (autoGenerateClosingBal) {
            String[] dOrC = new String[]{"C", "D"};
            int rnd = new Random().nextInt(dOrC.length);
            f62f.setComponent1(dOrC[rnd]);
            String closingBalDorC = dOrC[rnd];

            String closingDateString = firstOpeningBal.getComponent2();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            LocalDate closingDate = LocalDate.parse(closingDateString, formatter);
            closingDate.plusDays(daysOfData - 1);
            Date d = Date.from(closingDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            f62f.setComponent2(c);

            f62f.setComponent3(firstOpeningBal.getComponent3());

            int closingBalAmount;
            Random rand = new Random();
            closingBalAmount = rand.nextInt(999999);
            f62f.setComponent4(BigDecimal.valueOf(closingBalAmount).setScale(2, BigDecimal.ROUND_HALF_UP));

        } else {
            f62f.setComponent1(closingBalCOrDValue);

            String closingDateString = firstOpeningBal.getComponent2();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            LocalDate closingDate = LocalDate.parse(closingDateString, formatter);
            closingDate.plusDays(daysOfData - 1);
            Date d = Date.from(closingDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            f62f.setComponent2(c);

            f62f.setComponent3(currency);
            f62f.setComponent4(closingBalanceAmountValue);

        }
        return f62f;

    }

    //CREATES THE OPENING BALANCE BY GETTING THE FIRST OPENING BALANCE OR BY GETTING THE CLOSING BALANCE FOR THE PREVIOUS DAY
    public static Field60F createOpBal(Field60F firstOpeningBal, int day, int file, int numberOfFiles, int numAccounts, MT940[][][] allMessages, String openingBalCOrDValue, LocalDate openingBalanceDateValue, BigDecimal openingBalanceAmountValue, String currency, ObservableList<String> observableCurrencies, boolean autoGenerateOpeningBal) {
        Field60F f60f = new Field60F();
        if (day == 1 && file == 1) {
            f60f = firstOpeningBal;
        } else if (day != 1 && file == 1) {
            Field62F previousClosingBalance = allMessages[numAccounts][day - 1][numberOfFiles].getField62F();
            Calendar c = previousClosingBalance.getComponent2AsCalendar();
            c.add(Calendar.DATE, 1);
            f60f.setComponent1(previousClosingBalance.getComponent1());
            f60f.setComponent2(c);
            f60f.setComponent3(previousClosingBalance.getComponent3());
            f60f.setComponent4(previousClosingBalance.getComponent4AsBigDecimal());

        } else {
            Field62F previousClosingBalance = allMessages[numAccounts][day][file - 1].getField62F();
            Calendar c = previousClosingBalance.getComponent2AsCalendar();
            f60f.setComponent1(previousClosingBalance.getComponent1());
            f60f.setComponent2(c);
            f60f.setComponent3(previousClosingBalance.getComponent3());
            f60f.setComponent4(previousClosingBalance.getComponent4AsBigDecimal());
        }
        return f60f;
    }

    //CREATES THE TRANSACTION STATEMENT USING VARIOUS PROPERTIES DEPENDING ON COMPLEXITY OF FILE
    public static Field61 createStatement(MT940 m, MT940[][][] allMessages, int numAccounts, int day, int file, int daysOfData, int numberOfFiles, int numberOfStatements, int sizeOrAmountValue,
            BigDecimal transactionValueFrom, BigDecimal transactionValueTo, Field62F lastClosingBal, boolean autoTxnValue, double complexity, Field60F firstOpeningBal) {

        Field61 f61 = new Field61();
        f61.setComponent1(allMessages[numAccounts][day][file].getField60F().getComponent2());
        BigDecimal startingBal = allMessages[numAccounts][day][file].getField60F().getComponent4AsBigDecimal();
        BigDecimal firstOpeningBalFinal = firstOpeningBal.getComponent4AsBigDecimal();
        BigDecimal lastClosingBalFinal = lastClosingBal.getComponent4AsBigDecimal();
        ArrayList<String> txnCodes = new ArrayList<String>(Arrays.asList("S409", "S498", "S524", "S969", "NTRF", "NCHK", "NCOM", "NMSC", "NDDT", "NCHG", "NBRF", "NCMP", "S198",
                "S224", "S108", "S950"));
        Random r = new Random();

        //IF IT IS THE LAST STATEMENT FOR THE LAST FILE & LAST DAY, THE PROGRAM NEEDS TO ENSURE THAT THE LAST CLOSING BALANCE SPECIFIED BY USER IS MET INSTEAD OF RANDOMLY
        //GENERATING A TRANSACTION
        if (numberOfStatements == sizeOrAmountValue && day == daysOfData && file == numberOfFiles) {
            List<Field61> allTransactions = allMessages[numAccounts][day][file].getField61();
            BigDecimal penultimateBal = startingBal;
            for (int x = 0; x < allTransactions.size(); x++) {
                BigDecimal txnValue = allTransactions.get(x).amount();
                if (allTransactions.get(x).getDebitCreditMark().equals("D")) {
                    txnValue = txnValue.negate();
                }
                penultimateBal = penultimateBal.add(txnValue);
            }
            BigDecimal finalTxnValue = lastClosingBalFinal.subtract(penultimateBal);
            f61.setComponent5(finalTxnValue.abs());
            if (finalTxnValue.compareTo(BigDecimal.ZERO) < 0) {
                f61.setComponent3("D");
            } else {
                f61.setComponent3("C");
            }
            if (complexity == 3) {
                if (f61.getComponent3().equals("D")) {
                    f61.setComponent6(txnCodes.get(r.nextInt(txnCodes.size() - 4)));
                }
                if (f61.getComponent3().equals("C")) {
                    f61.setComponent6(txnCodes.get(r.nextInt(txnCodes.size() - 4) + 4));
                }
            } else {
                f61.setComponent6("NTRF");
            }
        } else {
            String[] dOrC = new String[]{"C", "D"};
            int rnd = new Random().nextInt(dOrC.length);
            String txnDorC = dOrC[rnd];
            f61.setComponent3(txnDorC);
            Random rand = new Random();
            BigDecimal txnAmount;
            DecimalFormat formatter = new DecimalFormat();
            formatter.setMaximumFractionDigits(2);
            if (autoTxnValue) {
                int startingBalDigits = firstOpeningBalFinal.setScale(0, RoundingMode.DOWN).toString().length();
                int closingBalDigits = lastClosingBalFinal.setScale(0, RoundingMode.DOWN).toString().length();
                int digits = Math.max(startingBalDigits, closingBalDigits);

                txnAmount = BigDecimal.valueOf(rand.nextFloat((float) Math.pow(9, digits)));
                txnAmount = txnAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                txnAmount = BigDecimal.valueOf(rand.nextDouble(transactionValueFrom.doubleValue(), transactionValueTo.add(BigDecimal.ONE).doubleValue()));
                txnAmount = txnAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            if (complexity == 3) {
                if (f61.getComponent3().equals("D")) {
                    f61.setComponent6(txnCodes.get(r.nextInt(txnCodes.size() - 4)));
                }
                if (f61.getComponent3().equals("C")) {
                    f61.setComponent6(txnCodes.get(r.nextInt(txnCodes.size() - 4) + 4));
                }
            } else {
                f61.setComponent6("NTRF");
            }
            f61.setAmount(txnAmount);

        }
        f61.setComponent8(allMessages[numAccounts][day][file].getField25().getAccount() + numberOfStatements);
        f61.setComponent9("REF" + numberOfStatements + f61.getComponent6() + "D" + day + "F" + file);
        return f61;
    }

    //CREATES THE STATEMENT NARRATIVE DEPENDING ON TXN TYPE
    public static Field86 createStatementNarrative(MT940 m, MT940[][][] allMessages, int numAccounts, int day, int file, int numberOfStatements) {
        Field86 f86 = new Field86();
        String txnType = m.getField61().get(numberOfStatements - 1).getComponent7();
        String txnMessage = "";
        switch (txnType) {
            case "409" ->
                txnMessage = "ANYDEBIT";
            case "498", "CMP", "198" ->
                txnMessage = "COMPENSATION";
            case "524", "COM", "224" ->
                txnMessage = "COMMISSION";
            case "969" ->
                txnMessage = "MISCDEBIT";
            case "TRF" ->
                txnMessage = "TRANSFER";
            case "CHK" ->
                txnMessage = "CHEQUE";
            case "MSC" ->
                txnMessage = "MISCALLANEOUS";
            case "DDT" ->
                txnMessage = "DIRECTDEBITITEM";
            case "CHG" ->
                txnMessage = "CHARGES";
            case "BRF" ->
                txnMessage = "BROKERAGEFEE";
            case "108" ->
                txnMessage = "ANYCREDIT";
            case "950" ->
                txnMessage = "MISCCREDIT";
        }

        f86.setNarrative(allMessages[numAccounts][day][file].getField25().getAccount() + " TXNNARRATIVE" + numberOfStatements + " FOR " + txnMessage);
        return f86;
    }

    //CREATES FINAL NARRATIVE
    public static Field86 createFinalNarrative(MT940 m, MT940[][][] allMessages, int numAccounts, int day, int file) {
        Field86 f86 = new Field86();
        f86.setNarrative(allMessages[numAccounts][day][file].getField25().getAccount() + "FINAL FILE NARRATIVE FOR DAY " + day + " AND FILE " + file);
        return f86;
    }

    //CREATES RELATED REFERENCE
    public static Field21 createRelatedReference(MT940 m, MT940[][][] allMessages, int numAccounts, int day, int file) {
        Field21 f21 = new Field21();
        f21.setReference("SWIFT FILE GENERATOR " + "OPENREFD" + day + "F" + file);
        return f21;
    }

    //CREATES THE CLOSING BALANCE BY ADDING THE TRANSACTIONS TO THE OPENING BALANCE FOR THE SPECIFIC FILE
    public static Field62F createCloseBal(Field62F lastClosingBal, int day, int file, int numAccounts, int numberOfFiles, int daysOfData, MT940[][][] allMessages, String closingBalCOrDValue, LocalDate closingBalanceDateValue, BigDecimal closingBalanceAmountValue, String currency, ObservableList<String> observableCurrencies, boolean autoGenerateClosingBal
    ) {
        Field62F f62f = new Field62F(allMessages[numAccounts][day][file].getField60F().getValue());
        BigDecimal startingBal = allMessages[numAccounts][day][file].getField60F().getComponent4AsBigDecimal();
        List<Field61> allTransactions = allMessages[numAccounts][day][file].getField61();
        for (Field61 f : allTransactions) {
            if (f.getComponent3().equals("D")) {
                startingBal = startingBal.subtract(f.getComponent5AsBigDecimal());
            } else {
                startingBal = startingBal.add(f.getComponent5AsBigDecimal());
            }
        }
        BigDecimal closingBal = startingBal;
        if (closingBal.compareTo(BigDecimal.ZERO) < 0) {
            f62f.setComponent1("D");
        } else {
            f62f.setComponent1("C");
        }
        f62f.setComponent4(closingBal.abs());
        return f62f;
    }

    //CREATES AVAILABLE BAALNCE
    public static Field64 createAvailableBal(MT940 m) {
        String closingBal = m.getField62F().getValue();
        Field64 f64a = new Field64(closingBal);
        return f64a;
    }

    //FUNCTIONALITY TO CREATE INTERNAL LEDGER CSV FILE THAT CORRESPONDS TO THE SWIFT MESSAGE(S) FOR THAT ACCOUNT&DAY
    public static void generateOppositeSide(MT940[][][] allMessages, int numAccounts, int day, String outputPath) throws FileNotFoundException, IOException {
        List<String[]> data = new ArrayList<>();
        String account = allMessages[numAccounts][day][1].getField25().getComponent1();
        for (int i = 1; i <= allMessages[numAccounts][day].length - 1; i++) {
            List<Field61> list = allMessages[numAccounts][day][i].getField61();
            for (int x = 0; x < list.size(); x++) {
                Field61 txn = list.get(x);
                data.add(new String[]{account, txn.getComponent1(), txn.getComponent3(), txn.getComponent5(), txn.getComponent6() + txn.getComponent7(),
                    txn.getComponent8(), txn.getComponent9()});
            }
        }
        String filename = "Day" + day + "Account" + numAccounts + " Internal Ledger";
        String finalPath = outputPath + "\\Day" + day;
        File fileToWrite = new File(finalPath, filename + ".csv");
        try ( CSVPrinter p = new CSVPrinter(new FileWriter(fileToWrite), CSVFormat.EXCEL)) {
            p.printRecord("Account number", "Value Date", "Debit/Credit", "Amount", "Txn Code", "Reference to Account Owner", "Reference to Servicing Institution");
            for (int i = 0; i < data.size(); i++) {
                p.printRecord(data.get(i));
            }

        }
    }

    //FUNCTIONALITY TO OUTPUT THE MESSAGE USING MULTITHREADING IN THE OUTPUTTER CLASS
    public static void outputMessage(MT940[][][] allMessages, String outputPath) throws InterruptedException {
        int halfway = (allMessages.length - 1) / 2;
        Thread t1 = new Thread(new Outputter(1, halfway, allMessages, outputPath));
        Thread t2 = new Thread(new Outputter(halfway + 1, allMessages.length - 1, allMessages, outputPath));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
