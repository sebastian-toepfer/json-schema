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
package io.github.sebastiantoepfer.jsonschema.core.impl.keyword;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.github.npathai.hamcrestopt.OptionalMatchers;
import io.github.sebastiantoepfer.jsonschema.core.keyword.KeywordType;
import java.net.URI;
import org.junit.jupiter.api.Test;

class BasicVocabularyTest {

    @Test
    void should_return_a_fake_id() {
        assertThat(
            new BasicVocabulary().id(),
            is(URI.create("http://https://github.com/sebastian-toepfer/json-schema/basic"))
        );
    }

    @Test
    void should_return_the_type_keywordtype() {
        assertThat(
            new BasicVocabulary().findKeywordTypeByName("type").map(KeywordType::name),
            OptionalMatchers.isPresentAndIs("type")
        );
    }

    @Test
    void should_return_the_custom_annotation_for_unknow_keyword() {
        assertThat(
            new BasicVocabulary().findKeywordTypeByName("unknow").map(KeywordType::name),
            OptionalMatchers.isPresentAndIs("unknow")
        );
    }
}
