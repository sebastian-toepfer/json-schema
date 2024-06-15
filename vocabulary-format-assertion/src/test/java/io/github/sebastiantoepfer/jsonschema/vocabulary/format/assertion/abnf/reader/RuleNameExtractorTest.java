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
package io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.reader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.RuleName;
import org.junit.jupiter.api.Test;

class RuleNameExtractorTest {

    @Test
    void should_create_rule_name_from_alpha() {
        assertThat(
            RuleNameExtractor.of(new FakeOwner()).append('a').append('b').finish().createAs(RuleName.class),
            is(RuleName.of("ab"))
        );
    }

    @Test
    void should_throw_illegal_argument_if_first_codepoint_is_not_a_valid_letter() {
        final Extractor extractor = RuleNameExtractor.of(new FakeOwner());
        assertThrows(IllegalArgumentException.class, () -> extractor.append('1'));
    }

    @Test
    void should_create_rule_name_from_alpha_and_digit() {
        assertThat(
            RuleNameExtractor.of(new FakeOwner()).append('a').append('1').finish().createAs(RuleName.class),
            is(RuleName.of("a1"))
        );
    }

    @Test
    void should_create_rule_name_from_alpha_and_minus() {
        assertThat(
            RuleNameExtractor.of(new FakeOwner()).append('a').append('-').append('b').finish().createAs(RuleName.class),
            is(RuleName.of("a-b"))
        );
    }

    @Test
    void should_stop_rule_name_after_first_non_valid_char() {
        assertThat(
            RuleNameExtractor
                .of(new FakeOwner())
                .append('a')
                .append('-')
                .append('b')
                .append(' ')
                .append('c')
                .finish()
                .createAs(RuleName.class),
            is(RuleName.of("a-b"))
        );
    }
}
