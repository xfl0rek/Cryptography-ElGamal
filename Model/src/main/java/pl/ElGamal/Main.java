package pl.ElGamal;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args){
        ElGamal elgamal = new ElGamal();

        elgamal.generateKeys();

        System.out.println("p: " + elgamal.p);
        System.out.println("g: " + elgamal.g);
        System.out.println("a: " + elgamal.a);
        System.out.println("h: " + elgamal.h);

        BigInteger m = new BigInteger("1242252");

        BigInteger[] C = elgamal.encrypt(m);

        System.out.println("Encrypted values:");
        for (BigInteger value : C) {
            System.out.println(value);
        }
    }
}
