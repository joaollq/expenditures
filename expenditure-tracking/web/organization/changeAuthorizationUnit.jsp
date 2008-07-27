<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.attribute.authorization" bundle="ORGANIZATION_RESOURCES"/></h2>
<br />
<fr:view name="authorization" property="person"
		type="pt.ist.expenditureTrackingSystem.domain.organization.Person"
		schema="viewPerson">
	<fr:layout name="tabular">
	</fr:layout>
</fr:view>
<br/>
<br/>
<br/>
<bean:define id="urlExpand" type="java.lang.String">/organization.do?method=expandAuthorizationUnit&amp;authorizationOid=<bean:write name="authorization" property="OID"/></bean:define>
<bean:define id="urlSelect" type="java.lang.String">/organization.do?method=changeAuthorizationUnit&amp;authorizationOid=<bean:write name="authorization" property="OID"/></bean:define>
<html:link action="<%= urlExpand %>">
	<bean:message key="link.top" bundle="EXPENDITURE_RESOURCES"/>
</html:link>
<br/>
<logic:present name="unit">
	<logic:present name="unit" property="parentUnit">
		<bean:write name="unit" property="parentUnit.name"/>
		<logic:notEmpty name="unit" property="parentUnit.costCenter">
			( <bean:write name="unit" property="parentUnit.costCenter"/> )
		</logic:notEmpty>
		<html:link action="<%= urlExpand %>" paramId="unitOid" paramName="unit" paramProperty="parentUnit.OID">
			<bean:message key="link.expand" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<html:link action="<%= urlSelect %>" paramId="unitOid" paramName="unit" paramProperty="parentUnit.OID">
			<bean:message key="link.select" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<br/>	
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</logic:present>
	<bean:write name="unit" property="name"/>
	<logic:notEmpty name="unit" property="costCenter">
		( <bean:write name="unit" property="costCenter"/> )
	</logic:notEmpty>
	<html:link action="<%= urlExpand %>" paramId="unitOid" paramName="unit" paramProperty="OID">
		<bean:message key="link.expand" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
	<html:link action="<%= urlSelect %>" paramId="unitOid" paramName="unit" paramProperty="OID">
		<bean:message key="link.select" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
	<br/>	
</logic:present>
<logic:present name="units">
	<logic:iterate id="u" name="units">
		<logic:present name="unit">
			<logic:present name="unit" property="parentUnit">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</logic:present>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</logic:present>
		<bean:write name="u" property="name"/>
		<logic:notEmpty name="u" property="costCenter">
			( <bean:write name="u" property="costCenter"/> )
		</logic:notEmpty>
		<html:link action="<%= urlExpand %>" paramId="unitOid" paramName="u" paramProperty="OID">
			<bean:message key="link.expand" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<html:link action="<%= urlSelect %>" paramId="unitOid" paramName="u" paramProperty="OID">
			<bean:message key="link.select" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
		<br/>
	</logic:iterate>
</logic:present>