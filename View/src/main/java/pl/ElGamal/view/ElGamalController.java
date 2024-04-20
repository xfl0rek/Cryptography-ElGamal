package pl.ElGamal.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import pl.ElGamal.ElGamal;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
    private Label fileStatus;

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
        try {
            String text = writeText.getText();
            BigInteger BigIntegerText = textToBigInteger(text);

            BigInteger[] encryptedText = elGamal.encrypt(BigIntegerText);

            readText.setText(Arrays.toString(encryptedText));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void decryptMessage() {
        try {
            String encryptedTextStr = readText.getText();
            encryptedTextStr = encryptedTextStr.substring(1, encryptedTextStr.length() - 1);

            String[] encryptedValues = encryptedTextStr.split(", ");
            BigInteger[] encryptedBigIntegers = new BigInteger[encryptedValues.length];

            for (int i = 0; i < encryptedValues.length; i++) {
                encryptedBigIntegers[i] = new BigInteger(encryptedValues[i]);
            }

            BigInteger decryptedValue = elGamal.decrypt(encryptedBigIntegers);

            String decryptedText = bigIntegerToText(decryptedValue);

            writeText.setText(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void encryptFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Proszę wybrać plik");
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        try {
            byte[] fileBytes = FileUtils.readFileToByteArray(selectedFile);
            int blockSize = elGamal.getP().bitLength() / 8;
            int numBlocks = (fileBytes.length + blockSize - 1) / blockSize;

            StringBuilder encryptedData = new StringBuilder();

            for (int i = 0; i < numBlocks; i++) {
                byte[] block = Arrays.copyOfRange(fileBytes, i * blockSize, Math.min((i + 1) * blockSize, fileBytes.length));
                BigInteger blockBigInteger = new BigInteger(1, block);
                BigInteger[] encryptedBlock = elGamal.encrypt(blockBigInteger);
                encryptedData.append(encryptedBlock[0]).append("\n").append(encryptedBlock[1]).append("\n");
            }

            File destination = fileChooser.showSaveDialog(new Stage());
            FileUtils.writeStringToFile(destination, encryptedData.toString(), StandardCharsets.UTF_8);

            fileStatus.setText("Zaszyfrowano plik.");

        } catch (Exception e) {
            e.printStackTrace();
            fileStatus.setText("Nie udało się zaszyfrować pliku.");
        }
    }


    @FXML
    public void decryptFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Proszę wybrać plik");
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        try {
            String encryptedText = FileUtils.readFileToString(selectedFile, StandardCharsets.UTF_8);
            String[] encryptedLines = encryptedText.split("\n");

            ByteArrayOutputStream decryptedBaos = new ByteArrayOutputStream();

            for (int i = 0; i < encryptedLines.length; i += 2) {
                BigInteger c1 = new BigInteger(encryptedLines[i]);
                BigInteger c2 = new BigInteger(encryptedLines[i + 1]);
                BigInteger[] encryptedData = {c1, c2};
                BigInteger decryptedValue = elGamal.decrypt(encryptedData);
                byte[] decryptedBytes = decryptedValue.toByteArray();
                decryptedBaos.write(decryptedBytes);
            }

            byte[] decryptedData = decryptedBaos.toByteArray();

            File destination = fileChooser.showSaveDialog(new Stage());
            FileUtils.writeByteArrayToFile(destination, decryptedData);

            fileStatus.setText("Odszyfrowano plik.");

        } catch (Exception e) {
            e.printStackTrace();
            fileStatus.setText("Nie udało się odszyfrować pliku.");
        }
    }


    // metoda zamieniająca text na BigInteger.
    private BigInteger textToBigInteger(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return new BigInteger(bytes);
    }

    // metoda zamieniająca BigInteger na text.
    private String bigIntegerToText(BigInteger bigInteger) {
        byte[] bytes = bigInteger.toByteArray();
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
