package ru.gx.core.jdbc.save;

import org.jetbrains.annotations.NotNull;
import ru.gx.core.data.save.AbstractDbSaver;
import ru.gx.core.data.save.DbSavingConfiguration;

import java.util.List;

public class JdbcDbSaver extends AbstractDbSaver {
    public JdbcDbSaver(@NotNull final List<DbSavingConfiguration> configurations) {
        super(configurations);
    }
}
