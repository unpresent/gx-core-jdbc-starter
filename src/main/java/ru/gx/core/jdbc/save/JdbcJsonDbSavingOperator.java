package ru.gx.core.jdbc.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractJsonDbSavingOperator;
import ru.gx.core.data.save.DbSavingAccumulateMode;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;
import ru.gx.core.jdbc.sqlwrapping.JdbcThreadConnectionsWrapper;

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
    public @NotNull SqlCommandWrapper prepareStatement(
            @NotNull final String sqlCommand,
            @NotNull final DbSavingAccumulateMode accumulateMode
    ) throws SQLException {
        var connection = getThreadConnectionsWrapper().getCurrentThreadConnection();
        return connection.getCallable(sqlCommand);
    }

    @Override
    protected void executeStatement(
            @NotNull final SqlCommandWrapper statement,
            @NotNull final Object data
    ) throws SQLException {
        statement.setStringParam(1, (String) data);
        statement.executeNoResult();
    }
}
