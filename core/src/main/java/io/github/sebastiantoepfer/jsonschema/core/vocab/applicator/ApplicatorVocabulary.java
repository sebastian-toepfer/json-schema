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
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.Affects;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.AffectsKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.ExtendedBy;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.LINKTYPE;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.LinkedWith;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.NamedJsonSchemaKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.ReplacedBy;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.ReplacingKeyword;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.SchemaArrayKeywordType;
import io.github.sebastiantoepfer.jsonschema.core.keyword.type.SubSchemaKeywordType;
import io.github.sebastiantoepfer.jsonschema.keyword.Keyword;
import io.github.sebastiantoepfer.jsonschema.keyword.KeywordType;
import io.github.sebastiantoepfer.jsonschema.vocabulary.spi.ListVocabulary;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import java.net.URI;
import java.util.EnumSet;
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

    public ApplicatorVocabulary(final JsonProvider provider) {
        this.vocab = new ListVocabulary(
            URI.create("https://json-schema.org/draft/2020-12/vocab/applicator"),
            new SchemaArrayKeywordType(AllOfKeyword.NAME, AllOfKeyword::new),
            new SchemaArrayKeywordType(AnyOfKeyword.NAME, AnyOfKeyword::new),
            new SchemaArrayKeywordType(OneOfKeyword.NAME, OneOfKeyword::new),
            new AffectedByKeywordType(
                ThenKeyword.NAME,
                List.of(new LinkedWith(IfKeyword.NAME, IfKeyword::new, LINKTYPE.VALID)),
                new SubSchemaKeywordType(ThenKeyword.NAME, ThenKeyword::new)::createKeyword
            ),
            //if keyword as no meanings without then or else -> needs a better affects keywordtype
            new SubSchemaKeywordType(IfKeyword.NAME, schema ->
                new ReplacingKeyword(new IfKeyword(schema), EnumSet.allOf(Keyword.KeywordCategory.class))
            ),
            new AffectedByKeywordType(
                ElseKeyword.NAME,
                List.of(new LinkedWith(IfKeyword.NAME, IfKeyword::new, LINKTYPE.INVALID)),
                new SubSchemaKeywordType(ElseKeyword.NAME, ElseKeyword::new)::createKeyword
            ),
            new SubSchemaKeywordType(NotKeyword.NAME, NotKeyword::new),
            new NamedJsonSchemaKeywordType(PropertiesKeyword.NAME, PropertiesKeyword::new),
            //nomally affectedBy ... but we had the needed function only in affects :(
            new AffectsKeywordType(
                AdditionalPropertiesKeyword.NAME,
                List.of(
                    new Affects("properties", JsonValue.EMPTY_JSON_ARRAY),
                    new Affects("patternProperties", JsonValue.EMPTY_JSON_ARRAY)
                ),
                (affects, schema) ->
                    new SubSchemaKeywordType(AdditionalPropertiesKeyword.NAME, s ->
                        new AdditionalPropertiesKeyword(affects, s)
                    ).createKeyword(schema)
            ),
            new NamedJsonSchemaKeywordType(PatternPropertiesKeyword.NAME, PatternPropertiesKeyword::new),
            new NamedJsonSchemaKeywordType(DependentSchemasKeyword.NAME, DependentSchemasKeyword::new),
            new SubSchemaKeywordType(PropertyNames.NAME, s -> new PropertyNames(provider, s)),
            //this example shows my missunderstanding from affects, affectedBy and keywordtypes :(
            new AffectedByKeywordType(
                ItemsKeyword.NAME,
                List.of(new ExtendedBy("minItems"), new ExtendedBy("maxItems")),
                //nomally affectedBy too ... but we had the needed function only in affects :(
                schema ->
                    new AffectsKeywordType(
                        ItemsKeyword.NAME,
                        List.of(new Affects("prefixItems", Json.createValue(-1))),
                        (affects, subSchema) ->
                            new SubSchemaKeywordType(ItemsKeyword.NAME, s -> new ItemsKeyword(affects, s)
                            ).createKeyword(subSchema)
                    ).createKeyword(schema)
            ),
            new SchemaArrayKeywordType(PrefixItemsKeyword.NAME, PrefixItemsKeyword::new),
            new AffectedByKeywordType(
                ContainsKeyword.NAME,
                List.of(new ReplacedBy("minContains"), new ExtendedBy("maxContains")),
                new SubSchemaKeywordType(ContainsKeyword.NAME, ContainsKeyword::new)::createKeyword
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
