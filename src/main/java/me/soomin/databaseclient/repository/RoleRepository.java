package me.soomin.databaseclient.repository;

import me.soomin.databaseclient.domain.Role;
import me.soomin.databaseclient.repository.custom.CustomRoleRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoleRepository extends R2dbcRepository<Role,Long>, CustomRoleRepository<Role> {
}
