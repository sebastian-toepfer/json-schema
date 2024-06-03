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
package io.github.sebastiantoepfer.jsonschema.core.vocab.applicator;

import io.github.sebastiantoepfer.jsonschema.Vocabulary;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.AffectedByKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.NamedJsonSchemaKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.SchemaArrayKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.SubSchemaKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.DefaultVocabulary;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * <b>Applicator</b>
 * Dialect: 2020-12
 * uri: https://json-schema.org/draft/2020-12/vocab/applicator
 * source: https://www.learnjsonschema.com/2020-12/applicator/
 * spec: https://json-schema.org/draft/2020-12/json-schema-core.html#section-10
 */
public final class ApplicatorVocabulary implements Vocabulary {

    private final Vocabulary vocab;

    public ApplicatorVocabulary() {
        this.vocab = new DefaultVocabulary(
            URI.create("https://json-schema.org/draft/2020-12/vocab/applicator"),
            new SchemaArrayKeywordType(AllOfKeyword.NAME, AllOfKeyword::new),
            new NamedJsonSchemaKeywordType(PropertiesKeyword.NAME, PropertiesKeyword::new),
            new SubSchemaKeywordType(AdditionalPropertiesKeyword.NAME, AdditionalPropertiesKeyword::new),
            new NamedJsonSchemaKeywordType(PatternPropertiesKeyword.NAME, PatternPropertiesKeyword::new),
            new SubSchemaKeywordType(ItemsKeyword.NAME, ItemsKeyword::new),
            new SchemaArrayKeywordType(PrefixItemsKeyword.NAME, PrefixItemsKeyword::new),
            //normally affeced by minContains and maxContains, but only min has a direct effect!
            new AffectedByKeywordType(
                ContainsKeyword.NAME,
                List.of("minContains"),
                (a, schema) ->
                    new SubSchemaKeywordType(ContainsKeyword.NAME, s -> new ContainsKeyword(a, s)).createKeyword(schema)
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
