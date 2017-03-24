Ext.define("Requestallot.model.ScmPurchaseReqModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmPurchaseReqId', 'purchasePackageId', 'prmProjectMainId', 'purchaseReqNo', 'bugdetId', 'subjectCode', 'state', 'principal', 'remark', 'companyCode', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'seqNo', 'officeId','totalMoney', {name: 'isProject', defaultValue: 0},'prmProjectMainIdName','officeIdDesc']
    }
);