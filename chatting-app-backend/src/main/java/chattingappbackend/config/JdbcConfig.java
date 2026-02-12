package chattingappbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.core.dialect.JdbcDialect;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.jspecify.annotations.NullMarked;

@Configuration
@NullMarked
public class JdbcConfig extends AbstractJdbcConfiguration {

    @Override
    public JdbcDialect jdbcDialect(NamedParameterJdbcOperations operations) {
        return super.jdbcDialect(operations);
    }
}