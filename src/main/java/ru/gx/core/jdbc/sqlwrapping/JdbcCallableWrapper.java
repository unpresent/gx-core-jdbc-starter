package ru.gx.core.jdbc.sqlwrapping;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ResultWrapper;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import static lombok.AccessLevel.PROTECTED;

public class JdbcCallableWrapper implements SqlCommandWrapper {
    @Getter(PROTECTED)
    @NotNull
    private final CallableStatement callableStatement;

    public JdbcCallableWrapper(@NotNull final CallableStatement callableStatement) {
        this.callableStatement = callableStatement;
    }

    @Override
    public @NotNull Object getInternalCommand() {
        return getCallableStatement();
    }

    @Override
    public void setStringParam(int paramIndex, @Nullable String value) throws SQLException {
        getCallableStatement().setString(paramIndex, value);
    }

    @Override
    public void setIntegerParam(int paramIndex, @Nullable Integer value) throws SQLException {
        if (value == null) {
            getCallableStatement().setNull(paramIndex, Types.INTEGER);
        } else {
            getCallableStatement().setInt(paramIndex, value);
        }
    }

    @Override
    public void setLongParam(int paramIndex, @Nullable Long value) throws SQLException {
        if (value == null) {
            getCallableStatement().setNull(paramIndex, Types.BIGINT);
        } else {
            getCallableStatement().setLong(paramIndex, value);
        }
    }

    @Override
    public void setNumericParam(int paramIndex, @Nullable BigDecimal value)  throws SQLException {
        if (value == null) {
            getCallableStatement().setNull(paramIndex, Types.NUMERIC);
        } else {
            getCallableStatement().setBigDecimal(paramIndex, value);
        }
    }

    @Override
    public void executeNoResult() throws SQLException {
        getCallableStatement().execute();
    }

    @Override
    public ResultWrapper executeWithResult() throws SQLException {
        return new JdbcResultWrapper(getCallableStatement().executeQuery());
    }

    @SneakyThrows(SQLException.class)
    @Override
    public void close() {
        getCallableStatement().close();
    }
}
