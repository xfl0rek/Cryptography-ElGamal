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

            BigInteger fileBytesToBigInteger = new BigInteger(1, Arrays.copyOf(fileBytes, 32));

            System.out.println(fileBytesToBigInteger);

            BigInteger[] encryptedText = elGamal.encrypt(fileBytesToBigInteger);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeInt(encryptedText[0].toByteArray().length);
            dos.writeInt(encryptedText[1].toByteArray().length);

            dos.write(encryptedText[0].toByteArray());
            dos.write(encryptedText[1].toByteArray());

            byte[] encryptedBigIntegerToBytes = baos.toByteArray();

            File destination = fileChooser.showSaveDialog(new Stage());
            FileUtils.writeByteArrayToFile(destination, encryptedBigIntegerToBytes);

            fileStatus.setText("Zaszyfrowano plik.");

        } catch (Exception e) {
            e.printStackTrace();
            fileStatus.setText("Nie udało się zaszyfrować");
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
