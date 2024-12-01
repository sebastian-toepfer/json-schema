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
package io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.util.List;
import org.junit.jupiter.api.Test;

class FormatAssertionKeywordTypeTest {

    @Test
    void should_know_his_name() {
        final Keyword keyword = new FormatAssertionKeywordType(List.of(new TestFormat()))
            .createKeyword(JsonSchemas.load(Json.createObjectBuilder().add("format", "date").build()));

        assertThat(keyword.hasName("format"), is(true));
        assertThat(keyword.hasName("test"), is(false));
    }

    @Test
    void should_not_be_createable_with_non_string() {
        final JsonSchema schema = JsonSchemas.load(JsonValue.TRUE);
        final KeywordType keywordType = new FormatAssertionKeywordType(List.of(new TestFormat()));
        assertThrows(IllegalArgumentException.class, () -> keywordType.createKeyword(schema));
    }

    @Test
    void should_not_be_createable_with_unknown_formatname() {
        final JsonSchema schema = JsonSchemas.load(Json.createObjectBuilder().add("format", "test").build());
        final KeywordType keywordType = new FormatAssertionKeywordType(List.of(new TestFormat()));
        assertThrows(IllegalArgumentException.class, () -> keywordType.createKeyword(schema));
    }

    @Test
    void should_be_assertion_and_annotation() {
        assertThat(
            new FormatAssertionKeywordType(List.of(new TestFormat()))
                .createKeyword(JsonSchemas.load(Json.createObjectBuilder().add("format", "date").build()))
                .categories(),
            containsInAnyOrder(Keyword.KeywordCategory.ASSERTION, Keyword.KeywordCategory.ANNOTATION)
        );
    }

    @Test
    void should_return_formatname_as_annotation() {
        assertThat(
            new FormatAssertionKeywordType(List.of(new TestFormat()))
                .createKeyword(JsonSchemas.load(Json.createObjectBuilder().add("format", "date").build()))
                .asAnnotation()
                .valueFor(JsonValue.EMPTY_JSON_OBJECT),
            is(Json.createValue("date"))
        );
    }

    private static final class TestFormat implements FormatAssertion {

        private final String name;

        public TestFormat() {
            this("date");
        }

        public TestFormat(final String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public boolean isValidFor(final String value) {
            return true;
        }
    }
}
