package pl.ElGamal;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args){
        ElGamal elgamal = new ElGamal();

        elgamal.generateKeys();

        //BigInteger m = new BigInteger("1242252");

        String dupa = "Dupa123!";

        BigInteger m = elgamal.textToBigInteger(dupa);

        BigInteger[] C = elgamal.encrypt(m);

        System.out.println("Encrypted values:");
        for (BigInteger value : C) {
            System.out.println(value);
        }

        BigInteger D = elgamal.decrypt(C);

        System.out.println("Decrypted value:");
        System.out.println(D);

        String dupa2 = elgamal.bigIntegerToText(D);

        System.out.println(dupa2);
    }
}
