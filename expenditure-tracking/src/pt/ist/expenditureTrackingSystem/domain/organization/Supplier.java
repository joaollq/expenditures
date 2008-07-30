package pt.ist.expenditureTrackingSystem.domain.organization;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;

public class Supplier extends Supplier_Base {

    private Supplier() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public Supplier(String fiscalCode) {
	this();
	if (fiscalCode == null || fiscalCode.length() == 0) {
	    throw new DomainException("error.fiscal.code.cannot.be.empty");
	}
	setFiscalIdentificationCode(fiscalCode);
    }

    public Supplier(String name, String fiscalCode, String address, String phone, String fax, String eMail) {
	this(fiscalCode);
	setName(name);
	setAddress(address);
	setPhone(phone);
	setFax(fax);
	setEMail(eMail);
    }

    public static Supplier readSupplierByFiscalIdentificationCode(String fiscalIdentificationCode) {
	for (Supplier supplier : ExpenditureTrackingSystem.getInstance().getSuppliersSet()) {
	    if (supplier.getFiscalIdentificationCode().equals(fiscalIdentificationCode)) {
		return supplier;
	    }
	}
	return null;
    }

    public BigDecimal getTotalAllocated() {
	BigDecimal result = BigDecimal.ZERO;
	for (final AcquisitionRequest acquisitionRequest : getAcquisitionRequestsSet()) {
	    final AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();
	    final AcquisitionProcessState acquisitionProcessState = acquisitionProcess.getAcquisitionProcessState();
	    final AcquisitionProcessStateType acquisitionProcessStateType = acquisitionProcessState.get$acquisitionProcessStateType();
	    if (acquisitionProcessStateType.compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER) >= 0) {
		result = result.add(acquisitionRequest.getTotalItemValue());
	    }
	}
	return result;	
    }

}
