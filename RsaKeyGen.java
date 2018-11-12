import java.util.*;
import java.math.*;
import java.io.*;
public class RsaKeyGen
{
  public static void main(String[] args) throws IOException
  {
    Random rand = new Random();
    BigInteger P = new BigInteger(RandomPrime.generate(256, rand));
    BigInteger Q = new BigInteger(RandomPrime.generate(256, rand));
    BigInteger N = P.multiply(Q);
    BigInteger phiN = P.subtract(BigInteger.ONE).multiply(Q.subtract(BigInteger.ONE));
    BigInteger E = new BigInteger(RandomPrime.generate(512, rand));
    System.out.println("Generating E...");
    E = generateE(E, phiN, rand);
    System.out.println("Generated");
    BigInteger D = E.modInverse(phiN);
    pubKeyPrint(E, N);
    privKeyPrint(D, N);
  }

  private static BigInteger generateE(BigInteger E, BigInteger phiN, Random rand)
  {
    while(!(E.compareTo(phiN)<1) && (E.gcd(phiN)!=BigInteger.ONE))
    {
      E = new BigInteger(RandomPrime.generate(512, rand));
    }
    return E;
  }

  private static void pubKeyPrint(BigInteger E, BigInteger N) throws IOException
  {
    File pubKey = new File("pubKey.rsa");
    PrintWriter toFile = new PrintWriter(pubKey);
    toFile.println(E);
    toFile.println(N);
    toFile.close();
  }

  private static void privKeyPrint(BigInteger D, BigInteger N) throws IOException
  {
    File privKey = new File("privKey.rsa");
    PrintWriter toFile = new PrintWriter(privKey);
    toFile.println(D);
    toFile.println(N);
    toFile.close();
  }
}
