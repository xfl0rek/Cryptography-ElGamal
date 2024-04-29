package pl.ElGamal;

import java.math.BigInteger;
import java.util.Random;

public class ElGamal {
    private BigInteger p;
    private BigInteger g;
    private BigInteger a;
    private BigInteger h;
    private BigInteger r;
    private final int KEY_LENGTH = 510;

    // p, g, h - wygenerowane klucze publiczne.
    // a - wygenerowany klucz prywatny.

    // metoda zwraca losową liczbę pierwszą p.
    private BigInteger generateP() {
        this.p = BigInteger.probablePrime(KEY_LENGTH + 2, new Random());
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

    //metoda generuje liczbę r potrzebną do szyfrowania.
    private BigInteger generateR() {
        do {
            this.r = new BigInteger(this.p.bitLength(), new Random());
        } while (this.r.compareTo(BigInteger.ONE) <= 0 || this.r.compareTo(this.p.subtract(BigInteger.ONE)) >= 0);

        return this.r;
    }

    // metoda szyfruje liczbe m zgodnie z działaniem algorytmu ElGamala.
    public BigInteger[] encrypt(byte[] message) {
        BigInteger m = new BigInteger(1, message);
        if (m.compareTo(this.p) < 0) {
            this.r = generateR();
        }

        BigInteger c1 = this.g.modPow(r, this.p);
        BigInteger c2 = m.multiply(this.h.modPow(r, this.p)).mod(this.p);

        return new BigInteger[]{c1, c2};
    }

    //metoda deszyfruje zgodnie z działaniem algorytmu ElGamala.
    public byte[] decrypt(BigInteger[] C) {
        BigInteger c1 = C[0];
        BigInteger c2 = C[1];

        BigInteger c1powA = c1.modPow(this.a, this.p);
        BigInteger inverse = c1powA.modInverse(this.p);

        return c2.multiply(inverse).mod(this.p).toByteArray();
    }

    // gettery potrzebne do controllera.
    public BigInteger getP() {
        return p;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger getH() {
        return h;
    }
}
