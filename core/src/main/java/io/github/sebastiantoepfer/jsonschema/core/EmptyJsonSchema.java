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
package io.github.sebastiantoepfer.jsonschema.core;

import io.github.sebastiantoepfer.common.condition4j.core.NoCondition;
import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.JsonSubSchema;
import io.github.sebastiantoepfer.jsonschema.Validator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.JsonPointer;
import jakarta.json.JsonValue;
import java.util.Optional;
import java.util.stream.Stream;

final class EmptyJsonSchema extends AbstractJsonValueSchema {

    public EmptyJsonSchema() {
        super(JsonValue.EMPTY_JSON_OBJECT);
    }

    @Override
    public Validator validator() {
        return new DefaultValidator(new NoCondition<>());
    }

    @Override
    public Optional<Keyword> keywordByName(final String name) {
        return Optional.empty();
    }

    @Override
    public Optional<JsonSubSchema> subSchema(final String name) {
        return Optional.empty();
    }

    @Override
    public Stream<JsonSubSchema> subSchemas(final String name) {
        return Stream.empty();
    }

    @Override
    public Optional<JsonSubSchema> subSchema(final JsonPointer pointer) {
        return Optional.empty();
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media;
    }
}
