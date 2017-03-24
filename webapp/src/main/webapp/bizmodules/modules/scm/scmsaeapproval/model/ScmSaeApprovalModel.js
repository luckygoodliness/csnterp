Ext.define("Scmsaeapproval.model.ScmSaeApprovalModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','curYear','title','state','curYearName','remark','scmSaeObjectDto','scmSaeObjectDto_c']
	}
);