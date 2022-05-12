package ru.gx.core.jdbc.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;
import ru.gx.core.jdbc.sqlwrapping.JdbcThreadConnectionsWrapper;
import ru.gx.core.data.save.AbstractBinaryDbSavingOperator;
import ru.gx.core.data.save.DbSavingAccumulateMode;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Accessors(chain = true)
public class JdbcBinaryDbSavingOperator
        extends AbstractBinaryDbSavingOperator {

    @Getter(AccessLevel.PROTECTED)
    @NotNull
    private final JdbcThreadConnectionsWrapper threadConnectionsWrapper;

    public JdbcBinaryDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final JdbcThreadConnectionsWrapper threadConnectionsWrapper
    ) {
        super(objectMapper);
        this.threadConnectionsWrapper = threadConnectionsWrapper;
    }

    @Override
    public @NotNull SqlCommandWrapper prepareStatement(
            @NotNull final String sqlCommand,
            @NotNull final DbSavingAccumulateMode accumulateMode
    ) throws SQLException {
        var connection = getThreadConnectionsWrapper().getCurrentThreadConnection();
        return connection.getCallable(sqlCommand);
    }

    @Override
    protected void executeStatement(
            @NotNull final Object statement,
            @NotNull final Object data
    ) throws SQLException {
        final var stmt = (SqlCommandWrapper)statement;
        stmt.setBinaryParam(1, (byte[])data);
        stmt.executeNoResult();
    }
}
