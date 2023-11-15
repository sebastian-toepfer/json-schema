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
package io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf;

import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.Alternative;
import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.Element;
import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.NumericCharacter;
import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.RuleName;
import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.StringElement;
import io.github.sebastiantoepfer.jsonschema.vocabulary.format.assertion.abnf.element.ValueRangeAlternatives;

public enum CoreRules implements Element {
    ALPHA() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return Alternative
                .of(
                    ValueRangeAlternatives.of(
                        NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x41),
                        NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x5A)
                    ),
                    ValueRangeAlternatives.of(
                        NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x61),
                        NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x7A)
                    )
                )
                .isValidFor(codePoint);
        }
    },
    BIT() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return Alternative.of(StringElement.of("0"), StringElement.of("1")).isValidFor(codePoint);
        }
    },
    CHAR() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return ValueRangeAlternatives
                .of(
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x01),
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x7F)
                )
                .isValidFor(codePoint);
        }
    },
    CR() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x0D).isValidFor(codePoint);
        }
    },
    CRLF, //CR LF
    CTL, //%x00-1F / %x7F
    DIGIT() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return ValueRangeAlternatives
                .of(
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x30),
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x39)
                )
                .isValidFor(codePoint);
        }
    },
    DQUOTE() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x22).isValidFor(codePoint);
        }
    },
    HEXDIG() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return Alternative
                .of(
                    DIGIT,
                    StringElement.of("A"),
                    StringElement.of("B"),
                    StringElement.of("C"),
                    StringElement.of("D"),
                    StringElement.of("E"),
                    StringElement.of("F")
                )
                .isValidFor(codePoint);
        }
    },
    HTAB() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x09).isValidFor(codePoint);
        }
    },
    LF() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x0A).isValidFor(codePoint);
        }
    },
    LWSP, //*(WSP / CRLF WSP)
    OCTET() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return ValueRangeAlternatives
                .of(
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x00),
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0xFF)
                )
                .isValidFor(codePoint);
        }
    },
    SP() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x20).isValidFor(codePoint);
        }
    },
    VCHAR() {
        @Override
        public boolean isValidFor(final int codePoint) {
            return ValueRangeAlternatives
                .of(
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x21),
                    NumericCharacter.of(NumericCharacter.BASE.HEXADECIMAL, 0x7E)
                )
                .isValidFor(codePoint);
        }
    },
    WSP {
        @Override
        public boolean isValidFor(final int codePoint) {
            return Alternative.of(SP, HTAB).isValidFor(codePoint);
        }
    };

    public RuleName asRuleName() {
        return RuleName.of(name());
    }

    @Override
    public boolean isValidFor(final int codePoint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
