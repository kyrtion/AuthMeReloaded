package fr.xephi.authme.security.crypts;

import fr.xephi.authme.AuthMe;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 */
public class SALTEDSHA512 implements EncryptionMethod {

    private static String getSHA512(String message)
        throws NoSuchAlgorithmException {
        MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        sha512.reset();
        sha512.update(message.getBytes());
        byte[] digest = sha512.digest();
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
    }

    @Override
    public String computeHash(String password, String salt, String name)
        throws NoSuchAlgorithmException {
        return getSHA512(password + salt);
    }

    @Override
    public boolean comparePassword(String hash, String password,
                                   String playerName) throws NoSuchAlgorithmException {
        String salt = AuthMe.getInstance().database.getAuth(playerName).getSalt();
        return hash.equals(computeHash(password, salt, ""));
    }
}
