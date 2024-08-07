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
import io.github.sebastiantoepfer.ddd.media.json.JsonObjectPrintable;
import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import jakarta.json.JsonNumber;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import java.util.Objects;

/**
 * <b>const</b> : <i>Any</i><br/>
 * Validation succeeds if the instance is equal to this keyword’s value.<br/>
 * <br/>
 * <ul>
 * <li>assertion</li>
 * </ul>
 *
 * source: https://www.learnjsonschema.com/2020-12/validation/const/
 * spec: https://json-schema.org/draft/2020-12/json-schema-validation.html#section-6.1.3
 */
class ConstKeyword implements Assertion {

    static final String NAME = "const";
    private final JsonProvider jsonContext;
    private final JsonValue allowedValue;

    public ConstKeyword(final JsonProvider jsonContext, final JsonValue allowedValue) {
        this.jsonContext = Objects.requireNonNull(jsonContext);
        this.allowedValue = Objects.requireNonNull(allowedValue);
    }

    @Override
    public boolean isValidFor(final JsonValue instance) {
        return (
            instance != null && allowedValue.getValueType() == instance.getValueType() && isEqualsToAllowed(instance)
        );
    }

    @SuppressWarnings("BigDecimalEquals")
    private boolean isEqualsToAllowed(final JsonValue instance) {
        final boolean result;
        if (InstanceType.NUMBER.isInstance(instance)) {
            result = ((JsonNumber) instance).bigDecimalValue()
                .stripTrailingZeros()
                .equals(((JsonNumber) allowedValue).bigDecimalValue().stripTrailingZeros());
        } else {
            result = Objects.equals(allowedValue, instance);
        }
        return result;
    }

    @Override
    public boolean hasName(final String name) {
        return Objects.equals(NAME, name);
    }

    @Override
    public <T extends Media<T>> T printOn(final T media) {
        return new JsonObjectPrintable(jsonContext.createObjectBuilder().add(NAME, allowedValue).build()).printOn(
            media
        );
    }
}
