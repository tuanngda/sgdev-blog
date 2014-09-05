package com.sgdevblog.security.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

public class SecurityUtil {

	public static String sign(String text, String secret){
		return DigestUtils.sha256Hex(text + secret);
	}

	public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String key = password + salt;

		String hexString = key;
		for(int i=0; i<20; i++){
		    hexString = DigestUtils.sha512Hex(hexString);
		}

		return hexString;
	}

	public static String generatePasswordSalt(){
		String salt = "";

		Random rand = new Random();

		for(int i=0; i<20; i++){
		    int randomInt = rand.nextInt(80)+10;
		    salt += Integer.toHexString(randomInt);
		}

		return salt;
	}
}
