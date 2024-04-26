package pl.ElGamal.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.ElGamal.ElGamal;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ElGamalController {
    ElGamal elGamal;

    @FXML
    private TextField p;

    @FXML
    private TextField g;

    @FXML
    private TextField a;

    @FXML
    private TextField h;

    @FXML
    private TextArea writeText;

    @FXML
    private TextArea readText;

    @FXML
    private Label encryptFileStatus;

    @FXML
    private Label decryptFileStatus;

    private File encodedInputFile;

    private File decodedInputFile;

    private File encodedOutputFile;

    private File decodedOutputFile;


    public ElGamalController() {
        this.elGamal = new ElGamal();
    }

    @FXML
    public void generateKeys() {
        elGamal.generateKeys();

        BigInteger pValue = elGamal.getP();
        BigInteger gValue = elGamal.getG();
        BigInteger aValue = elGamal.getA();
        BigInteger hValue = elGamal.getH();

        p.setText(pValue.toString());
        g.setText(gValue.toString());
        a.setText(aValue.toString());
        h.setText(hValue.toString());
    }

    @FXML
    public void encryptMessage() {
        String text = writeText.getText();
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        BigInteger[] encryptedText = elGamal.encrypt(bytes);
        readText.setText(bigIntegerArrayToHexString(encryptedText));
    }

    @FXML
    public void decryptMessage() {
        String encryptedText = readText.getText();
        BigInteger[] message = hexStringToBigIntegerArray(encryptedText);
        byte[] decryptedText = elGamal.decrypt(message);
        writeText.setText(new String(decryptedText, StandardCharsets.UTF_8));
    }

    @FXML
    public void encryptFileButtonClick() throws IOException {
        byte[] message = Files.readAllBytes(Paths.get(decodedInputFile.toURI()));
        BigInteger[] encryptedText = elGamal.encrypt(message);
        String hexStringEncryptedMessage = bigIntegerArrayToHexString(encryptedText);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(encodedOutputFile))) {
            bufferedWriter.write(hexStringEncryptedMessage);
        }
        encryptFileStatus.setText("Udało się zaszyfrować plik.");
    }

    @FXML
    public void decryptFileButtonClick() throws IOException {
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(encodedInputFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line);
            }
        }
        String encryptedMessageHex = fileContent.toString();
        BigInteger[] message = hexStringToBigIntegerArray(encryptedMessageHex);
        byte[] decryptedMessage = elGamal.decrypt(message);
        Files.write(Paths.get(decodedOutputFile.toURI()), decryptedMessage);
        decryptFileStatus.setText("Udało się odszyfrować plik.");
    }

    private File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Wybierz plik do zapisu.");
        return fileChooser.showSaveDialog(new Stage());
    }

    @FXML
    public void encodedInputFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Wybierz zaszyfrowany plik");
        encodedInputFile = fileChooser.showOpenDialog(new Stage());
    }

    @FXML
    public void decodedInputFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Wybierz jawny plik.");
        decodedInputFile = fileChooser.showOpenDialog(new Stage());
    }

    @FXML
    public void encodedOutputFileButtonClick() {
        encodedOutputFile = chooseFile();
    }

    @FXML
    public void decodedOutputFileButtonClick() {
        decodedOutputFile = chooseFile();
    }

    private String bigIntegerArrayToHexString(BigInteger[] array) {
        StringBuilder hexString = new StringBuilder();
        for (BigInteger num : array) {
            hexString.append(num.toString(16));
        }
        return hexString.toString();
    }

    private BigInteger[] hexStringToBigIntegerArray(String hexString) {
        int length = hexString.length();
        int numBigIntegers = (int) Math.ceil((double) length / 64);
        BigInteger[] array = new BigInteger[numBigIntegers];

        for (int i = 0; i < numBigIntegers; i++) {
            int startIndex = i * 64;
            int endIndex = Math.min(startIndex + 64, length);
            String hexNumber = hexString.substring(startIndex, endIndex);
            array[i] = new BigInteger(hexNumber, 16);
        }
        return array;
    }
}
