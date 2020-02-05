package com.haulmont.sample.petclinic.service;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.View;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(VisitStatusService.NAME)
public class VisitStatusServiceBean implements VisitStatusService {
    @Inject
    private DataManager dataManager;

    @Override
    public void setStatus(Visit visit, String status) {
        Visit reloadedVisit = dataManager.reload(visit, View.LOCAL);
        reloadedVisit.setStatus(status);
        dataManager.commit(reloadedVisit);
    }
}