package pl.ElGamal;

import java.math.BigInteger;
import java.util.Random;

public class ElGamal {
    public BigInteger p;

    // metoda zwraca losową liczbę pierwszą p.
    public BigInteger generateP() {
        this.p = BigInteger.probablePrime(256, new Random());
        return this.p;
    }
}
