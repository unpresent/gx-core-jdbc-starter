package ru.gx.core.jdbc.sqlwrapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ConnectionWrapper;
import ru.gx.core.data.sqlwrapping.ThreadConnectionsWrapper;

import javax.sql.DataSource;
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
    private final Map<Thread, JdbcConnectionWrapper> connections = new HashMap<>();

    public JdbcThreadConnectionsWrapper(@NotNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    @NotNull
    public ConnectionWrapper getCurrentThreadConnection() throws SQLException {
        var result = internalGet(Thread.currentThread());
        if (result != null) {
            if (!result.getConnection().isClosed()) {
                result.incRefs();
                return result;
            } else {
                internalRemove(Thread.currentThread(), result);
            }
        }
        result = new JdbcConnectionWrapper(this, this.dataSource.getConnection());
        internalPut(Thread.currentThread(), result);
        return result;
    }

    @Nullable
    protected synchronized JdbcConnectionWrapper internalGet(
            @NotNull final Thread thread
    ) {
        return this.connections.get(thread);
    }

    protected synchronized void internalPut(
            @NotNull final Thread thread,
            @NotNull final JdbcConnectionWrapper connectionWrapper
    ) {
        this.connections.put(thread, connectionWrapper);
    }

    protected synchronized void internalRemove(
            @NotNull final Thread thread,
            @NotNull final JdbcConnectionWrapper connectionWrapper
    ) {
        if (internalGet(thread) == connectionWrapper) {
            this.connections.remove(thread);
        }
    }
}
