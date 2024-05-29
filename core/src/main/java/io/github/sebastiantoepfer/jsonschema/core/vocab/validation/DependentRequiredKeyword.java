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
package io.github.sebastiantoepfer.jsonschema.core.vocab.validation;

import io.github.sebastiantoepfer.common.condition4j.Fulfilable;
import io.github.sebastiantoepfer.common.condition4j.core.ContainsOnlyItemsWhichFulfilThe;
import io.github.sebastiantoepfer.common.condition4j.json.JsonValueOfType;
import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.ddd.media.json.JsonObjectPrintable;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.Objects;

/**
 * <b>dependentRequired</b> : <i>Object<String, Array<String>></i><br/>
 * Validation succeeds if, for each name that appears in both the instance and as a name within this keyword’s value,
 * every item in the corresponding array is also the name of a property in the instance.<br/>
 * <br/>
 * <ul>
 * <li>assertion</li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/validation/const/
 * spec: https://json-schema.org/draft/2020-12/json-schema-validation.html#section-6.1.3
 */
final class DependentRequiredKeyword implements Assertion {

    static final String NAME = "dependentRequired";
    private final JsonObject dependentRequired;

    public DependentRequiredKeyword(final JsonObject dependentRequired) {
        new AllPropertiesAreArraysOfString()
            .asVerification("objects must contains only string array props.")
            .check(dependentRequired);
        this.dependentRequired = dependentRequired;
    }

    @Override
    public boolean isValidFor(final JsonValue instance) {
        return !InstanceType.OBJECT.isInstance(instance) || isValidFor(instance.asJsonObject());
    }

    private boolean isValidFor(final JsonObject instance) {
        return instance
            .keySet()
            .stream()
            .filter(dependentRequired::containsKey)
            .map(dependentRequired::getJsonArray)
            .flatMap(Collection::stream)
            .map(JsonString.class::cast)
            .map(JsonString::getString)
            .allMatch(instance::containsKey);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, new JsonObjectPrintable(dependentRequired));
    }

    private static class AllPropertiesAreArraysOfString implements Fulfilable<JsonObject> {

        private final JsonValueOfType isArray;
        private final ContainsOnlyItemsWhichFulfilThe<JsonValue> values;

        public AllPropertiesAreArraysOfString() {
            this.isArray = new JsonValueOfType(JsonValue.ValueType.ARRAY);
            this.values = new ContainsOnlyItemsWhichFulfilThe<>(new JsonValueOfType(JsonValue.ValueType.STRING));
        }

        @Override
        public boolean isFulfilledBy(final JsonObject value) {
            return (
                value.values().stream().allMatch(v -> isArray.isFulfilledBy(v) && values.isFulfilledBy(v.asJsonArray()))
            );
        }
    }
}
