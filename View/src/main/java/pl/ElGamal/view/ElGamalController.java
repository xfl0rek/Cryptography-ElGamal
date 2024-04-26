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

    public void encryptFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Proszę wybrać plik");
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        try {
            FileInputStream fis = new FileInputStream(selectedFile);
            byte[] fileBytes = fis.readAllBytes();
            fis.close();

            int blockSize = (elGamal.getP().bitLength() - 1) / 8;
            int numBlocks = (fileBytes.length + blockSize - 1) / blockSize;

            ByteArrayOutputStream encryptedBaos = new ByteArrayOutputStream();

            for (int i = 0; i < numBlocks; i++) {
                byte[] block = Arrays.copyOfRange(fileBytes, i * blockSize, Math.min((i + 1) * blockSize, fileBytes.length));
                BigInteger[] encryptedBlock = elGamal.encrypt(block);
                for (BigInteger num : encryptedBlock) {
                    byte[] numBytes = num.toByteArray();
                    encryptedBaos.write(numBytes.length);
                    encryptedBaos.write(numBytes);
                }
            }

            File destination = fileChooser.showSaveDialog(new Stage());
            FileOutputStream fos = new FileOutputStream(destination);
            encryptedBaos.writeTo(fos);
            fos.close();

            fileStatus.setText("Zaszyfrowano plik.");

        } catch (Exception e) {
            e.printStackTrace();
            fileStatus.setText("Nie udało się zaszyfrować pliku.");
        }
    }

    public void decryptFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Proszę wybrać plik");
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        try {
            FileInputStream fis = new FileInputStream(selectedFile);
            ByteArrayOutputStream decryptedBaos = new ByteArrayOutputStream();

            while (fis.available() > 0) {
                int c1Length = fis.read();
                byte[] c1Bytes = new byte[c1Length];
                fis.read(c1Bytes);
                BigInteger c1 = new BigInteger(c1Bytes);

                int c2Length = fis.read();
                byte[] c2Bytes = new byte[c2Length];
                fis.read(c2Bytes);
                BigInteger c2 = new BigInteger(c2Bytes);

                byte[] block = elGamal.decrypt(new BigInteger[]{c1, c2});
                decryptedBaos.write(block);
            }

            fis.close();

            File destination = fileChooser.showSaveDialog(new Stage());
            FileOutputStream fos = new FileOutputStream(destination);
            decryptedBaos.writeTo(fos);
            fos.close();

            fileStatus.setText("Odszyfrowano plik.");

        } catch (Exception e) {
            e.printStackTrace();
            fileStatus.setText("Nie udało się odszyfrować pliku.");
        }
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
