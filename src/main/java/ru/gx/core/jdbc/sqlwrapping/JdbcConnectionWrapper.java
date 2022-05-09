package ru.gx.core.jdbc.sqlwrapping;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.sqlwrapping.ConnectionWrapper;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcConnectionWrapper implements ConnectionWrapper {
    @Getter(AccessLevel.PROTECTED)
    @NotNull
    private final Connection connection;

    /**
     * Определяет количество использований данного объекта.
     * Когда оно становится равно 0, то вызываем close() у внутреннего connection.
     */
    private int refsCount = 1;

    public JdbcConnectionWrapper(@NotNull final Connection connection) {
        this.connection = connection;
    }

    @Override
    public @NotNull Object getInternalConnection() {
        return getConnection();
    }

    @Override
    public @NotNull SqlCommandWrapper getQuery(@NotNull final String sql) throws SQLException {
        return new JdbcQueryWrapper(getConnection().prepareStatement(sql));
    }

    @Override
    public @NotNull SqlCommandWrapper getCallable(@NotNull String sql) throws SQLException {
        return new JdbcCallableWrapper(getConnection().prepareCall(sql));
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
