
package com.commerce.apirest.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class HashUtil {

	public String calculateHash(String texto, String algoritmo) {
		if (texto == null || algoritmo == null) {
			throw new IllegalArgumentException("O texto e o algoritmo devem ser informados.");
		}

		String algoritmoNormalizado = algoritmo.replace("-", "").toUpperCase(Locale.ROOT);

		if (!algoritmoNormalizado.equals("MD5") && !algoritmoNormalizado.equals("SHA256")
				&& !algoritmoNormalizado.equals("SHA512")) {
			throw new IllegalArgumentException("Algoritmo não suportado. Utilize MD5, SHA-256 ou SHA-512.");
		}

		try {
			MessageDigest digest = MessageDigest.getInstance(algoritmoNormalizado);

			byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));

			return HexFormat.of().formatHex(hash);

		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("O algoritmo de hash não está disponível nesta JVM.", e);
		}
	}
}
