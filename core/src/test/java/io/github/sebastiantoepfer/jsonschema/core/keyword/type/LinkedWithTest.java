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
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import io.github.sebastiantoepfer.jsonschema.JsonSchemas;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.util.EnumSet;
import org.junit.jupiter.api.Test;

class LinkedWithTest {

    @Test
    void should_replace_keyword_if_previous_chain_link_not_available() {
        assertThat(
            new LinkedWith(
                "if",
                s -> new MockKeyword("if", EnumSet.of(Keyword.KeywordCategory.APPLICATOR)),
                LINKTYPE.VALID
            )
                .findAffectedByKeywordIn(JsonSchemas.load(JsonValue.TRUE))
                .apply(new MockKeyword("MOCK"))
                .categories(),
            not(hasItems(Keyword.KeywordCategory.ASSERTION, Keyword.KeywordCategory.APPLICATOR))
        );
    }

    @Test
    void should_chain_keyword_if_previous_chain_link_is_available() {
        assertThat(
            new LinkedWith(
                "if",
                s -> new MockKeyword("if", EnumSet.of(Keyword.KeywordCategory.APPLICATOR)),
                LINKTYPE.VALID
            )
                .findAffectedByKeywordIn(JsonSchemas.load(Json.createObjectBuilder().add("if", JsonValue.TRUE).build()))
                .apply(new ReplacingKeyword(new MockKeyword("MOCK"), EnumSet.of(Keyword.KeywordCategory.ASSERTION)))
                .categories(),
            hasItems(Keyword.KeywordCategory.APPLICATOR, Keyword.KeywordCategory.ANNOTATION)
        );
    }
}
