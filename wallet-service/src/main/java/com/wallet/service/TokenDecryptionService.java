package com.wallet.service;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class TokenDecryptionService {

	public String getNameFromToken(String token) {
		Algorithm algorithmHS = Algorithm.HMAC256("password");
		JWTVerifier verifier = JWT.require(algorithmHS).build(); // Reusable verifier instance
		DecodedJWT jwt = verifier.verify(token);
		Claim usernameClaim = jwt.getClaim("email");
		String username = usernameClaim.asString();
		return username;
	}
	
}
