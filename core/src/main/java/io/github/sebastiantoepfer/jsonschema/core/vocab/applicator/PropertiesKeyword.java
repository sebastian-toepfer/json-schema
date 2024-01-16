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
package io.github.sebastiantoepfer.jsonschema.core.vocab.applicator;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.NamedJsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonCollectors;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <b>properties</b> : <i>Object<String, Schema></i><br/>
 * Validation succeeds if, for each name that appears in both the instance and as a name within this keyword’s value,
 * the child instance for that name successfully validates against the corresponding schema.<br/>
 * <br/>
 * <b>Kind</b><br/>
 * <ul>
 * <li>Applicator</li>
 * <li>Annotation<br/>
 * The annotation result of this keyword is the set of instance property names matched by this keyword.
 * </li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/applicator/properties/
 * spec: https://json-schema.org/draft/2020-12/json-schema-core.html#section-10.3.2.1
 */
final class PropertiesKeyword implements Applicator, Annotation {

    static final String NAME = "properties";
    private final NamedJsonSchemas properties;

    public PropertiesKeyword(final NamedJsonSchemas properties) {
        this.properties = Objects.requireNonNull(properties);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, properties);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
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
        return properties
            .schemaWithName(property.getKey())
            .map(JsonSchema::validator)
            .map(validator -> validator.isValid(property.getValue()))
            .orElse(true);
    }

    @Override
    public JsonValue valueFor(final JsonValue instance) {
        return instance
            .asJsonObject()
            .keySet()
            .stream()
            .filter(properties::hasSchemaWithName)
            .map(Json::createValue)
            .collect(JsonCollectors.toJsonArray());
    }
}
