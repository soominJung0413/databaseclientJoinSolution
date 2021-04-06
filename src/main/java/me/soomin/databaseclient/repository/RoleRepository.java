package me.soomin.databaseclient.repository;

import me.soomin.databaseclient.domain.Role;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoleRepository extends R2dbcRepository<Role,Long> {
}
