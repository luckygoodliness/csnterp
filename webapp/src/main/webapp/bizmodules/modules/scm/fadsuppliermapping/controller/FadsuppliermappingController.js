Ext.define("Fadsuppliermapping.controller.FadsuppliermappingController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Fadsuppliermapping.view.FadsuppliermappingView',
    uniqueValidateFields: [],
    extraEvents: [{cid: 'batchSetting', name: 'click', fn: 'batchSetting'}],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.fadsuppliermapping.dto.FadSupplierMappingDto',
    queryAction: 'fadsuppliermapping-query',
    loadAction: 'fadsuppliermapping-load',
    addAction: 'fadsuppliermapping-add',
    modifyAction: 'fadsuppliermapping-modify',
    deleteAction: 'fadsuppliermapping-delete',
    exportXlsAction: "fadsuppliermapping-exportxls",
    afterInit: function () {
        var me = this;
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.view.getCmp("fadSupplierMappingDto->dataFrom").sotValue("1");
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
        return me.whetherModified();
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
    whetherModified:function(){
        var me = this;
        var view =me.view;
        var resultPanel = view.getCmp('resultPanel');
        var selection = resultPanel.getSelectionModel().getSelection();
        var isExist = true;
        Ext.Array.each(selection, function (item) {
            var dataFrom = item.get("dataFrom");
            if (dataFrom =="0"){
                Scdp.MsgUtil.info("该数据不能修改");
                isExist=false;
            }
        });
        return isExist;
    },
    //点击批量设置按钮
    batchSetting: function () {
        var me = this;
        var view = this.view;
        var resultPanel = view.getCmp('resultPanel');
        var selection = resultPanel.getSelectionModel().getSelection();
        if (selection.length =="0"){
            Scdp.MsgUtil.info("请选择数据");
            return false;
        }
        var callBack = function (subView) {
            var isExist = false;
            var postData = {};
            var arr = [];
            Ext.Array.each(selection, function (item) {
                var uuid = item.get("uuid");
                arr.push({uuid: uuid})
            });
            postData.supplieid = subView.getCmp("Supplierid").gotValue();
            postData.uuidList = arr;
            Scdp.doAction("fadsuppliermapping-replacesupplierid", postData, function () {
                return false;
            });
            win.close();
            me.doQuery();
        };
        var form = Ext.widget("JForm", {
            height: 150,
            width: 300,
            layout: 'form',
            bodyPadding: '55 10 10 10',
            items: [
                {
                    xtype: 'JHidden',
                    cid: 'SupplieridDesc'
                },
                {
                    xtype: 'JSupplierGrid',
                    cid: 'Supplierid',
                    fieldLabel: '新名字',
                    displayDesc: true,
                    valueField: 'uuid',
                    descField: 'completeName'
                }
            ]
        });
        win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '批量修改', "确认");
    },
});