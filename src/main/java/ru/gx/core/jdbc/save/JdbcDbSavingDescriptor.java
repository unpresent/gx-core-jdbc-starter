package ru.gx.core.jdbc.save;

import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractDbSavingDescriptor;
import ru.gx.core.data.save.DbSavingOperator;

public class JdbcDbSavingDescriptor extends AbstractDbSavingDescriptor {
    public JdbcDbSavingDescriptor(@NotNull DbSavingOperator saveOperator) {
        super(saveOperator);
    }
}
