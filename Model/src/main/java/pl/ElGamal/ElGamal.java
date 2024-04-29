package pl.ElGamal;

import java.math.BigInteger;
import java.security.SecureRandom;

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
        return this.p = BigInteger.probablePrime(KEY_LENGTH + 2, new SecureRandom());
    }

    // metoda zwraca losową liczbę g.
    private BigInteger generateG() {
        return this.g = new BigInteger(KEY_LENGTH, new SecureRandom());
    }

    // metoda znajduje liczbe a.
    private BigInteger generateA() {
        return this.a = new BigInteger(KEY_LENGTH, new SecureRandom());
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
        return this.r = new BigInteger(KEY_LENGTH - 10, new SecureRandom());
    }

    // metoda szyfruje liczbe m zgodnie z działaniem algorytmu ElGamala.
    public BigInteger[] encrypt(byte[] message) {
        BigInteger m = new BigInteger(1, message);
        this.r = generateR();

        BigInteger c1 = this.g.modPow(r, this.p);
        BigInteger c2 = m.multiply(this.h.modPow(r, this.p));

        BigInteger[] result = new BigInteger[2];
        result[0] = c1;
        result[1] = c2;

        return result;
    }

    //metoda deszyfruje zgodnie z działaniem algorytmu ElGamala.
    public byte[] decrypt(BigInteger[] C) {
        BigInteger c1 = C[0];
        BigInteger c2 = C[1];

        BigInteger c1powA = c1.modPow(this.a, this.p);
        BigInteger result = c2.divide(c1powA);

        return result.toByteArray();
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
