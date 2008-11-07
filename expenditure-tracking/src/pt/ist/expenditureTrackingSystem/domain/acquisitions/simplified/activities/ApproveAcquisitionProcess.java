package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class ApproveAcquisitionProcess extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	User user = getUser();
	return user != null
		&& process.isResponsibleForUnit(user.getPerson(), process.getAcquisitionRequest()
			.getTotalItemValueWithAdditionalCostsAndVat())
		&& !process.getAcquisitionRequest().hasBeenApprovedBy(user.getPerson());
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return  super.isAvailable(process) && process.getAcquisitionProcessState().isInAllocatedToUnitState();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	Person person = (Person) objects[0];
	process.approveBy(person);
    }

}
