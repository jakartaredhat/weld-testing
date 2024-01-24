/*
 * JBoss, Home of Professional Open Source
 * Copyright 2024, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.junit5.records;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddEnabledInterceptors;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A sample test of a pattern that could be used to enable the injection of validated Java records.
 */
@EnableAutoWeld
@AddEnabledInterceptors(RecordCtorInterceptor.class)
@AddBeanClasses({ RecordProducer.class, BadRecordUser.class })
public class RecordsProducerTest {
    @Inject
    private ASomeRecordUser user;

    @Inject
    private Instance<BadRecordUser> badUser;

    @Test
    @DisplayName("Test that @AddBeanClasses pulls in producer to fulfill the injected ASomeRecordUser")
    void test() {
        assertNotNull(user);
        user.printRecord();
    }

    @Test
    @DisplayName("Test that BadRecordUser fails validation")
    void testBadUser() {
        try {
            BadRecordUser badRecordUser = badUser.get();
            badRecordUser.printRecord();
            fail("BadRecordUser should have failed validation");
        } catch (Exception e) {
            // expected
            System.out.printf("Caught expected exception: %s%n", e.getMessage());
        }
    }
}
