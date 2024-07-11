package jwtToken.jwtlib;

import com.auth0.jwt.JWT;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

//import org.apache.tomcat.util.codec.binary.Base64;

public class JWTfun {

    private static String token = "";
    private String pubkey = "";

    public JWTfun(String token) {
        this.token = token;
        readPubKey();
    }

    private void readPubKey() {
        File keyFile = new File("src/main/resources/pubkey.txt");

        try {
            Scanner reader = new Scanner(keyFile);
            while(reader.hasNextLine()) pubkey += reader.nextLine();
            System.out.println("--- Begin PubKey --- \n" +pubkey+"\n --- End PubKey ---");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean verifySignature() {
        try {
            try {
                byte[] pubkeyBytes = Base64.getDecoder().decode(pubkey);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubkeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey key = keyFactory.generatePublic(keySpec);
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); //questa riga lancia un'eccezione se non ci si può fidare del token
            } catch (JwtException e) {
                // DEBUG: print reason for invalid token
                System.out.println(e.toString());
                return false;
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean isExpired() {
        Date exp = new Date(getExp());
        Date now = new Date(new Date().getTime()/1000);

        if(exp.after(now))
            return false;
        else
            return true;
    }

    public static String getUserType() {
        String userType = getPayload().split("\"roles\":")[1].split("]")[0];

        return userType;
    }

    public static String getUserId() {
        return getPayload().split("\"sub\":")[1].split(",\"typ\"")[0];
    }

    public Integer getExp() {
        String exp = getPayload().split("\"exp\":")[1].split(",\"iat\"")[0];
        return Integer.parseInt(exp);
    }

    public String getHeader() {
        return new String(Base64.getUrlDecoder().decode(JWT.decode(token).getHeader().getBytes()));
    }

    public static String getPayload() {
        return new String(Base64.getUrlDecoder().decode(JWT.decode(token).getPayload().getBytes()));
    }

    public String getSignature() {
        return JWT.decode(token).getSignature();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
