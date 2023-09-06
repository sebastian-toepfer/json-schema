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
package io.github.sebastiantoepfer.jsonschema.testsuite.junit;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

class JsonSchemaSuitesTestDescriptorTest {

    @Test
    void should_return_id_with_parent() {
        final JsonSchemaSuitesTestDescriptor desc = new JsonSchemaSuitesTestDescriptor(
            new Resource("tests/boolean_schema.json")
        );
        //mutable :(
        desc.setParent(new EngineDescriptor(UniqueId.forEngine("test"), "test"));

        assertThat(desc.getUniqueId(), is(UniqueId.parse("[engine:test]/[suite:tests%2Fboolean_schema.json]")));
    }

    @Test
    void should_return_id_without_parent() {
        final JsonSchemaSuitesTestDescriptor desc = new JsonSchemaSuitesTestDescriptor(
            new Resource("tests/boolean_schema.json")
        );
        //mutable :(
        desc.setParent(null);

        assertThat(desc.getUniqueId(), is(UniqueId.parse("[suite:tests%2Fboolean_schema.json]")));
    }

    @Test
    void should_return_parent() {
        final JsonSchemaSuitesTestDescriptor desc = new JsonSchemaSuitesTestDescriptor(
            new Resource("tests/boolean_schema.json")
        );
        //mutable :(
        desc.setParent(new EngineDescriptor(UniqueId.forEngine("test"), "test"));

        assertThat(desc.getParent(), isPresent());
    }

    @Test
    void should_return_empty_parent() {
        final JsonSchemaSuitesTestDescriptor desc = new JsonSchemaSuitesTestDescriptor(
            new Resource("tests/boolean_schema.json")
        );
        //mutable :(
        desc.setParent(null);

        assertThat(desc.getParent(), isEmpty());
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(JsonSchemaSuitesTestDescriptor.class).withIgnoredFields("parent").verify();
    }
}
