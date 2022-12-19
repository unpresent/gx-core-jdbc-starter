package ru.gx.core.jdbc.sqlwrapping;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ConnectionWrapper;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;

import java.sql.Connection;
import java.sql.SQLException;

import static lombok.AccessLevel.PROTECTED;

public class JdbcConnectionWrapper implements ConnectionWrapper {

    @Getter
    @NotNull
    private final JdbcThreadConnectionsWrapper owner;

    @Getter(PROTECTED)
    @NotNull
    private final Connection connection;

    /**
     * Определяет количество использований данного объекта.
     * Когда оно становится равно 0, то вызываем close() у внутреннего connection.
     */
    private int refsCount = 1;

    public JdbcConnectionWrapper(
            @NotNull final JdbcThreadConnectionsWrapper owner,
            @NotNull final Connection connection
    ) {
        this.owner = owner;
        this.connection = connection;
    }

    @Override
    public @NotNull Object getInternalConnection() {
        return getConnection();
    }

    @Override
    public @NotNull SqlCommandWrapper getQuery(@NotNull final String sql) throws SQLException {
        return new JdbcQueryWrapper(getConnection().prepareStatement(sql), this);
    }

    @Override
    public @NotNull SqlCommandWrapper getCallable(@NotNull String sql) throws SQLException {
        return new JdbcCallableWrapper(getConnection().prepareCall(sql), this);
    }

    @Override
    public void openTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    @Override
    public void commitTransaction() throws SQLException {
        getConnection().commit();
    }

    @Override
    public void rollbackTransaction() throws SQLException {
        getConnection().rollback();
    }

    @Override
    public boolean isEqual(@Nullable ConnectionWrapper connectionWrapper) {
        if (connectionWrapper == null) {
            return false;
        }
        return getInternalConnection().equals(connectionWrapper.getInternalConnection());
    }

    public void incRefs() {
        this.refsCount++;
    }

    @SneakyThrows(SQLException.class)
    @Override
    public void close() {
        this.refsCount--;
        if (this.refsCount <= 0) {
            getConnection().close();
        }
    }
}
