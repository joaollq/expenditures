package module.workingCapital.domain;

import module.organization.domain.Accountability;
import myorg.domain.User;
import myorg.domain.util.Money;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class WorkingCapitalAcquisition extends WorkingCapitalAcquisition_Base {
    
    public WorkingCapitalAcquisition() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstance());
    }

    public WorkingCapitalAcquisition(final WorkingCapital workingCapital, final String documentNumber, final Supplier supplier,
	    final String description, final AcquisitionClassification acquisitionClassification, final Money valueWithoutVat, final Money money) {
	this();
	setWorkingCapital(workingCapital);
	edit(documentNumber, supplier, description, acquisitionClassification, valueWithoutVat);
	new WorkingCapitalAcquisitionTransaction(this, money);
    }

    public void edit(String documentNumber, Supplier supplier, String description,
	    AcquisitionClassification acquisitionClassification, Money valueWithoutVat) {
	setDocumentNumber(documentNumber);
	setAcquisitionClassification(acquisitionClassification);
	setSupplier(supplier);
	setDescription(description);
	setValueWithoutVat(valueWithoutVat);
    }

    public void edit(String documentNumber, Supplier supplier, String description,
	    AcquisitionClassification acquisitionClassification, Money valueWithoutVat, Money value) {
	edit(documentNumber, supplier, description, acquisitionClassification, valueWithoutVat);
	final WorkingCapitalAcquisitionTransaction workingCapitalAcquisitionTransaction = getWorkingCapitalAcquisitionTransaction();
	workingCapitalAcquisitionTransaction.resetValue(value);
    }

    public void approve(final User user) {
	setApproved(new DateTime());
	final Money valueForAuthorization = Money.ZERO;
	final WorkingCapital workingCapital = getWorkingCapital();
	final Authorization authorization = workingCapital.findUnitResponsible(user.getPerson(), valueForAuthorization);
	setApprover(authorization);
    }

    public void verify(User user) {
	setVerified(new DateTime());
	final Accountability accountability = getWorkingCapitalSystem().getAccountingAccountability(user);
	setVerifier(accountability);
    }

}
