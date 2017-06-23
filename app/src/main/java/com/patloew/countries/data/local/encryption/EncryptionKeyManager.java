package com.patloew.countries.data.local.encryption;

import android.content.Context;
import android.security.KeyChain;
import android.security.keystore.KeyProperties;

import com.patloew.countries.BuildConfig;
import com.patloew.countries.data.local.PrefRepo;
import com.patloew.countries.injection.qualifier.AppContext;
import com.patloew.countries.injection.scopes.PerApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/* Copyright 2017 Tailored Media GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
@PerApplication
public class EncryptionKeyManager {

    private static final String KEY_ALIAS = "realm";
    private static final int MAX_KEY_LENGTH_BYTES = 64; // 512 bit
    private static final int AES_256_KEY_LENGTH_BYTES = 32; // 256 bit

    private static final String AES_CIPHER = "AES/CBC/PKCS5Padding";

    private final SecureRandom random = new SecureRandom();
    private final PrefRepo prefRepo;
    private final Context context;

    private SecretKey fullSecretKey = null;

    @Inject
    public EncryptionKeyManager(PrefRepo prefRepo, @AppContext Context context) {
        this.prefRepo = prefRepo;
        this.context = context;
    }

    public void initEncryptedRealm() {
        Timber.i("Initializing encrypted Realm …");

        Realm.init(context);
        byte[] key;

        try {
            key = loadOrGenerateKeyBytes(MAX_KEY_LENGTH_BYTES);
        } catch(Exception e) {
            Timber.e(e, "Could not load or generate key, resetting …");

            try {
                resetEncryptionKey();
                key = loadOrGenerateKeyBytes(MAX_KEY_LENGTH_BYTES);
            } catch(Exception fatal) {
                throw new RuntimeException("Could not reset encryption key", fatal);
            }
        }

        RealmConfiguration.Builder builder = new RealmConfiguration.Builder();
        if(BuildConfig.DEBUG) { builder = builder.deleteRealmIfMigrationNeeded(); }
        builder.encryptionKey(key);
        RealmConfiguration configuration = builder.build();
        Realm.setDefaultConfiguration(configuration);

        try {
            Realm realm = Realm.getDefaultInstance();
            realm.close();
        } catch(Exception e) {
            Timber.e(e, "Could not open Realm with current encryption key, resetting …");
            Realm.deleteRealm(configuration);
        }

        Timber.i("Realm is now initialized with encryption");
    }

    public CipherInputStream getCipherInputStream(String path) throws IOException, GeneralSecurityException {
        return getCipherInputStream(new File(path));
    }

    public CipherInputStream getCipherInputStream(File file) throws IOException, GeneralSecurityException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Cipher cipher = Cipher.getInstance(AES_CIPHER);
        IvParameterSpec iv = readIv(cipher.getBlockSize(), fileInputStream);
        cipher.init(Cipher.DECRYPT_MODE, loadOrGenerateKey(AES_256_KEY_LENGTH_BYTES), iv);
        return new CipherInputStream(fileInputStream, cipher);
    }

    public CipherOutputStream getCipherOutputStream(String path) throws IOException, GeneralSecurityException {
        return getCipherOutputStream(new File(path));
    }

    public CipherOutputStream getCipherOutputStream(File file) throws IOException, GeneralSecurityException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        Cipher cipher = Cipher.getInstance(AES_CIPHER);
        IvParameterSpec iv = generateIv(cipher.getBlockSize());
        cipher.init(Cipher.ENCRYPT_MODE, loadOrGenerateKey(AES_256_KEY_LENGTH_BYTES), iv);
        fileOutputStream.write(iv.getIV());
        return new CipherOutputStream(fileOutputStream, cipher);
    }

    private IvParameterSpec generateIv(int size) {
        byte[] iv = new byte[size];
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private static IvParameterSpec readIv(int size, InputStream is) throws IOException {
        byte[] iv = new byte[size];
        is.read(iv);
        return new IvParameterSpec(iv);
    }

    private SecretKey loadOrGenerateKey(int keySize) throws GeneralSecurityException, IOException {
        if(keySize > MAX_KEY_LENGTH_BYTES) {
            Timber.e("Key size must be smaller than " + keySize);
        }

        if(fullSecretKey == null) {
            Timber.i("Encryption key is " + (KeyChain.isBoundKeyAlgorithm(KeyProperties.KEY_ALGORITHM_RSA) ? "hardware-backed" : "software-backed"));
            final SecretKeyWrapper wrapper = new SecretKeyWrapper(context, KEY_ALIAS);

            byte[] wrapped = prefRepo.getRealmEncryptionKey();

            // Generate secret key if none exists
            if (wrapped == null) {
                Timber.i("Generating encryption key …");
                final byte[] raw = new byte[MAX_KEY_LENGTH_BYTES];
                new SecureRandom().nextBytes(raw);
                final SecretKey key = new SecretKeySpec(raw, "AES");

                wrapped = wrapper.wrap(key);
                prefRepo.setRealmEncryptionKey(wrapped);
                wrapped = prefRepo.getRealmEncryptionKey();
                Timber.i("Encryption key generated");
            }

            fullSecretKey = wrapper.unwrap(wrapped);
        }

        if(keySize != MAX_KEY_LENGTH_BYTES) {
            return new SecretKeySpec(Arrays.copyOf(fullSecretKey.getEncoded(), keySize), "AES");
        } else {
            return fullSecretKey;
        }
    }

    private byte[] loadOrGenerateKeyBytes(int keySize) throws GeneralSecurityException, IOException {
        return loadOrGenerateKey(keySize).getEncoded();
    }

    private static void resetEncryptionKey() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        final KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        keyStore.deleteEntry(KEY_ALIAS);
    }
}
