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
package io.github.sebastiantoepfer.jsonschema.core.keyword.type;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import java.util.Collection;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class BooleanKeywordTypeTest {

    @ParameterizedTest
    @MethodSource("invalidJsonValues")
    void should_not_create_keyword_from_non_integer(final JsonValue notValid) {
        final BooleanKeywordType type = new BooleanKeywordType(
            JsonProvider.provider(),
            "test",
            TestBooleanKeyword::new
        );
        final JsonSchema schema = JsonSchemas.load(Json.createObjectBuilder().add("test", notValid).build());
        assertThrows(IllegalArgumentException.class, () -> type.createKeyword(schema));
    }

    static Stream<JsonValue> invalidJsonValues() {
        return Stream.of(
            JsonValue.EMPTY_JSON_ARRAY,
            JsonValue.EMPTY_JSON_OBJECT,
            JsonValue.NULL,
            Json.createValue(12.1),
            Json.createValue("test")
        );
    }

    @Test
    void should_create_keyword_from_strings() {
        assertThat(
            new BooleanKeywordType(JsonProvider.provider(), "test", TestBooleanKeyword::new).createKeyword(
                JsonSchemas.load(Json.createObjectBuilder().add("test", true).build())
            ),
            isA(TestBooleanKeyword.class)
        );
    }

    private static class TestBooleanKeyword implements Keyword {

        public TestBooleanKeyword(final Boolean value) {}

        @Override
        public Collection<Keyword.KeywordCategory> categories() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean hasName(String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T extends Media<T>> T printOn(T media) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
