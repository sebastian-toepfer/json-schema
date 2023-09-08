/*
 * The MIT License
 *
 * Copyright 2023 sebastian.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.sebastiantoepfer.jsonschema.testsuite.junit.engine;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import io.github.sebastiantoepfer.jsonschema.testsuite.junit.engine.JsonSchemaSuiteTestDescriptor;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

class JsonSchemaSuiteTestDescriptorTest {

    @Test
    void should_return_parent() {
        final JsonSchemaSuiteTestDescriptor desc = new JsonSchemaSuiteTestDescriptor(
            new EngineDescriptor(UniqueId.forEngine("test"), "test"),
            JsonValue.EMPTY_JSON_OBJECT
        );

        assertThat(desc.getParent(), isPresent());
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(JsonSchemaSuiteTestDescriptor.class).withIgnoredFields("parent").verify();
    }

    @Test
    void should_return_all_children() {
        assertThat(
            new JsonSchemaSuiteTestDescriptor(
                new EngineDescriptor(UniqueId.forEngine("test"), "test"),
                Json
                    .createObjectBuilder()
                    .add("description", "boolean schema 'true'")
                    .add("schema", JsonValue.TRUE)
                    .add("tests", Json.createArrayBuilder().add(JsonValue.EMPTY_JSON_OBJECT))
                    .build()
            )
                .getChildren(),
            hasSize(1)
        );
    }
}
