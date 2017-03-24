Ext.define("Prmcustomer.model.PrmCustomerModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','customerCode','customerName','countryName','provinceName','customerNation','customerProvince','customerAddress','customerPostalcode','customerTel','customerLink','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'ncCode','taxNo','isSubcompany']
	}
);