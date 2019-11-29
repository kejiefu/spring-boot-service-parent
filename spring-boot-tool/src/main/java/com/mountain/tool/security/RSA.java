package com.mountain.tool.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {

	/**
	 * 从输入流中加载公钥
	 * 
	 * @param stream
	 *            公钥流
	 * @throws Exception
	 *             加载异常
	 */
	public static RSAPublicKey loadPublicKey(InputStream stream)
			throws Exception {
		return loadPublicKey(loadFromStream(stream));
	}

	

	/**
	 * 从流中加载私钥
	 * 
	 * @param stream
	 *            私钥流
	 * @return 是否成功
	 * @throws Exception
	 */
	public static RSAPrivateKey loadPrivateKey(InputStream stream)
			throws Exception {
		return loadPrivateKey(loadFromStream(stream));
	}

	/**
	 * 从字符串加载私钥
	 * 
	 * @param privateKeyStr
	 *            私钥字符串
	 * @return
	 * @throws Exception
	 *             加载异常
	 */
	public static RSAPrivateKey loadPrivateKey(String privateKeyStr)
			throws Exception {
		byte[] buffer = Base64.decodeBase64(privateKeyStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
	}
	
	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr 公钥数据字符串
	 * @throws Exception 加载异常
	 */
	public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
		byte[] buffer = Base64.decodeBase64(publicKeyStr);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}

	/**
	 * 加密
	 * 
	 * @param publicKey 公钥
	 * @param plainData 明文数据
	 * @return 加密后的数据
	 * @throws Exception 异常信息
	 */
	public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainData) throws Exception {
		Cipher cipher = null;
		cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] output = cipher.doFinal(plainData);
		return output;

	}

	/**
	 * 加密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param plainData 明文数据
	 * @return 加密后的数据
	 * @throws Exception 异常信息
	 */
	public static byte[] encrypt(String publicKeyStr, byte[] plainData) throws Exception {
		return encrypt(loadPublicKey(publicKeyStr), plainData);
	}

	/**
	 * 加密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param plainText 明文数据
	 * @return 加密后的数据,Base64格式
	 * @throws Exception 异常信息
	 */
	public static String encrypt(String publicKeyStr, String plainText) throws Exception {
		return Base64.encodeBase64String(encrypt(loadPublicKey(publicKeyStr),
				plainText.getBytes()));
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] output = cipher.doFinal(cipherData);
		return output;
	}

	/**
	 * 解密
	 * 
	 * @param privateKeyStr
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static byte[] decrypt(String privateKeyStr, byte[] cipherData)
			throws Exception {
		return decrypt(loadPrivateKey(privateKeyStr), cipherData);
	}

	/**
	 * 解密
	 * 
	 * @param privateKeyStr
	 *            私钥
	 * @param cipherTextBase64
	 *            密文数据,Base64格式
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static String decrypt(String privateKeyStr, String cipherTextBase64)
			throws Exception {
		return new String(decrypt(loadPrivateKey(privateKeyStr),
				Base64.decodeBase64(cipherTextBase64)));
	}

	private static String loadFromStream(InputStream in) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			if (readLine.startsWith("-")) {
				continue;
			} else {
				sb.append(readLine);
			}
		}
		return sb.toString();
	}

	public static String[] genRSAKey(int bits) throws Exception {
		String[] keys = new String[2];
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(bits);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		PrivateKey privateKey = keyPair.getPrivate(); 
		PublicKey publicKey = keyPair.getPublic();           
        keys[0] = Base64.encodeBase64String(privateKey.getEncoded());
        keys[1] = Base64.encodeBase64String(publicKey.getEncoded());
        return keys;
	}

	public static void main(String[] args) throws Exception {

		String[] keys = genRSAKey(1024);
		System.out.println("rsa privateKey:" + keys[0]);
		System.out.println("rsa publicKey:" + keys[1]);
		
		// PCK8
		String privateKey = keys[0];//"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIe+C7Ll4v6RprrZNHtBRTR4RP2K6iT4iEseLjOX2KJXj1ENAlFFlitGTlKFip2l1Wxq9z6xoNG/mVAW+rX03e5tIfyE86LPGANruaZdYeD1NDLyU3pJDvpYQLAtYDT8cX6xu2/GG+EvAMjnWP5u2bImpEHlD7BUD6umaeMPdaQZAgMBAAECgYAM0rAqOztBTyMP/ZrOlfDirwXdabUKvwupBPXaW9rUIy9fubnhUZCJEAAwHXtlvxe1QTmQC5mkltP10+XlMHeSfjX0cKlmuC5X+17tnTfglkfPNJ6Zv9armXUV8m2NhtMRNvpTL65KOj9bAIGzrLmI7ouK8E0vjZ12pjWmJ43cMQJBALxUIjEihoS428KuzfzrpEg/rqWD3EFK6qJQmf8I1FLM/hlLoBQ6HJAuLsLaUCCJetADGBVtAG376vGDtRsthE0CQQC4hKLEtbiHhrhwj77wtoDlq+KvZfOwqihu1AjNuerXBU1HHqJAJhUmDjgQs0Vh550uVa3Ozaf3u6pkFpXxI3T9AkEAgaHL81RXkc4I/wYCJiExcMXc+nM8Tfk6bsXaapFyTRhro4/JvYKOnLQdXeSWGP9o+PvYPQ9Np2crPlkxdWasVQJAGSi8qOW+S3xrGa3/8wJvgOF2/0N/fs80xCfVK3f2NWtbph7fMLAAoje+HUjUZQ0XuYGlOVWBWZ4VFaCewUp8YQJAHWOMJAPPL5uBb41UspZg4kHtRNFvW3MRfLZNUylB5+1RCFyLQSO/cSMNaDkT0IRWCZs2jb8m2Do6MoIX5+vhWw==";//keys[0];

		String publicKey = keys[1];//"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHvguy5eL+kaa62TR7QUU0eET9iuok+IhLHi4zl9iiV49RDQJRRZYrRk5ShYqdpdVsavc+saDRv5lQFvq19N3ubSH8hPOizxgDa7mmXWHg9TQy8lN6SQ76WECwLWA0/HF+sbtvxhvhLwDI51j+btmyJqRB5Q+wVA+rpmnjD3WkGQIDAQAB";//keys[1];
		
		String uid = "123456";
		String e = RSA.encrypt(publicKey, uid);
		System.out.println("encrypt:" + e);
		e = java.net.URLEncoder.encode(e, "UTF-8");
		System.out.println("encode:" + e);
		e = java.net.URLEncoder.encode(e, "UTF-8");
		System.out.println("encode:" + e);
		e = java.net.URLDecoder.decode(e, "UTF-8");
		e = java.net.URLDecoder.decode(e, "UTF-8");
		String d = decrypt(privateKey, e);
		System.out.println("decrypt:" + d);

	}
}