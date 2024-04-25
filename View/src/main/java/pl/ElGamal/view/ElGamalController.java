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
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);

            BigInteger[] encryptedText = elGamal.encrypt(bytes);

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

            byte[] decryptedValue = elGamal.decrypt(encryptedBigIntegers);

            String decryptedText = new String(decryptedValue, StandardCharsets.UTF_8);

            writeText.setText(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void encryptFile() {

    }


    @FXML
    public void decryptFile() {

    }
}
