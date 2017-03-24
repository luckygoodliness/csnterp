Ext.define("Purchasereq.model.ScmPurchaseReqModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'principal', 'fadSubjectCode', 'projectCode', 'prmPurchaseReqId', 'purchasePackageId', 'prmProjectMainId', 'purchaseReqNo', 'financialSubject', 'financialSubjectCode', 'subjectLevel', 'state', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'prmPurchaseReqDetailDto', 'prmPurchaseReqDetailDto_c', 'officeId', 'subPackageNo', 'projectName', 'orgName', 'stampCount', 'isProject', 'bugdetId', 'subjectCode', 'withAttach', 'officeIdDesc', 'isRead', 'projectCode']
    }
);