package com.cdoalgonquin;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.keys.KeyClient;
import com.azure.security.keyvault.keys.KeyClientBuilder;
import com.azure.security.keyvault.keys.cryptography.CryptographyClient;
import com.azure.security.keyvault.keys.cryptography.CryptographyClientBuilder;
import com.azure.security.keyvault.keys.cryptography.models.DecryptResult;
import com.azure.security.keyvault.keys.cryptography.models.EncryptResult;
import com.azure.security.keyvault.keys.cryptography.models.EncryptionAlgorithm;
import com.azure.security.keyvault.keys.models.KeyVaultKey;

import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws Exception {

        String keyVaultUrl = "https://kvlab5yuan0037.vault.azure.net/";
        String keyName = "lab5key";

        // Create a KeyClient instance to interact with Azure Key Vault
        KeyClient keyClient = new KeyClientBuilder()
                .vaultUrl(keyVaultUrl)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        // Retrieve the RSA key from Azure Key Vault
        KeyVaultKey key = keyClient.getKey(keyName);

        // Create a CryptographyClient instance to interact with Azure Key Vault
        CryptographyClient cryptoClient = new CryptographyClientBuilder()
                .credential(new DefaultAzureCredentialBuilder().build())
                .keyIdentifier(key.getId())
                .buildClient();

        String str = "Hello CDO Students!";
        byte[] plaintext = str.getBytes(StandardCharsets.UTF_8);
        EncryptResult encryptionResult = cryptoClient.encrypt(EncryptionAlgorithm.RSA_OAEP, plaintext);
        String encryptedText = new String(encryptionResult.getCipherText(), StandardCharsets.UTF_8);
        System.out.println("After encryption: " + encryptedText);        
        DecryptResult decryptionResult = cryptoClient.decrypt(EncryptionAlgorithm.RSA_OAEP,
                encryptionResult.getCipherText());
        String decyprtedText = new String(decryptionResult.getPlainText(), StandardCharsets.UTF_8);
        System.out.println("After decryption: " + decyprtedText);        
    }

}
