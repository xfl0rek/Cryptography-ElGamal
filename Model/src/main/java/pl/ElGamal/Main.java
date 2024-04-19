package pl.ElGamal;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args){
        ElGamal elgamal = new ElGamal();

        BigInteger dupa = elgamal.generateP();

        System.out.println(dupa);
    }
}
