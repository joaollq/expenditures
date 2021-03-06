/*
 * @(#)AuthorizeActivity.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalInitializationReenforcement;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalRequest;
import module.workingCapital.domain.util.PaymentMethod;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.bennu.core.util.BundleUtil;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class AuthorizeActivity extends WorkflowActivity<WorkingCapitalProcess, WorkingCapitalInitializationInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
                + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
        final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
        return !workingCapital.isCanceledOrRejected() && workingCapital.isPendingAuthorization(user)
        // && workingCapital.getWorkingCapitalRequestsSet().isEmpty()
        ;
    }

    @Override
    protected void process(final WorkingCapitalInitializationInformation activityInformation) {
        final WorkingCapitalInitialization workingCapitalInitialization = activityInformation.getWorkingCapitalInitialization();
        final User user = getLoggedPerson();
        workingCapitalInitialization.authorize(user);

        final WorkingCapitalProcess workingCapitalProcess = activityInformation.getProcess();
        final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();

        final Money maxAnualValue = workingCapitalInitialization.getMaxAuthorizedAnualValue();
        Money requestedValue;
        if (workingCapitalInitialization instanceof WorkingCapitalInitializationReenforcement) {
            final WorkingCapitalInitializationReenforcement workingCapitalInitializationReenforcement =
                    (WorkingCapitalInitializationReenforcement) workingCapitalInitialization;
            requestedValue = workingCapitalInitializationReenforcement.getAuthorizedReenforcementValue();
        } else {
            requestedValue = workingCapitalInitialization.getAuthorizedAnualValue();
//	    requestedValue = maxAnualValue.divideAndRound(new BigDecimal(6));
        }
        final Money anualValue = workingCapitalInitialization.getMaxAuthorizedAnualValue();
        final Money possibaySpent = workingCapital.getPossibaySpent();
        final Money maxAllocatableValue = anualValue.subtract(possibaySpent);
        if (requestedValue.isGreaterThan(maxAllocatableValue)) {
            requestedValue = maxAllocatableValue;
        }
        final PaymentMethod paymentMethod =
                workingCapitalInitialization.getInternationalBankAccountNumber() == null
                        || workingCapitalInitialization.getInternationalBankAccountNumber().isEmpty() ? PaymentMethod.CHECK : PaymentMethod.WIRETRANSFER;
        new WorkingCapitalRequest(workingCapital, requestedValue, paymentMethod);
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new WorkingCapitalInitializationInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return true;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
