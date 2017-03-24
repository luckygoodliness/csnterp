Ext.define("Weekly.model.PrmMeetingSummaryModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmWeeklyId','prmProjectMainId','beginDate','endDate','meetingLocation','meetingPersons','customerId','content','workPlan','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);