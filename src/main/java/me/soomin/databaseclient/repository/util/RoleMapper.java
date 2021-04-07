package me.soomin.databaseclient.repository.util;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import me.soomin.databaseclient.domain.Account;
import me.soomin.databaseclient.domain.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class RoleMapper {

    public static final BiFunction<Row, RowMetadata, Role> ROLE_MAPPING = (row, rowMetadata) -> {
        return Role.builder()
                .id(row.get("role_id",Long.class))
                .name(row.get("name",String.class))
                .accounts(Collections.singletonList(Account.builder().id(row.get("id",Long.class)).username(row.get("username",String.class)).password(row.get("password",String.class)).build()))
                .build();
    };

    public static final BiFunction<Row, RowMetadata, Role> ROLE_MAPPING_EACH = (row, rowMetadata) -> {
        return Role.builder()
                .id(row.get("id",Long.class))
                .name(row.get("name",String.class))
                .build();
    };

}
