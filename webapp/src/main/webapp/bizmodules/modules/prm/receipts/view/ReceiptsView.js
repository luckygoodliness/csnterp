Ext.define('Receipts.view.ReceiptsView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/receipts',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 130,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'receipts-query-layout.xml',
    editLayoutFile: 'receipts-edit-layout.xml',
    //extraLayoutFile: 'receipts-extra-layout.xml',
    bindings: ['prmReceiptsDto', 'prmReceiptsDto.prmReceiptsScmInvoiceDto', 'prmReceiptsDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Receipt',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
        me.getCmp('editToolbar->copyAddBtn').hide();
    },
    afterInit: function () {
        var me = this;
        me.getEditToolbar().add({
            text: '凭证生成',
            cid: 'certificate',
            iconCls: 'erp-voucher',
            disabled: "true"
        });

        me.getEditToolbar().add({
            text: '凭证',
            cid: 'toCertificate',
            iconCls: 'erp-voucher',
            disabled: "true"
        });
        me.getEditToolbar().add({
            text: '关联合同',
            cid: 'cognateContract',
            iconCls: 'erp-voucher'
        });
        me.getEditToolbar().add(
            {
                text: '考核时间修正',
                cid: 'btnExamDate',
                iconCls: 'erp-examDate'
            });
        me.initQueryGrid();
        var projectNameCmp = me.getCmp("prmReceiptsDto->projectName");
        projectNameCmp.afterSotValue = function () {
            me.getCmp("prmReceiptsDto->customerId").sotValue();
            me.getCmp("prmReceiptsDto->prmContractId").sotValue();
        }

        var grid = me.getCmp("prmReceiptsScmInvoiceDto");
        grid.getCmp("toolbar->copyAddRowBtn").hide();
        me.getCmp("queryPanel->batchDelBtn").hide();
        me.getCmp("editToolbar->cognateContract").setDisabled(true);
        grid.doAddRow = function (model) {
            var projectName = me.getCmp("prmReceiptsDto->projectName").gotValue();
            if (Scdp.ObjUtil.isEmpty(projectName)) {
                Scdp.MsgUtil.info("请先选择所属项目");
                return;
            }
            //付款单位为空的情况下，应该给出提示
            var payer = me.getCmp("prmReceiptsDto->payer").gotValue();
            if (Scdp.ObjUtil.isEmpty(payer)) {
                Scdp.MsgUtil.info("请先选择付款单位");
                return;
            }
            var controller = Scdp.getActiveModule().controller;
            controller.pickContract();
        };
    },

    initQueryGrid: function () {
        var me = this;
        var grid = me.getCmp("queryPanel->resultPanel");
        //set the total row style
        Ext.Array.each(grid.columns, function (column) {
            if (column.dataIndex == 'estimateMoney' || column.dataIndex == 'actualMoney') {
                //summary type:count/sum/average/max/min
                if (Scdp.ObjUtil.isNotEmpty(column)) {
                    Ext.apply(column, {
                        summaryRenderer: function (value, summaryData, dataIndex) {
                            return '<strong style="color: red">' + Ext.util.Format.number(value, '0,0.00') + '</strong>';
                        }
                    });
                    Ext.apply(column, {
                        renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                            var estimateMoney = record.data.estimateMoney;
                            var actualMoney = 0.0;
                            if (record.data.actualMoney) {
                                actualMoney = record.data.actualMoney;
                            }
                            if (estimateMoney != actualMoney) {
                                metaData.style = "background-color: lightgreen";
                            }
                            return Ext.util.Format.number(value, '0,0.00');
                        }
                    });
                }
            } else if (column.isXType('rownumberer')) {
                Ext.apply(column, {
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return '<strong style="color: red">合计</strong>';
                    }
                });
            }
        });
    },
    rowColorConfigFn: function (record) {
        // 流程状态 已提交 内委的不需要
        if (record.data.wfStatus == '已提交' && record.data.isInternal != 1) {
            // 预计到款时间
            var estimateDate = new Date(record.data.estimateDate);
            var now = new Date();
            if (now > estimateDate) {
                return 'erp-grid-font-color-red';
            }
        }
    },
    collectMoreWorkFlowParamOnLoadAssignee: function () {
        var param = {};
        param.assignLoadUserMethod='default';
        return param;
    }
});