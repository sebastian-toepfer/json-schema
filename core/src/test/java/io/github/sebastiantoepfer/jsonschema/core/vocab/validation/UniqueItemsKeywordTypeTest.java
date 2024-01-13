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
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.io.StringReader;
import java.math.BigDecimal;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class UniqueItemsKeywordTypeTest {

    @Test
    void should_know_his_name() {
        final Keyword keyword = new UniqueItemsKeywordType()
            .createKeyword(
                new DefaultJsonSchemaFactory()
                    .create(Json.createObjectBuilder().add("uniqueItems", JsonValue.FALSE).build())
            );

        assertThat(keyword.hasName("uniqueItems"), is(true));
        assertThat(keyword.hasName("test"), is(false));
    }

    @Test
    void should_not_be_createbale_from_non_boolean() {
        final JsonSchema schema = new DefaultJsonSchemaFactory()
            .create(Json.createObjectBuilder().add("uniqueItems", JsonValue.EMPTY_JSON_OBJECT).build());
        final KeywordType keywordType = new UniqueItemsKeywordType();
        assertThrows(IllegalArgumentException.class, () -> keywordType.createKeyword(schema));
    }

    @Test
    void should_be_valid_for_uniqueItems() {
        assertThat(
            new UniqueItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(Json.createObjectBuilder().add("uniqueItems", JsonValue.TRUE).build())
                )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("1").add("2").build()),
            is(true)
        );
    }

    @Test
    void should_be_valid_for_non_uniqueItems_if_false() {
        assertThat(
            new UniqueItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(Json.createObjectBuilder().add("uniqueItems", JsonValue.FALSE).build())
                )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("1").add("1").build()),
            is(true)
        );
    }

    @Test
    void should_be_invalid_for_non_uniqueItems() {
        assertThat(
            new UniqueItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(Json.createObjectBuilder().add("uniqueItems", JsonValue.TRUE).build())
                )
                .asAssertion()
                .isValidFor(Json.createArrayBuilder().add("1").add("1").build()),
            is(false)
        );
    }

    @Test
    void should_be_valid_for_non_arrays() {
        assertThat(
            new UniqueItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(Json.createObjectBuilder().add("uniqueItems", JsonValue.FALSE).build())
                )
                .asAssertion()
                .isValidFor(JsonValue.EMPTY_JSON_OBJECT),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_numbers_mathematically_unequal() {
        assertThat(
            new UniqueItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(Json.createObjectBuilder().add("uniqueItems", JsonValue.TRUE).build())
                )
                .asAssertion()
                .isValidFor(Json.createReader(new StringReader("[1.0,1.00,1]")).readArray()),
            is(false)
        );
    }

    @Test
    void pitests_say_i_must_write_this_tests() {
        //hashset uses hashCode to determine if equals needs to be used, so we don't really need equals.
        //but to have a valid java class we should override both
        final UniqueItemsKeywordType.JsonValueNumberEqualsFix obj = new UniqueItemsKeywordType.JsonValueNumberEqualsFix(
            JsonValue.EMPTY_JSON_OBJECT
        );
        final UniqueItemsKeywordType.JsonValueNumberEqualsFix number1 =
            new UniqueItemsKeywordType.JsonValueNumberEqualsFix(Json.createValue(new BigDecimal("1.0")));
        final UniqueItemsKeywordType.JsonValueNumberEqualsFix number2 =
            new UniqueItemsKeywordType.JsonValueNumberEqualsFix(Json.createValue(new BigDecimal("1.00")));

        assertThat(obj.equals(obj), is(true));
        assertThat(number1.equals(number1), is(true));
        assertThat(number1.equals(number2), is(true));

        assertThat(obj.equals(number1), is(false));
    }

    @Test
    void should_be_printable() {
        assertThat(
            new UniqueItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(Json.createObjectBuilder().add("uniqueItems", JsonValue.TRUE).build())
                )
                .printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("uniqueItems"), is(true))
        );
    }
}
