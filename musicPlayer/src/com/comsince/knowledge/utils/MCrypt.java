package com.comsince.knowledge.utils;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import android.util.Log;

public class MCrypt {

	private String iv = "IyReJSYmKlk=";// iv
	private RC2ParameterSpec rc2spec;
	private SecretKeySpec keyspec;
	private Cipher cipher;
	private String SecretKey = "bmV3dHJlay5jb20gICAgIA==";// secretKey

	public MCrypt() {
		Provider secProvider = new BouncyCastleProvider();
		rc2spec = new RC2ParameterSpec(128, Base64.decode(iv));

		keyspec = new SecretKeySpec(Base64.decode(SecretKey), "RC2");

		try {
			cipher = Cipher.getInstance("RC2/CBC/PKCS7Padding", secProvider);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	public byte[] encrypt(String text) throws Exception {
		if (text == null || text.length() == 0)
			throw new Exception("Empty string");

		byte[] encrypted = null;

		try {
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, rc2spec);
			int blockSize = cipher.getBlockSize();
			Log.d("MCrypt", "MCrypt block size: " + blockSize);
			encrypted = cipher.doFinal(text.getBytes(), 0, text.length());
		} catch (Exception e) {
			throw new Exception("[encrypt] " + e.getMessage());
		}

		return encrypted;
	}

	public byte[] decrypt(String code) throws Exception {
		if (code == null || code.length() == 0)
			throw new Exception("Empty string");

		byte[] decrypted = null;

		try {
			cipher.init(Cipher.DECRYPT_MODE, keyspec, rc2spec);

			decrypted = cipher.doFinal(Base64.decode(code), 0, Base64.decode(code).length);
		} catch (Exception e) {
			throw new Exception("[decrypt] " + e.getMessage());
		}
		return decrypted;
	}

	public byte[] encrypt(byte[] byteArray) throws Exception {
		if (byteArray == null || byteArray.length == 0)
			throw new Exception("Empty byteArray");

		byte[] encrypted = null;

		try {
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, rc2spec);
			int blockSize = cipher.getBlockSize();
			Log.d("MCrypt", "MCrypt block size: " + blockSize);
			encrypted = cipher.doFinal(byteArray, 0, byteArray.length);
		} catch (Exception e) {
			throw new Exception("[encrypt] " + e.getMessage());
		}

		return encrypted;
	}

	public byte[] decrypt(byte[] byteArrayCode) throws Exception {
		if (byteArrayCode == null || byteArrayCode.length == 0)
			throw new Exception("Empty byteArrayCode");

		byte[] decrypted = null;

		try {
			cipher.init(Cipher.DECRYPT_MODE, keyspec, rc2spec);

			decrypted = cipher.doFinal(byteArrayCode, 0, byteArrayCode.length);
		} catch (Exception e) {
			throw new Exception("[decrypt] " + e.getMessage());
		}
		return decrypted;
	}

}