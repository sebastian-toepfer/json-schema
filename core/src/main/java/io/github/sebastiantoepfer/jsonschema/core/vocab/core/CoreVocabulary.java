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

import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * see: https://json-schema.org/draft/2020-12/json-schema-core.html#name-the-json-schema-core-vocabu
 */
public final class CoreVocabulary implements Vocabulary {

    private Collection<KeywordType> supportedKeywords = List.of(
        new SchemaKeywordType(),
        new VocabularyKeywordType(),
        new IdKeywordType(),
        new RefKeywordType(),
        new DynamicRefKeywordType(),
        new DefsKeywordType(),
        new CommentKeywordType()
    );

    @Override
    public URI id() {
        //we need a way to find out which draft we are using!
        return URI.create("https://json-schema.org/draft/2020-12/vocab/core");
    }

    @Override
    public Optional<KeywordType> findKeywordTypeByName(final String name) {
        return supportedKeywords.stream().filter(keywordType -> keywordType.hasName(name)).findFirst();
    }
}
