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
package io.github.sebastiantoepfer.jsonschema.core.impl.vocab.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.github.npathai.hamcrestopt.OptionalMatchers;
import jakarta.json.JsonValue;
import java.net.URI;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CoreVocabularyTest {

    void should_return_the_uri_of_the_current_core_vocabulary() {
        assertThat(new CoreVocabulary().id(), is(URI.create("https://json-schema.org/draft/2020-12/vocab/core")));
    }

    @ParameterizedTest(name = "should know keyword {0}")
    @ValueSource(strings = { "$schema", "$vocabulary", "$id", "$ref", "$dynamicRef", "$defs", "$comment" })
    void should_return_keywords_for_name(final String name) {
        //keyword creation for easier pitesting :) -> i know it is bad
        assertThat(
            new CoreVocabulary()
                .findKeywordTypeByName(name)
                .map(keywordType -> keywordType.createKeyword(JsonValue.EMPTY_JSON_OBJECT))
                .map(keyword -> keyword.hasName(name)),
            OptionalMatchers.isPresentAndIs(true)
        );
    }
}
