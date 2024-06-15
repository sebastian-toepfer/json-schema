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

class NewLineDetector implements ElementEndDetector {

    private final boolean cr;
    private final boolean lf;
    private final int lastCodePoint;

    public NewLineDetector() {
        this(false, false, 0);
    }

    private NewLineDetector(final boolean cr, final boolean lf, final int lastCodePoint) {
        this.cr = cr;
        this.lf = lf;
        this.lastCodePoint = lastCodePoint;
    }

    @Override
    public NewLineDetector append(final int codePoint) {
        final NewLineDetector result;
        if (lf) {
            result =
                new NewLineDetector(
                    cr && !Character.isWhitespace(codePoint),
                    !Character.isWhitespace(codePoint),
                    codePoint
                );
        } else if (cr) {
            result = new NewLineDetector(codePoint == '\n', codePoint == '\n', codePoint);
        } else {
            result = new NewLineDetector(codePoint == '\r', false, codePoint);
        }
        return result;
    }

    @Override
    public boolean isEndReached() {
        return cr && lf && !Character.isWhitespace(lastCodePoint);
    }

    @Override
    public Extractor applyTo(final Extractor imDone) {
        return imDone.append('\r').append('\n').append(lastCodePoint);
    }
}
