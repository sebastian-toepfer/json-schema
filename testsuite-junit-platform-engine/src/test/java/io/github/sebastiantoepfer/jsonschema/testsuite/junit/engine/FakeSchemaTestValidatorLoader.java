package io.github.sebastiantoepfer.jsonschema.testsuite.junit.engine;

import io.github.sebastiantoepfer.jsonschema.testsuite.junit.SchemaTestValidatorLoader;

public final class FakeSchemaTestValidatorLoader implements SchemaTestValidatorLoader {

    @Override
    public SchemaTestValidator loadSchemaTestValidator(final String schema) {
        return data -> true;
    }
}
