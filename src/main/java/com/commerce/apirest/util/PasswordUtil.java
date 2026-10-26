package com.commerce.apirest.util;

import java.security.SecureRandom;

/** Gera senhas temporárias aleatórias para contas sem senha informada. */
public final class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TAMANHO_SENHA = 20;
    private static final String MAIUSCULAS = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String MINUSCULAS = "abcdefghijkmnopqrstuvwxyz";
    private static final String NUMEROS = "23456789";
    private static final String SIMBOLOS = "!@#$%&*+-_?";
    private static final String TODOS_CARACTERES = MAIUSCULAS + MINUSCULAS + NUMEROS + SIMBOLOS;

    private PasswordUtil() {
    }

    /** Gera uma senha de 20 caracteres com maiúscula, minúscula, número e símbolo. */
    public static String gerarSenha() {
        char[] senha = new char[TAMANHO_SENHA];
        senha[0] = sortear(MAIUSCULAS);
        senha[1] = sortear(MINUSCULAS);
        senha[2] = sortear(NUMEROS);
        senha[3] = sortear(SIMBOLOS);

        for (int i = 4; i < senha.length; i++) {
            senha[i] = sortear(TODOS_CARACTERES);
        }

        // Embaralha para que as categorias não ocupem posições previsíveis.
        for (int i = senha.length - 1; i > 0; i--) {
            int indice = RANDOM.nextInt(i + 1);
            char temporario = senha[i];
            senha[i] = senha[indice];
            senha[indice] = temporario;
        }

        return new String(senha);
    }

    private static char sortear(String caracteres) {
        return caracteres.charAt(RANDOM.nextInt(caracteres.length()));
    }
}
