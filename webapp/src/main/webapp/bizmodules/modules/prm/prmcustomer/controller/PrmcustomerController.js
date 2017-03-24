Ext.define("Prmcustomer.controller.PrmcustomerController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Prmcustomer.view.PrmcustomerView',
    uniqueValidateFields: ['customerName'],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.prmcustomer.dto.PrmCustomerDto',
    queryAction: 'prmcustomer-query',
    loadAction: 'prmcustomer-load',
    addAction: 'prmcustomer-add',
    modifyAction: 'prmcustomer-modify',
    voidAction: 'prmcustomer-delete',
    exportXlsAction: "prmcustomer-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
    },
    beforeSave: function () {
        var me = this;
        var view = this.view;
        var customerPostalcode = view.getCmp("prmCustomerDto->customerPostalcode").gotValue();
        var customerTel = view.getCmp("prmCustomerDto->customerTel").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(customerTel)&&!Scdp.StrUtil.isZipCode(customerPostalcode)) {
            Scdp.MsgUtil.info("客户邮编格式不正确！");
            return false;
        }
        /*if(!Scdp.StrUtil.isMobile(customerTel)){
         Scdp.MsgUtil.info("客户电话格式不正确！");
         }*/
        var linkManInfoValue = view.getCmp("prmCustomerLinkmanDto");
        var modifyRow = linkManInfoValue.gotValue(true);
        Ext.Array.each(modifyRow, function (item) {
            var mobileNumber = item.mobileNumber;
            if (Scdp.ObjUtil.isNotEmpty(mobileNumber)
                && !Scdp.StrUtil.isMobile(mobileNumber)) {
                Scdp.MsgUtil.info("手机号格式不正确！");
                return false;
            }
        });
        return true;
    },
    afterSave: function (retData) {
        var me = this;
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
    },
    beforeBatchDel: function () {
        var me = this;
        return true;
    },
    afterBatchDel: function () {
        var me = this;
    },
    beforeExport: function () {
        var me = this;
        return true;
    },
    afterExport: function () {
        var me = this;
    }
});