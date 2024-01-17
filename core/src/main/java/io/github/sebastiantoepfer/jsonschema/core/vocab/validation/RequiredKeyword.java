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

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * <b>required</b> : <i>Array<String></i><br/>
 * An object instance is valid against this keyword if every item in the array is the name of a property in the<br/>
 * instance.<br/>
 * <br/>
 *
 * source: https://www.learnjsonschema.com/2020-12/validation/required/
 * spec: https://json-schema.org/draft/2020-12/json-schema-validation.html#section-6.5.3
 */
final class RequiredKeyword implements Assertion {

    static final String NAME = "required";
    private final Set<String> required;

    public RequiredKeyword(final Collection<String> required) {
        this.required = Set.copyOf(required);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return media.withValue(NAME, required);
    }

    @Override
    public boolean isValidFor(final JsonValue instance) {
        return !InstanceType.OBJECT.isInstance(instance) || hasAllRequiredProperties(instance.asJsonObject());
    }

    private boolean hasAllRequiredProperties(final JsonObject instance) {
        return required.stream().allMatch(instance.keySet()::contains);
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }
}
