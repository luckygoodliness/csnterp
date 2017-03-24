Ext.define("Requestallot.controller.RequestallotController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Requestallot.view.RequestallotView',
    uniqueValidateFields: [],
    extraEvents: [{cid: 'batchSet', name: 'click', fn: 'batchSet'}],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.requestallot.dto.ScmPurchaseReqDto',
    queryAction: 'requestallot-query',
    loadAction: 'requestallot-load',
    addAction: 'requestallot-add',
    modifyAction: 'requestallot-modify',
    deleteAction: 'requestallot-delete',
    exportXlsAction: "requestallot-exportxls",
    afterInit: function () {
        var me = this;
        me.view.getCmp("isAllot").sotValue("N");
        var businessObj = me.actionParams.erp_msg_business_obj;
        if (businessObj && businessObj.uuid) {
            me.callParent(arguments);
        } else {
            me.doQuery();
        }
    },
    beforeAdd: function () {
        return true;
    },
    afterAdd: function () {
    },
    beforeCopyAdd: function () {
        return true;
    },
    afterCopyAdd: function () {
    },
    beforeModify: function () {
        return true;
    },
    afterModify: function () {
        var me = this;
        me.view.getCmp("principalName").focus();
    },
    beforeSave: function () {
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.doQuery();
    },
    beforeLoadItem: function () {
        var me = this;
        var view = me.view;
        var fadSubjectCodeCmp = view.getCmp("scmPurchaseReqDto->purchaseReqNo")
        fadSubjectCodeCmp.setFieldStyle("color: blue;line-height:1;height:21px;text-decoration: underline;cursor:pointer");

        return true;
    },
    afterLoadItem: function () {
    },
    beforeCancel: function () {
        return true;
    },
    afterCancel: function () {
    },
    beforeDelete: function () {
        return true;
    },
    afterDelete: function () {
    },
    beforeBatchDel: function () {
        return true;
    },
    afterBatchDel: function () {
    },
    beforeExport: function () {
        return true;
    },
    afterExport: function () {
    },
    //批量设置，目前只设置负责人一项
    batchSet: function () {
        var me = this;
        var selectedRecords = me.view.getResultPanel().getSelectionModel().getSelection();
        if (selectedRecords.length == 0) {
            Scdp.MsgUtil.info("未选择记录！");
            return false;
        }
        var uuidLst = [];
        for (var i = 0; i < selectedRecords.length; i++) {
            uuidLst.push(selectedRecords[i].data.uuid);
        }
        var callBack = function (subView) {
            var paramId = subView.getCmp("paramId").gotValue();
            var postData = {uuidLst: uuidLst, paramId: paramId};
            Scdp.doAction("requestallot-batchset", postData, function () {
                win.close();
                me.doQuery();
            }, null, null, null, subView.getForm());
        };
        var form = Ext.widget("JForm", {
            height: 80,
            width: 400,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            items: [
                {
                    xtype: 'JUserGrid',
                    allowBlank: false,
                    fieldLabel: '负责人',
                    fullInfo: true,
                    cid: 'paramName',
                    //filterConditions: {orgfilter: " org_code='" + Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE) + "' "},
                    refer: [{"refField": "paramId", "valueField": "userId"}]
                },
                {
                    xtype: 'JHidden',
                    cid: 'paramId'
                }
            ]
        });
        var win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '批量设置', "选择负责人");
        form.getCmp("paramName").focus();
        //var paramName = me.view.getCmp("paramName");
        //paramName.filterConditions={orgfilter:" org_code='" +Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE)+"' "};
    }
});