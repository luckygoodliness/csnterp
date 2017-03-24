/**
 * Created by lijx on 2016/8/12.
 */
Ext.define("Receipts.model.PrmContractQueryModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'scmContractCode','contractNature','supplierCode', 'supplierName', 'amount',
            'quantity', 'purchaseTypes', 'contractPayType', 'otherDes', 'debter', 'debterDepartment',
            'bank', 'bankId', 'totalValue', 'payType', 'isUrgernt', 'DebtDate', 'state', 'effectiveDate',
            'stampDate', 'contractState', 'isVirtual', 'paid', 'invoiceAmount', 'remark','scmPaidRate',
            'companyCode', 'projectId','departmentCode', 'createBy', 'createTime', 'updateBy','scmUnpaidMoney',
            'updateTime','locTimezone','tblVersion','projectMoney','actualMoney','prmReceiptRate','scmSupplierUnPaidMoney',{
                name: 'isVoid',
                defaultValue: 0
            },'subjectCode','seqNo','officeId','payeeName','purchasePackageId','isProject','operatorId','operatorName','bugdetId','isClosed','electricCommercialStore','jdName','jdPassword','jdOrderNo','jdOrderDate','jdLastDate','fadInvoiceMoneyL',
            'fadReqMoney','fadReqMoneyLock','invoiceScmValueRealPaid','fadPayReqPayableMoney','fadPayReqPayableMoneyLock','clearanceMoney','hedgeMoney','hedgeMoneyLocked','scmPaidMoney','scmNeedPayMoney','scmNeedPayMoneyLock']
    }
);