package pl.ElGamal;

import java.math.BigInteger;
import java.util.Random;

public class ElGamal {
    public BigInteger p;
    public BigInteger g;

    // metoda zwraca losową liczbę pierwszą p.
    public BigInteger generateP() {
        this.p = BigInteger.probablePrime(256, new Random());
        return this.p;
    }

    // metoda zwraca losową liczbę g.
    public BigInteger generateG() {
        do {
            this.g = new BigInteger(p.bitLength(), new Random());
        } while (this.g.compareTo(BigInteger.ONE) <= 0 || this.g.compareTo(p.subtract(BigInteger.ONE)) >= 0);

        return this.g;
    }
}
