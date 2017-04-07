package com.tailoredapps.template.data.local.encryption;

import android.content.Context;
import android.security.KeyChain;
import android.security.keystore.KeyProperties;

import com.tailoredapps.template.BuildConfig;
import com.tailoredapps.template.data.local.PrefRepo;
import com.tailoredapps.template.injection.qualifier.AppContext;
import com.tailoredapps.template.injection.scopes.PerApplication;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.crypto.SecretKey;
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
    private static final int KEY_LENGTH_BYTES = 64; // 512 bit

    private final PrefRepo prefRepo;
    private final Context context;

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
            key = loadOrGenerateKey();
        } catch(Exception e) {
            Timber.e(e, "Could not load or generate key, resetting …");

            try {
                resetEncryptionKey();
                key = loadOrGenerateKey();
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

    private byte[] loadOrGenerateKey() throws GeneralSecurityException, IOException {
        Timber.i("Encryption key is " + (KeyChain.isBoundKeyAlgorithm(KeyProperties.KEY_ALGORITHM_RSA) ? "hardware-backed" : "software-backed"));
        final SecretKeyWrapper wrapper = new SecretKeyWrapper(context, KEY_ALIAS);

        byte[] wrapped = prefRepo.getRealmEncryptionKey();

        // Generate secret key if none exists
        if (wrapped == null) {
            Timber.i("Generating encryption key …");
            final byte[] raw = new byte[KEY_LENGTH_BYTES];
            new SecureRandom().nextBytes(raw);
            final SecretKey key = new SecretKeySpec(raw, "AES");

            wrapped = wrapper.wrap(key);
            prefRepo.setRealmEncryptionKey(wrapped);
            wrapped = prefRepo.getRealmEncryptionKey();
            Timber.i("Encryption key generated");
        }

        final SecretKey key = wrapper.unwrap(wrapped);
        return key.getEncoded();
    }

    private static void resetEncryptionKey() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        final KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        keyStore.deleteEntry(KEY_ALIAS);
    }
}
