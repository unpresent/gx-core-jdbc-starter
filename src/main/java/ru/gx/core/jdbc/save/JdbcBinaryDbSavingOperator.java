package ru.gx.core.jdbc.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.jdbc.ActiveConnectionsContainer;
import ru.gx.core.data.save.AbstractBinaryDbSavingOperator;
import ru.gx.core.data.save.DbSavingAccumulateMode;

import java.sql.CallableStatement;
import java.sql.SQLException;

@Accessors(chain = true)
public class JdbcBinaryDbSavingOperator
        extends AbstractBinaryDbSavingOperator {

    @Getter(AccessLevel.PROTECTED)
    @NotNull
    private final ActiveConnectionsContainer activeConnectionsContainer;

    public JdbcBinaryDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final ActiveConnectionsContainer activeConnectionsContainer
    ) {
        super(objectMapper);
        this.activeConnectionsContainer = activeConnectionsContainer;
    }

    @Override
    public @NotNull CallableStatement prepareStatement(
            @NotNull final String sqlCommand,
            @NotNull final DbSavingAccumulateMode accumulateMode
    ) throws SQLException {
        var connection = getActiveConnectionsContainer().getCurrent();
        if (connection == null) {
            throw new SQLException("Connection isn't registered in ActiveConnectionsContainer");
        }

        return connection.prepareCall(sqlCommand);
    }

    @Override
    protected void executeStatement(
            @NotNull final Object statement,
            @NotNull final Object data
    ) throws SQLException {
        final var stmt = (CallableStatement)statement;
        stmt.setBytes(1, (byte[])data);
        stmt.execute();
    }
}
