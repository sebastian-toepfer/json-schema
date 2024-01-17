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
package io.github.sebastiantoepfer.jsonschema.core.codition;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.json.JsonObject;
import jakarta.json.JsonPointer;
import jakarta.json.JsonValue;
import java.util.Objects;

public final class JsonPropertyCondition implements Condition<JsonObject> {

    private final JsonPointer pointer;
    private final Condition<JsonValue> condition;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public JsonPropertyCondition(final JsonPointer pointer, final Condition<JsonValue> condition) {
        this.pointer = Objects.requireNonNull(pointer);
        this.condition = Objects.requireNonNull(condition);
    }

    @Override
    public boolean isFulfilledBy(final JsonObject value) {
        final boolean result;
        if (pointer.containsValue(value)) {
            result = condition.isFulfilledBy(pointer.getValue(value));
        } else {
            result = condition.isFulfilledBy(JsonValue.NULL);
        }
        return result;
    }
}
