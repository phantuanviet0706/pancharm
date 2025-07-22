package com.example.pancharm.service;

import com.example.pancharm.dto.request.auth.AuthenticationRequest;
import com.example.pancharm.dto.request.auth.IntrospectRequest;
import com.example.pancharm.dto.request.auth.LogoutRequest;
import com.example.pancharm.dto.response.AuthenticationResponse;
import com.example.pancharm.dto.response.IntrospectResponse;
import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.entity.InvalidatedToken;
import com.example.pancharm.entity.Users;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.repository.InvalidatedTokenRepository;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
	UserRepository userRepository;
	InvalidatedTokenRepository invalidatedTokenRepository;

	@NonFinal
	@Value("${jwt.signerKey}")
	protected String SIGNER_KEY;

	public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
		var token = request.getToken();
		boolean isValid = true;
		try {
			verifyToken(token);
		} catch (AppException e) {
			isValid = false;
		}

		return IntrospectResponse.builder()
				.valid(isValid)
				.build();
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

	public void logout(LogoutRequest request) throws ParseException, JOSEException {
		var signedToken = verifyToken(request.getToken());

		try {
			String jit = signedToken.getJWTClaimsSet()
					.getJWTID();

			Date expiryTime = signedToken.getJWTClaimsSet()
					.getExpirationTime();

			InvalidatedToken invalidatedToken = InvalidatedToken.builder()
					.id(jit)
					.expiryTime(expiryTime)
					.build();

			invalidatedTokenRepository.save(invalidatedToken);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}
	}

	private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
		JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

		SignedJWT signedJWT = SignedJWT.parse(token);

		Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

		var verified = signedJWT.verify(verifier);

		if (!(verified && expiryTime.after(new Date()))) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}

		if (invalidatedTokenRepository
				.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}

		return signedJWT;
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
				.jwtID(UUID.randomUUID().toString())
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
