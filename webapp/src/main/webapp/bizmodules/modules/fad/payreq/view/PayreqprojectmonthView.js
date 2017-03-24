Ext.define('Payreq.view.PayreqprojectmonthView', {
    extend: 'Payreq.view.PayreqView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/payreq',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 50,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'payreqmonth-query-layout.xml',
    editLayoutFile: 'payreqmonth-edit-layout.xml',
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Scm_PurchasePaymentRequestMonthPayment',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },

    resetToolbar: function () {
        var me = this;
        me.getEditToolbar().add({
            xtype: 'button',
            text: '提交',
            cid: 'commitBtn',
            tooltip: '创建人点击提交后可提交至本部门领导审核。',
            iconCls: "adquery_panel_up_arrow"
        });
        me.getEditToolbar().add({
            xtype: 'button',
            text: '撤回',
            cid: 'backBtn',
            iconCls: "adquery_panel_down_arrow"
        });
        me.getEditToolbar().add({
            xtype: 'JButton',
            text: '全部确认',
            cid: 'checkAllBtn',
            tooltip: '系统会将所有待确认的明细全部提交至供应链部。',
            iconCls: "ok_btn"
        });
        me.getEditToolbar().add({
            xtype: 'JButton',
            text: '勾选确认',
            cid: 'checkBtn',
            tooltip: '选择需确认的行后，点击确认可提交至供应链部。',
            iconCls: "ok_btn"
        });
        me.getEditToolbar().add({
            xtype: 'button',
            text: '退回部门',
            cid: 'backDeptBtn',
            iconCls: "adquery_panel_down_arrow"
        });
        me.getEditToolbar().add({
            xtype: 'button',
            text: '锁定',
            cid: 'lockBtn',
            iconCls: "pass_icon_16"
        });

        this.callParent(arguments);
    },

    afterInit: function () {
        var me = this;
        this.callParent(arguments);
        var grid = me.getCmp("fadPayReqCDto");
        grid.beforeBeforeEditGrid = me.beforeBeforeEditGridCdto;
        grid.beforeDeleteRow = me.fadPayReqCDtoGridRowBeforeDelete;
        if (me.controller.actionParams.type == "project") {
            me.getCmp('editPanel->workFlowViewLogBtn').setVisible(false);
            me.getCmp('editPanel->workFlowCommitBtn').setVisible(false);
            me.getCmp('editPanel->workFlowAssignBtn').setVisible(false);
            me.getCmp('editPanel->workFlowRollBackBtn').setVisible(false);
            me.getCmp('editPanel->workFlowCancelBtn').setVisible(false);

            grid.getColumnBydataIndex("purchaseManagerMoney").hide();
            grid.getColumnBydataIndex("purchaseChiefMoney").hide();
            grid.getColumnBydataIndex("purchaseManagerMoneyRate").hide();
            grid.getColumnBydataIndex("purchaseChiefMoneyRate").hide();

            grid.getColumnBydataIndex("reqMoney").editor.readOnly = false;
            if (me.controller.isBizVp) {
                grid.getColumnBydataIndex("approveMoney").editor.readOnly = false;
            }
        } else {
            me.getCmp('editPanel->workFlowViewLogBtn').setVisible(true);
            me.getCmp('editPanel->workFlowCommitBtn').setVisible(true);
            me.getCmp('editPanel->workFlowAssignBtn').setVisible(true);
            me.getCmp('editPanel->workFlowRollBackBtn').setVisible(true);
            me.getCmp('editPanel->workFlowCancelBtn').setVisible(true);

            grid.getColumnBydataIndex("reqMoney").editor.readOnly = true;
            grid.getColumnBydataIndex("approveMoney").editor.readOnly = true;

            grid.getColumnBydataIndex("purchaseManagerMoney").editor.readOnly = false;
            if (me.controller.isScmVp) {
                grid.getColumnBydataIndex("purchaseChiefMoney").editor.readOnly = false;
                grid.getColumnBydataIndex("auditMoney").editor.readOnly = false;

                me.getCmp('editPanel->fileUpload').setVisible(true);
                me.getCmp('editPanel->fileDownload').setVisible(true);
                me.getCmp('editPanel->fileDelete').setVisible(true);
            } else {
                me.getCmp("editPanel->commitBtn").setVisible(true);
                grid.getColumnBydataIndex("purchaseChiefMoney").editor.readOnly = true;
                grid.getColumnBydataIndex("auditMoney").editor.readOnly = true;
            }
        }
    },

    UIStatusChanged: function (view, newStatus) {
        var me = this;
        if (newStatus == Scdp.Const.UI_INFO_STATUS_VIEW) {
            me.getCmp('editPanel->fileUpload').setDisabled(false);
            me.getCmp('editPanel->fileDelete').setDisabled(false);
        } else {
            me.getCmp('editPanel->fileUpload').setDisabled(true);
            me.getCmp('editPanel->fileDelete').setDisabled(true);
        }
    },

    resetComState: function () {
        var me = this;
        var newStatus = me.uistatus;
        var state = me.getCmp("fadPayReqHDto->state").gotValue();
        var canBePayed = (state === "2");
        var canBeHandled = (state === "0");
        if (newStatus == "VIEW") {
            me.getCmp("editPanel->payBtn").setDisabled(!canBePayed);
            me.getCmp("editPanel->cashReqBtn").setDisabled(!canBePayed);
            me.getCmp("editPanel->lockBtn").setDisabled(!canBeHandled);
            me.getCmp("editPanel->backBtn").setDisabled(!canBeHandled);
            me.getCmp("editPanel->commitBtn").setDisabled(!canBeHandled);
            me.getCmp("editPanel->backDeptBtn").setDisabled(!canBeHandled);
            me.getCmp("editPanel->checkBtn").setDisabled(!canBeHandled);
            me.getCmp("editPanel->checkAllBtn").setDisabled(!canBeHandled);
        } else {
            me.getCmp("editPanel->payBtn") && me.getCmp("editPanel->payBtn").setDisabled(true);
            me.getCmp("editPanel->cashReqBtn") && me.getCmp("editPanel->cashReqBtn").setDisabled(true);
            me.getCmp("editPanel->lockBtn") && me.getCmp("editPanel->lockBtn").setDisabled(true);
            me.getCmp("editPanel->backBtn") && me.getCmp("editPanel->backBtn").setDisabled(true);
            me.getCmp("editPanel->commitBtn") && me.getCmp("editPanel->commitBtn").setDisabled(true);
            me.getCmp("editPanel->backDeptBtn") && me.getCmp("editPanel->backDeptBtn").setDisabled(true);
            me.getCmp("editPanel->checkBtn") && me.getCmp("editPanel->checkBtn").setDisabled(true);
            me.getCmp("editPanel->checkAllBtn") && me.getCmp("editPanel->checkAllBtn").setDisabled(true);
        }
    },

    beforeBeforeEditGridCdto: function (a, b, d) {
        //非新增状态 除事业部分管采购领导 供应链部主任外  不可编辑
        if (d.record.data.state != '0' && Scdp.getActiveModule().controller.isBizVp == false && Scdp.getActiveModule().controller.isScmVp == false) {
            return false;
        }
        //主单据处于新增状态，事业部分管领导可以修改序号
        if (this.up("IView").getCmp("fadPayReqHDto->state").gotValue() == "0" && Scdp.getActiveModule().controller.isBizVp == true && d.field == "seqNo") {
            return true;
        }

        if (d.record.data.state == '1' && Scdp.getActiveModule().controller.isScmVp == false) {
            return false;
        }
        //只有创建人才能修改申请金额
        if (d.field == "reqMoney" || d.field == "purchaseManagerMoney") {
            return d.record.data.createBy == Scdp.getCurrentUserId();
        }
        return true;
    },

    fadPayReqCDtoGridRowBeforeDelete: function (b) {
        var me = this;
        var controller = me.up("IView").controller;
        var checkDelete = true;
        Ext.Array.forEach(b, function (a) {
            if (checkDelete) {
                if (controller.isScmVp) {
                    checkDelete = a.get("createBy") == Scdp.getCurrentUserId();
                } else {
                    checkDelete = a.get("createBy") == Scdp.getCurrentUserId() && a.get("state") == "0";
                }
            }
        });
        if (checkDelete == false) {
            Scdp.MsgUtil.warn("存在非新增状态的支付明细，无法删除！");
            return false;
        }
        return true;
    }
})
;