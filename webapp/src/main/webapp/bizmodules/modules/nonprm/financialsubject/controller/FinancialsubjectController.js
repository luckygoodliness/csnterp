Ext.define("Financialsubject.controller.FinancialsubjectController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Financialsubject.view.FinancialsubjectView',
//    定义唯一字段
    uniqueValidateFields: ['subjectNo'],
    extraEvents: [
        {cid: 'extBenFill', name: 'click', fn: 'allocation'},
        {cid: 'subjectName', name: 'query', fn: 'findFinancialSubject'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.nonprm.financialsubject.dto.FinancialSubjectDto',
    queryAction: 'financialsubject-query',
    loadAction: 'financialsubject-load',
    addAction: 'financialsubject-add',
    modifyAction: 'financialsubject-modify',
    deleteAction: 'financialsubject-delete',
    exportXlsAction: "financialsubject-exportxls",
    financialQuaryAction: "finalcialsubject-allocationAction",
    afterInit: function () {
        var me = this;
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        if (me.actionParams.type == "nonprm") {
            me.view.getCmp("financialSubjectDto->subjectType").setValue('0');
            me.view.getCmp("financialSubjectDto->subjectType").setReadOnly(true);
        }
        me.view.getCmp("financialSubjectDto->isenabled").setValue(true);
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        if (me.actionParams.type == "nonprm") {
            me.view.getCmp("financialSubjectDto->subjectType").setValue('0');
            me.view.getCmp("financialSubjectDto->subjectType").setReadOnly(true);
        }
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        view.getCmp("financialSubjectDto->subjectNo").setReadOnly(true);
        view.getCmp("financialSubjectDto->subjectName").setReadOnly(true);
        if (me.actionParams.type == "nonprm") {
            me.view.getCmp("financialSubjectDto->subjectType").setReadOnly(true);
        }
    },
    beforeSave: function () {
        var me = this;
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
    },

    /*弹出框  部门树  入口：分配按钮*/
    allocation: function () {
        var me = this;
        //获取选中model
        var selectedSubject = me.view.getResultPanel().getSelectionModel().getSelection();
        if (selectedSubject.length == 0) {
            Scdp.MsgUtil.info("请选择一条记录");
            return;
        }
        var checkedSubjectlst = [];
        for (var i = 0; i < selectedSubject.length; i++) {
            checkedSubjectlst.push(selectedSubject[i].data);
        }

        var callBack = function (subView) {
            var lst = subView.items.items[0].getChecked();//
            var checkedDeptlst = [];
            for (var i = 0; i < lst.length; i++) {  //从节点中取出子节点依次遍历
                var subnode = lst[i];
                var id = subnode.data.id;
                var name = subnode.data.text;
                if (name != undefined && name != '-' && name != "中海科技") {
                    checkedDeptlst.push(id);
                }
            }
            var postData = {deptlst: checkedDeptlst, subjectlst: checkedSubjectlst};
            Scdp.doAction("finalcialsubject-allocationAction", postData, function (subView) {
                win.close();
                Scdp.MsgUtil.info("分配成功");
            });
        };
        var form = Ext.widget("JForm", {
            Posiontion: (50, 50),
            height: 400,
            width: 800,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            items: [
                {
                    xtype: 'JCheckTree',
                    iconCls: 'menu_header',
                    //True表示为面板是可收缩的，并自动渲染一个展开/收缩的轮换按钮在头部工具条
                    collapsible: 'true',
//                    storeAction: 'sys-menu-treeload',
                    storeAction: 'sys-org-treeload',
                    id: 'fbDepartment',
                    rootVisible_b: 'false',
                    singleExpand_b: 'false',
                    allowDragDrop_b: 'false',
                    withSearchBar_b: 'true',
                    treeDisabled_b: 'false',
//                    useArrows_b: 'false',
                    onlyLeafCheckable_b: 'true'
                }
            ]
        });
        var win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '组织机构选择', "分配");
    }
});
