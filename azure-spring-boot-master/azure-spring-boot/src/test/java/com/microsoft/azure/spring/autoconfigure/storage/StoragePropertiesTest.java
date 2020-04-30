/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.spring.autoconfigure.storage;

import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindException;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class StoragePropertiesTest {
    private static final String ACCOUNT_NAME_PROP = "azure.storage.account-name";
    private static final String ACCOUNT_KEY_PROP = "azure.storage.account-key";
    private static final String CONTAINER_NAME_PROP = "azure.storage.container-name";
    private static final String USE_EMULATOR_PROP = "azure.storage.use-emulator";
    private static final String EMULATOR_BLOB_HOST_PROP = "azure.storage.emulator-blob-host";

    private static final String ACCOUNT_NAME = "fakeStorageAccountName";
    private static final String ACCOUNT_KEY = "ZmFrZUFjY291bnRLZXk="; /* Base64 encoded for string fakeAccountKey */
    private static final String CONTAINER_NAME = "fakestoragecontainername";
    private static final boolean USE_EMULATOR = true;
    private static final String EMULATOR_BLOB_HOST = "http://127.0.0.1:1000";

    private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(StorageAutoConfiguration.class));

    @Test
    public void canSetProperties() {
        contextRunner.withPropertyValues(propValuePair(ACCOUNT_NAME_PROP, ACCOUNT_NAME),
                propValuePair(ACCOUNT_KEY_PROP, ACCOUNT_KEY), propValuePair(CONTAINER_NAME_PROP, CONTAINER_NAME))
                .run(context -> {
                    final StorageProperties properties = context.getBean(StorageProperties.class);
                    assertThat(properties.getAccountName()).isEqualTo(ACCOUNT_NAME);
                    assertThat(properties.getAccountKey()).isEqualTo(ACCOUNT_KEY);
                    assertThat(properties.getContainerName()).isEqualTo(CONTAINER_NAME);
                });
    }

    @Test
    public void canSetEmulatorProperties() {
        contextRunner.withPropertyValues(propValuePair(ACCOUNT_NAME_PROP, ACCOUNT_NAME),
                propValuePair(ACCOUNT_KEY_PROP, ACCOUNT_KEY), propValuePair(CONTAINER_NAME_PROP, CONTAINER_NAME),
                propValuePair(USE_EMULATOR_PROP, Boolean.toString(USE_EMULATOR)),
                propValuePair(EMULATOR_BLOB_HOST_PROP, EMULATOR_BLOB_HOST))
                .run(context -> {
                    final StorageProperties properties = context.getBean(StorageProperties.class);
                    assertThat(properties.isUseEmulator()).isEqualTo(USE_EMULATOR);
                    assertThat(properties.getEmulatorBlobHost()).isEqualTo(EMULATOR_BLOB_HOST);
                });
    }

    @Test
    public void emptySettingNotAllowed() {
        try {
            contextRunner.withPropertyValues(propValuePair(ACCOUNT_NAME_PROP, ""),
                    propValuePair(ACCOUNT_KEY_PROP, ""))
                    .run(context -> context.getBean(StorageProperties.class));
        } catch (IllegalStateException e) {
            assertThat(e.getCause().getCause()).isInstanceOf(ConfigurationPropertiesBindException.class);
        }
    }

    private static String propValuePair(String propName, String propValue) {
        return propName + "=" + propValue;
    }
}
