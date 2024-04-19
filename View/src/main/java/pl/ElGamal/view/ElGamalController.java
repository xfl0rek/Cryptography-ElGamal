package pl.ElGamal.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import pl.ElGamal.ElGamal;

import java.math.BigInteger;

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
}
