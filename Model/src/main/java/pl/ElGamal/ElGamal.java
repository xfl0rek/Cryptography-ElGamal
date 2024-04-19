package pl.ElGamal;

import java.math.BigInteger;
import java.util.Random;

public class ElGamal {
    public BigInteger p;
    public BigInteger g;
    public BigInteger a;
    public BigInteger h;

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

    // metoda znajduje liczbe a.
    public BigInteger generateA() {
        do {
            this.a = new BigInteger(this.p.bitLength(), new Random());
        } while (this.a.compareTo(BigInteger.ONE) <= 0 || this.a.compareTo(this.p.subtract(BigInteger.ONE)) >= 0);

        return this.a;
    }
}
