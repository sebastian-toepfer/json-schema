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
package io.github.sebastiantoepfer.jsonschema.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.jsonschema.Validator;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AbstractJsonValueSchemaTest {

    @Test
    void should_return_booleantype_for_boolean_schema() {
        assertThat(new MyJsonValueSchema(JsonValue.FALSE).getValueType(), is(JsonValue.ValueType.FALSE));
    }

    @Test
    void should_return_objetctype_for_empty_schema() {
        assertThat(new MyJsonValueSchema(JsonValue.EMPTY_JSON_OBJECT).getValueType(), is(JsonValue.ValueType.OBJECT));
    }

    @Test
    void should_convert_to_jsonobject() {
        assertThat(new MyJsonValueSchema(JsonValue.EMPTY_JSON_OBJECT).asJsonObject(), is(instanceOf(JsonObject.class)));
    }

    @Test
    void should_convert_to_jsonarray() {
        assertThat(new MyJsonValueSchema(JsonValue.EMPTY_JSON_ARRAY).asJsonArray(), is(instanceOf(JsonArray.class)));
    }

    private static class MyJsonValueSchema extends AbstractJsonValueSchema {

        public MyJsonValueSchema(JsonValue value) {
            super(value);
        }

        @Override
        public Validator validator() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Optional<Keyword> keywordByName(final String name) {
            return Optional.empty();
        }
    }
}
