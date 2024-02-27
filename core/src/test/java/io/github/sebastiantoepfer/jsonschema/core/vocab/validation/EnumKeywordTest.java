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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.ArrayKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class EnumKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword enumKeyword = createKeywordFrom(
            Json.createObjectBuilder().add("enum", JsonValue.EMPTY_JSON_ARRAY).build()
        );

        assertThat(enumKeyword.hasName("enum"), is(true));
        assertThat(enumKeyword.hasName("test"), is(false));
    }

    @Test
    void should_valid_for_string_value_which_is_in_array() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("enum", Json.createArrayBuilder().add("TEST").add("VALID")).build()
            )
                .asAssertion()
                .isValidFor(Json.createValue("TEST")),
            is(true)
        );
    }

    @Test
    void should_be_invalid_for_number_which_is_not_in_array() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("enum", Json.createArrayBuilder().add("TEST").add("VALID")).build()
            )
                .asAssertion()
                .isValidFor(Json.createValue(2)),
            is(false)
        );
    }

    @Test
    void should_be_valid_for_decimal_without_scale_if_number_is_valid() {
        assertThat(
            createKeywordFrom(Json.createObjectBuilder().add("enum", Json.createArrayBuilder().add(1)).build())
                .asAssertion()
                .isValidFor(Json.createValue(1.0)),
            is(true)
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("enum", Json.createArrayBuilder().add("TEST").add("VALID")).build()
            ).printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("enum"), containsInAnyOrder("TEST", "VALID"))
        );
    }

    private static Keyword createKeywordFrom(final JsonObject json) {
        return new ArrayKeywordType("enum", EnumKeyword::new).createKeyword(
            new DefaultJsonSchemaFactory().create(json)
        );
    }
}
