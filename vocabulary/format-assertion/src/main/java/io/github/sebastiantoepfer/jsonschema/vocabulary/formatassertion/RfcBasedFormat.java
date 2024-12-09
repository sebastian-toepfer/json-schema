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

import io.github.sebastiantoepfer.jsonschema.vocabulary.formatassertion.rfc.Rfc;
import java.util.Objects;

public final class RfcBasedFormat implements Format {

    private final String formatName;
    private final Rfc rfc;
    private final String ruleName;

    public RfcBasedFormat(final String formatName, final Rfc rfc, final String ruleName) {
        this.formatName = Objects.requireNonNull(formatName);
        this.rfc = Objects.requireNonNull(rfc);
        this.ruleName = Objects.requireNonNull(ruleName);
    }

    @Override
    public boolean applyTo(final String value) {
        return rfc.findRuleByName(ruleName).map(r -> r.applyTo(value)).orElse(Boolean.FALSE);
    }

    @Override
    public String name() {
        return formatName;
    }
}
