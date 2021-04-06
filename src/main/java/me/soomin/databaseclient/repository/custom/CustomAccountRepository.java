package me.soomin.databaseclient.repository.custom;

import me.soomin.databaseclient.domain.Account;
import org.springframework.data.relational.core.sql.In;
import reactor.core.publisher.Mono;

public interface CustomAccountRepository<T> {

    Mono<Account> selectOneMapEntity(Long id);

    Mono<Account> selectOneJoinWithRoleReturnEntity(Long id);

    Mono<Long> insertOneReturnGeneratedVar(Account account);

    Mono<Integer> deleteOneReturnUpdatedRow(Long id);
}
