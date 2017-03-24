Ext.define("FadSupplierinfo.controller.SupplierinfoController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'FadSupplierinfo.view.SupplierinfoView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'updateNcCode_edit', name: 'click', fn: 'updateNcCodeBtn'},
        //{cid: 'fadSupplierDto->supplierGenre', name: 'change', fn: 'supplierGenreChange'},
        {cid: 'fadSupplierDto->completeName', name: 'blur', fn: 'supplierNameValidate'},
        {cid: 'fadSupplierDto->simpleName', name: 'blur', fn: 'supplierNameValidate'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.supplierinfo.dto.FadSupplierDto',
    queryAction: 'supplierinfo-query',
    loadAction: 'supplierinfo-load',
    addAction: 'supplierinfo-add',
    modifyAction: 'supplierinfo-modify',
    deleteAction: 'supplierinfo-delete',
    exportXlsAction: "supplierinfo-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        //me.doQuery();
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.view.getCmp("fadSupplierDto->taxTypes").sotValue("0");
        me.view.getCmp("fadSupplierDto->supplierGenre").sotValue("4");
        me.view.getCmp("fadSupplierDto->state").sotValue("2");
        me.setNcCodeButtonState(false);
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.setNcCodeButtonState(false);
    },
    beforeModify: function () {
        var me = this;
        return me.whetherModified();
    },
    afterModify: function () {
        var me = this;
        //var supplierGenre=me.view.getCmp("scmSupplierDto->supplierGenre");
        //if (Scdp.ObjUtil.isEmpty(supplierGenre.gotValue())) {
        //    supplierGenre.sotValue("1");
        //}
        me.setNcCodeButtonState(false);
    },
    beforeSave: function () {
        var me = this;
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        me.setNcCodeButtonState(true);
        me.controlEditButton();
        if (Scdp.ObjUtil.isNotEmpty(retData.supplierCode)) {
            me.view.getCmp("fadSupplierDto->supplierCode").sotValue(retData.supplierCode);
        }
        //me.view.getCmp("query\x26edit").setActiveTab(me.view.getQueryPanel());
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.controlEditButton();
        me.setNcCodeButtonState(true);
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        me.controlEditButton();
        me.setNcCodeButtonState(true);
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
        //me.view.getCmp("query\x26edit").setActiveTab(me.view.getQueryPanel());
        me.setNcCodeButtonState(true);
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
    },

    controlEditButton: function () {
        var role = Erp.Util.getCurrentUserRoleName();
        var isScmVp = false;
        var isKJ = false;
        if (Scdp.ObjUtil.isNotEmpty(role)) {
            isScmVp = role.ROLE.indexOf("供应链部供应商管理专员") > -1;
            //isKJ=role.ROLE.indexOf("会计") > -1;
        }
        var me = this;
        var view = me.view;
        if (isScmVp || isKJ) {
            view.getCmp("editPanel->modifyBtn").setDisabled(false);
        }
    },

    refreshUIStatusBasedOnWorkFlow: function (returnData) {
        var me = this;
        me.controlEditButton();
    },

    updateNcCodeBtn: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("fadSupplierDto->uuid").gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据！");
            return false;
        }
        var callBack = function (subView) {
            if (!subView.validate()) {
                return false;
            }
            var ncCode = subView.getCmp("ncCode").gotValue();
            var postData = {uuid: uuid, ncCode: ncCode};
            Scdp.doAction("supplierinfor-updatenccode", postData, function (result) {
                win.close();
                if (result.success) {
                    Scdp.MsgUtil.info("NC编号设置成功!");
                    view.getCmp("fadSupplierDto->ncCode").sotValue(ncCode);
                }
            }, null, null, null, subView.getForm());
        };
        var form = Ext.widget("JForm", {
            height: 80,
            width: 400,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            items: [
                {
                    xtype: 'JText',
                    allowBlank: false,
                    fieldLabel: 'NC编号',
                    fullInfo: true,
                    cid: 'ncCode'
                }
            ]
        });
        var win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', 'NC编号设置');
        form.getCmp("ncCode").focus();
    },
    setNcCodeButtonState: function (flag) {
        var me = this;
        var updateNcCodeButton = me.view.getCmp("updateNcCode_edit");
        updateNcCodeButton.setDisabled(!flag);
    },
    supplierNameValidate: function () {
        var me = this;
        var completeName = me.view.getCmp('fadSupplierDto->completeName').gotValue();
        var simpleName = me.view.getCmp('fadSupplierDto->simpleName').gotValue();
        var supplierCode = me.view.getCmp('fadSupplierDto->supplierCode').gotValue();
        var postData = {completeName: completeName, simpleName: simpleName, supplierCode: supplierCode};
        Scdp.doAction("supplierinfochange-supplierNameValidate", postData, function () {
        });
    },
    whetherModified: function () {
        var me = this;
        var view = me.view;
        var supplierGenre = view.getCmp("fadSupplierDto->supplierGenre").gotValue();
        if (supplierGenre != "4") {
            Scdp.MsgUtil.info("该供方类型只有供应部有权修改");
            return false;
        } else {
            return true;
        }
    }
    //supplierGenreChange: function () {
    //    var me = this;
    //    var view = me.view;
    //    var supplierGenre = view.getCmp("fadSupplierDto->supplierGenre").gotValue();
    //    if (supplierGenre == "0" || supplierGenre == "1") {//如果付款方式为现金、电汇时 收款单位可以为空
    //        view.getCmp("fadSupplierDto->taxRegistrationNo").allowBlank = false;
    //        view.getCmp("fadSupplierDto->taxTypes").allowBlank = false;
    //    } else {
    //        view.getCmp("fadSupplierDto->taxRegistrationNo").allowBlank = true;
    //        view.getCmp("fadSupplierDto->taxTypes").allowBlank = true;
    //    }
    //}
})
;