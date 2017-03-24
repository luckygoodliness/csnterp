Ext.define("Rtfreevalue.model.FadRtfreevalueModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','accountNo','accountType','accountInfoCode','accountInfoName','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);