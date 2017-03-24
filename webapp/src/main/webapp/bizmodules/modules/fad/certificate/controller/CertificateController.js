Ext.define("Certificate.controller.CertificateController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Certificate.view.CertificateView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'years', name: 'change', fn: 'yearsChange'}
        ,
        {cid: 'months', name: 'change', fn: 'monthsChange'}
        ,
        {cid: 'fadCertificateDto->originalFormType', name: 'change', fn: 'originalFormTypeChange'}
        ,
        {cid: 'fadCertificateDto->receiverOrPayerName', name: 'change', fn: 'receiverOrPayerNameChange'}
        ,
        // M7_C1_F1_关联收款
        {cid: 'fadCertificateDto->state', name: 'change', fn: 'changeState'}
        ,
        {cid: 'fadCertificateDto->receiverOrPayerName', name: 'blur', fn: 'receiverOrPayerNameChange'}
        /*,
         {cid: 'certificateDatefieldFrom', name: 'change', fn: 'certificateDatefieldFromChange'}*/
        ,
        {cid: 'certificateDatefieldTo', name: 'change', fn: 'certificateDatefieldToChange'}
        ,
        {cid: 'sendNC', name: 'click', fn: 'sendNC'}
        ,
        {cid: 'toLogicVoid', name: 'click', fn: 'toLogicVoid'}
        ,
        {cid: 'toLogicVoidAlone', name: 'click', fn: 'toLogicVoidAlone'}
        ,
        {cid: 'originalToCertificate', name: 'click', fn: 'originalToCertificate'}
        ,
        {cid: 'certificateMerge', name: 'click', fn: 'certificateMerge'}
        ,
        {cid: 'certificateSplit', name: 'click', fn: 'certificateSplit'}
        ,
        {cid: 'certificateMergeRestore', name: 'click', fn: 'certificateMergeRestore'}
        ,
        {cid: 'certificateSplitRestore', name: 'click', fn: 'certificateSplitRestore'}
        ,
        {cid: 'certificateDeficit', name: 'click', fn: 'certificateDeficit'}
        ,
        {cid: 'batchSendNC', name: 'click', fn: 'batchSendNC'}
        ,
        {cid: 'toBusiness', name: 'click', fn: 'toBusiness'}
        ,
        {cid: 'nCCodeSet', name: 'click', fn: 'nCCodeSet'}
        ,
        //M7_C1_F1_关联收款
        {cid: 'Collection', name: 'click', fn: 'Collection'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.certificate.dto.FadCertificateDto',
    queryAction: 'certificate-query',
    loadAction: 'certificate-load',
    addAction: 'certificate-add',
    modifyAction: 'certificate-modify',
    voidAction: 'certificate-delete',
    exportXlsAction: "certificate-exportxls",
    afterInit: function () {
        var me = this;
        oldSubjectCode = "";
        oldFadCertificateDetailFree2 = "";
        oldFadCertificateDetailFree3 = "";
        oldOrderNo = "";
        me.view.getCmp("state").sotValue("0");
        me.view.getCmp("years").sotValue(fadCertificateYears);
        me.view.getCmp("months").sotValue(fadCertificateMonths);
        me.view.getCmp("state").resetOriginalValue();
        me.view.getCmp("years").resetOriginalValue();
        me.view.getCmp("months").resetOriginalValue();

        //控件数据过滤(只显示计财部人员)
        me.view.getCmp("makerName").filterConditions = {orgfilter: " org_code='CSNT_JCB' "};

        //me.view.getCmp('editToolbar->cancelBtn').hide();
        //me.view.getCmp('editToolbar->addNew2Btn').hide();
        //me.view.getCmp('editToolbar->copyAddBtn').hide();
        me.view.getCmp('editToolbar->deleteBtn').hide();
        //me.view.getCmp('addNew1Btn').hide();
        me.view.getCmp('batchDelBtn').hide();
        me.view.getCmp("fadCertificateAccountDto").getCmp("toolbar->addRowBtn").hide();
        me.view.getCmp("fadCertificateAccountDto").getCmp("toolbar->delRowBtn").hide();
        me.view.getCmp("fadCertificateAccountDto").getCmp("toolbar->copyAddRowBtn").hide();

        var fadCertificateDetailGrid = me.view.getCmp("fadCertificateDetailDto");
        fadCertificateDetailGrid.afterAddRow = function () {
            var fadCertificateDetailGridRowDataCur = fadCertificateDetailGrid.getCurRecord();
            if (fadCertificateDetailGridRowDataCur == null) {
                return;
            }
            fadCertificateDetailGridRowDataCur.set("orderNo", fadCertificateDetailGrid.getStore().getCount());
            /*fadCertificateDetailGridRowDataCur.set("uuid", fadCertificateDetailGrid.getStore().getCount());

             var fadCertificateAccountGrid = me.view.getCmp("fadCertificateAccountDto");
             for (var i = 0; i < fadCertificateAccountGrid.getStore().getCount(); i++) {
             var fadCertificateAccountGridRowData = fadCertificateAccountGrid.getStore().data.items[i];
             if (!(Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("uuid")).length > 0)) {
             fadCertificateAccountGridRowData.set("fadCertificateDetailId", fadCertificateDetailGrid.getStore().getCount());
             fadCertificateAccountGridRowData.set("uuid", fadCertificateDetailGrid.getStore().getCount());
             }
             }*/

            fadCertificateDetailGridRowDataCur.set("currency", "CNY");
            fadCertificateDetailGridRowDataCur.set("currtypename", "人民币元");
            fadCertificateDetailGridRowDataCur.set("primary", 0.00);
            fadCertificateDetailGridRowDataCur.set("local", 0.00);
            fadCertificateDetailGridRowDataCur.set("free2", 0.00);
            fadCertificateDetailGridRowDataCur.set("free3", 0.00);
            //fadCertificateDetailGridRowDataCur.set("abstracts", Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->abstracts").gotValue()));
            //fadCertificateDetailGridRowDataCur.set("originalFormCode", Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormCode").gotValue()));
            fadCertificateDetailGridRowDataCur.set("originalFormType", Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()));
            oldSubjectCode = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("subjectCode"));
            oldFadCertificateDetailFree2 = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2"));
            oldFadCertificateDetailFree3 = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3"));
            oldOrderNo = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("orderNo"));
        };
        fadCertificateDetailGrid.afterCopyAddRow = function () {
            var fadCertificateDetailGridRowDataCur = fadCertificateDetailGrid.getCurRecord();
            if (fadCertificateDetailGridRowDataCur == null) {
                return;
            }
            fadCertificateDetailGridRowDataCur.set("orderNo", fadCertificateDetailGrid.getStore().getCount());
            /*fadCertificateDetailGridRowDataCur.set("uuid", fadCertificateDetailGrid.getStore().getCount());

             var fadCertificateAccountGrid = me.view.getCmp("fadCertificateAccountDto");
             for (var i = 0; i < fadCertificateAccountGrid.getStore().getCount(); i++) {
             var fadCertificateAccountGridRowData = fadCertificateAccountGrid.getStore().data.items[i];
             if (!(Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("uuid")).length > 0)) {
             fadCertificateAccountGridRowData.set("fadCertificateDetailId", fadCertificateDetailGrid.getStore().getCount());
             fadCertificateAccountGridRowData.set("uuid", fadCertificateDetailGrid.getStore().getCount());
             }
             }*/

            oldSubjectCode = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("subjectCode"));
            oldFadCertificateDetailFree2 = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2"));
            oldFadCertificateDetailFree3 = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3"));
            oldOrderNo = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("orderNo"));
        };
        fadCertificateDetailGrid.afterDeleteRow = function () {
            for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
                var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
                fadCertificateDetailGridRowData.set("orderNo", i + 1);
                oldSubjectCode = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("subjectCode"));
                oldFadCertificateDetailFree2 = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free2"));
                oldFadCertificateDetailFree3 = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free3"));
                oldOrderNo = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("orderNo"));
            }
        };
        fadCertificateDetailGrid.on('select', function (grid, record, index) {
            var fadCertificateDetailGridRowDataCur = fadCertificateDetailGrid.getCurRecord();
            if (fadCertificateDetailGridRowDataCur == null) {
                return;
            }

            //bdCurrtypeChoiceCurrencyCode = Erp.Util.isNullReturnEmpty(record.data.currency);
            bdCurrtypeChoiceCurrencyCode = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("currency"));
            Erp.Util.SetCookie("bdCurrtypeChoiceCurrencyCode", bdCurrtypeChoiceCurrencyCode);

            //accsubjRtfreevalueSubjectCode = Erp.Util.isNullReturnEmpty(record.data.subjectCode);
            accsubjRtfreevalueSubjectCode = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("subjectCode"));
            Erp.Util.SetCookie("accsubjRtfreevalueSubjectCode", accsubjRtfreevalueSubjectCode);

            //accsubjRtfreevalueSubjectName = Erp.Util.isNullReturnEmpty(record.data.subjectName);
            accsubjRtfreevalueSubjectName = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("subjectName"));
            Erp.Util.SetCookie("accsubjRtfreevalueSubjectName", accsubjRtfreevalueSubjectName);

            //oldSubjectCode = Erp.Util.isNullReturnEmpty(record.data.subjectCode);
            //oldOrderNo = Erp.Util.isNullReturnEmpty(record.data.orderNo);
            oldSubjectCode = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("subjectCode"));
            oldFadCertificateDetailFree2 = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2"));
            oldFadCertificateDetailFree3 = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3"));
            oldOrderNo = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("orderNo"));
        });
        fadCertificateDetailGrid.store.on('update', function (grid, record, operation) {
            var fadCertificateDetailGridRowDataCur = fadCertificateDetailGrid.getCurRecord();
            if (fadCertificateDetailGridRowDataCur == null) {
                return;
            }

            //bdCurrtypeChoiceCurrencyCode = Erp.Util.isNullReturnEmpty(record.data.currency);
            bdCurrtypeChoiceCurrencyCode = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("currency"));
            Erp.Util.SetCookie("bdCurrtypeChoiceCurrencyCode", bdCurrtypeChoiceCurrencyCode);

            //accsubjRtfreevalueSubjectCode = Erp.Util.isNullReturnEmpty(record.data.subjectCode);
            accsubjRtfreevalueSubjectCode = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("subjectCode"));
            Erp.Util.SetCookie("accsubjRtfreevalueSubjectCode", accsubjRtfreevalueSubjectCode);

            //accsubjRtfreevalueSubjectName = Erp.Util.isNullReturnEmpty(record.data.subjectName);
            accsubjRtfreevalueSubjectName = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("subjectName"));
            Erp.Util.SetCookie("accsubjRtfreevalueSubjectName", accsubjRtfreevalueSubjectName);

            if (operation == "edit") {
                //var newSubjectCode = Erp.Util.isNullReturnEmpty(record.data.subjectCode);
                //var newOrderNo = Erp.Util.isNullReturnEmpty(record.data.orderNo);
                var fadCertificateUuid = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->uuid").gotValue());
                var fadCertificateDetailUuid = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("uuid"));
                var newSubjectCode = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("subjectCode"));
                var newFadCertificateDetailFree2 = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2"));
                var newFadCertificateDetailFree3 = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3"));
                var newOrderNo = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("orderNo"));
                var receiverOrPayerType = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->receiverOrPayerType").gotValue());
                var receiverOrPayerId = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->receiverOrPayerId").gotValue());
                var receiverOrPayerCode = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->receiverOrPayerCode").gotValue());
                var receiverOrPayerName = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->receiverOrPayerName").gotValue());
                var businessId = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->businessId").gotValue());
                var originalFormCode = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormCode").gotValue());
                var originalFormType = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue());

                if (Erp.Util.isNullReturnEmpty(oldOrderNo) == Erp.Util.isNullReturnEmpty(newOrderNo)) {
                    /*if (
                     (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(oldFadCertificateDetailFree2))) != parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(newFadCertificateDetailFree2))))
                     &&
                     (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(newFadCertificateDetailFree2))) != 0)
                     &&
                     (Erp.Util.isNullReturnEmpty(oldOrderNo) == Erp.Util.isNullReturnEmpty(newOrderNo))
                     ) {
                     fadCertificateDetailGridRowDataCur.set("free3", 0.00);
                     }
                     else if (
                     (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(oldFadCertificateDetailFree3))) != parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(newFadCertificateDetailFree3))))
                     &&
                     (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(newFadCertificateDetailFree3))) != 0)
                     &&
                     (Erp.Util.isNullReturnEmpty(oldOrderNo) == Erp.Util.isNullReturnEmpty(newOrderNo))
                     ) {
                     fadCertificateDetailGridRowDataCur.set("free2", 0.00);
                     }*/

                    if (
                        (Erp.Util.isNullReturnEmpty(newSubjectCode).length > 0)
                        && (Erp.Util.isNullReturnEmpty(oldSubjectCode) != Erp.Util.isNullReturnEmpty(newSubjectCode))
                        && (Erp.Util.isNullReturnEmpty(oldOrderNo) == Erp.Util.isNullReturnEmpty(newOrderNo))
                    ) {
                        var postData = {
                            fadCertificateUuid: fadCertificateUuid,
                            fadCertificateDetailUuid: fadCertificateDetailUuid,
                            subjectCode: newSubjectCode,
                            receiverOrPayerType: receiverOrPayerType,
                            receiverOrPayerId: receiverOrPayerId,
                            receiverOrPayerCode: receiverOrPayerCode,
                            receiverOrPayerName: receiverOrPayerName,
                            businessId: businessId,
                            originalFormCode: originalFormCode,
                            originalFormType: originalFormType
                        };
                        //me.view.getCmp('editToolbar->cancelBtn').setDisabled(true);
                        //Scdp.MsgUtil.info("科目已更新!");
                        Scdp.doAction("certificate-accsubjRtfreevalueQuery", postData, function (result) {
                            var fadCertificateAccountGrid = me.view.getCmp("fadCertificateAccountDto");

                            //清空分录辅助核算
                            //fadCertificateAccountGrid.clearData();
                            //fadCertificateAccountGrid.removeAll();
                            //fadCertificateAccountGrid.store.removeAll();
                            var fadCertificateAccountGridStoreCount = fadCertificateAccountGrid.getStore().getCount();
                            for (var i = 0; i < fadCertificateAccountGridStoreCount; i++) {
                                var fadCertificateAccountGridRowData = fadCertificateAccountGrid.getStore().data.items[i];
                                //if (Erp.Util.isNullReturnEmpty(record.data.uuid) == Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("fadCertificateDetailId"))) {
                                //if (fadCertificateDetailUuid == Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("fadCertificateDetailId"))) {
                                fadCertificateAccountGridStoreCount = fadCertificateAccountGridStoreCount - 1;
                                i = -1;
                                fadCertificateAccountGrid.store.remove(fadCertificateAccountGridRowData);
                                //}
                            }

                            if (fadCertificateAccountGrid.getStore().getCount() == 0) {
                                var fadAccsubjRtfreevalueList = result.fadAccsubjRtfreevalueList;
                                for (var i = 0; i < fadAccsubjRtfreevalueList.length; i++) {
                                    //var fadCertificateAccountGridDataItem = Ext.ModelManager.create({}, fadCertificateAccountGrid.store.model.modelName);
                                    //fadCertificateAccountGrid.store.insert(i + 1, fadCertificateAccountGridDataItem);

                                    if (Erp.Util.isNullReturnEmpty(fadAccsubjRtfreevalueList[i].accountNo).length > 0) {
                                        fadCertificateAccountGrid.addRowItem(null);
                                        var fadCertificateAccountGridRowData = fadCertificateAccountGrid.getStore().data.items[i];
                                        //fadCertificateAccountGridRowData.set("fadCertificateDetailId", record.data.uuid);
                                        fadCertificateAccountGridRowData.set("fadCertificateDetailId", fadCertificateDetailUuid);
                                        fadCertificateAccountGridRowData.set("accountNo", fadAccsubjRtfreevalueList[i].accountNo);
                                        fadCertificateAccountGridRowData.set("accountType", fadAccsubjRtfreevalueList[i].accountType);
                                        fadCertificateAccountGridRowData.set("accountInfoId", fadAccsubjRtfreevalueList[i].accountInfoId);
                                        fadCertificateAccountGridRowData.set("accountInfoCode", fadAccsubjRtfreevalueList[i].accountInfoCode);
                                        fadCertificateAccountGridRowData.set("accountInfoName", fadAccsubjRtfreevalueList[i].accountInfoName);
                                    }
                                }
                            }
                        });
                    }
                    else if (
                        (Erp.Util.isNullReturnEmpty(oldSubjectCode) != Erp.Util.isNullReturnEmpty(newSubjectCode))
                        && (Erp.Util.isNullReturnEmpty(oldOrderNo) == Erp.Util.isNullReturnEmpty(newOrderNo))
                    ) {
                        try {
                            var fadCertificateAccountGrid = me.view.getCmp("fadCertificateAccountDto");
                            var fadCertificateAccountGridStoreCount = fadCertificateAccountGrid.getStore().getCount();
                            for (var i = 0; i < fadCertificateAccountGridStoreCount; i++) {
                                var fadCertificateAccountGridRowData = fadCertificateAccountGrid.getStore().data.items[i];
                                //if (Erp.Util.isNullReturnEmpty(record.data.uuid) == Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("fadCertificateDetailId"))) {
                                //if (fadCertificateDetailUuid == Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("fadCertificateDetailId"))) {
                                fadCertificateAccountGridStoreCount = fadCertificateAccountGridStoreCount - 1;
                                i = -1;
                                fadCertificateAccountGrid.store.remove(fadCertificateAccountGridRowData);
                                //}
                            }
                        }
                        catch (e) {
                        }
                    }
                    oldSubjectCode = newSubjectCode;
                    oldFadCertificateDetailFree2 = newFadCertificateDetailFree2;
                    oldFadCertificateDetailFree3 = newFadCertificateDetailFree3;
                    oldOrderNo = newOrderNo;
                }
            }
        });

        var free2column = fadCertificateDetailGrid.getColumnBydataIndex("free2");
        var free2editor = free2column.getEditor();
        free2editor.enableKeyEvents = true;
        free2editor.on("keypress", function (obj, event) {
            var me = this;
            if ((event.getKey() == 61)) {
                if (me.isEditable()) {
                    var fadCertificateDetailGridRowDataCur = fadCertificateDetailGrid.getCurRecord();
                    if (fadCertificateDetailGridRowDataCur == null) {
                        return;
                    }

                    var debtor = 0;
                    var creditor = 0;
                    for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
                        var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
                        if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("orderNo")) == Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("orderNo"))) {

                        }
                        else if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "借" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "0") {
                            debtor = parseFloat((parseFloat(debtor) + parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local"))))).toFixed(2));
                        }
                        else if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "贷" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "1") {
                            creditor = parseFloat((parseFloat(creditor) + parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local"))))).toFixed(2));
                        }
                    }

                    if ((creditor - debtor) <= 0) {
                        return;
                    }

                    fadCertificateDetailGridRowDataCur.set("free2", creditor - debtor);
                    fadCertificateDetailGridRowDataCur.set("free3", 0.00);
                    fadCertificateDetailGridRowDataCur.set("local", creditor - debtor);
                    fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "0");

                    if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("primary")).length > 0)) {
                        fadCertificateDetailGridRowDataCur.set("primary", 0.00);
                    }
                    if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("local")).length > 0)) {
                        fadCertificateDetailGridRowDataCur.set("local", 0.00);
                    }
                    if ((!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2")).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2")))) == 0)) {
                        fadCertificateDetailGridRowDataCur.set("free2", 0.00);
                        if ((!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")))) == 0)) {
                            fadCertificateDetailGridRowDataCur.set("free3", 0.00);
                            fadCertificateDetailGridRowDataCur.set("local", 0.00);
                            fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "");
                        }
                        else {
                            fadCertificateDetailGridRowDataCur.set("local", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")));
                            fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "1");
                        }
                    }
                    else {
                        fadCertificateDetailGridRowDataCur.set("free3", 0.00);
                        fadCertificateDetailGridRowDataCur.set("local", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2")));
                        fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "0");
                    }
                    //obj.value = creditor - debtor;
                    //obj.setRawValue(obj.value);
                    free2Keypress = "1";
                }
            }
        });
        free2editor.on("blur", function () {
            var me = this;
            var fadCertificateDetailGridRowDataCur = fadCertificateDetailGrid.getCurRecord();
            if (fadCertificateDetailGridRowDataCur == null) {
                return;
            }

            if (free2Keypress == "0") {
                if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("primary")).length > 0)) {
                    fadCertificateDetailGridRowDataCur.set("primary", 0.00);
                }
                if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("local")).length > 0)) {
                    fadCertificateDetailGridRowDataCur.set("local", 0.00);
                }
                if ((!(Erp.Util.isNullReturnEmpty(me.value).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.value))) == 0)) {
                    fadCertificateDetailGridRowDataCur.set("free2", 0.00);
                    if ((!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")))) == 0)) {
                        fadCertificateDetailGridRowDataCur.set("free3", 0.00);
                        fadCertificateDetailGridRowDataCur.set("local", 0.00);
                        fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "");
                    }
                    else {
                        fadCertificateDetailGridRowDataCur.set("local", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")));
                        fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "1");
                    }
                }
                else {
                    fadCertificateDetailGridRowDataCur.set("free3", 0.00);
                    fadCertificateDetailGridRowDataCur.set("local", Erp.Util.isNullReturnEmpty(me.value));
                    fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "0");
                }
            }
            else {
                /*fadCertificateDetailGridRowDataCur.set("free2", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("local")));
                 me.value = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("local"));
                 Scdp.MsgUtil.info("借方金额已计算!" + me.value);*/
                free2Keypress = "0";
            }
        });

        var free3column = fadCertificateDetailGrid.getColumnBydataIndex("free3");
        var free3editor = free3column.getEditor();
        free3editor.enableKeyEvents = true;
        free3editor.on("keypress", function (obj, event) {
            var me = this;
            if ((event.getKey() == 61)) {
                if (me.isEditable()) {
                    var fadCertificateDetailGridRowDataCur = fadCertificateDetailGrid.getCurRecord();
                    if (fadCertificateDetailGridRowDataCur == null) {
                        return;
                    }

                    var debtor = 0;
                    var creditor = 0;
                    for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
                        var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
                        if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("orderNo")) == Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("orderNo"))) {

                        }
                        else if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "借" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "0") {
                            debtor = parseFloat((parseFloat(debtor) + parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local"))))).toFixed(2));
                        }
                        else if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "贷" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "1") {
                            creditor = parseFloat((parseFloat(creditor) + parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local"))))).toFixed(2));
                        }
                    }

                    if ((debtor - creditor) <= 0) {
                        return;
                    }

                    fadCertificateDetailGridRowDataCur.set("free2", 0.00);
                    fadCertificateDetailGridRowDataCur.set("free3", debtor - creditor);
                    fadCertificateDetailGridRowDataCur.set("local", debtor - creditor);
                    fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "1");

                    if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("primary")).length > 0)) {
                        fadCertificateDetailGridRowDataCur.set("primary", 0.00);
                    }
                    if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("local")).length > 0)) {
                        fadCertificateDetailGridRowDataCur.set("local", 0.00);
                    }
                    if ((!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2")).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2")))) == 0)) {
                        fadCertificateDetailGridRowDataCur.set("free2", 0.00);
                        if ((!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")))) == 0)) {
                            fadCertificateDetailGridRowDataCur.set("free3", 0.00);
                            fadCertificateDetailGridRowDataCur.set("local", 0.00);
                            fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "");
                        }
                        else {
                            fadCertificateDetailGridRowDataCur.set("local", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")));
                            fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "1");
                        }
                    }
                    else {
                        if ((!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")))) == 0)) {
                            fadCertificateDetailGridRowDataCur.set("free3", 0.00);
                            fadCertificateDetailGridRowDataCur.set("local", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2")));
                            fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "0");
                        }
                        else {
                            fadCertificateDetailGridRowDataCur.set("free2", 0.00);
                            fadCertificateDetailGridRowDataCur.set("local", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free3")));
                            fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "1");
                        }
                    }
                    //obj.value = debtor - creditor;
                    //obj.setRawValue(obj.value);
                    free3Keypress = "1";
                }
            }
        });
        free3editor.on("blur", function () {
            var me = this;
            var fadCertificateDetailGridRowDataCur = fadCertificateDetailGrid.getCurRecord();
            if (fadCertificateDetailGridRowDataCur == null) {
                return;
            }

            if (free3Keypress == "0") {
                if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("primary")).length > 0)) {
                    fadCertificateDetailGridRowDataCur.set("primary", 0.00);
                }
                if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("local")).length > 0)) {
                    fadCertificateDetailGridRowDataCur.set("local", 0.00);
                }
                if ((!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2")).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2")))) == 0)) {
                    fadCertificateDetailGridRowDataCur.set("free2", 0.00);
                    if ((!(Erp.Util.isNullReturnEmpty(me.value).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.value))) == 0)) {
                        fadCertificateDetailGridRowDataCur.set("free3", 0.00);
                        fadCertificateDetailGridRowDataCur.set("local", 0.00);
                        fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "");
                    }
                    else {
                        fadCertificateDetailGridRowDataCur.set("local", Erp.Util.isNullReturnEmpty(me.value));
                        fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "1");
                    }
                }
                else {
                    if ((!(Erp.Util.isNullReturnEmpty(me.value).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.value))) == 0)) {
                        fadCertificateDetailGridRowDataCur.set("free3", 0.00);
                        fadCertificateDetailGridRowDataCur.set("local", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("free2")));
                        fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "0");
                    }
                    else {
                        fadCertificateDetailGridRowDataCur.set("free2", 0.00);
                        fadCertificateDetailGridRowDataCur.set("local", Erp.Util.isNullReturnEmpty(me.value));
                        fadCertificateDetailGridRowDataCur.set("debtorOrCreditor", "1");
                    }
                }
            }
            else {
                /*fadCertificateDetailGridRowDataCur.set("free3", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("local")));
                 me.value = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowDataCur.get("local"));
                 Scdp.MsgUtil.info("贷方金额已计算!" + me.value);*/
                free3Keypress = "0";
            }
        });

        var fadCertificateAccountGrid = me.view.getCmp("fadCertificateAccountDto");
        fadCertificateAccountGrid.on('select', function (grid, record, index) {
            var fadCertificateAccountGridRowDataCur = fadCertificateAccountGrid.getCurRecord();
            if (fadCertificateAccountGridRowDataCur == null) {
                return;
            }

            //rtfreevalueAccountInfoCode = Erp.Util.isNullReturnEmpty(record.data.accountInfoCode);
            rtfreevalueAccountInfoCode = Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowDataCur.get("accountInfoCode"));
            Erp.Util.SetCookie("rtfreevalueAccountInfoCode", rtfreevalueAccountInfoCode);

            //rtfreevalueAccountInfoName = Erp.Util.isNullReturnEmpty(record.data.accountInfoName);
            rtfreevalueAccountInfoName = Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowDataCur.get("accountInfoName"));
            Erp.Util.SetCookie("rtfreevalueAccountInfoName", rtfreevalueAccountInfoName);

            //rtfreevalueAccountNo = Erp.Util.isNullReturnEmpty(record.data.accountNo);
            rtfreevalueAccountNo = Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowDataCur.get("accountNo"));
            Erp.Util.SetCookie("rtfreevalueAccountNo", rtfreevalueAccountNo);
        });
        fadCertificateAccountGrid.store.on('update', function (grid, record, operation) {
            var fadCertificateAccountGridRowDataCur = fadCertificateAccountGrid.getCurRecord();
            if (fadCertificateAccountGridRowDataCur == null) {
                return;
            }

            //rtfreevalueAccountInfoCode = Erp.Util.isNullReturnEmpty(record.data.accountInfoCode);
            rtfreevalueAccountInfoCode = Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowDataCur.get("accountInfoCode"));
            Erp.Util.SetCookie("rtfreevalueAccountInfoCode", rtfreevalueAccountInfoCode);

            //rtfreevalueAccountInfoName = Erp.Util.isNullReturnEmpty(record.data.accountInfoName);
            rtfreevalueAccountInfoName = Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowDataCur.get("accountInfoName"));
            Erp.Util.SetCookie("rtfreevalueAccountInfoName", rtfreevalueAccountInfoName);

            //rtfreevalueAccountNo = Erp.Util.isNullReturnEmpty(record.data.accountNo);
            rtfreevalueAccountNo = Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowDataCur.get("accountNo"));
            Erp.Util.SetCookie("rtfreevalueAccountNo", rtfreevalueAccountNo);
        });

        if (me.view.getCmp("editToolbar->nCCodeSet") != undefined) {
            if (Scdp.ObjUtil.isNotEmpty(me.actionParams)) {
                if (Scdp.ObjUtil.isNotEmpty(me.actionParams.fadCertificateUuid)) {
                    me.loadItem(me.actionParams.fadCertificateUuid);
                    me.afterLoadItem();
                }
            }
        }
    },
    beforeAdd: function () {
        var me = this;
        oldSubjectCode = "";
        oldFadCertificateDetailFree2 = "";
        oldFadCertificateDetailFree3 = "";
        oldOrderNo = "";
        return true;
    },
    afterAdd: function () {
        var me = this;

        setReceiverOrPayer(me);

        me.view.getCmp("fadCertificateDto->state").sotValue("0");
        me.view.getCmp("fadCertificateDto->years").sotValue(fadCertificateYears);
        me.view.getCmp("fadCertificateDto->months").sotValue(fadCertificateMonths);
        me.view.getCmp("editToolbar->sendNC").setDisabled(true);
        me.view.getCmp("editToolbar->toLogicVoid").setDisabled(true);
        me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(true);
        setToBusinessBtnState(me);
        //me.view.getCmp("fadCertificateDto->originalFormType").setReadOnly(true);
        ncState = "0";
        deficitCertifiState = "1";
        copyAddState = "0";
        mergeSplitState = "0";

        var fadCertificateDetailGrid = me.view.getCmp("fadCertificateDetailDto");
        var abstracts = "";
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts")).length > 0) {
                abstracts = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts"));
                break;
            }
        }
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts")).length > 0)) {
                //fadCertificateDetailGridRowData.set("abstracts", abstracts);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("primary")).length > 0)) {
                fadCertificateDetailGridRowData.set("primary", 0.00);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")).length > 0)) {
                fadCertificateDetailGridRowData.set("local", 0.00);
            }
            else {
                if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "借" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "0") {
                    fadCertificateDetailGridRowData.set("free2", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")));
                }
                if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "贷" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "1") {
                    fadCertificateDetailGridRowData.set("free3", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")));
                }
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free2")).length > 0)) {
                fadCertificateDetailGridRowData.set("free2", 0.00);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free3")).length > 0)) {
                fadCertificateDetailGridRowData.set("free3", 0.00);
            }
        }
    },
    beforeCopyAdd: function () {
        var me = this;
        if (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2") {
            Scdp.MsgUtil.warn("【作废凭证】不能复制新增");
            return false;
        }
        else if (deficitCertifiState == "0") {
            Scdp.MsgUtil.warn("【红冲凭证】不能复制新增");
            return false;
        }
        else if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) < 0) {
            Scdp.MsgUtil.warn("【赤字凭证】不能复制新增");
            return false;
        }
        else if (deficitCertifiState != "-1") {
            Scdp.MsgUtil.warn("【未红冲凭证】不能复制新增");
            return false;
        }
        else {
            oldSubjectCode = "";
            oldFadCertificateDetailFree2 = "";
            oldFadCertificateDetailFree3 = "";
            oldOrderNo = "";
            //me.view.getCmp("fadCertificateDto->free2").sotValue(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->uuid").gotValue()));
            //me.view.getCmp("fadCertificateDto->free3").sotValue(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->tblVersion").gotValue()));
            fadCertificateFree2 = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->uuid").gotValue());
            fadCertificateFree3 = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->tblVersion").gotValue());
            return true;
        }
    },
    afterCopyAdd: function () {
        var me = this;
        me.view.getCmp("editToolbar->modifyBtn").setDisabled(true);
        me.view.getCmp("fadCertificateDto->state").sotValue("0");
        //me.view.getCmp("fadCertificateDto->years").sotValue(fadCertificateYears);
        //me.view.getCmp("fadCertificateDto->months").sotValue(fadCertificateMonths);
        me.view.getCmp("fadCertificateDto->free2").sotValue(fadCertificateFree2);
        me.view.getCmp("fadCertificateDto->free3").sotValue(fadCertificateFree3);
        me.view.getCmp("editToolbar->sendNC").setDisabled(true);
        me.view.getCmp("editToolbar->toLogicVoid").setDisabled(true);
        me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(true);
        setToBusinessBtnState(me);
        me.view.getCmp("fadCertificateDto->originalFormType").setReadOnly(true);
        ncState = "0";
        deficitCertifiState = "1";
        copyAddState = "0";
        mergeSplitState = "0";

        me.view.getCmp("fadCertificateDto->certificateNo").sotValue("");
        me.view.getCmp("fadCertificateDto->feedback").sotValue("");
        me.view.getCmp("fadCertificateDto->makeDate").sotValue("");
        me.view.getCmp("fadCertificateDto->makerName").sotValue("");
        me.view.getCmp("fadCertificateDto->maker").sotValue("");
        me.view.getCmp("fadCertificateDto->certificateDate").sotValue("");
        me.view.getCmp("fadCertificateDto->ncrequestUrl").sotValue("");
        me.view.getCmp("fadCertificateDto->ncrequestXml").sotValue("");
        me.view.getCmp("fadCertificateDto->ncresponseXml").sotValue("");

        var fadCertificateDetailGrid = me.view.getCmp("fadCertificateDetailDto");
        var abstracts = "";
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts")).length > 0) {
                abstracts = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts"));
                break;
            }
        }
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts")).length > 0)) {
                //fadCertificateDetailGridRowData.set("abstracts", abstracts);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("primary")).length > 0)) {
                fadCertificateDetailGridRowData.set("primary", 0.00);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")).length > 0)) {
                fadCertificateDetailGridRowData.set("local", 0.00);
            }
            else {
                if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "借" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "0") {
                    fadCertificateDetailGridRowData.set("free2", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")));
                }
                if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "贷" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "1") {
                    fadCertificateDetailGridRowData.set("free3", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")));
                }
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free2")).length > 0)) {
                fadCertificateDetailGridRowData.set("free2", 0.00);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free3")).length > 0)) {
                fadCertificateDetailGridRowData.set("free3", 0.00);
            }
        }
    },
    beforeModify: function () {
        var me = this;
        oldSubjectCode = "";
        oldFadCertificateDetailFree2 = "";
        oldFadCertificateDetailFree3 = "";
        oldOrderNo = "";
        return true;
    },
    afterModify: function () {
        var me = this;
        me.view.getCmp("editToolbar->sendNC").setDisabled(true);
        me.view.getCmp("editToolbar->toLogicVoid").setDisabled(true);
        me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(true);
        setToBusinessBtnState(me);
        me.view.getCmp("fadCertificateDto->originalFormType").setReadOnly(true);

        me.view.getCmp("fadCertificateDto->free4").sotValue(false);

        var fadCertificateDetailGrid = me.view.getCmp("fadCertificateDetailDto");
        var abstracts = "";
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts")).length > 0) {
                abstracts = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts"));
                break;
            }
        }
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts")).length > 0)) {
                //fadCertificateDetailGridRowData.set("abstracts", abstracts);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("primary")).length > 0)) {
                fadCertificateDetailGridRowData.set("primary", 0.00);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")).length > 0)) {
                fadCertificateDetailGridRowData.set("local", 0.00);
            }
            else {
                if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "借" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "0") {
                    fadCertificateDetailGridRowData.set("free2", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")));
                }
                if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "贷" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "1") {
                    fadCertificateDetailGridRowData.set("free3", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")));
                }
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free2")).length > 0)) {
                fadCertificateDetailGridRowData.set("free2", 0.00);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free3")).length > 0)) {
                fadCertificateDetailGridRowData.set("free3", 0.00);
            }
        }
        if (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "1") {
            me.view.getCmp("fadCertificateDto->years").sotEditable(false);
            me.view.getCmp("fadCertificateDto->months").sotEditable(false);
        }
    },
    beforeSave: function () {
        var me = this;
        var fadCertificateDetailGrid = me.view.getCmp("fadCertificateDetailDto");
        var debtor = 0;
        var creditor = 0;
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("primary")).length > 0)) {
                fadCertificateDetailGridRowData.set("primary", 0.00);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")).length > 0)) {
                fadCertificateDetailGridRowData.set("local", 0.00);
            }
            if ((!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free2")).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free2")))) == 0)) {
                fadCertificateDetailGridRowData.set("free2", 0.00);
                if ((!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free3")).length > 0)) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free3")))) == 0)) {
                    fadCertificateDetailGridRowData.set("free3", 0.00);
                    fadCertificateDetailGridRowData.set("local", 0.00);
                    fadCertificateDetailGridRowData.set("debtorOrCreditor", "");
                }
                else {
                    fadCertificateDetailGridRowData.set("local", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free3")));
                    fadCertificateDetailGridRowData.set("debtorOrCreditor", "1");
                }
            }
            else {
                fadCertificateDetailGridRowData.set("free3", 0.00);
                fadCertificateDetailGridRowData.set("local", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free2")));
                fadCertificateDetailGridRowData.set("debtorOrCreditor", "0");
            }
            if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "借" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "0") {
                debtor = parseFloat((parseFloat(debtor) + parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local"))))).toFixed(2));
            }
            else if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "贷" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "1") {
                creditor = parseFloat((parseFloat(creditor) + parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local"))))).toFixed(2));
            }
        }
        if (
            (mergeSplitState != "1")
            && (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->uuid").gotValue()).length > 0)
            && (me.view.getCmp("fadCertificateDto->free4").gotValue() == false)
        ) {
            if ((parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) != debtor) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->creditor").gotValue()))) != creditor)) {
                Scdp.MsgUtil.warn("当前凭证金额与原凭证金额不符,可参照【原凭证金额】对借贷金额做适当调整!(当前凭证金额:[借方:" + debtor + ",贷方:" + creditor + "],原凭证金额:[借方:" + Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue())) + ",贷方:" + Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->creditor").gotValue())) + "])");
                return false;
            }
        }
        else {
            me.view.getCmp("fadCertificateDto->debtor").sotValue(debtor);
            me.view.getCmp("fadCertificateDto->creditor").sotValue(creditor);
            if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) != parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->creditor").gotValue())))) {
                Scdp.MsgUtil.warn("【凭证抬头】中的【借方(本币)】与【贷方(本币)】金额必须保持一致,请注意分录中的借贷金额是否一致!");
                return false;
            }
            else if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) == 0) {
                Scdp.MsgUtil.warn("【0金额凭证】不能保存!");
                return false;
            }
            else if (
                (
                    (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) < 0)
                    && (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()) != '12')
                    && (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()) != '12.2')
                )
            //&& (deficitCertifiState != "0")
            ) {
                Scdp.MsgUtil.warn("【赤字凭证】不能保存!");
                return false;
            }
        }

        if (!(fadCertificateDetailGrid.getStore().getCount() > 0)) {
            Scdp.MsgUtil.warn("【分录摘要】不能为空!");
            return false;
        }

        var debtorCount = 1;
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "借" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "0") {
                fadCertificateDetailGridRowData.set("seqNo", debtorCount);
                fadCertificateDetailGridRowData.set("orderNo", debtorCount);
                debtorCount = debtorCount + 1;
            }
            if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "借") {
                fadCertificateDetailGridRowData.set("debtorOrCreditor", "0");
            }
        }
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "贷" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "1") {
                fadCertificateDetailGridRowData.set("seqNo", debtorCount);
                fadCertificateDetailGridRowData.set("orderNo", debtorCount);
                debtorCount = debtorCount + 1;
            }
            if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "贷") {
                fadCertificateDetailGridRowData.set("debtorOrCreditor", "1");
            }
        }

        /*if (
         (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "1")
         || (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2")
         ) {
         me.view.getCmp("editToolbar->sendNC").setDisabled(true);
         }
         else {
         me.view.getCmp("editToolbar->sendNC").setDisabled(false);
         }
         if ((Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2") || (deficitCertifiState == "-1") || (copyAddState == "1")) {
         me.view.getCmp("editToolbar->toLogicVoid").setDisabled(true);
         me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(true);
         setToBusinessBtnState(me);
         if (
         (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2")
         || (deficitCertifiState == "0")
         || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) < 0)
         || (deficitCertifiState != "-1")
         || (copyAddState != "0")
         ) {
         me.view.getCmp('editToolbar->copyAddBtn').setDisabled(true);
         }
         }
         else {
         me.view.getCmp("editToolbar->toLogicVoid").setDisabled(false);
         me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(false);
         setToBusinessBtnState(me);
         me.view.getCmp('editToolbar->copyAddBtn').setDisabled(true);
         }*/

        return true;
    },
    afterSave: function (retData) {
        var me = this;
        if (
            (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "1")
            || (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2")
        ) {
            me.view.getCmp("editToolbar->sendNC").setDisabled(true);
        }
        else {
            me.view.getCmp("editToolbar->sendNC").setDisabled(false);
        }
        if ((Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2") || (deficitCertifiState == "-1") || (copyAddState == "1")) {
            me.view.getCmp("editToolbar->toLogicVoid").setDisabled(true);
            me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(true);
            setToBusinessBtnState(me);
            if (
                (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2")
                || (deficitCertifiState == "0")
                || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) < 0)
                || (deficitCertifiState != "-1")
                || (copyAddState != "0")
            ) {
                me.view.getCmp('editToolbar->copyAddBtn').setDisabled(true);
            }
        }
        else {
            me.view.getCmp("editToolbar->toLogicVoid").setDisabled(false);
            me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(false);
            setToBusinessBtnState(me);
            me.view.getCmp('editToolbar->copyAddBtn').setDisabled(true);
        }
        me.loadItem(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->uuid").gotValue()));
        me.afterLoadItem();
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;

        setReceiverOrPayer(me);

        var fadCertificateGridRowDataCur = me.view.getResultPanel().getCurRecord();
        if (fadCertificateGridRowDataCur == null) {
            var fadCertificateUuid = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->uuid").gotValue());
            if (Erp.Util.isNullReturnEmpty(fadCertificateUuid).length > 0) {
                var postData = {
                    fadCertificateUuid: fadCertificateUuid
                };
                var actionResult = Scdp.doAction("certificate-fadCertificateLoad", postData, null, null, true, false);
                if (Erp.Util.isNullReturnEmpty(actionResult.ncState).length > 0) {
                    ncState = Erp.Util.isNullReturnEmpty(actionResult.ncState);
                    deficitCertifiState = Erp.Util.isNullReturnEmpty(actionResult.deficitCertifiState);
                    copyAddState = Erp.Util.isNullReturnEmpty(actionResult.copyAddState);
                    mergeSplitState = Erp.Util.isNullReturnEmpty(actionResult.mergeSplitState);
                }
                else {
                    return;
                }
            }
            else {
                return;
            }
        }
        else {
            ncState = Erp.Util.isNullReturnEmpty(fadCertificateGridRowDataCur.data.ncState);
            deficitCertifiState = Erp.Util.isNullReturnEmpty(fadCertificateGridRowDataCur.data.deficitCertifiState);
            copyAddState = Erp.Util.isNullReturnEmpty(fadCertificateGridRowDataCur.data.copyAddState);
            mergeSplitState = Erp.Util.isNullReturnEmpty(fadCertificateGridRowDataCur.data.mergeSplitState);
        }

        if (
            (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "1")
            || (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2")
        ) {
            if (
                (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2")
                || (deficitCertifiState == "0")
                || (
                    (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) < 0)
                    && (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()) != '12')
                    && (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()) != '12.2')
                )
                || (deficitCertifiState == "-1")
            ) {
                me.view.getCmp("editToolbar->modifyBtn").setDisabled(true);
            }
            else {
                me.view.getCmp("editToolbar->modifyBtn").setDisabled(false);
            }
            me.view.getCmp("editToolbar->sendNC").setDisabled(true);
        }
        else {
            if (
                (deficitCertifiState == "0")
                || (
                    (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) < 0)
                    && (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()) != '12')
                    && (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()) != '12.2')
                )
            ) {
                me.view.getCmp("editToolbar->modifyBtn").setDisabled(true);
            }
            else {
                me.view.getCmp("editToolbar->modifyBtn").setDisabled(false);
            }
            me.view.getCmp("editToolbar->sendNC").setDisabled(false);
        }
        if ((Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2") || (deficitCertifiState == "-1") || (copyAddState == "1")) {
            me.view.getCmp("editToolbar->toLogicVoid").setDisabled(true);
            me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(true);
            setToBusinessBtnState(me);
            if (
                (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2")
                || (deficitCertifiState == "0")
                || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) < 0)
                || (deficitCertifiState != "-1")
                || (copyAddState != "0")
            ) {
                me.view.getCmp('editToolbar->copyAddBtn').setDisabled(true);
            }
        }
        else {
            me.view.getCmp("editToolbar->toLogicVoid").setDisabled(false);
            me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(false);
            setToBusinessBtnState(me);
            me.view.getCmp('editToolbar->copyAddBtn').setDisabled(true);
        }

        var fadCertificateDetailGrid = me.view.getCmp("fadCertificateDetailDto");
        var abstracts = "";
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts")).length > 0) {
                abstracts = Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts"));
                break;
            }
        }
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("abstracts")).length > 0)) {
                //fadCertificateDetailGridRowData.set("abstracts", abstracts);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("primary")).length > 0)) {
                fadCertificateDetailGridRowData.set("primary", 0.00);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")).length > 0)) {
                fadCertificateDetailGridRowData.set("local", 0.00);
            }
            else {
                if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "借" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "0") {
                    fadCertificateDetailGridRowData.set("free2", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")));
                }
                if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "贷" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "1") {
                    fadCertificateDetailGridRowData.set("free3", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")));
                }
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free2")).length > 0)) {
                fadCertificateDetailGridRowData.set("free2", 0.00);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free3")).length > 0)) {
                fadCertificateDetailGridRowData.set("free3", 0.00);
            }
        }
        //M7_C1_F1_关联收款 已发送状态设置为可以关联收款
        if (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "1"&&Scdp.ObjUtil.isEmpty(me.view.getCmp("fadCertificateDto->businessId").gotValue())) {
            me.view.getCmp("editToolbar->Collection").setDisabled(false);
        } else {
            me.view.getCmp("editToolbar->Collection").setDisabled(true);
        }
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;

        setReceiverOrPayer(me);

        if (
            (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "1")
            || (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2")
            || (!(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()).length > 0))
        ) {
            me.view.getCmp("editToolbar->sendNC").setDisabled(true);
        }
        else {
            me.view.getCmp("editToolbar->sendNC").setDisabled(false);
        }
        if (
            (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2")
            || (!(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()).length > 0))
            || (deficitCertifiState == "-1")
            || (copyAddState == "1")
        ) {
            me.view.getCmp("editToolbar->toLogicVoid").setDisabled(true);
            me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(true);
            setToBusinessBtnState(me);
            if (
                !(
                    (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2")
                    || (deficitCertifiState == "0")
                    || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) < 0)
                    || (deficitCertifiState != "-1")
                    || (copyAddState != "0")
                )
            ) {
                me.view.getCmp('editToolbar->copyAddBtn').setDisabled(false);
            }
            else {
                me.view.getCmp('editToolbar->copyAddBtn').setDisabled(true);
            }
        }
        else {
            me.view.getCmp("editToolbar->toLogicVoid").setDisabled(false);
            me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(false);
            setToBusinessBtnState(me);
        }

        var fadCertificateDetailGrid = me.view.getCmp("fadCertificateDetailDto");
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("primary")).length > 0)) {
                fadCertificateDetailGridRowData.set("primary", 0.00);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")).length > 0)) {
                fadCertificateDetailGridRowData.set("local", 0.00);
            }
            else {
                if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "借" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "0") {
                    fadCertificateDetailGridRowData.set("free2", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")));
                }
                if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "贷" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "1") {
                    fadCertificateDetailGridRowData.set("free3", Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local")));
                }
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free2")).length > 0)) {
                fadCertificateDetailGridRowData.set("free2", 0.00);
            }
            if (!(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("free3")).length > 0)) {
                fadCertificateDetailGridRowData.set("free3", 0.00);
            }
        }
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
    yearsChange: function () {
        var me = this;
        fadCertificateYears = Erp.Util.isNullReturnEmpty(me.view.getCmp("years").gotValue());
    },
    monthsChange: function () {
        var me = this;
        fadCertificateMonths = Erp.Util.isNullReturnEmpty(me.view.getCmp("months").gotValue());
    },
    originalFormTypeChange: function () {
        var me = this;
        var fadCertificateDetailGrid = me.view.getCmp("fadCertificateDetailDto");
        for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
            var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
            fadCertificateDetailGridRowData.set("originalFormType", Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()));
        }
    },
    receiverOrPayerNameChange: function () {
        var me = this;

        setReceiverOrPayer(me);

        var fadCertificateDetailGrid = me.view.getCmp("fadCertificateDetailDto");
        var sourceCurRecord = fadCertificateDetailGrid.getCurRecord();
        for (var h = 0; h < fadCertificateDetailGrid.getStore().getCount(); h++) {
            fadCertificateDetailGrid.setCurRecord(h);
            var fadCertificateAccountGrid = me.view.getCmp("fadCertificateAccountDto");
            for (var i = 0; i < fadCertificateAccountGrid.getStore().getCount(); i++) {
                var fadCertificateAccountGridRowData = fadCertificateAccountGrid.getStore().data.items[i];
                if
                (
                    (
                        (Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("accountNo")) == "73")
                        || (Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("accountNo")) == "NEIBU")
                        || (Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("accountNo")) == "WAIBU")
                    )
                    &&
                    (
                        (receiverOrPayerType == "SCM_SUPPLIER")
                        || (receiverOrPayerType == "PRM_CUSTOMER")
                    )
                    &&
                    (
                        !(Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("accountInfoName")).length > 0)
                    )
                ) {
                    fadCertificateAccountGridRowData.set("accountInfoId", receiverOrPayerId);
                    fadCertificateAccountGridRowData.set("accountInfoCode", receiverOrPayerCode);
                    fadCertificateAccountGridRowData.set("accountInfoName", receiverOrPayerName);
                }
                else if
                (
                    (
                        (Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("accountNo")) == "1")
                        || (Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("accountNo")) == "2")
                    )
                    &&
                    (
                        (receiverOrPayerType == "SCDP_USER")
                    )
                    &&
                    (
                        !(Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("accountInfoName")).length > 0)
                    )
                ) {
                    var postData = {
                        receiverOrPayerId: receiverOrPayerId,
                        receiverOrPayerCode: receiverOrPayerCode
                    };
                    var actionResult = Scdp.doAction("certificate-userOrgQuery", postData, null, null, true, false);
                    if (Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("accountNo")) == "1") {
                        fadCertificateAccountGridRowData.set("accountInfoId", receiverOrPayerId);
                        if (Erp.Util.isNullReturnEmpty(actionResult.OrgCode).length > 0) {
                            fadCertificateAccountGridRowData.set("accountInfoCode", receiverOrPayerCode + "-" + Erp.Util.isNullReturnEmpty(actionResult.OrgCode));
                        }
                        else {
                            fadCertificateAccountGridRowData.set("accountInfoCode", receiverOrPayerCode);
                        }
                        if (Erp.Util.isNullReturnEmpty(actionResult.OrgName).length > 0) {
                            fadCertificateAccountGridRowData.set("accountInfoName", receiverOrPayerName + "-" + Erp.Util.isNullReturnEmpty(actionResult.OrgName));
                        }
                        else {
                            fadCertificateAccountGridRowData.set("accountInfoName", receiverOrPayerName);
                        }
                    }
                    else if (Erp.Util.isNullReturnEmpty(fadCertificateAccountGridRowData.get("accountNo")) == "2") {
                        fadCertificateAccountGridRowData.set("accountInfoId", Erp.Util.isNullReturnEmpty(actionResult.OrgUuid));
                        fadCertificateAccountGridRowData.set("accountInfoCode", Erp.Util.isNullReturnEmpty(actionResult.OrgCode));
                        fadCertificateAccountGridRowData.set("accountInfoName", Erp.Util.isNullReturnEmpty(actionResult.OrgName));
                    }
                }
            }
        }
        fadCertificateDetailGrid.setCurRecord(sourceCurRecord);
    },
    /*certificateDatefieldFromChange: function () {
     var me = this;

     if (Scdp.ObjUtil.isNotEmpty(me.view.getCmp("certificateDatefieldFrom").gotValue())) {
     if (Erp.Util.isDate(me.view.getCmp("certificateDatefieldFrom").gotValue())) {
     //if (Erp.Util.isNullReturnEmpty(me.view.getCmp("certificateDatefieldFrom").gotValue()).length <= 10) {
     var certificateDatefieldVar = Erp.Util.getDateForStandard(me.view.getCmp("certificateDatefieldFrom").gotValue()) + " 00:00:00";
     me.view.getCmp("certificateDatefieldFrom").sotValue(new Date(certificateDatefieldVar));
     //}
     }
     }
     },*/
    certificateDatefieldToChange: function () {
        var me = this;

        /*if (Scdp.ObjUtil.isNotEmpty(me.view.getCmp("certificateDatefieldTo").gotValue())) {
         if (Erp.Util.isDate(me.view.getCmp("certificateDatefieldTo").gotValue())) {
         //if (Erp.Util.isNullReturnEmpty(me.view.getCmp("certificateDatefieldTo").gotValue()).length <= 10) {
         var certificateDatefieldVar = Erp.Util.getDateForStandard(me.view.getCmp("certificateDatefieldTo").gotValue()) + " 23:59:59";
         me.view.getCmp("certificateDatefieldTo").sotValue(new Date(certificateDatefieldVar));
         //}
         }
         }*/
        if (Erp.Util.isDate(me.view.getCmp("certificateDatefieldTo").getValue())) {
            if (Erp.Util.getDateTimeForStandard(me.view.getCmp("certificateDatefieldTo").getValue()).indexOf(" 00:00:00") != -1) {
                me.view.getCmp("certificateDatefieldTo").sotValue(Erp.Util.getDateTimeForStandard(me.view.getCmp("certificateDatefieldTo").getValue()).replace("00:00:00", "23:59:59"));
            }
        }
    },
    sendNC: function () {
        var me = this;
        if (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "0") {
            if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) == 0) {
                Scdp.MsgUtil.warn("【0金额凭证】不能发送NC!");
                return;
            }
            else {
                var fadCertificateDetailGrid = me.view.getCmp("fadCertificateDetailDto");
                var debtor = 0;
                var creditor = 0;
                for (var i = 0; i < fadCertificateDetailGrid.getStore().getCount(); i++) {
                    var fadCertificateDetailGridRowData = fadCertificateDetailGrid.getStore().data.items[i];
                    if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "借" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "0") {
                        debtor = parseFloat((parseFloat(debtor) + parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local"))))).toFixed(2));
                    }
                    else if (Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "贷" || Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("debtorOrCreditor")) == "1") {
                        creditor = parseFloat((parseFloat(creditor) + parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateDetailGridRowData.get("local"))))).toFixed(2));
                    }
                }
                //if (mergeSplitState != "1") {
                if ((parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) != debtor) || (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->creditor").gotValue()))) != creditor)) {
                    Scdp.MsgUtil.warn("当前凭证金额与原凭证金额不符,可参照【原凭证金额】对借贷金额做适当调整!(当前凭证金额:[借方:" + debtor + ",贷方:" + creditor + "],原凭证金额:[借方:" + Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue())) + ",贷方:" + Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->creditor").gotValue())) + "])");
                    return;
                }
                //}
                //else {
                //me.view.getCmp("fadCertificateDto->debtor").sotValue(debtor);
                //me.view.getCmp("fadCertificateDto->creditor").sotValue(creditor);
                if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) != parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->creditor").gotValue())))) {
                    Scdp.MsgUtil.warn("【凭证抬头】中的【借方(本币)】与【贷方(本币)】金额必须保持一致,请注意分录中的借贷金额是否一致!");
                    return;
                }
                //}
            }

            Scdp.MsgUtil.confirm("是否发送凭证到NC?请确认!", function (e) {
                if (e == "yes") {

                    //后台Post代码开始
                    var fadCertificateUuid = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->uuid").gotValue());
                    var tblVersion = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->tblVersion").gotValue());
                    var makerUuid = Erp.Util.isNullReturnEmpty(Scdp.CacheUtil.get(Scdp.Const.USER_UUID));
                    var makeDate = new Date();
                    var postData = {
                        fadCertificateUuid: fadCertificateUuid,
                        tblVersion: tblVersion,
                        makerUuid: makerUuid,
                        makeDate: makeDate
                    };
                    Scdp.doAction("certificate-certificateSendNCUpdata", postData, function (result) {
                        if (Erp.Util.isNullReturnEmpty(result.state).length > 0) {
                            me.view.getCmp("fadCertificateDto->state").sotValue(result.state);
                            me.view.getCmp("fadCertificateDto->certificateNo").sotValue(result.certificateNo);
                            me.view.getCmp("fadCertificateDto->feedback").sotValue(result.feedback);
                            me.view.getCmp("fadCertificateDto->maker").sotValue(result.maker);
                            me.view.getCmp("fadCertificateDto->makerName").sotValue(result.makerName);
                            me.view.getCmp("fadCertificateDto->makeDate").sotValue(new Date(result.makeDate));
                            me.view.getCmp("fadCertificateDto->ncrequestUrl").sotValue(result.ncrequestUrl);
                            me.view.getCmp("fadCertificateDto->ncrequestXml").sotValue(result.ncrequestXml);
                            me.view.getCmp("fadCertificateDto->ncresponseXml").sotValue(result.ncresponseXml);
                            if (result.state == "1") {
                                if (
                                    (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "2")
                                    || (deficitCertifiState == "0")
                                    || (
                                        (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) < 0)
                                        && (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()) != '12')
                                        && (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()) != '12.2')
                                    )
                                    || (deficitCertifiState == "-1")
                                ) {
                                    me.view.getCmp("editToolbar->modifyBtn").setDisabled(true);
                                }
                                else {
                                    me.view.getCmp("editToolbar->modifyBtn").setDisabled(false);
                                }
                                me.view.getCmp("editToolbar->sendNC").setDisabled(true);
                                Scdp.MsgUtil.info("发送NC成功");
                            }
                            else {
                                if (
                                    (deficitCertifiState == "0")
                                    || (
                                        (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->debtor").gotValue()))) < 0)
                                        && (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()) != '12')
                                        && (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()) != '12.2')
                                    )
                                ) {
                                    me.view.getCmp("editToolbar->modifyBtn").setDisabled(true);
                                }
                                else {
                                    me.view.getCmp("editToolbar->modifyBtn").setDisabled(false);
                                }
                                me.view.getCmp("editToolbar->sendNC").setDisabled(false);
                                Scdp.MsgUtil.warn(result.errMessages);
                            }
                        }
                        else if (Erp.Util.isNullReturnEmpty(result.errMessages).length > 0) {
                            if (Erp.Util.isNullReturnEmpty(result.feedback).length > 0) {
                                me.view.getCmp("fadCertificateDto->feedback").sotValue(result.feedback);
                                me.view.getCmp("fadCertificateDto->maker").sotValue(result.maker);
                                me.view.getCmp("fadCertificateDto->makerName").sotValue(result.makerName);
                                me.view.getCmp("fadCertificateDto->makeDate").sotValue(new Date(result.makeDate));
                            }
                            Scdp.MsgUtil.warn(result.errMessages);
                        }
                        else {
                            Scdp.MsgUtil.warn("连接【NCService】失败,请联系网络管理员!");
                        }
                        me.loadItem(Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->uuid").gotValue()));
                        me.afterLoadItem();
                    });
                    //后台Post代码结束
                }
            });
        }
    },
    toLogicVoid: function () {
        var me = this;
        if (
            (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "0")
            || (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "1")
        ) {
            Scdp.MsgUtil.confirm("是否作废凭证(一旦作废凭证即无法恢复)?请确认!", function (e) {
                if (e == "yes") {
                    var fadCertificateUuid = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->uuid").gotValue());
                    var tblVersion = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->tblVersion").gotValue());
                    var makerUuid = Erp.Util.isNullReturnEmpty(Scdp.CacheUtil.get(Scdp.Const.USER_UUID));
                    var makeDate = new Date();
                    var postData = {
                        fadCertificateUuid: fadCertificateUuid,
                        tblVersion: tblVersion,
                        makerUuid: makerUuid,
                        makeDate: makeDate
                    };
                    Scdp.doAction("certificate-certificateToLogicVoidUpdata", postData, function (result) {
                        if (Erp.Util.isNullReturnEmpty(result.state).length > 0) {
                            var sState = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue());
                            me.view.getCmp("fadCertificateDto->state").sotValue(result.state);
                            me.view.getCmp("fadCertificateDto->maker").sotValue(result.maker);
                            me.view.getCmp("fadCertificateDto->makerName").sotValue(result.makerName);
                            me.view.getCmp("fadCertificateDto->makeDate").sotValue(new Date(result.makeDate));
                            me.view.getCmp("editToolbar->modifyBtn").setDisabled(true);
                            me.view.getCmp("editToolbar->sendNC").setDisabled(true);
                            me.view.getCmp("editToolbar->toLogicVoid").setDisabled(true);
                            me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(true);
                            setToBusinessBtnState(me);
                            me.view.getCmp('editToolbar->copyAddBtn').setDisabled(true);
                            if (sState == "1") {
                                Scdp.MsgUtil.info("作废凭证成功!警告:【凭证】【" + Erp.Util.nvl(me.view.getCmp("fadCertificateDto->certificateNo").gotValue(), me.view.getCmp("fadCertificateDto->uuid").gotValue()) + "】为【已发送NC凭证】,请您务必登录【NC系统】删除该凭证!");
                            }
                            else {
                                Scdp.MsgUtil.info("作废凭证成功!");
                            }
                        }
                        else if (Erp.Util.isNullReturnEmpty(result.errMessages).length > 0) {
                            Scdp.MsgUtil.warn(result.errMessages);
                        }
                    });
                }
            });
        }
    },
    toLogicVoidAlone: function () {
        var me = this;
        if (
            (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "0")
            || (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "1")
        ) {
            Scdp.MsgUtil.confirm("是否消退凭证(一旦消退凭证即无法恢复)?请确认!", function (e) {
                if (e == "yes") {
                    var fadCertificateUuid = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->uuid").gotValue());
                    var tblVersion = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->tblVersion").gotValue());
                    var makerUuid = Erp.Util.isNullReturnEmpty(Scdp.CacheUtil.get(Scdp.Const.USER_UUID));
                    var makeDate = new Date();
                    var postData = {
                        fadCertificateUuid: fadCertificateUuid,
                        tblVersion: tblVersion,
                        makerUuid: makerUuid,
                        makeDate: makeDate
                    };
                    Scdp.doAction("certificate-certificateToLogicVoidAloneUpdata", postData, function (result) {
                        if (Erp.Util.isNullReturnEmpty(result.state).length > 0) {
                            var sState = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue());
                            me.view.getCmp("fadCertificateDto->state").sotValue(result.state);
                            me.view.getCmp("fadCertificateDto->maker").sotValue(result.maker);
                            me.view.getCmp("fadCertificateDto->makerName").sotValue(result.makerName);
                            me.view.getCmp("fadCertificateDto->makeDate").sotValue(new Date(result.makeDate));
                            me.view.getCmp("editToolbar->modifyBtn").setDisabled(true);
                            me.view.getCmp("editToolbar->sendNC").setDisabled(true);
                            me.view.getCmp("editToolbar->toLogicVoid").setDisabled(true);
                            me.view.getCmp("editToolbar->toLogicVoidAlone").setDisabled(true);
                            setToBusinessBtnState(me);
                            me.view.getCmp('editToolbar->copyAddBtn').setDisabled(true);
                            if (sState == "1") {
                                Scdp.MsgUtil.info("消退凭证成功!警告:【凭证】【" + Erp.Util.nvl(me.view.getCmp("fadCertificateDto->certificateNo").gotValue(), me.view.getCmp("fadCertificateDto->uuid").gotValue()) + "】为【已发送NC凭证】,请您务必登录【NC系统】删除该凭证!");
                            }
                            else {
                                Scdp.MsgUtil.info("消退凭证成功!");
                            }
                        }
                        else if (Erp.Util.isNullReturnEmpty(result.errMessages).length > 0) {
                            Scdp.MsgUtil.warn(result.errMessages);
                        }
                    });
                }
            });
        }
    },
    certificateMerge: function () {
        var me = this;
        var fadCertificateGridSelection = me.view.getResultPanel().getSelectionModel().getSelection();
        if (fadCertificateGridSelection.length < 2) {
            Scdp.MsgUtil.warn("请选择要合并的凭证!");
        }
        else {
            var isSend = false;
            for (var i = 0; i < fadCertificateGridSelection.length; i++) {
                if (fadCertificateGridSelection[i].data.state == "1") {
                    isSend = true;
                    Scdp.MsgUtil.warn("请勿选择【NC状态】为已发送的凭证!");
                    return;
                }
                else if (fadCertificateGridSelection[i].data.state == "2") {
                    isSend = true;
                    Scdp.MsgUtil.warn("请勿选择【NC状态】为作废的凭证!");
                    return;
                }
                else if (fadCertificateGridSelection[i].data.deficitCertifiState == "-1") {
                    isSend = true;
                    Scdp.MsgUtil.warn("请勿选择【红冲状态】为已红冲的凭证!");
                    return;
                }
                else if (fadCertificateGridSelection[i].data.deficitCertifiState == "0") {
                    isSend = true;
                    Scdp.MsgUtil.warn("请勿选择【红冲状态】为红冲的凭证!");
                    return;
                }
                else if (fadCertificateGridSelection[i].data.mergeSplitState == "2") {
                    isSend = true;
                    Scdp.MsgUtil.warn("请勿选择【合拆状态】为合并的凭证!");
                    return;
                }
                else if (fadCertificateGridSelection[i].data.mergeSplitState == "1") {
                    isSend = true;
                    Scdp.MsgUtil.warn("请勿选择【合拆状态】为拆分的凭证!");
                    return;
                }
                else {
                    isSend = false;
                }
            }

            var isMoneyMoreThanZero = true;
            for (var i = 0; i < fadCertificateGridSelection.length; i++) {
                if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateGridSelection[i].data.debtor))) <= 0) {
                    isMoneyMoreThanZero = false;
                    Scdp.MsgUtil.warn("【赤字凭证】、【0金额凭证】不能参与合并!");
                    return;
                }
            }

            var isOriginalFormTypeAgreement = true;
            for (var i = 0; i < fadCertificateGridSelection.length; i++) {
                for (var ii = 0; ii < fadCertificateGridSelection.length; ii++) {
                    if (fadCertificateGridSelection[i].data.originalFormType != fadCertificateGridSelection[ii].data.originalFormType) {
                        isOriginalFormTypeAgreement = false;
                        i = fadCertificateGridSelection.length - 1;
                        Scdp.MsgUtil.warn("【原始单据类型】不同的凭证不能互相合并!");
                        return;
                    }
                }
            }

            if ((!isSend) && (isMoneyMoreThanZero) && (isOriginalFormTypeAgreement)) {
                Scdp.MsgUtil.confirm("是否合并凭证?请确认!", function (e) {
                    if (e == "yes") {
                        var fadCertificateGridSelectionUuidList = [];
                        var fadCertificateGridSelectionCertificateDateList = [];
                        var fadCertificateGridSelectionTblVersionList = [];
                        for (var i = 0; i < fadCertificateGridSelection.length; i++) {
                            fadCertificateGridSelectionUuidList.push(fadCertificateGridSelection[i].data.uuid);
                            fadCertificateGridSelectionCertificateDateList.push(fadCertificateGridSelection[i].data.certificateDate);
                            fadCertificateGridSelectionTblVersionList.push(fadCertificateGridSelection[i].data.tblVersion);
                        }

                        for (var i = 0; i < fadCertificateGridSelectionUuidList.length; i++) {
                            for (var y = i + 1; y < fadCertificateGridSelectionUuidList.length; y++) {
                                if (fadCertificateGridSelectionCertificateDateList[i] > fadCertificateGridSelectionCertificateDateList[y]) {
                                    var fadCertificateGridSelectionUuid = fadCertificateGridSelectionUuidList[i];
                                    var fadCertificateGridSelectionCertificateDate = fadCertificateGridSelectionCertificateDateList[i];
                                    var fadCertificateGridSelectionTblVersion = fadCertificateGridSelectionTblVersionList[i];

                                    fadCertificateGridSelectionUuidList[i] = fadCertificateGridSelectionUuidList[y];
                                    fadCertificateGridSelectionCertificateDateList[i] = fadCertificateGridSelectionCertificateDateList[y];
                                    fadCertificateGridSelectionTblVersionList[i] = fadCertificateGridSelectionTblVersionList[y];

                                    fadCertificateGridSelectionUuidList[y] = fadCertificateGridSelectionUuid;
                                    fadCertificateGridSelectionCertificateDateList[y] = fadCertificateGridSelectionCertificateDate;
                                    fadCertificateGridSelectionTblVersionList[y] = fadCertificateGridSelectionTblVersion;
                                }
                            }
                        }

                        var postData = {
                            fadCertificateGridSelectionUuidList: fadCertificateGridSelectionUuidList,
                            fadCertificateGridSelectionTblVersionList: fadCertificateGridSelectionTblVersionList
                        };
                        Scdp.doAction("certificate-certificateMerge", postData, function (result) {
                            if (Erp.Util.isNullReturnEmpty(result.errorMsg).length > 0) {
                                Scdp.MsgUtil.warn(result.errorMsg);
                            }
                            else {
                                //me.doReset();
                                me.doQuery();
                                Scdp.MsgUtil.info("合并凭证成功!");
                            }
                        });
                    }
                });
            }
        }
    },
    certificateSplit: function () {
        var me = this;
        var fadCertificateGridSelection = me.view.getResultPanel().getSelectionModel().getSelection();
        if (fadCertificateGridSelection.length < 1) {
            Scdp.MsgUtil.warn("请选择要拆分的凭证!");
        }
        else if (fadCertificateGridSelection.length > 1) {
            Scdp.MsgUtil.warn("一次只能拆分一条凭证!");
        }
        else if (fadCertificateGridSelection[0].data.state == "1") {
            Scdp.MsgUtil.warn("请勿选择【NC状态】为已发送的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.state == "2") {
            Scdp.MsgUtil.warn("请勿选择【NC状态】为作废的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.deficitCertifiState == "-1") {
            Scdp.MsgUtil.warn("请勿选择【红冲状态】为已红冲的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.deficitCertifiState == "0") {
            Scdp.MsgUtil.warn("请勿选择【红冲状态】为红冲的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.mergeSplitState == "2") {
            Scdp.MsgUtil.warn("请勿选择【合拆状态】为合并的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.mergeSplitState == "1") {
            Scdp.MsgUtil.warn("请勿选择【合拆状态】为拆分的凭证!");
        }
        else if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateGridSelection[0].data.debtor))) <= 0) {
            Scdp.MsgUtil.warn("【赤字凭证】、【0金额凭证】不能拆分!");
        }
        else {
            /*var certificateSplitCount = prompt("请输入【拆分凭证份数】!", "2");
             if (certificateSplitCount != null) {
             if (Erp.Util.isZeroReturnEmpty(Erp.Util.isNullReturnEmpty(certificateSplitCount)).length > 0) {
             if (!isNaN(certificateSplitCount)) {
             if (certificateSplitCount > 1) {
             if (certificateSplitCount <= 20) {
             Scdp.MsgUtil.confirm("是否拆分凭证为" + certificateSplitCount + "份?请确认!", function (e) {
             if (e == "yes") {
             var fadCertificateUuid = fadCertificateGridSelection[0].data.uuid;
             var tblVersion = fadCertificateGridSelection[0].data.tblVersion;
             var postData = {
             fadCertificateUuid: fadCertificateUuid,
             tblVersion: tblVersion,
             certificateSplitCount: certificateSplitCount
             };
             Scdp.doAction("certificate-certificateSplit", postData, function (result) {
             if (Erp.Util.isNullReturnEmpty(result.errorMsg).length > 0) {
             Scdp.MsgUtil.warn(result.errorMsg);
             }
             else {
             //me.doReset();
             me.doQuery();
             Scdp.MsgUtil.info("拆分凭证成功!");
             }
             });
             }
             });
             }
             else {
             Scdp.MsgUtil.warn("【拆分凭证份数】不能大于20!");
             me.certificateSplit();
             }
             }
             else {
             Scdp.MsgUtil.warn("请输入【拆分凭证份数】!");
             me.certificateSplit();
             }
             }
             else {
             Scdp.MsgUtil.warn("【拆分凭证份数】仅能输入数字!");
             me.certificateSplit();
             }
             }
             else {
             Scdp.MsgUtil.warn("请输入【拆分凭证份数】!");
             me.certificateSplit();
             }
             }*/
            Scdp.MsgUtil.prompt("请输入【拆分凭证份数】!", function (buttonId, certificateSplitCount) {
                if ((certificateSplitCount != null) && (buttonId == "ok")) {
                    if (Erp.Util.isZeroReturnEmpty(Erp.Util.isNullReturnEmpty(certificateSplitCount)).length > 0) {
                        if (!isNaN(certificateSplitCount)) {
                            if (certificateSplitCount > 1) {
                                if (certificateSplitCount <= 20) {
                                    Scdp.MsgUtil.confirm("是否拆分凭证为" + certificateSplitCount + "份?请确认!", function (e) {
                                        if (e == "yes") {
                                            var fadCertificateUuid = fadCertificateGridSelection[0].data.uuid;
                                            var tblVersion = fadCertificateGridSelection[0].data.tblVersion;
                                            var postData = {
                                                fadCertificateUuid: fadCertificateUuid,
                                                tblVersion: tblVersion,
                                                certificateSplitCount: certificateSplitCount
                                            };
                                            Scdp.doAction("certificate-certificateSplit", postData, function (result) {
                                                if (Erp.Util.isNullReturnEmpty(result.errorMsg).length > 0) {
                                                    Scdp.MsgUtil.warn(result.errorMsg);
                                                }
                                                else {
                                                    //me.doReset();
                                                    me.doQuery();
                                                    Scdp.MsgUtil.info("拆分凭证成功!");
                                                }
                                            });
                                        }
                                    });
                                }
                                else {
                                    Scdp.MsgUtil.warn("【拆分凭证份数】不能大于20!");
                                    me.certificateSplit();
                                }
                            }
                            else {
                                Scdp.MsgUtil.warn("请输入【拆分凭证份数】!");
                                me.certificateSplit();
                            }
                        }
                        else {
                            Scdp.MsgUtil.warn("【拆分凭证份数】仅能输入数字!");
                            me.certificateSplit();
                        }
                    }
                    else {
                        Scdp.MsgUtil.warn("请输入【拆分凭证份数】!");
                        me.certificateSplit();
                    }
                }
            });
        }
    },
    certificateMergeRestore: function () {
        var me = this;
        var fadCertificateGridSelection = me.view.getResultPanel().getSelectionModel().getSelection();
        if (fadCertificateGridSelection.length < 1) {
            Scdp.MsgUtil.warn("请选择要复原的【合并凭证】!");
        }
        else if (fadCertificateGridSelection.length > 1) {
            Scdp.MsgUtil.warn("一次只能复原一条【合并凭证】!");
        }
        /*else if (fadCertificateGridSelection[0].data.state == "1") {
         Scdp.MsgUtil.warn("请勿选择【NC状态】为已发送的凭证!");
         }*/
        else if (fadCertificateGridSelection[0].data.state == "2") {
            Scdp.MsgUtil.warn("请勿选择【NC状态】为作废的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.deficitCertifiState == "-1") {
            Scdp.MsgUtil.warn("请勿选择【红冲状态】为已红冲的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.deficitCertifiState == "0") {
            Scdp.MsgUtil.warn("请勿选择【红冲状态】为红冲的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.mergeSplitState == "1") {
            Scdp.MsgUtil.warn("请勿选择【合拆状态】为拆分的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.mergeSplitState != "2") {
            Scdp.MsgUtil.warn("请选择【合拆状态】为合并的凭证!");
        }
        else if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateGridSelection[0].data.debtor))) <= 0) {
            Scdp.MsgUtil.warn("【赤字凭证】、【0金额凭证】不能复原!");
        }
        else {
            Scdp.MsgUtil.confirm("是否复原【合并凭证】到【原始凭证】?请确认!", function (e) {
                if (e == "yes") {
                    var fadCertificateUuid = fadCertificateGridSelection[0].data.uuid;
                    var tblVersion = fadCertificateGridSelection[0].data.tblVersion;
                    var postData = {
                        fadCertificateUuid: fadCertificateUuid,
                        tblVersion: tblVersion
                    };
                    Scdp.doAction("certificate-certificateMergeRestore", postData, function (result) {
                        //me.doReset();
                        me.doQuery();
                        if (Erp.Util.isNullReturnEmpty(result.errorMsg).length > 0) {
                            Scdp.MsgUtil.warn(result.errorMsg);
                        }
                        else {
                            Scdp.MsgUtil.info("复原【合并凭证】到【原始凭证】成功!");
                        }
                    });
                }
            });
        }
    },
    certificateSplitRestore: function () {
        var me = this;
        var fadCertificateGridSelection = me.view.getResultPanel().getSelectionModel().getSelection();
        if (fadCertificateGridSelection.length < 1) {
            Scdp.MsgUtil.warn("请选择要复原的【拆分凭证】!");
        }
        else if (fadCertificateGridSelection.length > 1) {
            Scdp.MsgUtil.warn("一次只能复原一条【拆分凭证】!");
        }
        /*else if (fadCertificateGridSelection[0].data.state == "1") {
         Scdp.MsgUtil.warn("请勿选择【NC状态】为已发送的凭证!");
         }*/
        else if (fadCertificateGridSelection[0].data.state == "2") {
            Scdp.MsgUtil.warn("请勿选择【NC状态】为作废的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.deficitCertifiState == "-1") {
            Scdp.MsgUtil.warn("请勿选择【红冲状态】为已红冲的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.deficitCertifiState == "0") {
            Scdp.MsgUtil.warn("请勿选择【红冲状态】为红冲的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.mergeSplitState == "2") {
            Scdp.MsgUtil.warn("请勿选择【合拆状态】为合并的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.mergeSplitState != "1") {
            Scdp.MsgUtil.warn("请选择【合拆状态】为拆分的凭证!");
        }
        else if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateGridSelection[0].data.debtor))) <= 0) {
            Scdp.MsgUtil.warn("【赤字凭证】、【0金额凭证】不能复原!");
        }
        else {
            Scdp.MsgUtil.confirm("是否复原【拆分凭证】到【原始凭证】?请确认!", function (e) {
                if (e == "yes") {
                    var fadCertificateUuid = fadCertificateGridSelection[0].data.uuid;
                    var tblVersion = fadCertificateGridSelection[0].data.tblVersion;
                    var postData = {
                        fadCertificateUuid: fadCertificateUuid,
                        tblVersion: tblVersion
                    };
                    Scdp.doAction("certificate-certificateSplitRestore", postData, function (result) {
                        //me.doReset();
                        me.doQuery();
                        if (Erp.Util.isNullReturnEmpty(result.errorMsg).length > 0) {
                            Scdp.MsgUtil.warn(result.errorMsg);
                        }
                        else {
                            Scdp.MsgUtil.info("复原【拆分凭证】到【原始凭证】成功!");
                        }
                    });
                }
            });
        }
    },
    certificateDeficit: function () {
        var me = this;
        var fadCertificateGridSelection = me.view.getResultPanel().getSelectionModel().getSelection();
        if (fadCertificateGridSelection.length < 1) {
            Scdp.MsgUtil.warn("请选择要生成红冲的凭证!");
        }
        else if (fadCertificateGridSelection.length > 1) {
            Scdp.MsgUtil.warn("一次只能生成一条红冲凭证!");
        }
        else if (fadCertificateGridSelection[0].data.state == "0") {
            Scdp.MsgUtil.warn("请勿选择【NC状态】为未发送的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.state == "2") {
            Scdp.MsgUtil.warn("请勿选择【NC状态】为作废的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.deficitCertifiState == "-1") {
            Scdp.MsgUtil.warn("请勿选择【红冲状态】为已红冲的凭证!");
        }
        else if (fadCertificateGridSelection[0].data.deficitCertifiState == "0") {
            Scdp.MsgUtil.warn("请勿选择【红冲状态】为红冲的凭证!");
        }
        /*else if (fadCertificateGridSelection[0].data.mergeSplitState == "2") {
         Scdp.MsgUtil.warn("请勿选择【合拆状态】为合并的凭证!");
         }
         else if (fadCertificateGridSelection[0].data.mergeSplitState == "1") {
         Scdp.MsgUtil.warn("请勿选择【合拆状态】为拆分的凭证!");
         }*/
        else if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateGridSelection[0].data.debtor))) <= 0) {
            Scdp.MsgUtil.warn("【赤字凭证】、【0金额凭证】不能红冲!");
        }
        else {
            Scdp.MsgUtil.confirm("是否生成红冲凭证?请确认!", function (e) {
                if (e == "yes") {
                    var fadCertificateUuid = fadCertificateGridSelection[0].data.uuid;
                    var tblVersion = fadCertificateGridSelection[0].data.tblVersion;
                    var postData = {fadCertificateUuid: fadCertificateUuid, tblVersion: tblVersion};
                    Scdp.doAction("certificate-certificateDeficit", postData, function (result) {
                        if (Erp.Util.isNullReturnEmpty(result.errorMsg).length > 0) {
                            Scdp.MsgUtil.warn(result.errorMsg);
                        }
                        else {
                            //me.doReset();
                            me.doQuery();
                            Scdp.MsgUtil.info("生成红冲凭证成功!");
                        }
                    });
                }
            });
        }
    },
    originalToCertificate: function () {
        var me = this;
        var businessId = Erp.Util.isNullReturnEmpty(me.view.getCmp("businessId").gotValue());
        var originalFormTypeForeign = Erp.Util.isNullReturnEmpty(me.view.getCmp("originalFormTypeForeign").gotValue());
        if ((Erp.Util.isNullReturnEmpty(businessId).length > 0) && (Erp.Util.isNullReturnEmpty(originalFormTypeForeign).length > 0)) {
            var postData = {businessId: businessId, originalFormTypeForeign: originalFormTypeForeign};
            Scdp.doAction("certificate-originalToCertificate", postData, function (result) {
                if (Erp.Util.isNullReturnEmpty(result.fadCertificateUuid).length > 0) {
                    me.view.getCmp("businessId").sotValue(businessId);
                    me.view.getCmp("originalFormTypeForeign").sotValue(originalFormTypeForeign);
                    me.view.getCmp("fadCertificateUuid").sotValue(result.fadCertificateUuid);
                    //me.doReset();
                    me.doQuery();
                    Scdp.MsgUtil.info("【原始单据UUID】生成凭证成功,【凭证UUID】(" + Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateUuid").gotValue()) + ")成功返回!");
                }
                else {
                    Scdp.MsgUtil.warn("【原始单据UUID】生成凭证失败,请检查您输入的【原始单据UUID】、【原始单据类型】是否正确!");
                }
            });
        }
        else {
            Scdp.MsgUtil.warn("请务必输入【原始单据UUID】、【原始单据类型】");
        }
    },
    batchSendNC: function () {
        var me = this;
        var fadCertificateGridSelection = me.view.getResultPanel().getSelectionModel().getSelection();
        if (fadCertificateGridSelection.length < 1) {
            Scdp.MsgUtil.warn("请选择要发送的凭证!");
        }
        else {
            var isSend = false;
            for (var i = 0; i < fadCertificateGridSelection.length; i++) {
                if (fadCertificateGridSelection[i].data.state == "1") {
                    isSend = true;
                    Scdp.MsgUtil.warn("请勿选择【NC状态】为已发送的凭证!");
                    return;
                }
                else if (fadCertificateGridSelection[i].data.state == "2") {
                    isSend = true;
                    Scdp.MsgUtil.warn("请勿选择【NC状态】为作废的凭证!");
                    return;
                }
                else if (fadCertificateGridSelection[i].data.deficitCertifiState == "-1") {
                    isSend = true;
                    Scdp.MsgUtil.warn("请勿选择【红冲状态】为已红冲的凭证!");
                    return;
                }
                /*else if (fadCertificateGridSelection[i].data.deficitCertifiState == "0") {
                 isSend = true;
                 Scdp.MsgUtil.warn("请勿选择【红冲状态】为红冲的凭证!");
                 return;
                 }
                 else if (fadCertificateGridSelection[i].data.mergeSplitState == "2") {
                 isSend = true;
                 Scdp.MsgUtil.warn("请勿选择【合拆状态】为合并的凭证!");
                 return;
                 }
                 else if (fadCertificateGridSelection[i].data.mergeSplitState == "1") {
                 isSend = true;
                 Scdp.MsgUtil.warn("请勿选择【合拆状态】为拆分的凭证!");
                 return;
                 }*/
                else {
                    isSend = false;
                }
            }

            var isMoneyMoreThanZero = true;
            for (var i = 0; i < fadCertificateGridSelection.length; i++) {
                if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateGridSelection[i].data.debtor))) == 0) {
                    isMoneyMoreThanZero = false;
                    Scdp.MsgUtil.warn("【0金额凭证】不能参与发送NC!");
                    return;
                }
                else if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateGridSelection[i].data.debtor))) != parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(fadCertificateGridSelection[i].data.creditor)))) {
                    isMoneyMoreThanZero = false;
                    Scdp.MsgUtil.warn("【凭证抬头】中的【借方(本币)】与【贷方(本币)】金额必须保持一致,请注意分录中的借贷金额是否一致!");
                    return;
                }
            }

            /*var isOriginalFormTypeAgreement = true;
             for (var i = 0; i < fadCertificateGridSelection.length; i++) {
             for (var ii = 0; ii < fadCertificateGridSelection.length; ii++) {
             if (fadCertificateGridSelection[i].data.originalFormType != fadCertificateGridSelection[ii].data.originalFormType) {
             isOriginalFormTypeAgreement = false;
             i = fadCertificateGridSelection.length - 1;
             Scdp.MsgUtil.warn("【原始单据类型】不同的凭证不能互相合并!");
             return;
             }
             }
             }*/

            if ((!isSend) && (isMoneyMoreThanZero) /*&& (isOriginalFormTypeAgreement)*/) {
                Scdp.MsgUtil.confirm("是否发送凭证?请确认!", function (e) {
                    if (e == "yes") {
                        var fadCertificateGridSelectionUuidList = [];
                        var fadCertificateGridSelectionCertificateDateList = [];
                        var fadCertificateGridSelectionTblVersionList = [];
                        for (var i = 0; i < fadCertificateGridSelection.length; i++) {
                            fadCertificateGridSelectionUuidList.push(fadCertificateGridSelection[i].data.uuid);
                            fadCertificateGridSelectionCertificateDateList.push(fadCertificateGridSelection[i].data.certificateDate);
                            fadCertificateGridSelectionTblVersionList.push(fadCertificateGridSelection[i].data.tblVersion);
                        }

                        for (var i = 0; i < fadCertificateGridSelectionUuidList.length; i++) {
                            for (var y = i; y < fadCertificateGridSelectionUuidList.length; y++) {
                                if (fadCertificateGridSelectionCertificateDateList[i] > fadCertificateGridSelectionCertificateDateList[y]) {
                                    var fadCertificateGridSelectionUuid = fadCertificateGridSelectionUuidList[i];
                                    var fadCertificateGridSelectionCertificateDate = fadCertificateGridSelectionCertificateDateList[i];
                                    var fadCertificateGridSelectionTblVersion = fadCertificateGridSelectionTblVersionList[i];

                                    fadCertificateGridSelectionUuidList[i] = fadCertificateGridSelectionUuidList[y];
                                    fadCertificateGridSelectionCertificateDateList[i] = fadCertificateGridSelectionCertificateDateList[y];
                                    fadCertificateGridSelectionTblVersionList[i] = fadCertificateGridSelectionTblVersionList[y];

                                    fadCertificateGridSelectionUuidList[y] = fadCertificateGridSelectionUuid;
                                    fadCertificateGridSelectionCertificateDateList[y] = fadCertificateGridSelectionCertificateDate;
                                    fadCertificateGridSelectionTblVersionList[y] = fadCertificateGridSelectionTblVersion;
                                }
                            }
                        }

                        var makerUuid = Erp.Util.isNullReturnEmpty(Scdp.CacheUtil.get(Scdp.Const.USER_UUID));
                        var makeDate = new Date();
                        var postData = {
                            fadCertificateGridSelectionUuidList: fadCertificateGridSelectionUuidList,
                            fadCertificateGridSelectionTblVersionList: fadCertificateGridSelectionTblVersionList,
                            makerUuid: makerUuid,
                            makeDate: makeDate
                        };
                        Scdp.doAction("certificate-certificateBatchSendNCUpdata", postData, function (result) {
                            if (Erp.Util.isNullReturnEmpty(result.errorMsg).length > 0) {
                                //Scdp.MsgUtil.warn(result.errorMsg);
                                Scdp.MsgUtil.warn(result.errorMsg);
                            }
                            else {
                                //me.doReset();
                                me.doQuery();
                                Scdp.MsgUtil.info("发送凭证成功!");
                            }
                        });
                    }
                });
            }
        }
    },
    toBusiness: function () {
        var me = this;
        var businessId = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->businessId").gotValue());
        var originalFormType = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue());
        gotoOriginalModule(businessId, getOriginalFormTypeForeign(originalFormType));
    },
    nCCodeSet: function () {
        var me = this;

        setReceiverOrPayer(me);

        CheckNcCode(receiverOrPayerType, receiverOrPayerId, receiverOrPayerCode, receiverOrPayerName);
    },
    // M7_C1_F1_关联收款
    Collection: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("fadCertificateDto->uuid").gotValue();
        var debotrs = view.getCmp("fadCertificateDto->debtor").gotValue();
        var callback = function (subView, isCancel) {
            if (isCancel) {
                return;
            }
            Scdp.MsgUtil.confirm("是否关联收款", function (e) {
                if (e == "yes") {
                    var grid = subView.getQueryPanel().getCmp("resultPanel");
                    var selectedRecords = grid.getSelectionModel().getSelection();
                    if (selectedRecords.length > 0) {
                        var uuids = selectedRecords[0].data.uuid;
                        var actualDate = selectedRecords[0].data.actualDate;
                        var geshiactualDate = Ext.Date.format(new Date(actualDate), "Y-m-d");
                        var queren = "(已确认收款)";
                        var hebing = uuids + queren + geshiactualDate;
                        var actualMoney = selectedRecords[0].data.actualMoney;
                        if (parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(actualMoney))) != parseFloat(Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(debotrs)))) {
                            Scdp.MsgUtil.warn("项目的实际收款金额和凭证编辑借方（本币）金额必须相同");
                            return;
                        } else {
                            var params = {
                                uuid: uuid,
                                businessId: uuids,
                                originalFormCode: hebing
                            }
                            Scdp.doAction("certificate-CollectionUpdate", params, null, null, true, false);

                        }
                        view.getCmp("fadCertificateDto->originalFormCode").sotValue(hebing);
                        view.getCmp("fadCertificateDto->businessId").sotValue(uuids);
                        view.getCmp("editToolbar->Collection").setDisabled(true);
                        view.getCmp("editToolbar->toBusiness").setDisabled(false);
                        //view.getCmp("editPanel->saveBtn").setDisabled(false);
                        subView.up('window').close();
                        return true;
                    } else {
                        return true;
                    }

                }
            });

        };
        var queryController = Ext.create("Receipts.controller.ReceiptsSController");
        Scdp.openNewWinByController(queryController, callback, 'temp_icon_16', "关联收款", null, true);


    },
    //M7_C1_F1_关联收款 改变关联收款状态
    changeState: function () {
        var me = this;
        if (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->state").gotValue()) == "1") {
            me.view.getCmp("editToolbar->Collection").setDisabled(false);
        } else {
            me.view.getCmp("editToolbar->Collection").setDisabled(true);
        }
    }
});

function setToBusinessBtnState(me) {
    if (
        (mergeSplitState != "2")
        &&
        (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->businessId").gotValue()).length > 0)
        &&
        (Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->originalFormType").gotValue()).length > 0)
    ) {
        me.view.getCmp("editToolbar->toBusiness").setDisabled(false);
    }
    else {
        me.view.getCmp("editToolbar->toBusiness").setDisabled(true);
    }
}

function gotoOriginalModule(businessId, originalFormTypeForeign) {
    var menuCode = "";
    var param = {};
    param.wf_businessKey = businessId;
    var businessTable = "";
    var postData = {businessId: businessId};
    if (originalFormTypeForeign == "0") {
        businessTable = "certificate-certificateInvoiceLoad";
        Scdp.doAction(businessTable, postData, function (result) {
            if (Erp.Util.isNullReturnEmpty(result.fadInvoice).length > 0) {
                if (result.fadInvoice.expensesType == "0") {
                    menuCode = "FAD_SCMCONTRACTINVOICE";
                }
                else if (result.fadInvoice.expensesType == "1") {
                    if (Erp.Util.isNullReturnEmpty(result.fadInvoice.prmProjectMainId).length > 0) {
                        menuCode = "FAD_PRMFAILYCLAIMS";
                    }
                    else {
                        menuCode = "FAD_NONFAILYCLAIMS";
                    }
                }
                else if (result.fadInvoice.expensesType == "2") {
                    if (Erp.Util.isNullReturnEmpty(result.fadInvoice.prmProjectMainId).length > 0) {
                        menuCode = "FAD_PRMBUSINESSTRIPCLAIMS";
                    }
                    else {
                        menuCode = "FAD_NONBUSINESSTRIPCLAIMS";
                    }
                }
                Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
            }
        });
    }
    else if (originalFormTypeForeign == "1") {
        businessTable = "certificate-certificateCashReqLoad";
        Scdp.doAction(businessTable, postData, function (result) {
            if (Erp.Util.isNullReturnEmpty(result.fadCashReq).length > 0) {
                if (result.fadCashReq.reqType == "0") {
                    menuCode = "FAD_CASHREQ_PURCHASE";
                }
                else if (
                    result.fadCashReq.reqType == "1"
                    ||
                    result.fadCashReq.reqType == "2"
                ) {
                    if (Erp.Util.isNullReturnEmpty(result.fadCashReq.isProject) == "1") {
                        menuCode = "FAD_CASHREQ_PROJECT";
                    }
                    else {
                        menuCode = "FAD_CASHREQ_NON";
                    }
                }
                else if (result.fadCashReq.reqType == "3") {
                    menuCode = "FAD_CASHREQ_DEPOSIT";
                }
                else if(result.fadCashReq.reqType == "4") {
                    menuCode = "FAD_CASHREQ_REVOLVING";
                }
                Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
            }
        });
    }
    else if (originalFormTypeForeign == "2") {
        businessTable = "certificate-certificatePayReqCLoad";
        Scdp.doAction(businessTable, postData, function (result) {
            if (Erp.Util.isNullReturnEmpty(result.fadPayReqH).length > 0) {
                if (result.fadPayReqH.reqType == "1") {
                    menuCode = "FAD_PAY_PRO_MONTH_REQ";
                }
                else {
                    menuCode = "FAD_PAY_PRO_TMP_REQ";
                }
                param.wf_businessKey = result.fadPayReqH.uuid;
                Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
            }
        });
    }
    else if (originalFormTypeForeign == "3") {
        businessTable = "certificate-certificateCashClearanceLoad";
        Scdp.doAction(businessTable, postData, function (result) {
            if (Erp.Util.isNullReturnEmpty(result.fadCashClearance).length > 0) {
                menuCode = "CASHCLEARANCE";
                Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
            }
        });
    }
    else if (originalFormTypeForeign == "4") {
        businessTable = "certificate-certificateReceiptsLoad";
        Scdp.doAction(businessTable, postData, function (result) {
            if (Erp.Util.isNullReturnEmpty(result.prmReceipts).length > 0) {
                menuCode = "RECEIPTS";
                Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
            }
        });
    }
    else if (originalFormTypeForeign == "5") {
        businessTable = "certificate-certificateBillingLoad";
        Scdp.doAction(businessTable, postData, function (result) {
            if (Erp.Util.isNullReturnEmpty(result.prmBilling).length > 0) {
                menuCode = "PRMBILLING";
                Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
            }
        });
    }
}

function getOriginalFormTypeForeign(originalFormType) {
    if (
        originalFormType == "4"
        ||
        originalFormType == "5"
        ||
        originalFormType == "5.1"
        ||
        originalFormType == "6"
        ||
        originalFormType == "7"
        ||
        originalFormType == "8"
        ||
        originalFormType == "9"
    ) {
        return "0";
    }
    else if (
        originalFormType == "1"
        ||
        originalFormType == "2"
        ||
        originalFormType == "3"
        ||
        originalFormType == "14"
    ) {
        return "1";
    }
    else if (originalFormType == "10") {
        return "2";
    }
    else if (originalFormType == "11") {
        return "3";
    }
    else if (
        originalFormType == "12"
        ||
        originalFormType == "12.1"
        ||
        originalFormType == "12.2"
    ) {
        return "4";
    }
    else if (originalFormType == "13") {
        return "5";
    }
}

function CheckNcCode(receiverOrPayerType, receiverOrPayerId, receiverOrPayerCode, receiverOrPayerName) {
    if (Erp.Util.isNullReturnEmpty(receiverOrPayerType).length > 0) {
        if (Erp.Util.isNullReturnEmpty(receiverOrPayerId) != '[$nc_code]') {
            if (Erp.Util.isNullReturnEmpty(receiverOrPayerType) == "SCM_SUPPLIER" || Erp.Util.isNullReturnEmpty(receiverOrPayerType) == "PRM_CUSTOMER" || Erp.Util.isNullReturnEmpty(receiverOrPayerType) == "SCDP_USER") {
                var receiverOrPayerNcCode = "";
                var postData = {
                    receiverOrPayerType: receiverOrPayerType,
                    receiverOrPayerId: receiverOrPayerId,
                    receiverOrPayerCode: receiverOrPayerCode,
                    receiverOrPayerName: receiverOrPayerName,
                    receiverOrPayerNcCode: receiverOrPayerNcCode
                };
                var actionResult = Scdp.doAction("certificate-receiverOrPayerViewLoad", postData, null, null, true, false);
                if (Erp.Util.isNullReturnEmpty(actionResult.receiverOrPayerId).length > 0) {
                    var promptInfoTemp = "【" + Erp.Util.isNullReturnEmpty(actionResult.receiverOrPayerName) + "】的";
                    if (Erp.Util.isNullReturnEmpty(receiverOrPayerType) == "SCDP_USER" && getDepartmentNameInFadReceiverOrPayerView(actionResult.departmentCode).length > 0) {
                        promptInfoTemp = "【" + getDepartmentNameInFadReceiverOrPayerView(actionResult.departmentCode) + "】【" + Erp.Util.isNullReturnEmpty(actionResult.receiverOrPayerName) + "】的";
                    }
                    var promptInfoText = "请在这里录入" + promptInfoTemp + "【NCCode】";
                    if (Erp.Util.isNullReturnEmpty(receiverOrPayerType) == "SCDP_USER" && Erp.Util.isNullReturnEmpty(receiverOrPayerName) != Erp.Util.isNullReturnEmpty(actionResult.receiverOrPayerName)) {
                        promptInfoText = promptInfoText + "(说明:【对方单位名称】【" + Erp.Util.isNullReturnEmpty(receiverOrPayerName) + "】仅有姓名信息)";
                    }
                    //receiverOrPayerNcCode = prompt(promptInfoText, Erp.Util.isNullReturnEmpty(actionResult.receiverOrPayerNcCode));

                    if (Erp.Util.isNullReturnEmpty(actionResult.receiverOrPayerNcCode).length > 0) {
                        Scdp.MsgUtil.warn("已存在" + promptInfoTemp + "【NCCode】【" + Erp.Util.isNullReturnEmpty(actionResult.receiverOrPayerNcCode) + "】");
                    } else {
                        Scdp.MsgUtil.prompt(promptInfoText, function (buttonId, receiverOrPayerNcCode) {
                            if ((receiverOrPayerNcCode == null) || (buttonId != "ok")) {
                                Scdp.MsgUtil.info("【NCCode】取消调整成功!");
                            }
                            else if (Erp.Util.isNullReturnEmpty(receiverOrPayerNcCode).length > 0) {
                                if (Erp.Util.isNullReturnEmpty(receiverOrPayerNcCode) != Erp.Util.isNullReturnEmpty(actionResult.receiverOrPayerNcCode)) {
                                    var confirmWarnText = "是否将" + promptInfoTemp + "【NCCode】调整为【" + Erp.Util.isNullReturnEmpty(receiverOrPayerNcCode) + "】?请确认!";
                                    if (Erp.Util.isNullReturnEmpty(actionResult.receiverOrPayerNcCode).length > 0) {
                                        confirmWarnText = "是否将" + promptInfoTemp + "【NCCode】由【" + Erp.Util.isNullReturnEmpty(actionResult.receiverOrPayerNcCode) + "】调整为【" + Erp.Util.isNullReturnEmpty(receiverOrPayerNcCode) + "】?请确认!";
                                    }
                                    /*if (confirm(confirmWarnText)) {
                                     postData = {
                                     receiverOrPayerType: receiverOrPayerType,
                                     receiverOrPayerId: receiverOrPayerId,
                                     receiverOrPayerCode: receiverOrPayerCode,
                                     receiverOrPayerName: receiverOrPayerName,
                                     receiverOrPayerNcCode: receiverOrPayerNcCode
                                     };
                                     Scdp.doAction("certificate-receiverOrPayerViewUpdata", postData, null, null, true, false);
                                     Scdp.MsgUtil.info("【NCCode】调整成功!");
                                     }
                                     else {
                                     Scdp.MsgUtil.info("【NCCode】取消调整成功!");
                                     }*/
                                    Scdp.MsgUtil.confirm(confirmWarnText, function (e) {
                                        if (e == "yes") {
                                            postData = {
                                                receiverOrPayerType: receiverOrPayerType,
                                                receiverOrPayerId: receiverOrPayerId,
                                                receiverOrPayerCode: receiverOrPayerCode,
                                                receiverOrPayerName: receiverOrPayerName,
                                                receiverOrPayerNcCode: receiverOrPayerNcCode
                                            };
                                            Scdp.doAction("certificate-receiverOrPayerViewUpdata", postData, null, null, true, false);
                                            Scdp.MsgUtil.info("【NCCode】调整成功!");
                                        }
                                        else {
                                            Scdp.MsgUtil.info("【NCCode】取消调整成功!");
                                        }
                                    });
                                }
                                else {
                                    Scdp.MsgUtil.info("【NCCode】没做任何调整!");
                                }
                            }
                            else {
                                Scdp.MsgUtil.warn("【NCCode】不能为空!");
                                CheckNcCode(receiverOrPayerType, receiverOrPayerId, receiverOrPayerCode, receiverOrPayerName);
                            }
                        });
                    }
                }
                else {
                    Scdp.MsgUtil.warn("无对应【NCCode】的对方单位对象!");
                }
            }
            else {
                Scdp.MsgUtil.warn("【对方单位名称】的【单位类型】仅能为【供应商】、【客户】、【用户】!");
            }
        }
        else {
            Scdp.MsgUtil.warn("【NCCode】已选定!");
        }
    }
    else {
        Scdp.MsgUtil.warn("【对方单位名称】不能为空!");
    }
}

//设定对方单位Cookie
function setReceiverOrPayer(me) {
    receiverOrPayerType = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->receiverOrPayerType").gotValue());
    Erp.Util.SetCookie("receiverOrPayerType", receiverOrPayerType);

    receiverOrPayerId = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->receiverOrPayerId").gotValue());
    Erp.Util.SetCookie("receiverOrPayerId", receiverOrPayerId);

    receiverOrPayerCode = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->receiverOrPayerCode").gotValue());
    Erp.Util.SetCookie("receiverOrPayerCode", receiverOrPayerCode);

    receiverOrPayerName = Erp.Util.isNullReturnEmpty(me.view.getCmp("fadCertificateDto->receiverOrPayerName").gotValue());
    Erp.Util.SetCookie("receiverOrPayerName", receiverOrPayerName);

    if (
        (Erp.Util.isNullReturnEmpty(receiverOrPayerType) == "SCM_SUPPLIER" || Erp.Util.isNullReturnEmpty(receiverOrPayerType) == "PRM_CUSTOMER" || Erp.Util.isNullReturnEmpty(receiverOrPayerType) == "SCDP_USER")
        && (Erp.Util.isNullReturnEmpty(receiverOrPayerId) != '[$nc_code]')
        && (Erp.Util.isNullReturnEmpty(receiverOrPayerId).length > 0 || Erp.Util.isNullReturnEmpty(receiverOrPayerCode).length > 0 || Erp.Util.isNullReturnEmpty(receiverOrPayerName).length > 0)
    ) {
        /*var receiverOrPayerNcCode = "";
         var postData = {
         receiverOrPayerType: receiverOrPayerType,
         receiverOrPayerId: receiverOrPayerId,
         receiverOrPayerCode: receiverOrPayerCode,
         receiverOrPayerName: receiverOrPayerName,
         receiverOrPayerNcCode: receiverOrPayerNcCode
         };
         var actionResult = Scdp.doAction("certificate-receiverOrPayerViewLoad", postData, null, null, true, false);
         if (Erp.Util.isNullReturnEmpty(actionResult.receiverOrPayerNcCode).length > 0) {
         me.view.getCmp("editToolbar->nCCodeSet").setDisabled(true);
         }
         else {*/
        me.view.getCmp("editToolbar->nCCodeSet").setDisabled(false);
        //}
    }
    else {
        me.view.getCmp("editToolbar->nCCodeSet").setDisabled(true);
    }
}

function getDepartmentNameInFadReceiverOrPayerView(DepartmentVar) {
    try {
        return Erp.Util.isNullReturnEmpty(Erp.Util.replaceAll(Erp.Util.isNullReturnEmpty(Erp.Util.isNullReturnEmpty(DepartmentVar).split("][")[1]), "]", ""));
    } catch (e) {
        return Erp.Util.isNullReturnEmpty(DepartmentVar);
    }
}

var fadCertificateYears = Erp.Util.getCurrentYear();
var fadCertificateMonths = Erp.Util.getCurrentMonth();
var ncState = "0";
var deficitCertifiState = "1";
var copyAddState = "0";
var mergeSplitState = "0";
var bdCurrtypeChoiceCurrencyCode = "";
var accsubjRtfreevalueSubjectCode = "";
var accsubjRtfreevalueSubjectName = "";
var rtfreevalueAccountInfoCode = "";
var rtfreevalueAccountInfoName = "";
var rtfreevalueAccountNo = "";
var receiverOrPayerType = "";
var receiverOrPayerId = "";
var receiverOrPayerCode = "";
var receiverOrPayerName = "";
var oldSubjectCode = "";
var oldFadCertificateDetailFree2 = "";
var oldFadCertificateDetailFree3 = "";
var oldOrderNo = "";
var fadCertificateFree2 = "";
var fadCertificateFree3 = "";
var free2Keypress = "0";
var free3Keypress = "0";