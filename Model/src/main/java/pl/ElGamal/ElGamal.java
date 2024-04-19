package pl.ElGamal;

import java.math.BigInteger;
import java.util.Random;

public class ElGamal {
    public BigInteger p;
    public BigInteger g;
    public BigInteger a;
    public BigInteger h;
    public BigInteger r;

    // p, g, h - wygenerowane klucze publiczne.
    // a - wygenerowany klucz prywatny.

    // metoda zwraca losową liczbę pierwszą p.
    private BigInteger generateP() {
        this.p = BigInteger.probablePrime(256, new Random());
        return this.p;
    }

    // metoda zwraca losową liczbę g.
    private BigInteger generateG() {
        do {
            this.g = new BigInteger(p.bitLength(), new Random());
        } while (this.g.compareTo(BigInteger.ONE) <= 0 || this.g.compareTo(p.subtract(BigInteger.ONE)) >= 0);

        return this.g;
    }

    // metoda znajduje liczbe a.
    private BigInteger generateA() {
        do {
            this.a = new BigInteger(this.p.bitLength(), new Random());
        } while (this.a.compareTo(BigInteger.ONE) <= 0 || this.a.compareTo(this.p.subtract(BigInteger.ONE)) >= 0);

        return this.a;
    }

    // metoda oblicza liczbę h.
    private BigInteger generateH() {
        return this.h = this.g.modPow(this.a, this.p);
    }

    // metoda generuje 3 klucze publicze (p, g, h) oraz klucz prywatny a.
    public void generateKeys() {
        this.p = generateP();
        this.g = generateG();
        this.a = generateA();
        this.h = generateH();
    }

    private BigInteger generateR() {
        do {
            this.r = new BigInteger(this.p.bitLength(), new Random());
        } while (this.r.compareTo(BigInteger.ONE) <= 0 || this.r.compareTo(this.p.subtract(BigInteger.ONE)) >= 0);

        return this.r;
    }

    // metoda szyfruje liczbe m zgodnie z działaniem algorytmu ElGamala.
    public BigInteger[] encrypt(BigInteger m) {
        if (m.compareTo(this.p) < 0) {
            this.r = generateR();
        }

        BigInteger c1 = this.g.modPow(r, this.p);
        BigInteger c2 = this.h.modPow(r, this.p);

        return new BigInteger[]{c1, c2};
    }
}
