package me.soomin.databaseclient.repository.custom;

import me.soomin.databaseclient.domain.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CustomRoleRepository<T> {

    Mono<Role> selectByIdRelationEntity(Long id);

    Flux<Role> selectByIdJoinAccount(Long id);

    Mono<Long> insertOneReturnGeneratedValue(Role role);

    Mono<Integer> deleteByIdReturnUpdatedRow(Long id);
}
