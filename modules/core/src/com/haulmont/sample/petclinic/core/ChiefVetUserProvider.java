package com.haulmont.sample.petclinic.core;

import com.haulmont.addon.bproc.provider.UserProvider;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component("petclinic_ChiefVetUserProvider")
public class ChiefVetUserProvider implements UserProvider {
    private static final String CHIEF_VET_ROLE_NAME = "Chief";

    @Inject
    private DataManager dataManager;

    @Override
    public User get(String executionId) {
        List<User> chiefDoctors = dataManager.load(User.class)
                .query("select u from sec$User u join u.userRoles ur where ur.role.name = :roleName")
                .parameter("roleName", CHIEF_VET_ROLE_NAME)
                .list();
        if (chiefDoctors.isEmpty()) {
            throw new RuntimeException("No chief doctor found");
        }
        return chiefDoctors.get(0);
    }
}