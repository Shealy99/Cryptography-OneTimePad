package crypto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

public class Crypto {

    public static File decrypt(String path, String keyPath) throws FileNotFoundException, IOException {
        byte[] key = Files.readAllBytes(new File(keyPath).toPath());
        File encrypted = new File(path);
        byte[] encrypt = Files.readAllBytes(encrypted.toPath());
        byte[] dec = new byte[encrypt.length];
        int in = encrypted.getAbsolutePath().lastIndexOf(".");
        String ext = "";
        int index = 0;
        for (int i = 0; i < encrypt.length; i++) {
            if (index == key.length) {
                index = 0;
            }
            dec[i] = (byte) (key[index] ^ encrypt[i]);
            index++;
        }
        if (in > -1) {
            ext = encrypted.getAbsolutePath().substring(in + 1);
        }
        File newFile = new File("decrypted." + ext);
        FileOutputStream stream = new FileOutputStream(newFile);
        stream.write(dec);
        stream.close();

        return newFile;
    }

    public static File encrypt(String path) throws FileNotFoundException, IOException {
        File f = new File(path);

        byte[] key = getRandomKey(64);

        FileOutputStream writeKey = new FileOutputStream(new File("key.txt"));
        writeKey.write(key);
        writeKey.close();

        byte[] b = Files.readAllBytes(f.toPath());
        int index = 0;
        byte[] encrypt = new byte[b.length];
        String ext = "";
        for (int i = 0; i < b.length; i++) {
            if (index == key.length) {
                index = 0;
            }
            encrypt[i] = (byte) (key[index] ^ b[i]);
            index++;
        }
        int in = f.getAbsolutePath().lastIndexOf(".");
        if (in > -1) {
            ext = f.getAbsolutePath().substring(in + 1);
        }

        File newFile = new File("encrypted." + ext);
        FileOutputStream stream = new FileOutputStream(newFile);
        stream.write(encrypt);
        stream.close();

        return newFile;
    }

    private static byte[] getRandomKey(int n) {
        Random r = new Random();
        byte[] keyBytes = new byte[n];
        r.nextBytes(keyBytes);
        return keyBytes;
    }

    public static void main(String[] args) throws IOException {
        encrypt("real.jpg");
        decrypt("encrypted.jpg", "key.txt");
    }

}
