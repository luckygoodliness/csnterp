Ext.define("Payreq.model.FadPayReqHModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','reqno','approveMoney','year','month','state','remark','companyCode','companyName','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','recordVersion',{name: 'reqtype', defaultValue: 0}]
	}
);