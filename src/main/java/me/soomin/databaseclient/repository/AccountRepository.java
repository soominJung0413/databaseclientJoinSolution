package me.soomin.databaseclient.repository;

import me.soomin.databaseclient.domain.Account;
import me.soomin.databaseclient.repository.custom.CustomAccountRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface AccountRepository extends R2dbcRepository<Account, Long>, CustomAccountRepository<Account> {
}
