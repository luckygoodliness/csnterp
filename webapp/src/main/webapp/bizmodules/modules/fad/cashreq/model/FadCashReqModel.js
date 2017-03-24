Ext.define("Cashreq.model.FadCashReqModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid','records', 'runningNo', 'projectId', 'budgetCUuid', 'projectName', 'packageUuid', 'subjectCode', 'subjectName', 'officeId', 'officeIdDesc', 'reqType', 'depositCharacteristic',
            'isUrgent', 'depositType', 'staffId', 'staffIdDesc', 'supplierId', 'supplierName', 'purchaseContractId', 'explanation',
            'model', 'other', 'tripLocation', 'tripDays', 'tripMemberNum', 'vehicle', 'tripReason', 'purpose', 'money', 'operateBusinessBidInfoId', 'operateBusinessBidInfoIdDesc',
            'writeoffMoney', 'interestRate', 'payStyle', 'payeeName', 'financeNo', 'payeeBankName', 'payeeLocation', 'fadPayReqCUuid', 'fadPayReqHUuid',
            'electricCommercialStore', 'isProject', 'preclearDate', 'applyDate', 'operatorId', 'operatorName', 'operateTime', 'totalOverdueInterest',//M7_C5_F2_逾期利息
            'payeeAccount', 'payeePostcode', 'squareDate', 'budgetType', 'state', 'remark', 'budgetDesc',
            'cashBackMoney', 'companyCode', 'companyName', 'departmentCode', 'createBy', 'createTime', 'fileId', 'attachmentFileName',
            'seqNo', 'updateBy', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'locTimezone', 'tblVersion', 'contractPayType',
            'meetingStartTime', 'meetingEndTime', 'meetingDays', 'meetingLocation', 'meetingInPersonNum', 'meetingOutPersonNum', 'meetingSubject']
    }
);