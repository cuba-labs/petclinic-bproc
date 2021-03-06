package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.addon.bproc.service.BprocRuntimeService;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import org.apache.commons.lang3.time.DateUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@UiController("petclinic_Visit.edit")
@UiDescriptor("visit-edit.xml")
@EditedEntityContainer("visitDc")
@LoadDataBeforeShow
public class VisitEdit extends StandardEditor<Visit> {

    private static final String PROCESS_DEFINITION_KEY = "new-visit";
    private static final String VISIT_STATUS_NEW = "New";

    private boolean isNewVisit;

    @Inject
    private UserSession userSession;

    @Inject
    private TimeSource timeSource;

    @Inject
    private BprocRuntimeService bprocRuntimeService;

    @Inject
    private MetadataTools metadataTools;

    @Subscribe
    public void onInitEntity(InitEntityEvent<Visit> event) {
        isNewVisit = true;
        event.getEntity().setStatus(VISIT_STATUS_NEW);
    }


    @Subscribe
    public void onAfterCommitChanges(AfterCommitChangesEvent event) {
        Visit visit = getEditedEntity();
        if (isNewVisit) {
            Map<String, Object> processVariables = new HashMap<>();
            processVariables.put("visit", visit);
            processVariables.put("confirmVisitDate", DateUtils.addSeconds(timeSource.currentTimestamp(), 5));
            processVariables.put("overdueVisitDate", DateUtils.addDays(visit.getVisitDate(), 1));
            processVariables.put("administrator", userSession.getCurrentOrSubstitutedUser());
            bprocRuntimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, metadataTools.getInstanceName(visit), processVariables);
        }
    }
}