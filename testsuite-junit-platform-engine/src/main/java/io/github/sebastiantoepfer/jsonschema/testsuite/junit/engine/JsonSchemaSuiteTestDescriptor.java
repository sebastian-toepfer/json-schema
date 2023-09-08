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

import static java.util.stream.Collectors.toSet;

import jakarta.json.JsonObject;
import java.util.Objects;
import java.util.Set;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;

final class JsonSchemaSuiteTestDescriptor extends ImmutableTestDescription {

    private final JsonObject testSuiteDescription;

    JsonSchemaSuiteTestDescriptor(final TestDescriptor parent, final JsonObject testSuiteDescription) {
        super(parent);
        this.testSuiteDescription = Objects.requireNonNull(testSuiteDescription);
    }

    @Override
    public UniqueId getUniqueId() {
        return getParent().orElseThrow().getUniqueId().append("innersuite", getDisplayName());
    }

    @Override
    public String getDisplayName() {
        return testSuiteDescription.getString("description");
    }

    @Override
    public Set<? extends TestDescriptor> getChildren() {
        return testSuiteDescription
            .getJsonArray("tests")
            .stream()
            .map(test -> new JsonSchemaTestDescriptor(this, testSuiteDescription.get("schema"), test.asJsonObject()))
            .collect(toSet());
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    @Override
    public String toString() {
        return getUniqueId().toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.testSuiteDescription);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JsonSchemaSuiteTestDescriptor other = (JsonSchemaSuiteTestDescriptor) obj;
        return Objects.equals(this.testSuiteDescription, other.testSuiteDescription);
    }
}
