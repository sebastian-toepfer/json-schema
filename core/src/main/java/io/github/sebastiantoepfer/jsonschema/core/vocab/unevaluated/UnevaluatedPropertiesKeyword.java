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
package io.github.sebastiantoepfer.jsonschema.core.vocab.unevaluated;

import static jakarta.json.stream.JsonCollectors.toJsonArray;
import static java.util.function.Predicate.not;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Stream;

class UnevaluatedPropertiesKeyword implements Applicator, Annotation {

    static final String NAME = "unevaluatedProperties";
    private final Collection<Annotation> evaluatedProperties;
    private final JsonSchema schema;

    public UnevaluatedPropertiesKeyword(final Collection<Annotation> evaluatedProperties, final JsonSchema schema) {
        this.evaluatedProperties = List.copyOf(evaluatedProperties);
        this.schema = Objects.requireNonNull(schema);
    }

    @Override
    public Collection<KeywordCategory> categories() {
        return EnumSet.of(KeywordCategory.ANNOTATION, KeywordCategory.APPLICATOR);
    }

    @Override
    public boolean applyTo(final JsonValue instance) {
        return !InstanceType.OBJECT.isInstance(instance) || applyTo(instance.asJsonObject());
    }

    private boolean applyTo(final JsonObject instance) {
        return determineUnevaluatedPropertyNames(instance).allMatch(this::applyTo);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, schema);
    }

    @Override
    public JsonValue valueFor(final JsonValue value) {
        return valueFor(value.asJsonObject());
    }

    private JsonValue valueFor(final JsonObject obj) {
        return determineUnevaluatedPropertyNames(obj)
            .filter(this::applyTo)
            .map(Entry::getKey)
            .map(Json::createValue)
            .collect(toJsonArray());
    }

    private boolean applyTo(final Entry<String, JsonValue> property) {
        return schema.validator().isValid(property.getValue());
    }

    private Stream<Entry<String, JsonValue>> determineUnevaluatedPropertyNames(final JsonObject instance) {
        final List<String> alreadyEvalutatedProperties = determineEvalutatedPropertyNames(instance).toList();
        return instance.entrySet().stream().filter(not(entry -> alreadyEvalutatedProperties.contains(entry.getKey())));
    }

    private Stream<String> determineEvalutatedPropertyNames(final JsonObject obj) {
        return evaluatedProperties
            .stream()
            .map(eval -> eval.valueFor(obj))
            .filter(j -> j.getValueType() == JsonValue.ValueType.ARRAY)
            .map(JsonValue::asJsonArray)
            .flatMap(Collection::stream)
            .filter(JsonString.class::isInstance)
            .map(JsonString.class::cast)
            .map(JsonString::getString)
            .distinct();
    }
}
