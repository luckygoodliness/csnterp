Ext.define("Prmcustomer.model.PrmCustomerLinkmanModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmCustomerId', 'unitCode', 'contractCode', 'contractName', 'dept', 'position', 'mobileNumber', 'teleNumber', 'faxNumber', 'email', 'qqNo', 'wxNo', 'businessCode', 'postAddress', 'postCode', 'dutyArea', 'memo', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {name: 'isVoid', defaultValue: 0}]
    }
);