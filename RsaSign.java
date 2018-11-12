import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.*;
import java.math.*;
import java.io.*;
public class RsaSign
{
  public static void main(String[] args) throws FileNotFoundException
  {
    if(args[0].equals("s"))
    {
      sign(args[1]);
    }
    else if(args[0].equals("v"))
    {
      verify(args[1]);
    }
  }

  private static void sign(String file) throws FileNotFoundException
  {
    File fileToHash = new File(file);
    File privKey = new File("privKey.rsa");
    checkExistence(privKey);
    Random r = new Random();
    Scanner fileScan = new Scanner(privKey);
    BigInteger D = fileScan.nextBigInteger();
    BigInteger N = fileScan.nextBigInteger();
    BigInteger hash = getHash(file);
    hash = hash.modPow(D, N);
    writeSigExtension(hash, fileToHash);
  }

  private static void verify(String file) throws FileNotFoundException
  {
    File hashFile = new File(file);
    Scanner readFile = new Scanner(hashFile);
    Random r = new Random();
    BigInteger newHash = getHash(file);
    File ogSignedFile = new File(file + ".sig");
    checkExistence(ogSignedFile);
    readFile = new Scanner(ogSignedFile);
    BigInteger originalHash = readFile.nextBigInteger();
    File pubkey = new File("pubKey.rsa");
    checkExistence(pubkey);
    readFile = new Scanner(pubkey);
    BigInteger E = readFile.nextBigInteger();
    BigInteger N = readFile.nextBigInteger();
    originalHash = originalHash.modPow(E, N);
    System.out.println("Orignal hash: " + originalHash);
    System.out.println("New hash: " + newHash);
    if(originalHash.equals(newHash))
    {
      System.out.println("We good fam");
    }
    else
    {
      System.out.println("These do not match");
    }
  }

  private static BigInteger getHash(String file)
  {
    Random r = new Random();
    BigInteger newHash = new BigInteger(5, r);
    try
    {
      // read in the file to hash
      Path path = Paths.get(file);
      byte[] data = Files.readAllBytes(path);
      // create class instance to create SHA-256 hash
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      // process the file
      md.update(data);
      // generate a has of the file
      byte[] digest = md.digest();
      newHash = new BigInteger(digest);
    }
    catch(Exception e)
    {
      System.out.println(e.toString());
    }
    return newHash;
  }

  private static void checkExistence(File fileCheck) throws FileNotFoundException
  {
    if(!fileCheck.exists())
    {
      throw new FileNotFoundException();
    }
  }

  private static void writeSigExtension(BigInteger sig, File writeFile) throws FileNotFoundException
  {
    File sigFile = new File(writeFile.getName() + ".sig");
    PrintWriter writer = new PrintWriter(sigFile);
    writer.println(sig);
    writer.close();
  }
}
