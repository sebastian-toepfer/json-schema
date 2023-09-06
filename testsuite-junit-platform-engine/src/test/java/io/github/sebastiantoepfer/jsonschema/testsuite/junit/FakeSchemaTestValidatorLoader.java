package io.github.sebastiantoepfer.jsonschema.testsuite.junit;

public final class FakeSchemaTestValidatorLoader implements SchemaTestValidatorLoader {

    @Override
    public SchemaTestValidator loadSchemaTestValidator(final String schema) {
        return data -> true;
    }
}
