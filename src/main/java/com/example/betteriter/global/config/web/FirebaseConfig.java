package com.example.betteriter.global.config.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FirebaseConfig {

	@Value("${firebase.key}")
	private String firebaseKey;

	@PostConstruct
	public void init() {
		String base64String = firebaseKey;
		byte[] decodedBytes = Base64.getDecoder().decode(base64String);
		InputStream credentialStream = new ByteArrayInputStream(decodedBytes);

		try {
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(credentialStream))
					.build();
			FirebaseApp.initializeApp(options);

			log.info("Firebase init success");
		}catch (Exception e){
			log.error("Firebase init error: {}", e.getMessage());
		}
	}
}
