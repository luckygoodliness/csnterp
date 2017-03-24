Ext.define("Cashreq.model.FadCashReqShareModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','fadCashReqUuid','officeId','officeName','staffId','explanation','money','payStyle','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'seqNo']
	}
);