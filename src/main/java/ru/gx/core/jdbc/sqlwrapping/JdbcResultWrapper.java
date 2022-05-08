package ru.gx.core.jdbc.sqlwrapping;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gx.core.data.sqlwrapping.ResultWrapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static lombok.AccessLevel.PROTECTED;

public class JdbcResultWrapper implements ResultWrapper {
    @Getter(PROTECTED)
    @NotNull
    private final ResultSet resultSet;

    public JdbcResultWrapper(@NotNull final ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public @NotNull Object getInternalData() {
        return getResultSet();
    }

    @Override
    public boolean next() throws SQLException {
        return getResultSet().next();
    }

    @Override
    public boolean first() throws SQLException {
        return getResultSet().first();
    }

    @Override
    public boolean last() throws SQLException {
        return getResultSet().last();
    }

    @Override
    public @Nullable String getString(int columnIndex) throws SQLException {
        return getResultSet().getString(columnIndex);
    }

    @Override
    public @Nullable Integer getInteger(int columnIndex) throws SQLException {
        return getResultSet().getInt(columnIndex);
    }

    @Override
    public @Nullable Long getLong(int columnIndex) throws SQLException {
        return getResultSet().getLong(columnIndex);
    }

    @Override
    public @Nullable BigDecimal getNumeric(int columnIndex) throws SQLException {
        return getResultSet().getBigDecimal(columnIndex);
    }
}
