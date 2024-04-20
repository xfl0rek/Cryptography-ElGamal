package pl.ElGamal.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import pl.ElGamal.ElGamal;

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
