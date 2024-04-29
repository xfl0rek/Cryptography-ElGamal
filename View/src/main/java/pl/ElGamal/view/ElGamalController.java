package pl.ElGamal.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import pl.ElGamal.ElGamal;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

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

        p.setText(pValue.toString(16));
        g.setText(gValue.toString(16));
        a.setText(aValue.toString(16));
        h.setText(hValue.toString(16));
    }

    @FXML
    public void encryptMessage() {
        String text = writeText.getText();
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        BigInteger[] encryptedText = elGamal.encrypt(bytes);
        String result = encryptedText[0].toString(16) + '\n' + encryptedText[1].toString(16);
        readText.setText(result);
    }

    @FXML
    public void decryptMessage() {
        String[] content = readText.getText().split("\n");
        BigInteger c1 = new BigInteger(content[0], 16);
        BigInteger c2 = new BigInteger(content[1], 16);
        BigInteger[] encrypted = {c1, c2};
        byte[] decrypted = elGamal.decrypt(encrypted);
        String result = new String(decrypted, StandardCharsets.UTF_8);
        writeText.setText(result);
    }

    @FXML
    public void encryptFile() {

    }

    @FXML
    public void decryptFile() {

    }
}
