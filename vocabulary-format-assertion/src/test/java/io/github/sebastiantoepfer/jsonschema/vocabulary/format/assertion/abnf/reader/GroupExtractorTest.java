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

import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.Alternative;
import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.Concatenation;
import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.Element;
import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.RuleName;
import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.RuleReference;
import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.SequenceGroup;
import org.junit.jupiter.api.Test;

class GroupExtractorTest {

    @Test
    void should_create_group_with_one_element() {
        assertThat(
            GroupExtractor.of(new FakeOwner()).append('a').append(')').finish().createAs(Element.class),
            is(SequenceGroup.of(RuleReference.of(RuleName.of("a"))))
        );
    }

    @Test
    void should_create_group_with_more_than_one_element() {
        assertThat(
            GroupExtractor
                .of(new FakeOwner())
                .append('a')
                .append(' ')
                .append('b')
                .append(')')
                .finish()
                .createAs(Element.class),
            is(
                SequenceGroup.of(
                    Concatenation.of(RuleReference.of(RuleName.of("a")), RuleReference.of(RuleName.of("b")))
                )
            )
        );
    }

    @Test
    void should_create_alternative_inside() {
        assertThat(
            GroupExtractor
                .of(new FakeOwner())
                .append('a')
                .append('/')
                .append('b')
                .append(')')
                .finish()
                .createAs(Element.class),
            is(SequenceGroup.of(Alternative.of(RuleReference.of(RuleName.of("a")), RuleReference.of(RuleName.of("b")))))
        );
    }
}
