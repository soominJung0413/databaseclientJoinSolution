package me.soomin.databaseclient.repository.util;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import me.soomin.databaseclient.domain.Account;
import me.soomin.databaseclient.domain.Role;

import java.util.function.BiFunction;

public class AccountMapper {

    public static final BiFunction<Row, RowMetadata, Account> MAPPING_ACCOUNT = (row, rowMetaData) -> Account.builder()
            .id(row.get("id",Long.class))
            .username(row.get("username",String.class))
            .password(row.get("password",String.class))
            .roleId(row.get("role_id",Long.class))
            .build();

    public static final BiFunction<Row, RowMetadata, Account> MAPPING_JOIN_COLUMN = (row, rowMetadata) -> Account.builder()
            .id(row.get("id",Long.class))
            .username(row.get("username",String.class))
            .password(row.get("password",String.class))
            .role(new Role(row.get("role_id",Long.class),row.get("name",String.class)))
            .build();

}
