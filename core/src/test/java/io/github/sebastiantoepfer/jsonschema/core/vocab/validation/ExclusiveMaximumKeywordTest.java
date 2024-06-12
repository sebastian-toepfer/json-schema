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
import java.math.BigDecimal;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class ExclusiveMaximumKeywordTest {

    @Test
    void should_know_his_name() {
        final Keyword maximum = new ExclusiveMaximumKeyword(BigDecimal.valueOf(1));

        assertThat(maximum.hasName("exclusiveMaximum"), is(true));
        assertThat(maximum.hasName("test"), is(false));
    }

    @Test
    void should_be_valid_for_non_number_values() {
        assertThat(
            new ExclusiveMaximumKeyword(BigDecimal.valueOf(1)).asAssertion().isValidFor(JsonValue.EMPTY_JSON_OBJECT),
            is(true)
        );
    }

    @Test
    void should_be_invalid_for_greater_numbers() {
        assertThat(
            new ExclusiveMaximumKeyword(BigDecimal.valueOf(10)).asAssertion().isValidFor(Json.createValue(11)),
            is(false)
        );
    }

    @Test
    void should_be_invalid_for_equals_numbers() {
        assertThat(
            new ExclusiveMaximumKeyword(BigDecimal.valueOf(10)).asAssertion().isValidFor(Json.createValue(10)),
            is(false)
        );
    }

    @Test
    void shhould_be_valid_for_smaller_numbers() {
        assertThat(
            new ExclusiveMaximumKeyword(BigDecimal.valueOf(10)).asAssertion().isValidFor(Json.createValue(9)),
            is(true)
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            new ExclusiveMaximumKeyword(BigDecimal.valueOf(10)).printOn(new HashMapMedia()),
            (Matcher) hasEntry(is("exclusiveMaximum"), is(BigDecimal.valueOf(10)))
        );
    }
}
