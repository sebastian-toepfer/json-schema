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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.StaticAnnotation;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.util.List;
import org.junit.jupiter.api.Test;

class ItemsKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword items = new ItemsKeyword(List.of(), JsonSchemas.load(JsonValue.TRUE));
        assertThat(items.hasName("items"), is(true));
        assertThat(items.hasName("test"), is(false));
    }

    @Test
    void should_be_invalid_if_items_does_not_match_schema() {
        assertThat(
            new ItemsKeyword(List.of(), JsonSchemas.load(Json.createObjectBuilder().add("type", "number").build()))
                .asApplicator()
                .applyTo(Json.createArrayBuilder().add(1).add("invalid").add(2).build()),
            is(false)
        );
    }

    @Test
    void should_be_valid_if_all_items_match_schema() {
        assertThat(
            new ItemsKeyword(List.of(), JsonSchemas.load(Json.createObjectBuilder().add("type", "number").build()))
                .asApplicator()
                .applyTo(Json.createArrayBuilder().add(1).add(2).build()),
            is(true)
        );
    }

    @Test
    void should_be_applicator_and_annotation() {
        assertThat(
            new ItemsKeyword(List.of(), JsonSchemas.load(JsonValue.EMPTY_JSON_OBJECT)).categories(),
            contains(Keyword.KeywordCategory.APPLICATOR, Keyword.KeywordCategory.ANNOTATION)
        );
    }

    @Test
    void should_produces_true_if_is_applied_to_any_instance() {
        assertThat(
            new ItemsKeyword(List.of(), JsonSchemas.load(JsonValue.TRUE))
                .asAnnotation()
                .valueFor(Json.createArrayBuilder().add(1).build()),
            is(JsonValue.TRUE)
        );
    }

    @Test
    void should_return_false_if_not_applies_to_any_item() {
        assertThat(
            new ItemsKeyword(
                List.of(new StaticAnnotation("prefixItems", JsonValue.TRUE)),
                JsonSchemas.load(JsonValue.FALSE)
            )
                .asAnnotation()
                .valueFor(Json.createArrayBuilder().add(1).build()),
            is(JsonValue.FALSE)
        );
    }

    @Test
    void should_be_valid_if_invaliditem_is_already_checked_by_prefixItems() {
        assertThat(
            new ItemsKeyword(
                List.of(new StaticAnnotation("prefixItems", JsonValue.TRUE)),
                JsonSchemas.load(Json.createObjectBuilder().add("type", "integer").build())
            )
                .asApplicator()
                .applyTo(Json.createArrayBuilder().add("1").add("2").add(1).build()),
            is(true)
        );
    }

    @Test
    void should_be_invalid_if_invaliditem_is_not_already_checked_by_prefixItems() {
        assertThat(
            new ItemsKeyword(
                List.of(new StaticAnnotation("prefixItems", Json.createValue(0))),
                JsonSchemas.load(Json.createObjectBuilder().add("type", "integer").build())
            )
                .asApplicator()
                .applyTo(Json.createArrayBuilder().add("1").add("2").add(1).build()),
            is(false)
        );
    }
}
