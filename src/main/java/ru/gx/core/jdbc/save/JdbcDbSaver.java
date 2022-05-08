package ru.gx.core.jdbc.save;

import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractDbSaver;
import ru.gx.core.data.save.DbSavingDescriptor;
import ru.gx.core.data.save.DbSavingOperator;

public class JdbcDbSaver extends AbstractDbSaver {

    public JdbcDbSaver(@NotNull DbSavingOperator saveOperator) {
        super(saveOperator);
    }

    @Override
    @NotNull
    protected DbSavingDescriptor createDescriptor(@NotNull final DbSavingOperator saveOperator) {
        return new JdbcDbSavingDescriptor(saveOperator);
    }
}
