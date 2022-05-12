package ru.gx.core.jdbc.sqlwrapping;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ResultWrapper;
import ru.gx.core.data.sqlwrapping.SqlCommandWrapper;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import static lombok.AccessLevel.PROTECTED;

public class JdbcQueryWrapper implements SqlCommandWrapper {
    @Getter(PROTECTED)
    @NotNull
    private final PreparedStatement preparedStatement;

    @Getter
    @NotNull
    private final JdbcConnectionWrapper connection;

    public JdbcQueryWrapper(
            @NotNull final PreparedStatement preparedStatement,
            @NotNull final JdbcConnectionWrapper connection
    ) {
        this.preparedStatement = preparedStatement;
        this.connection = connection;
    }

    @Override
    public @NotNull Object getInternalCommand() {
        return getPreparedStatement();
    }

    @Override
    public void setStringParam(int paramIndex, @Nullable String value) throws SQLException {
        getPreparedStatement().setString(paramIndex, value);
    }

    @Override
    public void setIntegerParam(int paramIndex, @Nullable Integer value) throws SQLException {
        if (value == null) {
            getPreparedStatement().setNull(paramIndex, Types.INTEGER);
        } else {
            getPreparedStatement().setInt(paramIndex, value);
        }
    }

    @Override
    public void setLongParam(int paramIndex, @Nullable Long value) throws SQLException {
        if (value == null) {
            getPreparedStatement().setNull(paramIndex, Types.BIGINT);
        } else {
            getPreparedStatement().setLong(paramIndex, value);
        }
    }

    @Override
    public void setNumericParam(int paramIndex, @Nullable BigDecimal value)  throws SQLException {
        if (value == null) {
            getPreparedStatement().setNull(paramIndex, Types.NUMERIC);
        } else {
            getPreparedStatement().setBigDecimal(paramIndex, value);
        }
    }

    @Override
    public void setBinaryParam(int paramIndex, byte[] value) throws SQLException {
        if (value == null) {
            getPreparedStatement().setNull(paramIndex, Types.VARBINARY);
        } else {
            getPreparedStatement().setBytes(paramIndex, value);
        }
    }

    @Override
    public void executeNoResult() throws SQLException {
        getPreparedStatement().execute();
    }

    @Override
    public ResultWrapper executeWithResult() throws SQLException {
        return new JdbcResultWrapper(getPreparedStatement().executeQuery());
    }

    @SneakyThrows(SQLException.class)
    @Override
    public void close() {
        getPreparedStatement().close();
    }
}
