Ext.define("Scmsae.model.ScmSaeFormModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime',
    'updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion',
    'seqNo','scmSaeObjectId','scmSaeTaskId','userCode','userCodeDesc','comprehensive','item1','state','officeIdDesc',
    'item2','item3','item4','item5','item6','item7','item8','item9','item10','item11','item12','item13','item14','item15']
	}
);