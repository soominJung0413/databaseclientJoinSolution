package me.soomin.databaseclient.repository.custom;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import me.soomin.databaseclient.domain.Account;
import me.soomin.databaseclient.repository.RoleRepository;
import me.soomin.databaseclient.repository.util.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
@Transactional(readOnly = true)
public class CustomAccountRepositoryImpl implements CustomAccountRepository<Account> {

    private final DatabaseClient client;
    private final TransactionalOperator txOp;


    @Autowired
    public CustomAccountRepositoryImpl(DatabaseClient client, TransactionalOperator txOp) {
        this.txOp = txOp;
        this.client = client;
    }

    @Override
    public Mono<Account> selectOneMapEntity(Long id) {
        return client.sql("select * from account where id = :id")
                .bind("id", id)
                .map(AccountMapper.MAPPING_ACCOUNT)
                .one()
                .log();
    }

    @Override
    public Mono<Account> selectOneJoinWithRoleReturnEntity(Long id) {
        return client.sql("select * from account a left join role r on r.id = a.role_id where a.id = :id")
                .bind("id",id)
                .map(AccountMapper.MAPPING_JOIN_COLUMN)
                .one()
                .log();
    }


    @Override
    public Mono<Long> insertOneReturnGeneratedVar(Account account) {
        return client.sql("insert into account (username, password, role_id) values (:username, :password, :role_id)")
                .bind("username",account.getUsername())
                .bind("password",account.getPassword())
                .bind("role_id",account.getRoleId())
                .filter(statement -> statement.returnGeneratedValues("id"))
                .map(row -> row.get("id",Long.class))
                .one()
                .log();
    }


    @Override
    public Mono<Integer> deleteOneReturnUpdatedRow(Long id) {
        return client.sql("delete from account where id = :id")
                .bind("id",id)
                .fetch().rowsUpdated()
                .log();
    }
}
