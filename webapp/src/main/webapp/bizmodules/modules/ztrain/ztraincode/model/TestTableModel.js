//Ext.define("Ztraincode.model.TestTableModel", {
//        extend: 'Ext.data.Model',
//fields: ['uuid','code','name','age','state']
//	}
//);

Ext.define("Ztraincode.model.TestTableModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid','code','name','age',{name: 'state', defaultValue: 0},'companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo']
    }
);