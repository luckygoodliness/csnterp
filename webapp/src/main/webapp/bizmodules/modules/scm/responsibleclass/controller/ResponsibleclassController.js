Ext.define("Responsibleclass.controller.ResponsibleclassController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Responsibleclass.view.ResponsibleclassView',
    uniqueValidateFields: ["responsibleClass"],
    extraEvents: [{cid: 'batchSet', name: 'click', fn: 'batchSet'}],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.responsibleclass.dto.ScmResponsibleClassDto',
    queryAction: 'responsibleclass-query',
    loadAction: 'responsibleclass-load',
    addAction: 'responsibleclass-add',
    modifyAction: 'responsibleclass-modify',
    deleteAction: 'responsibleclass-delete',
    exportXlsAction: "responsibleclass-exportxls",
    afterInit: function () {
        var me = this;
        me.doQuery();
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
        //var responsibleClassValue = me.view.getCmp("responsibleClass").value;
        //me.view.getCmp("responsibleClass_query").sotValue(responsibleClassValue);
        //var principalValue = me.view.getCmp("responsibleClass").value;
        //me.view.getCmp("principal_query").sotValue(principalValue);
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
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.doQuery();
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
            Scdp.doAction("responsibleclass-batchset", postData, function () {
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
                    filterConditions: {orgfilter: " org_code='" + Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE) + "' "},
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