package ru.gx.core.jdbc.sqlwrapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ConnectionWrapper;
import ru.gx.core.data.sqlwrapping.ThreadConnectionsWrapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Требуется для работы с транзакциями.
 */
@SuppressWarnings("unused")
public class JdbcThreadConnectionsWrapper implements ThreadConnectionsWrapper {
    @NotNull
    private final DataSource dataSource;

    @NotNull
    private final Map<Thread, Connection> connections = new HashMap<>();

    public JdbcThreadConnectionsWrapper(@NotNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Nullable
    public synchronized Connection get(@NotNull final Thread thread) {
        return this.connections.get(thread);
    }

    @NotNull
    public Connection getCurrent() throws SQLException {
        final var result = get(Thread.currentThread());
        return (result != null) ?  result : this.dataSource.getConnection();
    }

    public synchronized void put(@NotNull final Thread thread, @NotNull final Connection connection) {
        this.connections.put(thread, connection);
    }

    public synchronized void clear(@NotNull final Thread thread) {
        this.connections.remove(thread);
    }

    public void putCurrent(@NotNull final Connection connection) {
        this.put(Thread.currentThread(), connection);
    }

    public void clearCurrent() {
        this.clear(Thread.currentThread());
    }
    @Override
    @NotNull
    public ConnectionWrapper getCurrentThreadConnection() throws SQLException {
        return new JdbcConnectionWrapper(getCurrent());
    }

    @Override
    public void putCurrentThreadConnection(@NotNull ConnectionWrapper connectionWrapper) {
        putCurrent((Connection) connectionWrapper.getInternalConnection());
    }

    @Override
    public void clearCurrentThreadConnection() {
        clearCurrent();
    }
}
