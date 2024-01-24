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

import java.util.Arrays;

import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@ValidationBinding
@Interceptor
public class RecordCtorInterceptor {
    @AroundInvoke
    private Object checkRecordProducer(InvocationContext ic) throws Exception {
        System.out.printf("RecordCtorInterceptor: %s, args=%s\n", ic.getMethod().getName(), Arrays.asList(ic.getParameters()));
        // Apply constructor parameter constraint checking...
        Object[] parameters = ic.getParameters();
        if (parameters[0] instanceof InjectionPoint) {
            InjectionPoint ip = (InjectionPoint) parameters[0];
            SomeRecordCtor ctorInfo = ip.getAnnotated().getAnnotation(SomeRecordCtor.class);
            if (ctorInfo.name() == null) {
                throw new IllegalArgumentException("ValidationError: name must not be null");
            }
            if (ctorInfo.value() < 0) {
                throw new IllegalArgumentException("ValidationError: value must be greater than 0");
            }
        }

        Object producedInstance = ic.proceed();
        // Apply constructor return value constraint checking...
        return producedInstance;
    }
}
