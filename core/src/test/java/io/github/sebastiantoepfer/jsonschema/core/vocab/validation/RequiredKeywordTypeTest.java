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
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class RequiredKeywordTypeTest {

    @Test
    void should_know_his_name() {
        final Keyword required = new RequiredKeywordType()
            .createKeyword(
                new DefaultJsonSchemaFactory()
                    .create(Json.createObjectBuilder().add("required", JsonValue.EMPTY_JSON_ARRAY).build())
            );

        assertThat(required.hasName("required"), is(true));
        assertThat(required.hasName("test"), is(false));
    }

    @Test
    void should_invalid_if_not_all_properties_in_the_instance() {
        assertThat(
            new RequiredKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("required", Json.createArrayBuilder().add("foo").add("bar"))
                                .build()
                        )
                )
                .asAssertion()
                .isValidFor(Json.createObjectBuilder().add("foo", BigDecimal.ONE).build()),
            is(false)
        );
    }

    @Test
    void should_valid_for_non_objects() {
        assertThat(
            new RequiredKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("required", Json.createArrayBuilder().add("foo").add("bar"))
                                .build()
                        )
                )
                .asAssertion()
                .isValidFor(JsonValue.EMPTY_JSON_ARRAY),
            is(true)
        );
    }

    @Test
    void should_valid_if_all_properties_are_in_the_instance() {
        assertThat(
            new RequiredKeywordType()
                .createKeyword(
                    new DefaultJsonSchemaFactory()
                        .create(
                            Json
                                .createObjectBuilder()
                                .add("required", Json.createArrayBuilder().add("foo").add("bar"))
                                .build()
                        )
                )
                .asAssertion()
                .isValidFor(Json.createObjectBuilder().add("foo", BigDecimal.ONE).add("bar", "test").build()),
            is(true)
        );
    }
}
