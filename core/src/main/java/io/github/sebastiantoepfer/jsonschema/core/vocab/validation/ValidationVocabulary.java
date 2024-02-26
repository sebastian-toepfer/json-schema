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
package io.github.sebastiantoepfer.jsonschema.core.vocab.validation;

import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.ArrayKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.BooleanKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.IntegerKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.NumberKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.StringArrayKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.StringKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.DefaultVocabulary;
import jakarta.json.spi.JsonProvider;
import java.net.URI;
import java.util.Optional;

public final class ValidationVocabulary implements Vocabulary {

    private final Vocabulary vocab;

    public ValidationVocabulary(final JsonProvider jsonContext) {
        this.vocab = new DefaultVocabulary(
            URI.create("https://json-schema.org/draft/2020-12/vocab/validation"),
            new TypeKeywordType(),
            new ArrayKeywordType(EnumKeyword.NAME, EnumKeyword::new),
            new StringKeywordType(jsonContext, PatternKeyword.NAME, PatternKeyword::new),
            new IntegerKeywordType(jsonContext, MinLengthKeyword.NAME, MinLengthKeyword::new),
            new IntegerKeywordType(jsonContext, MaxLengthKeyword.NAME, MaxLengthKeyword::new),
            new NumberKeywordType(jsonContext, ExclusiveMaximumKeyword.NAME, ExclusiveMaximumKeyword::new),
            new NumberKeywordType(jsonContext, MultipleOfKeyword.NAME, MultipleOfKeyword::new),
            new NumberKeywordType(jsonContext, ExclusiveMinimumKeyword.NAME, ExclusiveMinimumKeyword::new),
            new NumberKeywordType(jsonContext, MaximumKeyword.NAME, MaximumKeyword::new),
            new NumberKeywordType(jsonContext, MinimumKeyword.NAME, MinimumKeyword::new),
            new StringArrayKeywordType(jsonContext, RequiredKeyword.NAME, RequiredKeyword::new),
            new IntegerKeywordType(jsonContext, MaxItemsKeyword.NAME, MaxItemsKeyword::new),
            new IntegerKeywordType(jsonContext, MinItemsKeyword.NAME, MinItemsKeyword::new),
            new BooleanKeywordType(jsonContext, UniqueItemsKeyword.NAME, UniqueItemsKeyword::new)
        );
    }

    @Override
    public URI id() {
        return vocab.id();
    }

    @Override
    public Optional<KeywordType> findKeywordTypeByName(final String name) {
        return vocab.findKeywordTypeByName(name);
    }
}
