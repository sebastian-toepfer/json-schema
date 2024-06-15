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
package io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.Json;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

class FormatAssertionKeywordType implements KeywordType {

    private final List<FormatAssertion> formats;

    public FormatAssertionKeywordType(final List<FormatAssertion> formats) {
        this.formats = List.copyOf(formats);
    }

    @Override
    public String name() {
        return "format";
    }

    @Override
    public Keyword createKeyword(final JsonSchema js) {
        if (
            js.getValueType() == JsonValue.ValueType.OBJECT &&
            js.asJsonObject().containsKey(name()) &&
            js.asJsonObject().get(name()).getValueType() == JsonValue.ValueType.STRING
        ) {
            return createKeyword(js.asJsonObject().getString(name()));
        } else {
            throw new IllegalArgumentException("Value must be a string!");
        }
    }

    private FormatAssertionKeyword createKeyword(final String formatName) {
        return formats
            .stream()
            .filter(format -> format.name().equals(formatName))
            .map(FormatAssertionKeyword::new)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("format %s not supported!", formatName)));
    }

    private class FormatAssertionKeyword implements Annotation, Assertion {

        private final FormatAssertion format;

        public FormatAssertionKeyword(final FormatAssertion format) {
            this.format = format;
        }

        @Override
        public Collection<KeywordCategory> categories() {
            return List.of(KeywordCategory.ANNOTATION, KeywordCategory.ASSERTION);
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }

        @Override
        public JsonValue valueFor(final JsonValue value) {
            return Json.createValue(format.name());
        }

        @Override
        public boolean isValidFor(final JsonValue instance) {
            return !InstanceType.STRING.isInstance(instance) || format.isValidFor(((JsonString) instance).getString());
        }

        @Override
        public <T extends Media<T>> T printOn(final T media) {
            return media.withValue(name(), format.name());
        }
    }
}
