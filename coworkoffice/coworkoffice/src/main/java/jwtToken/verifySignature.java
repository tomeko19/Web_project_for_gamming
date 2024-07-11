package jwtToken;

import jwtToken.jwtlib.JWTfun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class verifySignature {
    public static boolean verifySignatureTokenJWT(String token) {
        try {
            JWTfun jot = new JWTfun(token);
            System.out.print("Verify signature returns: ");
            System.out.println(jot.verifySignature());
            if (jot.verifySignature()) {
                System.out.println("Valid token");
            } else System.out.println("Token is not valid (may be expired)");

            System.out.print("Is expired returns: ");
            System.out.println(jot.isExpired());

            System.out.println(jot.getPayload());

            System.out.println(jot.getUserType());
            System.out.println(jot.getUserId());
            System.out.println(jot.getHeader());
            System.out.println(jot.getExp());
        } catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }
}
