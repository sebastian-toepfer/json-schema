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

import io.github.sebastiantoepfer.jsonschema.core.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.core.keyword.DefaultAnnotation;
import io.github.sebastiantoepfer.jsonschema.core.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.core.keyword.KeywordType;
import jakarta.json.JsonValue;
import java.net.URI;
import java.util.Optional;

final class BasicVocabulary implements Vocabulary {

    @Override
    public URI id() {
        return URI.create("http://https://github.com/sebastian-toepfer/json-schema/basic");
    }

    @Override
    public Optional<KeywordType> findKeywordTypeByName(final String name) {
        KeywordType keywordType =
            switch (name) {
                case "type" -> new KeywordType() {
                    @Override
                    public String name() {
                        return "type";
                    }

                    @Override
                    public Keyword createKeyword(final JsonValue value) {
                        return new Type(value);
                    }
                };
                default -> new KeywordType() {
                    @Override
                    public String name() {
                        return name;
                    }

                    @Override
                    public Keyword createKeyword(final JsonValue value) {
                        return new DefaultAnnotation(name(), value);
                    }
                };
            };
        return Optional.of(keywordType);
    }
}
