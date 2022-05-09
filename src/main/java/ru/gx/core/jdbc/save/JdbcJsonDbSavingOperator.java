package ru.gx.core.jdbc.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractJsonDbSavingOperator;
import ru.gx.core.data.save.DbSavingAccumulateMode;
import ru.gx.core.jdbc.sqlwrapping.JdbcThreadConnectionsWrapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Accessors(chain = true)
public class JdbcJsonDbSavingOperator
        extends AbstractJsonDbSavingOperator {

    @Getter
    @NotNull
    private final JdbcThreadConnectionsWrapper threadConnectionsWrapper;

    public JdbcJsonDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final JdbcThreadConnectionsWrapper threadConnectionWrapper
    ) {
        super(objectMapper);
        this.threadConnectionsWrapper = threadConnectionWrapper;
    }

    @Override
    public @NotNull CallableStatement prepareStatement(
            @NotNull final String sqlCommand,
            @NotNull final DbSavingAccumulateMode accumulateMode
    ) throws SQLException {
        var connection = getThreadConnectionsWrapper().getCurrentThreadConnection();
        return ((Connection)connection.getInternalConnection()).prepareCall(sqlCommand);
    }

    @Override
    protected void executeStatement(
            @NotNull final Object statement,
            @NotNull final Object data
    ) throws SQLException {
        final var stmt = (CallableStatement)statement;
        stmt.setString(1, (String)data);
        stmt.execute();
    }
}
