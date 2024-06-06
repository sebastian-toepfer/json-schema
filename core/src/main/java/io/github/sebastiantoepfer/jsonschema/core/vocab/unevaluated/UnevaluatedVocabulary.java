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
package io.github.sebastiantoepfer.jsonschema.core.vocab.unevaluated;

import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.Affects;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.AffectsKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.SubSchemaKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.DefaultVocabulary;
import jakarta.json.JsonValue;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * <b>Unevaluated</b>
 * Dialect: 2020-12
 * uri: https://json-schema.org/draft/2020-12/vocab/unevaluated
 * source: https://www.learnjsonschema.com/2020-12/unevaluated/
 * spec: https://json-schema.org/draft/2020-12/json-schema-core.html#section-11
 */
public final class UnevaluatedVocabulary implements Vocabulary {

    private final Vocabulary vocab;

    public UnevaluatedVocabulary() {
        this.vocab = new DefaultVocabulary(
            URI.create("https://json-schema.org/draft/2020-12/vocab/unevaluated"),
            List.of(
                //must be a affectedbykeyword -> but current impl. doesnt' do this in this way :(,
                //must understand how they works .. and than recreate the logic!
                new AffectsKeywordType(
                    UnevaluatedPropertiesKeyword.NAME,
                    List.of(
                        new Affects("properties", JsonValue.EMPTY_JSON_ARRAY),
                        new Affects("patternProperties ", JsonValue.EMPTY_JSON_ARRAY),
                        new Affects("additionalProperties ", JsonValue.EMPTY_JSON_ARRAY)
                    ),
                    (annotations, schema) ->
                        new SubSchemaKeywordType(
                            UnevaluatedPropertiesKeyword.NAME,
                            s -> new UnevaluatedPropertiesKeyword(annotations, s)
                        ).createKeyword(schema)
                )
            )
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
