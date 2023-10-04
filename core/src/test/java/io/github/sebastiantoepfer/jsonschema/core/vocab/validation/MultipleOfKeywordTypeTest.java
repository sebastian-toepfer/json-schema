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
package io.github.sebastiantoepfer.jsonschema.core.vocab.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.core.vocab.validation.MultipleOfKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MultipleOfKeywordTypeTest {

    @Test
    void should_know_his_name() {
        final Keyword multipleOf = new MultipleOfKeywordType()
            .createKeyword(new DefaultJsonSchemaFactory().create(JsonValue.TRUE), Json.createValue(10));

        assertThat(multipleOf.hasName("multipleOf"), is(true));
        assertThat(multipleOf.hasName("test"), is(false));
    }

    @Test
    void should_not_be_creatable_with_non_integer_value() {
        final MultipleOfKeywordType keywordType = new MultipleOfKeywordType();
        final JsonSchema schema = new DefaultJsonSchemaFactory().create(JsonValue.TRUE);
        assertThrows(IllegalArgumentException.class, () -> keywordType.createKeyword(schema, JsonValue.FALSE));
    }

    @Test
    void should_be_valid_for_non_number_values() {
        assertThat(
            new MultipleOfKeywordType()
                .createKeyword(new DefaultJsonSchemaFactory().create(JsonValue.TRUE), Json.createValue(10))
                .asAssertion()
                .isValidFor(JsonValue.EMPTY_JSON_OBJECT),
            is(true)
        );
    }

    @Test
    void should_be_valid_for_a_multipleOf() {
        assertThat(
            new MultipleOfKeywordType()
                .createKeyword(new DefaultJsonSchemaFactory().create(JsonValue.TRUE), Json.createValue(1.5))
                .asAssertion()
                .isValidFor(Json.createValue(4.5)),
            is(true)
        );
    }

    @Test
    void should_be_invalid_for_non_multipleOf() {
        assertThat(
            new MultipleOfKeywordType()
                .createKeyword(new DefaultJsonSchemaFactory().create(JsonValue.TRUE), Json.createValue(2))
                .asAssertion()
                .isValidFor(Json.createValue(7)),
            is(false)
        );
    }

    @Test
    void should_be_valid_for_any_int_if_multipleOf_is_1en8() {
        assertThat(
            new MultipleOfKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory().create(JsonValue.TRUE),
                    Json.createValue(new BigDecimal("1e-8"))
                )
                .asAssertion()
                .isValidFor(Json.createValue(12391239123L)),
            is(true)
        );
    }
}
