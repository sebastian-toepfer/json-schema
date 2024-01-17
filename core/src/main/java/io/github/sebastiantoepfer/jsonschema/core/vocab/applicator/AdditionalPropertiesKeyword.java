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
import io.github.sebastiantoepfer.jsonschema.JsonSubSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.Annotation;
import io.github.sebastiantoepfer.jsonschema.keyword.Applicator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonCollectors;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * <b>additionalProperties</b> : <i>Schema</i><br/>
 * Validation succeeds if the schema validates against each value not matched by other object applicators
 * in this vocabulary.<br/>
 * <br/>
 * <b>Kind</b><br/>
 * <ul>
 * <li>Applicator</li>
 * <li>Annotation<br/>
 * The annotation result of this keyword is the set of instance property names validated by this keyword.
 * </li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/applicator/additionalproperties/<br/>
 * spec: https://json-schema.org/draft/2020-12/json-schema-core#section-10.3.2.3
 */
final class AdditionalPropertiesKeyword implements Applicator, Annotation {

    static final String NAME = "additionalProperties";
    private final JsonSubSchema additionalPropertiesSchema;

    public AdditionalPropertiesKeyword(final JsonSubSchema additionalPropertiesSchema) {
        this.additionalPropertiesSchema = additionalPropertiesSchema;
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, additionalPropertiesSchema);
    }

    @Override
    public boolean applyTo(final JsonValue instance) {
        return !InstanceType.OBJECT.isInstance(instance) || additionalPropertiesMatches(instance.asJsonObject());
    }

    private boolean additionalPropertiesMatches(final JsonObject instance) {
        return findPropertiesForValidation(instance)
            .map(Map.Entry::getValue)
            .allMatch(value -> additionalPropertiesSchema.validator().isValid(value));
    }

    @Override
    public JsonValue valueFor(final JsonValue instance) {
        return findPropertiesForValidation(instance.asJsonObject())
            .map(Map.Entry::getKey)
            .map(Json::createValue)
            .collect(JsonCollectors.toJsonArray());
    }

    private Stream<Map.Entry<String, JsonValue>> findPropertiesForValidation(final JsonObject instance) {
        final Collection<String> ignoredProperties = findPropertyNamesAlreadyConveredByOthersIn(instance);
        return instance.entrySet().stream().filter(Predicate.not(e -> ignoredProperties.contains(e.getKey())));
    }

    private Collection<String> findPropertyNamesAlreadyConveredByOthersIn(final JsonValue instance) {
        return Stream
            .of(
                additionalPropertiesSchema.owner().keywordByName("properties"),
                additionalPropertiesSchema.owner().keywordByName("patternProperties")
            )
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
        return Objects.equals(NAME, name);
    }

    @Override
    public Collection<KeywordCategory> categories() {
        return List.of(Keyword.KeywordCategory.APPLICATOR, Keyword.KeywordCategory.ANNOTATION);
    }
}
