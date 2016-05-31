package db.migration;

import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.security.privilege.SystemSecurityPrivilege;
import com.system.security.privilege.SystemSecurityPrivilegeRepository;
import com.system.security.privilege.SystemSecurityPrivileges;
import com.system.security.role.SystemSecurityRole;
import com.system.security.role.SystemSecurityRoleRepository;
import com.system.security.role.SystemSecurityRoles;
import com.system.security.user.SystemSecurityUser;
import com.system.security.user.SystemSecurityUserRepository;
import com.system.util.collection.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The <class>V1__initial_schema</class> defines the initial
 * security schema creation.
 * <p>
 * This defines various tables that manage the system security.
 * <p>
 * It then performs data insertion into these newly created tables.
 *
 * @author Andrew
 * @see com.system.security.user.SystemSecurityUser
 */
public class V2__initial_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private SystemSecurityPrivilegeRepository systemSecurityPrivilegeRepository;

    @Autowired
    private SystemSecurityRoleRepository systemSecurityRoleRepository;

    @Autowired
    private SystemSecurityUserRepository systemSecurityUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return CollectionUtils.asList(SystemSecurityUser.class, SystemSecurityPrivilege.class, SystemSecurityRole.class);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        systemSecurityPrivilegeRepository.save(getPrivilegeDataEntities());
        systemSecurityRoleRepository.save(getRoleDataEntities());
        systemSecurityUserRepository.save(getUserDataEntities());
    }

    private List<SystemSecurityPrivilege> getPrivilegeDataEntities() {
        SystemSecurityPrivilege readPrivilege = newSystemSecurityPrivilege(SystemSecurityPrivileges.READ_PRIVILEGE);
        SystemSecurityPrivilege writePrivilege = newSystemSecurityPrivilege(SystemSecurityPrivileges.WRITE_PRIVILEGE);
        return CollectionUtils.asList(readPrivilege, writePrivilege);
    }

    private List<SystemSecurityRole> getRoleDataEntities() {
        List<SystemSecurityRole> entityList = new ArrayList<>();
        entityList.add(newSystemSecurityRole(SystemSecurityRoles.ROLE_ADMIN, systemSecurityPrivilegeRepository.findAll()));
        entityList.add(newSystemSecurityRole(SystemSecurityRoles.ROLE_USER, CollectionUtils.asList(systemSecurityPrivilegeRepository.findByName(SystemSecurityPrivileges.READ_PRIVILEGE.toString()))));
        return entityList;
    }

    private List<SystemSecurityUser> getUserDataEntities() {
        SystemSecurityRole adminRole = systemSecurityRoleRepository.findByName(SystemSecurityRoles.ROLE_ADMIN.toString());
        List<SystemSecurityUser> entityList = new ArrayList<>();

        entityList.add(newSystemSecurityUser("Andrew", "Popp", "ampopp04", "password", Arrays.asList(adminRole)));

        return entityList;
    }

    private SystemSecurityPrivilege newSystemSecurityPrivilege(SystemSecurityPrivileges name) {
        return new SystemSecurityPrivilege(name.toString());
    }

    private SystemSecurityRole newSystemSecurityRole(SystemSecurityRoles name, List<SystemSecurityPrivilege> privilegeList) {
        SystemSecurityRole securityRole = new SystemSecurityRole(name.toString());
        securityRole.setPrivileges(privilegeList);
        return securityRole;
    }

    private SystemSecurityUser newSystemSecurityUser(String firstName, String lastName, String username, String password, Collection<SystemSecurityRole> roles) {
        SystemSecurityUser user = new SystemSecurityUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setUsername(username);
        user.setRoles(roles);
        user.setEnabled(true);
        return user;
    }
}