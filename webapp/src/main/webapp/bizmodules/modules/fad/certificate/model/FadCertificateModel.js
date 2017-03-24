Ext.define("Certificate.model.FadCertificateModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'businessId', 'certificateNo', 'ncSwiftNumber', 'certificateDate', 'makeDate', 'certificateType', 'abstracts', 'debtor', 'creditor', 'maker', 'cashier', 'auditor', 'bookkeeper', 'years', 'months', 'attachmentNumber', 'receiverOrPayerType', 'receiverOrPayerCode', 'receiverOrPayerId', 'receiverOrPayerName', 'originalFormType', 'originalFormCode', 'originalFormDate', 'state', 'feedback', 'ncrequestUrl', 'ncrequestXml', 'ncresponseXml', 'remark', 'free1', 'free2', 'free3', 'free4', 'free5', 'free6', 'free7', 'free8', 'free9', 'free10', 'free11', 'free12', 'free13', 'free14', 'free15', 'free16', 'free17', 'free18', 'free19', 'free20', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'seqNo']
    }
);