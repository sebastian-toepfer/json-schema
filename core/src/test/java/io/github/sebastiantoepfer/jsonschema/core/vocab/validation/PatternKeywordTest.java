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

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class PatternKeywordTest {

    @Test
    void should_be_know_his_name() {
        final Keyword pattern = new PatternKeyword("a");

        assertThat(pattern.hasName("pattern"), is(true));
        assertThat(pattern.hasName("test"), is(false));
    }

    @Test
    void should_be_valid_for_non_string_value() {
        assertThat(new PatternKeyword("a").asAssertion().isValidFor(JsonValue.TRUE), is(true));
    }

    @Test
    void should_be_invalid_for_non_matching_value() {
        assertThat(
            new PatternKeyword("^(\\([0-9]{3}\\))?[0-9]{3}-[0-9]{4}$")
                .asAssertion()
                .isValidFor(Json.createValue("(888)555-1212 ext. 532")),
            is(false)
        );
    }

    @Test
    void should_be_valid_matching_value() {
        assertThat(
            new PatternKeyword("^(\\([0-9]{3}\\))?[0-9]{3}-[0-9]{4}$")
                .asAssertion()
                .isValidFor(Json.createValue("(888)555-1212")),
            is(true)
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            new PatternKeyword("^(\\([0-9]{3}\\))?[0-9]{3}-[0-9]{4}$").printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("pattern"), is("^(\\([0-9]{3}\\))?[0-9]{3}-[0-9]{4}$"))
        );
    }
}
