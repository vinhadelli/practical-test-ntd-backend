package com.ntd.practical_test_ntd_backend.configuration.persistence;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

// Creating a custom naming scheme for hibernate to set all table and column names to uppercase
public class CamelCaseToUppercaseTablesNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {
    // Set name to uppercase
    private Identifier adjustName(final Identifier name) {
        if (name == null) {
            return null;
        }
        final String adjustedName = name.getText().toUpperCase();
        return new Identifier(adjustedName, true);
    }

    // Override table name
    @Override
    public Identifier toPhysicalTableName(final Identifier name, final JdbcEnvironment context) {
        return adjustName(super.toPhysicalTableName(name, context));
    }

    // Override column name
    @Override
    public Identifier toPhysicalColumnName(final Identifier name, final JdbcEnvironment context) {
        return adjustName(super.toPhysicalColumnName(name, context));
    }
}
