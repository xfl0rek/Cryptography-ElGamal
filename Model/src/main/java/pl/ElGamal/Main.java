package pl.ElGamal;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args){
        ElGamal elgamal = new ElGamal();

        BigInteger p = elgamal.generateP();

        System.out.println("p: " + p);

        BigInteger g = elgamal.generateG();

        System.out.println("g: " + g);

        BigInteger a = elgamal.generateA();

        System.out.println("a: " + a);

        BigInteger h = elgamal.generateH();

        System.out.println("h: " + h);

        if (BigInteger.ONE.compareTo(g) < 0 && g.compareTo(p.subtract(BigInteger.ONE)) < 0) {
            System.out.println("DUPA");
        }
    }
}
