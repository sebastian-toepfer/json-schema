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
package io.github.sebastiantoepfer.jsonschema.core.vocab.core;

import io.github.sebastiantoepfer.jsonschema.InstanceType;
import io.github.sebastiantoepfer.jsonschema.JsonSchema;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.VocabularyDefinitions;
import jakarta.json.JsonValue;

/**
 * see: https://json-schema.org/draft/2020-12/json-schema-core.html#name-the-vocabulary-keyword
 */
public final class VocabularyKeywordType implements KeywordType {

    @Override
    public String name() {
        return VocabularyKeyword.NAME;
    }

    @Override
    public VocabularyDefinitions createKeyword(final JsonSchema schema) {
        final JsonValue value = schema.asJsonObject().get((name()));
        final VocabularyKeyword result;
        if (InstanceType.OBJECT.isInstance(value)) {
            result = new VocabularyKeyword(value);
        } else {
            throw new IllegalArgumentException(
                "must be an object! " +
                "read https://json-schema.org/draft/2020-12/json-schema-core.html#name-the-vocabulary-keyword" +
                "for more infromations"
            );
        }
        return result;
    }
}
