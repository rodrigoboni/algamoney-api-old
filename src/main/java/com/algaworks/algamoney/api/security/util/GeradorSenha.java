package com.algaworks.algamoney.api.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {

	public static void main(String[] args) {
		String password = "admin";
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode(password);
		System.out.println(encoded);
		System.out.println(encoder.matches(password, encoded));;
	}

}
