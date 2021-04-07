package me.soomin.databaseclient.repository.custom;

import me.soomin.databaseclient.domain.Role;
import me.soomin.databaseclient.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class CustomRoleRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRoleRepositoryTest.class);

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;
    @Autowired
    private RoleRepository roleRepository;
    private Long gId;


    //@BeforeEach
    public void insertRole() {
//        LOGGER.warn("R2DBC Entity Template : {}",r2dbcEntityTemplate);
//
//        Role role = Role.builder().name("test").build();
//        System.out.println(role);
//        gId = roleRepository.insertOneReturnGeneratedValue(role).block();
//
//        assertThat(gId, Matchers.notNullValue());
//
//        LOGGER.warn("Generated Role Id : {}",gId);
    }

    @Test
    public void selectJoinWithAccountTest() {
        LOGGER.warn("Test Start Time : {}", LocalDateTime.now());

        AtomicReference<Long> MappingStartMillis = new AtomicReference<>();

        Long queryStartMillis = System.currentTimeMillis();
        StepVerifier.create(roleRepository.selectByIdJoinAccount(1L).doOnComplete(() -> {
            //LOGGER.warn("Query Executed Time : {}", LocalDateTime.now());
            LOGGER.warn("Query Executed Millis : {}", System.currentTimeMillis() - queryStartMillis);
        }).doOnComplete(() -> {
            //LOGGER.warn("Mapping start Time : {}", LocalDateTime.now());
            MappingStartMillis.set(System.currentTimeMillis());
                }).collectList()
                .map(roles -> {
                    //LOGGER.warn("Mapping start Time : {}", LocalDateTime.now());
                    Role returnRole = new Role();
                    returnRole.setAccounts(new ArrayList<>());
                    Long mappingStartMillis = System.currentTimeMillis();
                    returnRole.setName(roles.get(0).getName());
                    returnRole.setId(roles.get(0).getId());
                    roles.forEach(role -> {
                        returnRole.getAccounts().add(role.getAccounts().get(0));
                    });
                    return returnRole;
                }).doOnSuccess(role -> {
                    //LOGGER.warn("Mapping Executed Time : {}", LocalDateTime.now());
                    LOGGER.warn("Mapping Executed Millis : {}", System.currentTimeMillis() - MappingStartMillis.get());
                    LOGGER.warn("QueryExecuted Millis : {}", System.currentTimeMillis() - queryStartMillis);
                })
                
        ).expectNextMatches(role -> {
            LOGGER.warn("Mapping Role Account Field Size : {}",role.getAccounts().size());
            return role != null;
        }).verifyComplete();

        Long queryExecutedMillis = System.currentTimeMillis();

        LOGGER.warn("Test Executed Time : {}", LocalDateTime.now());
    }

    @Test
    public void selectEachEntity() {
        StepVerifier.create(roleRepository.selectByIdRelationEntity(1L)).expectNextMatches(role -> {
            LOGGER.warn("Mapping Role Id {}, Name: {}",role.getId(),role.getName());
            LOGGER.warn("Mapping Accounts Size : {}",role.getAccounts().size());
            return role != null;
        }).verifyComplete();
    }

    //@AfterEach
    public void deleteRoleTest() {
//        Integer deletedRow = roleRepository.deleteByIdReturnUpdatedRow(gId).block();
//        LOGGER.warn("Delete Role, Row Count : {}",deletedRow);
    }

}