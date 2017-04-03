package com.fatalsignal.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesUtils {
	private static final String PASSKEY = "afasdf";
	private static final String DESKEY = "asfsdfsdf";

	/**
	 * @Comments ：对文件进行加密
	 * @param filePath
	 *            要加密的文件路径
	 * @param fileName
	 *            文件
	 * @param mode
	 *            加密模式 加密：Cipher.ENCRYPT_MODE 解密：Cipher.DECRYPT_MODE
	 * @return
	 */
	public static String encoderOrdecoder(String filePath, String fileName,
			int mode) {

		InputStream is = null;
		OutputStream out = null;
		CipherInputStream cis = null;
		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(DESKEY.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
			IvParameterSpec iv = new IvParameterSpec(PASSKEY.getBytes());
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(mode, securekey, iv, sr);
			File encoderFile = new File(filePath + File.separator + "encoder");
			if (!encoderFile.exists()) {
				encoderFile.mkdir();
			}
			is = new FileInputStream(filePath + File.separator + fileName);
			out = new FileOutputStream(filePath + File.separator + "encoder"
					+ File.separator + fileName);
			cis = new CipherInputStream(is, cipher);
			byte[] buffer = new byte[1024];
			int r;
			while ((r = cis.read(buffer)) > 0) {
				out.write(buffer, 0, r);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (cis != null) {
					cis.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e1) {
			}
		}
		return filePath + File.separator + "encoder" + File.separator
				+ fileName;
	}

	/**
	 * @Comments ：对字符串进行加密
	 * @param src
	 *            源字符串
	 * @param mode
	 *            加密模式 加密：Cipher.ENCRYPT_MODE 解密：Cipher.DECRYPT_MODE
	 * @return
	 */
	public static String encoderOrdecoder(String src, int mode) {
		String tag = "";
		InputStream is = null;
		OutputStream out = null;
		CipherInputStream cis = null;
		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(DESKEY.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
			IvParameterSpec iv = new IvParameterSpec(PASSKEY.getBytes());
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(mode, securekey, iv, sr);
			cis = new CipherInputStream(
					new ByteArrayInputStream(src.getBytes()), cipher);
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int r;
			while ((r = cis.read(buffer)) > 0) {
				out.write(buffer, 0, r);
			}
			tag = out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (cis != null) {
					cis.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e1) {
			}
		}
		return tag;
	}

	public static void main(String[] args) {
		System.out.println("aaa");
		String t = encoderOrdecoder("aaa", Cipher.ENCRYPT_MODE);
		System.out.println(t);
		System.out.println(encoderOrdecoder(t, Cipher.DECRYPT_MODE));
	}
}