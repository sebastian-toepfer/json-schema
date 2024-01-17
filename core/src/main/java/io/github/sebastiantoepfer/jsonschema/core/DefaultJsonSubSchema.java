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

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.JsonSubSchema;
import io.github.sebastiantoepfer.jsonschema.Validator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

final class DefaultJsonSubSchema implements JsonSubSchema {

    private final JsonSchema owner;
    private final JsonSchema schema;

    DefaultJsonSubSchema(final JsonSchema owner, final JsonSchema schema) {
        this.owner = Objects.requireNonNull(owner);
        this.schema = Objects.requireNonNull(schema);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return schema.printOn(media);
    }

    @Override
    public JsonSchema owner() {
        return owner;
    }

    @Override
    public Validator validator() {
        final Validator result;
        if (isJsonObject()) {
            result = keywords().collect(collectingAndThen(toList(), KeywordBasedValidator::new));
        } else {
            result = schema.validator();
        }
        return result;
    }

    @Override
    public Optional<Keyword> keywordByName(final String name) {
        return keywords().filter(keyword -> keyword.hasName(name)).findAny();
    }

    private Stream<Keyword> keywords() {
        final Stream<Keyword> result;
        if (isJsonObject()) {
            final Keywords keywords = new KeywordExtractor(schema).createKeywords();
            result =
                asJsonObject().keySet().stream().map(propertyName -> keywords.createKeywordFor(this, propertyName));
        } else {
            result = Stream.empty();
        }
        return result;
    }

    private boolean isJsonObject() {
        return getValueType() == JsonValue.ValueType.OBJECT;
    }

    @Override
    public Optional<JsonSubSchema> asSubSchema(final String name) {
        return Optional
            .ofNullable(asJsonObject().get(name))
            .flatMap(new DefaultJsonSchemaFactory()::tryToCreateSchemaFrom)
            .map(subSchema -> new DefaultJsonSubSchema(this, subSchema));
    }

    @Override
    public ValueType getValueType() {
        return schema.getValueType();
    }

    @Override
    public JsonObject asJsonObject() {
        return schema.asJsonObject();
    }
}
