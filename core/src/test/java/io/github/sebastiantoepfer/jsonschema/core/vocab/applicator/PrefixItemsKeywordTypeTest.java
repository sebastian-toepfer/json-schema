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
package io.github.sebastiantoepfer.jsonschema.core.vocab.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.junit.jupiter.api.Test;

class PrefixItemsKeywordTypeTest {

    @Test
    void should_know_his_name() {
        final Keyword items = new PrefixItemsKeywordType()
            .createKeyword(
                new DefaultJsonSchemaFactory()
                    .create(
                        Json
                            .createObjectBuilder()
                            .add("prefixItems", Json.createArrayBuilder().add(JsonValue.TRUE))
                            .build()
                    )
            );

        assertThat(items.hasName("prefixItems"), is(true));
        assertThat(items.hasName("test"), is(false));
    }

    @Test
    void should_not_be_createbale_from_non_array() {
        final KeywordType keywordType = new PrefixItemsKeywordType();
        final JsonSchema schema = new DefaultJsonSchemaFactory()
            .create(Json.createObjectBuilder().add("prefixItems", JsonValue.EMPTY_JSON_OBJECT).build());

        assertThrows(IllegalArgumentException.class, () -> keywordType.createKeyword(schema));
    }

    @Test
    void should_return_zero_as_value() {
        assertThat(
            new PrefixItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("prefixItems", Json.createArrayBuilder().add(JsonValue.TRUE))
                                .build()
                        )
                )
                .asAnnotation()
                .valueFor(Json.createArrayBuilder().add(1).add(2).build()),
            is(Json.createValue(0))
        );
    }

    @Test
    void should_retrun_true_if_is_applies_to_all_values() {
        assertThat(
            new PrefixItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("prefixItems", Json.createArrayBuilder().add(JsonValue.TRUE).add(JsonValue.TRUE))
                                .build()
                        )
                )
                .asAnnotation()
                .valueFor(Json.createArrayBuilder().add(1).add(2).build()),
            is(JsonValue.TRUE)
        );
    }

    @Test
    void should_be_valid_for_non_arrays() {
        assertThat(
            new PrefixItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("prefixItems", Json.createArrayBuilder().add(JsonValue.FALSE))
                                .build()
                        )
                )
                .asApplicator()
                .applyTo(Json.createValue(1)),
            is(true)
        );
    }

    @Test
    void should_be_valid_if_first_item_match_schema() {
        assertThat(
            new PrefixItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("prefixItems", Json.createArrayBuilder().add(JsonValue.TRUE))
                                .build()
                        )
                )
                .asApplicator()
                .applyTo(Json.createArrayBuilder().add(1).build()),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_second_item_does_not_match_schema() {
        assertThat(
            new PrefixItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("prefixItems", Json.createArrayBuilder().add(JsonValue.TRUE).add(JsonValue.FALSE))
                                .build()
                        )
                )
                .asApplicator()
                .applyTo(Json.createArrayBuilder().add(1).add(3).build()),
            is(false)
        );
    }

    @Test
    void should_be_applicator_and_annotation() {
        assertThat(
            new PrefixItemsKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(Json.createObjectBuilder().add("prefixItems", JsonValue.EMPTY_JSON_ARRAY).build())
                )
                .categories(),
            containsInAnyOrder(Keyword.KeywordCategory.APPLICATOR, Keyword.KeywordCategory.ANNOTATION)
        );
    }
}
