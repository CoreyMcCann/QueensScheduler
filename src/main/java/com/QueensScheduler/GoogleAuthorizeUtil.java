package com.QueensScheduler;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class GoogleAuthorizeUtil {
    private static final String CREDENTIALS_FILE_PATH = "/google-sheets-client-secret.json";

    public static Credential authorize() throws IOException, GeneralSecurityException {
        // Load client secrets.
        InputStream in = GoogleAuthorizeUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
            JacksonFactory.getDefaultInstance(), new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            new NetHttpTransport(), JacksonFactory.getDefaultInstance(), clientSecrets,
            Arrays.asList(SheetsScopes.SPREADSHEETS))
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
            .setAccessType("offline")
            .build();

        // Create a LocalServerReceiver with a fixed port and path
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                                       .setPort(8888)
                                       .setCallbackPath("/Callback")
                                       .build();
        
        try {
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (TokenResponseException e) {
            if (e.getDetails() != null && "invalid_grant".equals(e.getDetails().getError())) {
                System.err.println("Token expired or revoked: " + e.getDetails().getErrorDescription());
                // Optionally, handle the re-authorization or clean up
            }
            throw e; // Continue throwing to signify there is an issue to be handled at a higher level
        }
    }
}

