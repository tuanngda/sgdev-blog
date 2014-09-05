package com.sgdevblog.security.util;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.Test;

public class SecurityUtilTest {

	@Test
	public void test() throws NoSuchAlgorithmException, InvalidKeySpecException {
		System.out.println(SecurityUtil.hashPassword("admin", "4f1b574119e452d351a521ae1917211832412a"));
		System.out.println(SecurityUtil.hashPassword("moderator", "54352b4f2a5317305659ad2d311b48463d2c10"));
		System.out.println(SecurityUtil.hashPassword("agent", "3f24251c33464c48e1816212f334ba40223423"));
	}

}
