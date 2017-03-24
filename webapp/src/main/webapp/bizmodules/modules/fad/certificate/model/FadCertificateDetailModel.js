Ext.define("Certificate.model.FadCertificateDetailModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'fadCertificateId', 'businessId', 'orderNo', 'certificateNo', 'abstracts', 'currency', 'currtypename', 'primary', 'local', 'token', 'subjectCode', 'subjectName', 'debtorOrCreditor', 'rate1', 'rate2', 'count', 'price', 'checkoutMethod', 'formNo', 'formDate', 'originalFormType', 'originalFormCode', 'originalFormDate', 'remark', 'free1', 'free2', 'free3', 'free4', 'free5', 'free6', 'free7', 'free8', 'free9', 'free10', 'free11', 'free12', 'free13', 'free14', 'free15', 'free16', 'free17', 'free18', 'free19', 'free20', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'seqNo', 'fadCertificateAccountDto', 'fadCertificateAccountDto_c']
    }
);