package pl.ElGamal.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.ElGamal.ElGamal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private File file;

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
        BigInteger[] C = elGamal.encrypt(bytes);
        String result = C[0].toString(16) + '\n' + C[1].toString(16);
        readText.setText(result);
    }

    @FXML
    public void decryptMessage() {
        String[] content = readText.getText().split("\n");
        BigInteger c1 = new BigInteger(content[0], 16);
        BigInteger c2 = new BigInteger(content[1], 16);
        BigInteger[] C = {c1, c2};
        byte[] decrypted = elGamal.decrypt(C);
        String D = new String(decrypted, StandardCharsets.UTF_8);
        writeText.setText(D);
    }

    @FXML
    public void encryptFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Proszę wybrać plik.");
        File file2 = fileChooser.showOpenDialog(new Stage());
        if (file2 != null) {
            byte[] content = Files.readAllBytes(Paths.get(file2.getPath()));
            BigInteger[] C = elGamal.encrypt(content);
            String result = C[0].toString() + '\n' + C[1].toString();
            byte[] encryptedBytes = result.getBytes(StandardCharsets.UTF_8);
            fileChooser.setTitle("Zapisz plik");
            file = fileChooser.showSaveDialog(new Stage());
            encryptFileStatus.setText("Udało się zaszyfrować plik.");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(encryptedBytes);
            } catch (Exception e) {
                e.printStackTrace();
                encryptFileStatus.setText("Nie udało się zaszyfrować pliku.");
            }
        }
    }

    @FXML
    public void decryptFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Proszę wybrać plik.");
        File file2 = fileChooser.showOpenDialog(new Stage());
        if (file2 != null) {
            byte[] content = Files.readAllBytes(Paths.get(file2.getPath()));
            String[] parts = new String(content).split("\n");
            BigInteger c1 = new BigInteger(parts[0]);
            BigInteger c2 = new BigInteger(parts[1]);
            BigInteger[] C = {c1, c2};
            byte[] decrypted = elGamal.decrypt(C);
            fileChooser.setTitle("Zapisz plik");
            file = fileChooser.showSaveDialog(new Stage());
            decryptFileStatus.setText("Udało się odszyfrować plik.");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(decrypted);
            } catch (Exception e) {
                e.printStackTrace();
                decryptFileStatus.setText("Nie udało się odszyfrować pliku.");
            }
        }
    }
}
