/*
 * @(#)CancelInvoiceConfirmation.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem._development.ExternalIntegration;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class CancelInvoiceConfirmation extends
        WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        Person person = user.getExpenditurePerson();
        return isUserProcessOwner(process, user)
                && process.isResponsibleForUnit(person)
                && !process.getConfirmedInvoices(person).isEmpty()
                && ((process.hasProjectsAsPayingUnits() && !process.getRequest()
                        .hasAllocatedFundsPermanentlyForAnyProjectFinancer()) || (!process.hasProjectsAsPayingUnits() && !process
                        .getRequest().hasAnyEffectiveFundAllocationId()));
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
        final RegularAcquisitionProcess process = activityInformation.getProcess();
        process.cancelInvoiceConfirmationBy(UserView.getCurrentUser().getExpenditurePerson());

        if (ExternalIntegration.isActive()) {
            process.cancelFundAllocationRequest(true);
        }
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    public boolean isUserAwarenessNeeded(RegularAcquisitionProcess process, User user) {
        return false;
    }
}
