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
package io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class StringElementTest {

    @Test
    void should_create_new() {
        assertThat(StringElement.of("test"), is(not(nullValue())));
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(StringElement.class).withNonnullFields("value").verify();
    }

    @Test
    void should_throw_execption_if_string_has_more_than_one_character() {
        final StringElement element = StringElement.of("invalid");
        assertThrows(UnsupportedOperationException.class, () -> element.isValidFor(0x01));
    }

    @Test
    void should_be_valid_if_codePoint_is_lowercase_representation() {
        assertThat(StringElement.of("A").isValidFor(0x61), is(true));
    }

    @Test
    void should_be_valid_if_codePoint_is_upercase_representation() {
        assertThat(StringElement.of("A").isValidFor(0x41), is(true));
    }

    @Test
    void should_be_invalid_if_codePoint_is_neither_upercase_nor_lowercase_representation() {
        assertThat(StringElement.of("A").isValidFor(0x42), is(false));
    }
}
