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
package io.github.sebastiantoepfer.jsonschema.core.vocab.applicator;

import static jakarta.json.stream.JsonCollectors.toJsonArray;

import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.JsonSubSchema;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSubSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

final class PropertiesKeywordType implements KeywordType {

    @Override
    public String name() {
        return "properties";
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema) {
        return new PropertiesKeyword(schema, schema.asJsonObject().getJsonObject(name()));
    }

    private class PropertiesKeyword implements Applicator, Annotation {

        private final JsonSchema schema;
        private final JsonObject schemas;

        public PropertiesKeyword(final JsonSchema schema, final JsonObject schemas) {
            this.schema = schema;
            this.schemas = schemas;
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }

        @Override
        public Collection<KeywordCategory> categories() {
            return List.of(KeywordCategory.ANNOTATION, KeywordCategory.APPLICATOR);
        }

        @Override
        public boolean applyTo(final JsonValue instance) {
            return !InstanceType.OBJECT.isInstance(instance) || propertiesMatches(instance.asJsonObject());
        }

        private boolean propertiesMatches(final JsonObject instance) {
            return instance.entrySet().stream().allMatch(this::propertyMatches);
        }

        private boolean propertyMatches(final Map.Entry<String, JsonValue> property) {
            return Optional
                .ofNullable(schemas.get(property.getKey()))
                .flatMap(this::toSubSchema)
                .map(JsonSchema::validator)
                .map(validator -> validator.isValid(property.getValue()))
                .orElse(true);
        }

        private Optional<JsonSubSchema> toSubSchema(final JsonValue value) {
            return new DefaultJsonSchemaFactory()
                .tryToCreateSchemaFrom(value)
                .map(subSchema -> new DefaultJsonSubSchema(schema, subSchema));
        }

        @Override
        public JsonValue valueFor(final JsonValue instance) {
            return instance
                .asJsonObject()
                .keySet()
                .stream()
                .filter(schemas.asJsonObject()::containsKey)
                .map(Json::createValue)
                .collect(toJsonArray());
        }
    }
}
