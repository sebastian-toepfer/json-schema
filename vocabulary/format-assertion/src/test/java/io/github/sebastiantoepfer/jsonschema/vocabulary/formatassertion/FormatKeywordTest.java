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
package io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;

import io.github.sebastiantoepfer.ddd.media.core.HashMapMedia;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import jakarta.json.Json;
import org.junit.jupiter.api.Test;

class FormatKeywordTest {

    @Test
    void should_be_valid_for_any_number() {
        assertThat(new FormatKeyword(new Formats().findByName("unknow")).isValidFor(Json.createValue(42)), is(true));
    }

    @Test
    void should_be_invalid_for_any_string_with_unknow_format() {
        assertThat(new FormatKeyword(new Formats().findByName("unknow")).isValidFor(Json.createValue("42")), is(false));
    }

    @Test
    void should_be_valid_for_valid_formated_string() {
        assertThat(
            new FormatKeyword(new Formats().findByName("date-time"))
                .asAssertion()
                .isValidFor(Json.createValue("2024-11-29T00:05:00+01:00")),
            is(true)
        );
    }

    @Test
    void should_know_hisName() {
        final FormatKeyword keyword = new FormatKeyword(new Formats().findByName("unknow"));

        assertThat(keyword.hasName("test"), is(false));
        assertThat(keyword.hasName("format"), is(true));
    }

    @Test
    void should_by_an_annotation_and_assertion() {
        assertThat(
            new FormatKeyword(new Formats().findByName("unknow")).categories(),
            containsInAnyOrder(Keyword.KeywordCategory.ANNOTATION, Keyword.KeywordCategory.ASSERTION)
        );
    }

    @Test
    void should_retrun_name_of_format() {
        assertThat(
            new FormatKeyword(new Formats().findByName("date-time")).asAnnotation().valueFor(Json.createValue("")),
            is(Json.createValue("date-time"))
        );
    }

    @Test
    void should_be_printable() {
        assertThat(
            new FormatKeyword(new Formats().findByName("unknow")).printOn(new HashMapMedia()),
            hasEntry(is("format"), is("unknow"))
        );
    }
}
