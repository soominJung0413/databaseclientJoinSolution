package me.soomin.databaseclient.domain;

import lombok.Data;

@Data
public class AccountRole {

    private Long id;
    private String username;
    private String password;
    private Long roleId;
    private String name;
}
