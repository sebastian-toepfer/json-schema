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
import static java.util.function.Predicate.not;

import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

class AdditionalPropertiesKeywordType implements KeywordType {

    @Override
    public String name() {
        return "additionalProperties";
    }

    @Override
    public Keyword createKeyword(final JsonSchema schema, final JsonValue value) {
        return new AdditionalPropertiesKeyword(schema, value);
    }

    private class AdditionalPropertiesKeyword implements Applicator, Annotation {

        private final JsonSchema schema;
        private final JsonValue additionalProperties;

        public AdditionalPropertiesKeyword(final JsonSchema schema, final JsonValue additionalPropertiesSchema) {
            this.schema = schema;
            this.additionalProperties = additionalPropertiesSchema;
        }

        @Override
        public boolean applyTo(final JsonValue instance) {
            return !InstanceType.OBJECT.isInstance(instance) || additionalPropertiesMatches(instance.asJsonObject());
        }

        private boolean additionalPropertiesMatches(final JsonObject instance) {
            final JsonSchema additionalPropertiesSchema = new DefaultJsonSchemaFactory().create(additionalProperties);
            return findPropertiesForValidation(instance)
                .map(Map.Entry::getValue)
                .allMatch(value -> additionalPropertiesSchema.validator().isValid(value));
        }

        @Override
        public JsonValue valueFor(final JsonValue instance) {
            return findPropertiesForValidation(instance.asJsonObject())
                .map(Map.Entry::getKey)
                .map(Json::createValue)
                .collect(toJsonArray());
        }

        private Stream<Map.Entry<String, JsonValue>> findPropertiesForValidation(final JsonObject instance) {
            final Collection<String> ignoredProperties = findePropertyNamesAlreadyConveredByOthersIn(instance);
            return instance.entrySet().stream().filter(not(e -> ignoredProperties.contains(e.getKey())));
        }

        private Collection<String> findePropertyNamesAlreadyConveredByOthersIn(final JsonValue instance) {
            return Stream
                .of(schema.keywordByName("properties"), schema.keywordByName("patternProperties"))
                .flatMap(Optional::stream)
                .map(Keyword::asAnnotation)
                .map(anno -> anno.valueFor(instance))
                .map(JsonValue::asJsonArray)
                .flatMap(Collection::stream)
                .filter(JsonString.class::isInstance)
                .map(JsonString.class::cast)
                .map(JsonString::getString)
                .toList();
        }

        @Override
        public boolean hasName(final String name) {
            return Objects.equals(name(), name);
        }

        @Override
        public Collection<KeywordCategory> categories() {
            return List.of(Keyword.KeywordCategory.APPLICATOR, Keyword.KeywordCategory.ANNOTATION);
        }
    }
}
