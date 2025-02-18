/*
 * Copyright 2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.java.spring.boot2

import org.junit.jupiter.api.Test
import org.openrewrite.Recipe
import org.openrewrite.java.JavaParser
import org.openrewrite.java.JavaRecipeTest

class RestTemplateBuilderRequestFactoryTest : JavaRecipeTest {
    override val parser: JavaParser
        get() = JavaParser.fromJavaVersion()
            .classpath("spring-boot", "spring-web")
            .build()

    override val recipe: Recipe
        get() = RestTemplateBuilderRequestFactory()

    @Test
    fun useSupplierArgument() = assertChanged(
        before = """
            import org.springframework.boot.web.client.RestTemplateBuilder;
            import org.springframework.http.client.ClientHttpRequestFactory;
            import org.springframework.http.client.SimpleClientHttpRequestFactory;

            public class A {
                static {
                    RestTemplateBuilder builder = new RestTemplateBuilder()
                            .requestFactory(new SimpleClientHttpRequestFactory());
                }
            }
        """,
        after = """
            import org.springframework.boot.web.client.RestTemplateBuilder;
            import org.springframework.http.client.ClientHttpRequestFactory;
            import org.springframework.http.client.SimpleClientHttpRequestFactory;

            public class A {
                static {
                    RestTemplateBuilder builder = new RestTemplateBuilder()
                            .requestFactory(() -> new SimpleClientHttpRequestFactory());
                }
            }
        """
    )
}
