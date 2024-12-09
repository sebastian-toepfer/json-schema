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
package io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import jakarta.json.Json;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

final class FormatKeyword implements Assertion, Annotation {

    static final String NAME = "format";
    private final Format format;

    FormatKeyword(final Format format) {
        this.format = Objects.requireNonNull(format);
    }

    @Override
    public Collection<KeywordCategory> categories() {
        return List.of(KeywordCategory.ASSERTION, KeywordCategory.ANNOTATION);
    }

    @Override
    public boolean isValidFor(final JsonValue instance) {
        return !InstanceType.STRING.isInstance(instance) || hasValidValue((JsonString) instance);
    }

    private boolean hasValidValue(final JsonString value) {
        return format.applyTo(value.getString());
    }

    @Override
    public boolean hasName(final String name) {
        return NAME.equals(name);
    }

    @Override
    public JsonValue valueFor(final JsonValue value) {
        return Json.createValue(format.name());
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, format.name());
    }
}
