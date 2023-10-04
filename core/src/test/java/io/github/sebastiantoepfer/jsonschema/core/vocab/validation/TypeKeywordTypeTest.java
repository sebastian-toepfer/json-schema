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

import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.core.vocab.validation.TypeKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Assertion;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.junit.jupiter.api.Test;

class TypeKeywordTypeTest {

    @Test
    void should_know_his_name() {
        assertThat(
            new TypeKeywordType()
                .createKeyword(new DefaultJsonSchemaFactory().create(JsonValue.TRUE), Json.createValue("string"))
                .hasName("type"),
            is(true)
        );
    }

    @Test
    void should_know_other_names() {
        assertThat(
            new TypeKeywordType()
                .createKeyword(new DefaultJsonSchemaFactory().create(JsonValue.TRUE), Json.createValue("string"))
                .hasName("id"),
            is(false)
        );
    }

    @Test
    void should_use_stringvalue_to_validate_type() {
        final Assertion typeAssertion = new TypeKeywordType()
            .createKeyword(new DefaultJsonSchemaFactory().create(JsonValue.TRUE), Json.createValue("string"))
            .asAssertion();

        assertThat(typeAssertion.isValidFor(Json.createValue("value")), is(true));
        assertThat(typeAssertion.isValidFor(JsonValue.EMPTY_JSON_OBJECT), is(false));
        assertThat(typeAssertion.isValidFor(Json.createValue(1L)), is(false));
        assertThat(typeAssertion.isValidFor(JsonValue.EMPTY_JSON_ARRAY), is(false));
    }

    @Test
    void should_use_arrayvalue_to_validate_type() {
        final Assertion typeAssertion = new TypeKeywordType()
            .createKeyword(
                new DefaultJsonSchemaFactory().create(JsonValue.TRUE),
                Json.createArrayBuilder().add(Json.createValue("string")).add(Json.createValue("object")).build()
            )
            .asAssertion();

        assertThat(typeAssertion.isValidFor(Json.createValue("value")), is(true));
        assertThat(typeAssertion.isValidFor(JsonValue.EMPTY_JSON_OBJECT), is(true));
        assertThat(typeAssertion.isValidFor(Json.createValue(1L)), is(false));
        assertThat(typeAssertion.isValidFor(JsonValue.EMPTY_JSON_ARRAY), is(false));
    }
}
