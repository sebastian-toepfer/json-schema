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

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.ddd.media.json.JsonObjectPrintable;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
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
import java.util.regex.Pattern;

final class PatternPropertiesKeywordType implements KeywordType {

    @Override
    public String name() {
        return "patternProperties";
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema) {
        return new PatternPropertiesKeyword(schema.asJsonObject().getJsonObject(name()));
    }

    private class PatternPropertiesKeyword implements Applicator, Annotation {

        private final JsonObject properties;

        public PatternPropertiesKeyword(final JsonObject properties) {
            this.properties = properties;
        }

        @Override
        public <T extends Media<T>> T printOn(final T media) {
            return media.withValue(name(), new JsonObjectPrintable(properties));
        }

        @Override
        public Collection<KeywordCategory> categories() {
            return List.of(Keyword.KeywordCategory.APPLICATOR, Keyword.KeywordCategory.ANNOTATION);
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }

        @Override
        public boolean applyTo(final JsonValue instance) {
            return !InstanceType.OBJECT.isInstance(instance) || propertiesMatches(instance.asJsonObject());
        }

        private boolean propertiesMatches(final JsonObject instance) {
            return instance.entrySet().stream().allMatch(this::propertyMatches);
        }

        private boolean propertyMatches(final Map.Entry<String, JsonValue> property) {
            return properties
                .entrySet()
                .stream()
                .map(e -> Map.entry(Pattern.compile(e.getKey()), e.getValue()))
                .filter(e -> e.getKey().matcher(property.getKey()).find())
                .map(Map.Entry::getValue)
                .map(JsonSchemas::load)
                .allMatch(schema -> schema.validator().isValid(property.getValue()));
        }

        @Override
        public JsonValue valueFor(final JsonValue value) {
            return value
                .asJsonObject()
                .keySet()
                .stream()
                .filter(this::isValidName)
                .map(Json::createValue)
                .collect(toJsonArray());
        }

        private boolean isValidName(final String name) {
            return properties.keySet().stream().map(Pattern::compile).anyMatch(p -> p.matcher(name).find());
        }
    }
}
