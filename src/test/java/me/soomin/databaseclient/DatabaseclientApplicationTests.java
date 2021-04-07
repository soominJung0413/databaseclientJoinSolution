package me.soomin.databaseclient;

import me.soomin.databaseclient.domain.Account;
import me.soomin.databaseclient.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.reactive.TransactionalOperator;

@SpringBootTest
class DatabaseclientApplicationTests {

    @Autowired
    private TransactionalOperator txOp;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void selectOneUseMapper() {

        Account account = accountRepository.selectOneMapEntity(3L).block();

        //Account(id=3, username=admin1234, password=1234, roleId=1, role=null)
        System.out.println(account);
    }

    @Test
    void selectOneWithJoinUseMapper() {

        Account account = accountRepository.selectOneJoinWithRoleReturnEntity(3L).block();

        //Account(id=3, username=admin1234, password=1234, roleId=null, role=Role{id=1, name='ADMIN'})
        System.out.println(account);


    }

}
