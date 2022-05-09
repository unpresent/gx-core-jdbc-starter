package ru.gx.core.jdbc.sqlwrapping;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ConnectionWrapper;
import ru.gx.core.data.sqlwrapping.ThreadConnectionsWrapper;

import javax.sql.DataSource;
import java.io.IOException;
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
            result.incRefs(); // incRefs() увеличивает "количество использований"
        } else {
            result = new JdbcConnectionWrapper(this.dataSource.getConnection());
            this.internalPut(Thread.currentThread(), result);
        }
        return result;
    }

    @Override
    public void putCurrentThreadConnection(@NotNull ConnectionWrapper connectionWrapper) {
        internalPut(Thread.currentThread(), (JdbcConnectionWrapper)connectionWrapper);
    }

    @Override
    public void clearCurrentThreadConnection() {
        internalRemove(Thread.currentThread());
    }

    @Nullable
    protected synchronized JdbcConnectionWrapper internalGet(@NotNull final Thread thread) {
        return this.connections.get(thread);
    }

    protected synchronized void internalPut(
            @NotNull final Thread thread,
            @NotNull final JdbcConnectionWrapper connectionWrapper
    ) {
        final var oldWrapper = this.connections.get(thread);
        if (oldWrapper != null && !oldWrapper.equals(connectionWrapper)) {
            oldWrapper.close();
        }
        this.connections.put(thread, connectionWrapper);
    }

    protected synchronized void internalRemove(@NotNull final Thread thread) {
        final var oldWrapper = this.connections.get(thread);
        if (oldWrapper != null) {
            oldWrapper.close();
        }
        this.connections.remove(thread);
    }
}
