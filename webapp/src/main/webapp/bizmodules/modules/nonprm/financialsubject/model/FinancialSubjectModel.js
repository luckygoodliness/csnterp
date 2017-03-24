Ext.define("Financialsubject.model.FinancialSubjectModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','subjectNo','rate','financialCombo','categoryCombo','subjectCode','subjectName','subjectCategory','isenabled','needPurchase','subjectType','descp','companyCode','companyName','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion']
	}
);