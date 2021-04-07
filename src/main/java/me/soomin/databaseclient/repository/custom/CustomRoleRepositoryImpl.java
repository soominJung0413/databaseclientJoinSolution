package me.soomin.databaseclient.repository.custom;

import lombok.extern.slf4j.Slf4j;
import me.soomin.databaseclient.domain.Account;
import me.soomin.databaseclient.domain.Role;
import me.soomin.databaseclient.repository.util.AccountMapper;
import me.soomin.databaseclient.repository.util.RoleMapper;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Repository
@Transactional(readOnly = true)
@Slf4j
public class CustomRoleRepositoryImpl implements CustomRoleRepository<Role> {

    private final DatabaseClient client;

    public CustomRoleRepositoryImpl(DatabaseClient client) {
        this.client = client;
    }


    @Override
    public Mono<Role> selectByIdRelationEntity(Long id) {
        Long queryStartMillis = System.currentTimeMillis();
        AtomicReference<Long> accountMappingStartMillis = new AtomicReference<>();
        Mono<Role> roleMono = client.sql("select * from role where id = :id")
                .bind("id", id)
                .map(RoleMapper.ROLE_MAPPING_EACH)
                .one()
                .doOnNext(role -> {
                    log.warn("Role Mapped Millis : {}", System.currentTimeMillis() - queryStartMillis);
                    accountMappingStartMillis.set(System.currentTimeMillis());
                })
                .flatMap(role -> client.sql("select * from account where role_id = :id limit 1000000 offset 2")
                        .bind("id", id)
                        .map(AccountMapper.MAPPING_ACCOUNT)
                        .all()
                        .collectList()
                        .map(accounts -> {
                            role.setAccounts(accounts);
                            log.warn("Account Mapped Millis : {}",System.currentTimeMillis() - accountMappingStartMillis.get());
                            return role;
                        })
                ).doOnSuccess(role -> log.warn("Method Runtime : {}", System.currentTimeMillis() - queryStartMillis));

        return roleMono;
        
    }

    @Override
    public Flux<Role> selectByIdJoinAccount(Long id) {
        Role returnRole = new Role();
        returnRole.setAccounts(new ArrayList<>());
        return client.sql("select * from role r left join account a on r.id = a.role_id where r.id = :id limit 1000000 offset 2")
                .bind("id",id)
                .map(RoleMapper.ROLE_MAPPING)
                .all()
                //.log()
                .doOnError(Mono::error);
//                .collectList()
//                .map(roles -> {
//                    Long mappingStartMillis = System.currentTimeMillis();
//                    returnRole.setName(roles.get(0).getName());
//                    returnRole.setId(roles.get(0).getId());
//                    roles.forEach(role -> {
//                        returnRole.getAccounts().add(role.getAccounts().get(0));
//                    });
//                    log.warn("Mapping Executed Millis > {}",System.currentTimeMillis() - mappingStartMillis);
//                    return returnRole;
//                })

    }

    @Override
    public Mono<Long> insertOneReturnGeneratedValue(Role role) {
        return client.sql("insert into role (name) values (:name)")
                .bind("name",role.getName())
                .filter(statement -> statement.returnGeneratedValues("id"))
                .map(row -> row.get("id",Long.class))
                .one()
                .log()
                .doOnError(Mono::error);
    }

    @Override
    public Mono<Integer> deleteByIdReturnUpdatedRow(Long id) {
        return client.sql("delete from role where id = :id")
                .bind("id",id)
                .fetch()
                .rowsUpdated()
                .log()
                .doOnError(Mono::error);
    }


}
