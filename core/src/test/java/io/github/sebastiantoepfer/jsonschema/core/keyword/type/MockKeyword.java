/*
 * The MIT License
 *
 * Copyright 2024 sebastian.
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
package io.github.sebastiantoepfer.jsonschema.core.keyword.type;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import io.github.sebastiantoepfer.jsonschema.keyword.Identifier;
import io.github.sebastiantoepfer.jsonschema.keyword.ReservedLocation;
import jakarta.json.JsonValue;
import java.net.URI;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;

final class MockKeyword implements Identifier, Assertion, Annotation, Applicator, ReservedLocation {

    private final String name;

    public MockKeyword(final String name) {
        this.name = name;
    }

    @Override
    public URI asUri() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isValidFor(final JsonValue instance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JsonValue valueFor(final JsonValue value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean applyTo(final JsonValue instance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<KeywordCategory> categories() {
        return EnumSet.allOf(KeywordCategory.class);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(this.name, name);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(name, name);
    }
}
