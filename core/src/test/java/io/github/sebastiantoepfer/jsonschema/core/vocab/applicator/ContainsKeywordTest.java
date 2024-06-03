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
package io.github.sebastiantoepfer.jsonschema.core.vocab.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.core.DefaultJsonSchemaFactory;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.SubSchemaKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class ContainsKeywordTest {

    @Test
    void should_be_printable() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "number")).build()
            ).printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("contains"), hasEntry(is("type"), is("number")))
        );
    }

    @Test
    void should_know_his_name() {
        final Keyword enumKeyword = new ContainsKeyword(JsonSchemas.load(JsonValue.TRUE));

        assertThat(enumKeyword.hasName("contains"), is(true));
        assertThat(enumKeyword.hasName("test"), is(false));
    }

    @Test
    void should_be_an_applicator_and_annotation() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "number")).build()
            ).categories(),
            Matchers.containsInAnyOrder(Keyword.KeywordCategory.ANNOTATION, Keyword.KeywordCategory.APPLICATOR)
        );
    }

    @Test
    void should_apply_for_non_array() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "number")).build()
            )
                .asApplicator()
                .applyTo(JsonValue.EMPTY_JSON_OBJECT),
            is(true)
        );
    }

    @Test
    void should_not_apply_to_empty_array_if_non_min_andor_max_is_provided() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "number")).build()
            )
                .asApplicator()
                .applyTo(JsonValue.EMPTY_JSON_ARRAY),
            is(false)
        );
    }

    @Test
    void should_apply_if_one_item_applies() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "number")).build()
            )
                .asApplicator()
                .applyTo(
                    Json.createArrayBuilder()
                        .add("foo")
                        .add(3)
                        .add(false)
                        .add(Json.createArrayBuilder().add("bar"))
                        .add(-5)
                        .build()
                ),
            is(true)
        );
    }

    @Test
    void should_apply_if_all_item_applies() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "string")).build()
            )
                .asApplicator()
                .applyTo(Json.createArrayBuilder().add("foo").add("bar").add("baz").build()),
            is(true)
        );
    }

    @Test
    void should_not_apply_if_non_item_applies() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "number")).build()
            )
                .asApplicator()
                .applyTo(
                    Json.createArrayBuilder().add("foo").add(false).add(Json.createArrayBuilder().add("bar")).build()
                ),
            is(false)
        );
    }

    @Test
    void should_return_false_for_non_array() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "number")).build()
            )
                .asAnnotation()
                .valueFor(JsonValue.EMPTY_JSON_OBJECT),
            is(JsonValue.FALSE)
        );
    }

    @Test
    void should_return_empty_array_if_no_item_applies() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "number")).build()
            )
                .asAnnotation()
                .valueFor(Json.createArrayBuilder().add("foo").build())
                .asJsonArray(),
            is(empty())
        );
    }

    @Test
    void should_return_empty_array_for_empty_array() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "number")).build()
            )
                .asAnnotation()
                .valueFor(JsonValue.EMPTY_JSON_ARRAY)
                .asJsonArray(),
            is(empty())
        );
    }

    @Test
    void should_return_matching_items() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "number")).build()
            )
                .asAnnotation()
                .valueFor(
                    Json.createArrayBuilder()
                        .add("foo")
                        .add(3)
                        .add(false)
                        .add(Json.createArrayBuilder().add("bar"))
                        .add(-5)
                        .build()
                )
                .asJsonArray(),
            containsInAnyOrder(Json.createValue(3), Json.createValue(-5))
        );
    }

    @Test
    void should_return_true_if_all_item_applies() {
        assertThat(
            createKeywordFrom(
                Json.createObjectBuilder().add("contains", Json.createObjectBuilder().add("type", "string")).build()
            )
                .asAnnotation()
                .valueFor(Json.createArrayBuilder().add("foo").add("bar").add("baz").build()),
            is(JsonValue.TRUE)
        );
    }

    private static Keyword createKeywordFrom(final JsonObject json) {
        return new SubSchemaKeywordType("contains", ContainsKeyword::new).createKeyword(
            new DefaultJsonSchemaFactory().create(json)
        );
    }
}
