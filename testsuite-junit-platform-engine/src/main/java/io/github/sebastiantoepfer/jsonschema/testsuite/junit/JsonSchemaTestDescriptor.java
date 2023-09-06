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

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.TestTag;
import org.junit.platform.engine.UniqueId;

final class JsonSchemaTestDescriptor implements TestDescriptor, ExecuteableTest {

    private final TestDescriptor parent;
    private final JsonValue schema;
    private final JsonObject testDescription;

    public JsonSchemaTestDescriptor(
        final TestDescriptor parent,
        final JsonValue schema,
        final JsonObject testDescription
    ) {
        this.parent = Objects.requireNonNull(parent);
        this.schema = Objects.requireNonNull(schema);
        this.testDescription = Objects.requireNonNull(testDescription);
    }

    @Override
    public UniqueId getUniqueId() {
        return parent.getUniqueId().append("test", getDisplayName());
    }

    @Override
    public String getDisplayName() {
        return testDescription.getString("description");
    }

    @Override
    public Set<TestTag> getTags() {
        return Set.of();
    }

    @Override
    public Optional<TestSource> getSource() {
        return Optional.empty();
    }

    @Override
    public Optional<TestDescriptor> getParent() {
        return Optional.of(parent);
    }

    @Override
    public void setParent(final TestDescriptor parent) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public Set<? extends TestDescriptor> getChildren() {
        return Set.of();
    }

    @Override
    public void addChild(final TestDescriptor descriptor) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public void removeChild(final TestDescriptor descriptor) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public void removeFromHierarchy() {}

    @Override
    public Type getType() {
        return Type.TEST;
    }

    @Override
    public Optional<? extends TestDescriptor> findByUniqueId(final UniqueId uniqueId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void run(final ExecutionRequest request) {
        request.getEngineExecutionListener().executionStarted(this);
        TestExecutionResult result;
        try {
            final boolean isValid = ServiceLoader
                .load(SchemaTestValidatorLoader.class)
                .findFirst()
                .orElseThrow()
                .loadSchemaTestValidator(schema.toString())
                .validate(testDescription.get("data").toString());
            if (isValid == testDescription.getBoolean("valid")) {
                result = TestExecutionResult.successful();
            } else {
                throw new RuntimeException("expected " + testDescription.getBoolean("valid") + " but was " + isValid);
            }
        } catch (Throwable t) {
            result = TestExecutionResult.failed(t);
        }
        request.getEngineExecutionListener().executionFinished(this, result);
    }

    @Override
    public String toString() {
        return getUniqueId().toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.schema);
        hash = 97 * hash + Objects.hashCode(this.testDescription);
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
        final JsonSchemaTestDescriptor other = (JsonSchemaTestDescriptor) obj;
        if (!Objects.equals(this.schema, other.schema)) {
            return false;
        }
        return Objects.equals(this.testDescription, other.testDescription);
    }
}
