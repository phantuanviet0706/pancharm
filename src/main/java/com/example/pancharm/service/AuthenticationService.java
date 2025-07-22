package com.example.pancharm.service;

import com.example.pancharm.dto.request.auth.AuthenticationRequest;
import com.example.pancharm.dto.request.auth.IntrospectRequest;
import com.example.pancharm.dto.response.AuthenticationResponse;
import com.example.pancharm.dto.response.IntrospectResponse;
import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.entity.Users;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
	UserRepository userRepository;

	@NonFinal
	@Value("${jwt.signerKey}")
	protected String SIGNER_KEY;

	public IntrospectResponse introspect(IntrospectRequest request) {
		var token = request.getToken();

		try {
			JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

			SignedJWT signedJWT = SignedJWT.parse(token);

			Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

			var verified = signedJWT.verify(verifier);

			return IntrospectResponse.builder()
					.valid(verified && expiryTime.after(new Date()))
					.build();
		} catch (JOSEException | ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		var user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
		if (!authenticated) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}

		var token = generateToken(user);

		return AuthenticationResponse.builder()
				.token(token)
				.authenticated(true)
				.build();
	}

	private String generateToken(Users user) {
		JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.subject(user.getUsername())
				.issuer("pancharm.vn")
				.issueTime(new Date())
				.expirationTime(new Date(
						Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
				))
				.claim("scope", buildScope(user))
				.build();

		Payload payload = new Payload(jwtClaimsSet.toJSONObject());

		JWSObject jwsObject = new JWSObject(jwsHeader, payload);

		try {
			jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
			return jwsObject.serialize();
		} catch (JOSEException e) {
			throw new RuntimeException(e);
		}
	}

	private String buildScope(Users user) {
		StringJoiner joiner = new StringJoiner(" ");
		if (CollectionUtils.isEmpty(user.getRoles())) {
			return joiner.toString();
		}

		user.getRoles().forEach(role -> {
			joiner.add("ROLE_" + role.getName());
			if (!CollectionUtils.isEmpty(role.getPermissions())) {
				role.getPermissions().forEach(permission -> {
					joiner.add(permission.getName());
				});
			}
		});

		return joiner.toString();
	}
}
