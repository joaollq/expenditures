package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

import org.joda.time.DateTime;

public class SubmitForValidationActivity extends WorkflowActivity<WorkingCapitalProcess, SubmitForValidationActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return !workingCapital.isCanceledOrRejected()
		&& workingCapital.isMovementResponsible(user)
		&& workingCapital.hasApprovedAndUnSubmittedAcquisitions();
    }

    @Override
    protected void process(final SubmitForValidationActivityInformation activityInformation) {
	final WorkingCapitalProcess workingCapitalProcess = activityInformation.getProcess();
	workingCapitalProcess.submitAcquisitionsForValidation();
	if (activityInformation.isLastSubmission()) {
	    final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
	    final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	    workingCapitalInitialization.setLastSubmission(new DateTime());
	}
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new SubmitForValidationActivityInformation(process, this);
    }

}
