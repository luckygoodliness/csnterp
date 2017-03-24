Ext.define("Scmsae.model.ScmSaeObjectModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','supplierName','materialTypeName','materialClassName','amount','contractNum','operativeSegments',
    'companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','scmSaeId','supplierId','materialCode',{name: 'materialType', defaultValue: 0},'comprehensive','item1','item2','item3','item4','item5','item6','item7','item8','item9','item10','item11','item12','item13','item14','item15','scmSaeFormDto','scmSaeFormDto_c']
	}
);