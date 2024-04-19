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
    }
}
