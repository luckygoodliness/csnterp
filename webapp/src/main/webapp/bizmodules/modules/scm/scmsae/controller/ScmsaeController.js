Ext.define("Scmsae.controller.ScmsaeController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Scmsae.view.ScmsaeView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'scmSaeDto->curYear', name: 'change', fn: 'setTitleFn'},
        {cid: 'createScmSaeForm', name: 'click', fn: 'createScmSaeForm'},
        {cid: 'createScmSaeTask', name: 'click', fn: 'createScmSaeTask'},
        {cid: 'computeSaeResult', name: 'click', fn: 'computeSaeResult'},
        {cid: 'addBySupplier', name: 'click', fn: 'addBySupplierFn'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.scmsae.dto.ScmSaeDto',
    queryAction: 'scmsae-query',
    loadAction: 'scmsae-load',
    addAction: 'scmsae-add',
    modifyAction: 'scmsae-modify',
    deleteAction: 'scmsae-delete',
    exportXlsAction: "scmsae-exportxls",
    afterInit: function () {
        var me = this;
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    pickSaeObject: function () {//选择考评对象
        var me = this;
        var view = me.view;
        var callBack = function (subView, isCancel) {
            if (isCancel) {
                return;
            }
            var grid = subView.getQueryPanel().getCmp("resultPanel");
            var selectedRecords = grid.getSelectionModel().getSelection();
            if (selectedRecords.length > 0) {
                me.addData(selectedRecords, me);
                subView.up("window").close();
            } else {
                return true;
            }
        };

        var connStr = "";
        var scmSaeObjectGrid = view.getCmp("scmSaeObjectDto");
        var count = scmSaeObjectGrid.getStore().getCount();
        if (count > 0) {
            connStr += " and not exists (select 1 from dual where ";
            for (var i = 0; i < count; i++) {
                var rowData = scmSaeObjectGrid.getStore().getAt(i).data;
                connStr = connStr + "(r.material_class_code = '" + rowData.materialCode + "' and r.supplier_id = '" + rowData.supplierId + "')";
                if (i != count - 1) {
                    connStr = connStr + " or ";
                }
            }
            connStr += ")";
        }

        var queryController = Ext.create("Scmsae.controller.ScmsaeObjectQueryController");
        var param = {
            connStr: connStr
        };
        queryController.actionParams = param;
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', '选择考评对象', null, true);
    },
    addBySupplierFn: function () {//通过供方选择考评对象
        var me = this;
        var view = me.view;
        var callBack = function (subView, isCancel) {
            if (isCancel) {
                return;
            }
            var grid = subView.getQueryPanel().getCmp("resultPanel");
            var selectedRecords = grid.getSelectionModel().getSelection();
            if (selectedRecords.length > 0) {
                var connStr = " and r.supplier_id in (";
                for (var i = 0; i < selectedRecords.length; i++) {
                    var rowData = selectedRecords[i].data;
                    if(i == 0) {
                        connStr += "'" + rowData.supplierId + "'";
                    } else if(Scdp.ObjUtil.isNotEmpty(rowData.supplierId) && connStr.indexOf(rowData.supplierId) < 0) {
                        connStr += ",'" + rowData.supplierId + "'";
                    }
                }
                connStr += ")";
                var postData = {queryConditions:{connStr:connStr},
                    layoutFile: 'scmsaeobject-query-layout.xml',
                    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmsae'};
                Scdp.doAction("pick-scmsaeobject-query", postData, function (result) {
                    if (result.success) {
                        me.addObjData(result.root, me);
                    }
                },false,false,false,null);

                subView.up("window").close();
            } else {
                return true;
            }
        };

        var connStr = "";
        var scmSaeObjectGrid = view.getCmp("scmSaeObjectDto");
        var count = scmSaeObjectGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = scmSaeObjectGrid.getStore().getAt(i).data;
                if (i == 0) {
                    connStr = "'" + rowData.supplierId + "'";
                } else if (Scdp.ObjUtil.isNotEmpty(rowData.supplierId) && connStr.indexOf(rowData.supplierId) < 0) {
                    connStr += ",'" + rowData.supplierId + "'";
                }
            }
        }

        var queryController = Ext.create("Scmsae.controller.ScmsaeSupplierQueryController");
        var param = {
            connStr: connStr
        };
        queryController.actionParams = param;
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', '选择考评供应商', null, true);
    },
    addData: function (selectedRecords, controller) {
        var me = controller;
        var view = me.view;
        var scmSaeObjectGrid = view.getCmp("scmSaeObjectDto");
        var scmSaeFormGrid = view.getCmp("scmSaeFormDto");
        Ext.Array.each(selectedRecords, function (a) {
            var rowData = a.data;
            var c = scmSaeObjectGrid.getStore(), d = Ext.ModelManager.create({}, scmSaeObjectGrid.store.model.modelName);
            if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                //供应商id
                d.set("supplierId", rowData.supplierId);
                //供应商名称
                d.set("supplierName", rowData.supplierName);
                //供应商编码
                d.set("supplierCode", rowData.supplierCode);
                //物料科目-码
                d.set("materialCode", rowData.materialClassCode);
                //物料科目-名
                d.set("materialClassName", rowData.materialClassName);
                //科目类别-名
                d.set("materialType", rowData.materialType);
                //科目类别-码
                d.set("materialTypeName", rowData.materialTypeName);
                //合同数量
                d.set("contractNum", rowData.contractNum);
                //金额
                d.set("amount", rowData.amount);
                //版块
                d.set("operativeSegments", rowData.operativeSegments);

                d.set("seqNo", Scdp.getMaxSnoInStore(c) + 1);

                scmSaeObjectGrid.store.insert(c.getCount(), d);
                scmSaeObjectGrid.getSelectionModel().select(c.getCount() - 1);
            }
        });
        //性能优化
        //scmSaeFormGrid.clearData();
        scmSaeFormGrid.getStore().removeAll();

    },
    addObjData: function (selectedRecords, controller) {
        var me = controller;
        var view = me.view;
        var scmSaeObjectGrid = view.getCmp("scmSaeObjectDto");
        var scmSaeFormGrid = view.getCmp("scmSaeFormDto");
        Ext.Array.each(selectedRecords, function (record) {
            var rowData = record;
            var c = scmSaeObjectGrid.getStore(), d = Ext.ModelManager.create({}, scmSaeObjectGrid.store.model.modelName);
            if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                //供应商id
                d.set("supplierId", rowData.supplierId);
                //供应商名称
                d.set("supplierName", rowData.supplierName);
                //供应商编码
                d.set("supplierCode", rowData.supplierCode);
                //物料科目-码
                d.set("materialCode", rowData.materialClassCode);
                //物料科目-名
                d.set("materialClassName", rowData.materialClassName);
                //科目类别-名
                d.set("materialType", rowData.materialType);
                //科目类别-码
                d.set("materialTypeName", rowData.materialTypeName);
                //合同数量
                d.set("contractNum", rowData.contractNum);
                //金额
                d.set("amount", rowData.amount);
                //版块
                d.set("operativeSegments", rowData.operativeSegments);

                d.set("seqNo", Scdp.getMaxSnoInStore(c) + 1);

                scmSaeObjectGrid.store.insert(c.getCount(), d);
                scmSaeObjectGrid.getSelectionModel().select(c.getCount() - 1);
            }
        });
        //性能优化
        //scmSaeFormGrid.clearData();
        scmSaeFormGrid.getStore().removeAll();

    },
    createScmSaeForm: function () {
        var me = this;
        var view = me.view;
        var scmSaeObjectGrid = view.getCmp("scmSaeObjectDto");
        var count = scmSaeObjectGrid.getStore().getCount();
        if (count <= 0) {
            Scdp.MsgUtil.info("待评价供方没有数据不允许生成考评人员名单！");
            return;
        }
        Scdp.MsgUtil.confirm("点击此按钮将删除原来的评价人员列表记录，是否继续？", function (e) {
            if (e == "yes") {
                var scmSaeId = view.getCmp("scmSaeDto->uuid").gotValue();
                var postData = {scmSaeId: scmSaeId};
                Scdp.doAction("scmsaeform-createscmsaeform", postData, function (result) {
                    me.loadItem(scmSaeId);
                }, false);
            }
        });
    },
    createScmSaeTask: function () {
        var me = this;
        var view = me.view;
        var scmSaeObjectGrid = view.getCmp("scmSaeObjectDto");
        var count = scmSaeObjectGrid.getStore().getCount();
        if (count <= 0) {
            Scdp.MsgUtil.info("待评价供方没有数据不允许生成考评任务！");
            return;
        }

        var callBack = function (subView) {
            if (!subView.validate()) {
                Scdp.MsgUtil.info("请输入正确的时间");
                return false;
            }
            var beginTime = subView.getCmp("beginTime").gotValue();
            var endTime = subView.getCmp("endTime").gotValue();
            if (beginTime == null || beginTime == '') {
                Scdp.MsgUtil.info("[考评时间从]字段不能为空!");
                return;
            }
            if (endTime == null || endTime == '') {
                Scdp.MsgUtil.info("[考评时间到]字段不能为空!");
                return;
            }

            if (beginTime > endTime) {
                Scdp.MsgUtil.info("[考评时间到]不能早于[考评时间从]!");
                return;
            }

            var remark = subView.getCmp("remark").gotValue();

            var scmSaeId = view.getCmp("scmSaeDto->uuid").gotValue();
            var postData = {scmSaeId: scmSaeId, beginTime: beginTime, endTime: endTime, remark: remark};
            Scdp.doAction("scmsaetask-createscmsaetask", postData, function (result) {
                me.loadItem(scmSaeId);
            }, false);

            win.close();
        };

        var form = Ext.widget("JForm", {
            height: 250,
            width: 400,
            layout: 'form',
            bodyPadding: '55 10 10 10',
            items: [
                {
                    xtype: 'JDate',
                    cid: 'beginTime',
                    allowBlank: false,
                    fieldLabel: '考评时间从'
                },
                {
                    xtype: 'JDate',
                    cid: 'endTime',
                    allowBlank: false,
                    fieldLabel: '到'
                },
                {
                    xtype: 'JTextArea',
                    cid: 'remark',
                    allowBlank: true,
                    fieldLabel: '备注'
                }
            ]
        });

        var scmSaeId = view.getCmp("scmSaeDto->uuid").gotValue();
        var postData = {scmSaeId: scmSaeId};
        Scdp.doAction("scmsaeform-checkformempty", postData, function (result) {
            if (result.success) {
                if ("FALSE" == result.message) {
                    Scdp.MsgUtil.info("评价人员列表为空,不允许生成考评任务！");
                    return;
                } else {
                    win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '生成考评任务', "确认");
                    return;
                }

            }
        });
    },
    computeSaeResult: function () {
        var me = this;
        var view = me.view;
        var scmSaeObjectGrid = view.getCmp("scmSaeObjectDto");
        var count = scmSaeObjectGrid.getStore().getCount();
        if (count <= 0) {
            Scdp.MsgUtil.info("待评价供方没有数据不允许计算考评结果！");
            return;
        }

        var callBack = function (subView) {
            var rate = subView.getCmp("rate").gotValue();
            if (rate == null || rate == '') {
                Scdp.MsgUtil.info("[年度考评占比（%）]字段不能为空!");
                return;
            }
            var scmSaeId = view.getCmp("scmSaeDto->uuid").gotValue();
            var postData = {scmSaeId: scmSaeId, rate: rate};
            Scdp.doAction("scmsaetask-computesaeresult", postData, function (result) {
                me.loadItem(scmSaeId);
            }, false);

            win.close();
        };

        var form = Ext.widget("JForm", {
            height: 150,
            width: 300,
            layout: 'form',
            bodyPadding: '55 10 10 10',
            items: [
                {
                    xtype: 'JDec',
                    cid: 'rate',
                    allowBlank: false,
                    fieldLabel: '年度考评占比（%）'
                }
            ]
        });

        var scmSaeId = view.getCmp("scmSaeDto->uuid").gotValue();
        var postData = {scmSaeId: scmSaeId};
        Scdp.doAction("scmsaeform-checkformempty", postData, function (result) {
            if (result.success) {
                if ("FALSE" == result.message) {
                    Scdp.MsgUtil.info("评价人员列表为空,不允许计算考评结果！");
                    return;
                } else {
                    win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '计算考评结果', "确认");
                    return;
                }

            }
        });

    },
    setTitleFn: function () {
        var me = this;
        var view = me.view;
        var curYear = view.getCmp("scmSaeDto->curYear").gotValue();
        var titleCmp = view.getCmp("scmSaeDto->title");
        titleCmp.sotValue(curYear + "年度合格供方考评");
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("editPanel->createScmSaeForm").setDisabled(true);
        view.getCmp("editPanel->createScmSaeTask").setDisabled(true);
        view.getCmp("editPanel->computeSaeResult").setDisabled(true);
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
        var view = me.view;
        view.getCmp("editPanel->createScmSaeForm").setDisabled(true);
        view.getCmp("editPanel->createScmSaeTask").setDisabled(true);
        view.getCmp("editPanel->computeSaeResult").setDisabled(true);
    },
    beforeSave: function () {
        var me = this;
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        var view = me.view;
        view.getCmp("editPanel->createScmSaeForm").setDisabled(false);
        view.getCmp("editPanel->createScmSaeTask").setDisabled(false);
        view.getCmp("editPanel->computeSaeResult").setDisabled(false);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        var view = me.view;
        var scmSaeId = view.getCmp("scmSaeDto->uuid").gotValue();
        if (Scdp.ObjUtil.isEmpty(scmSaeId)) {
            view.getCmp("editPanel->createScmSaeForm").setDisabled(true);
            view.getCmp("editPanel->createScmSaeTask").setDisabled(true);
            view.getCmp("editPanel->computeSaeResult").setDisabled(true);
        } else {
            view.getCmp("editPanel->createScmSaeForm").setDisabled(false);
            view.getCmp("editPanel->createScmSaeTask").setDisabled(false);
            view.getCmp("editPanel->computeSaeResult").setDisabled(false);
        }
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        var view = me.view;
        var scmSaeId = view.getCmp("scmSaeDto->uuid").gotValue();
        if (Scdp.ObjUtil.isEmpty(scmSaeId)) {
            view.getCmp("editPanel->createScmSaeForm").setDisabled(true);
            view.getCmp("editPanel->createScmSaeTask").setDisabled(true);
            view.getCmp("editPanel->computeSaeResult").setDisabled(true);
        } else {
            view.getCmp("editPanel->createScmSaeForm").setDisabled(false);
            view.getCmp("editPanel->createScmSaeTask").setDisabled(false);
            view.getCmp("editPanel->computeSaeResult").setDisabled(false);
        }
    },
    beforeDelete: function () {
        var me = this;
        var view = me.view;
        var scmSaeObjectGrid = view.getCmp("scmSaeObjectDto");
        var count = scmSaeObjectGrid.getStore().getCount();
        if (count > 0) {
            Scdp.MsgUtil.info("存在子表记录,不允许删除！");
            return false;
        }
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