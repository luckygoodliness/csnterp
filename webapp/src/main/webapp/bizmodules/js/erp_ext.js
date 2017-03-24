//*************************************************** 自定义组件 *******************************************************
//根据JQeryField封装的Jtest，可以省去controller/valueField/descField/defaultFields

Ext.override(Scdp.grid.JTextCol, {
    renderer: function (value, col, model, rowIdx, colIdx, store, view) {
        if (Scdp.ObjUtil.isNotEmpty(value)) {
            col.tdAttr = "data-qtip='" + value + "'";
            return value;
        } else {
            return null;
        }
    }
});

Ext.override(Scdp.grid.JCurCol, {
    renderer: function (value, col, model, rowIdx, colIdx, store, view) {
        if (this.JCurColConfig && "function" == typeof this.JCurColConfig) {
            this.JCurColConfig(value, col, model, rowIdx, colIdx, store, view);
        }
        return this.callParent(arguments);
    }
});
Ext.override(Scdp.grid.JDateCol, {
    renderer: function (value, col, model, rowIdx, colIdx, store, view) {
        if (this.JDateColConfig && "function" == typeof this.JDateColConfig) {
            this.JDateColConfig(value, col, model, rowIdx, colIdx, store, view);
        }
        return this.callParent(arguments);
    }
});
Ext.override(Scdp.grid.JComboCol, {
    renderer: function (value, col, model, rowIdx, colIdx, store, view) {
        if (this.JComboColConfig && "function" == typeof this.JComboColConfig) {
            this.JComboColConfig(value, col, model, rowIdx, colIdx, store, view);
        }
        return this.callParent(arguments);
    }
});
Ext.override(Scdp.grid.JGridFieldCol, {
    renderer: function (value, col, model, rowIdx, colIdx, store, view) {
        if (this.JGridFieldColConfig && "function" == typeof this.JGridFieldColConfig) {
            this.JGridFieldColConfig(value, col, model, rowIdx, colIdx, store, view);
        }
        return this.callParent(arguments);
    }
});

//测试
Ext.define('Sgl.Component.JTest', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JTest'],
    modulePath: "com/csnt/scdp/bizmodules/modules/testc/test",
    layoutFile: "testGridField-layout.xml",
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//采购包
Ext.define('Sgl.Component.JPurchasePackage', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JPurchasePackage'],
    modulePath: "com/csnt/scdp/bizmodules/modules/prm/purchaseplan",
    layoutFile: "grid-purchasepackage-query-layout.xml",
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//采购计划--采购包Grid
Ext.define('Sgl.Component.JPackageGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JPackageGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/prm/purchaseplan",
    layoutFile: "purchaseplan-backagegrid-query-layout.xml",
    valueField: 'code',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: 1,
    descField: 'name',
    valueField: 'code',
    readOnlyAsEditable: !0
});

//项目控件
Ext.define('Sgl.Component.JProject', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JProject'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "projectCode|projectName",
    menuCode: '',
    isPopup: false,
    displayDesc: true,
    valueField: 'projectName',
    descField: 'projectName',
    controller: "Projectmain.controller.ProjectmainQueryController",
    upperCase: false
});


//项目控件
Ext.define('Sgl.Component.JNoneFinishedProject', {
    extend: 'Sgl.Component.JProject',
    alias: ['widget.JNoneFinishedProject'],
    filterConditions: Ext.clone({filterFinishedProject_h: true})
});

//项目控件,数据源-开票申请
Ext.define('Sgl.Component.JProject2', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JProject2'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "projectCode|projectName",
    menuCode: '',
    isPopup: false,
    displayDesc: true,
    valueField: 'projectName',
    descField: 'projectName',
    controller: "Prmbilling.controller.PrmbillingDialogController",
    upperCase: false
});


//项目控件
Ext.define('Sgl.Component.JInnerProject', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JInnerProject'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "uuid|projectName",
    menuCode: '',
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: true,
    valueField: 'innerPurchaseReqId',
    descField: 'projectName',
//    codeValue: '',
    controller: "Projectmain.controller.InnerProjectQueryController",
    upperCase: false
});


//客户合同
Ext.define('Sgl.Component.JContract', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JContract'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "uuid|contractName",
    menuCode: '',
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: true,
    valueField: 'contractName',
    descField: 'contractName',
//    codeValue: '',
    controller: "Contract.controller.ContractQueryController",
    upperCase: false
});

//客户控件
Ext.define('Sgl.Component.JCustomer', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JCustomer'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "uuid|customerName",
    menuCode: '',
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'customerName',
    descField: 'customerName',
//    codeValue: '',
    controller: "Prmcustomer.controller.CustomerQueryController",
    upperCase: false
});

//供应商控件
Ext.define('Sgl.Component.JSupplier', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JSupplier'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "uuid|completeName",
    menuCode: '',
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'completeName',
    descField: 'completeName',
//    codeValue: '',
    controller: "Supplierinfor.controller.SupplierQueryController",
    upperCase: true
});

//采购合同
Ext.define('Sgl.Component.JSContract', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JSContract'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "uuid|scmContractCode",
    menuCode: '',
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'scmContractCode',
    descField: 'scmContractCode',
//    codeValue: '',
    controller: "Scmcontract.controller.ScmcontractQueryController",
    upperCase: true
});

//采购合同(简单的合同查询，不关联视图VW_PRM_BUDGET，已经过滤掉有完工结算数据的项目的合同)
Ext.define('Sgl.Component.JSContractQ', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JSContractQ'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "uuid|scmContractCode",
    menuCode: '',
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'scmContractCode',
    descField: 'scmContractCode',
//    codeValue: '',
    controller: "Scmcontract.controller.ScmcontractQController",
    upperCase: true
});

//固定资产卡片
Ext.define('Sgl.Component.JCard', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JCard'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    //defaultFields: "uuid|cardCode",
    defaultFields: "uuid|assetName",
    menuCode: '',
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'assetName',
    descField: 'cardCode',
//    codeValue: '',
    controller: "Card.controller.AssetCardQueryController",
    upperCase: true
});

//固定资产卡片2
Ext.define('Sgl.Component.JCard2', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JCard2'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "assetCode|assetName",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'assetName',
    descField: 'assetName',
    codeValue: '',
    controller: "Card.controller.AssetCard2QueryController",
    upperCase: true
});

//固定资产卡片3
Ext.define('Sgl.Component.JCard3', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JCard3'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    //defaultFields: "uuid|cardCode",
    defaultFields: "uuid|assetName",
    menuCode: '',
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'assetName',
    descField: 'cardCode',
//    codeValue: '',
    controller: "Card.controller.AssetCard3QueryController",
    upperCase: true
});

//预算类别
Ext.define('Sgl.Component.JFSubject', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JFSubject'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "subjectName|subjectName",
    menuCode: '',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'subjectName',
    descField: 'subjectName',
//    codeValue: '',
    controller: "Financialsubject.controller.FinancialsubjectQueryController",
    upperCase: true
});

//费用申请
Ext.define('Sgl.Component.JCash', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JCash'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "uuid|subjectName",
    menuCode: '',
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'uuid',
    descField: 'subjectName',
//    codeValue: '',
    controller: "Cashreq.controller.CashreqQueryController",
    upperCase: true
});

//部门控件
Ext.define('Sgl.Component.JOrg', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JOrg'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "uuid|orgName",
    menuCode: '',
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'uuid',
    descField: 'orgName',
//    codeValue: '',
    controller: "Scdp.controller.CustomerQueryController",
    upperCase: true
});

//部门控件
Ext.define('Sgl.Component.JScdpOrg', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JScdpOrg'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "orgCode|orgName",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'orgName',
    descField: 'orgName',
    codeValue: '',
    controller: "Certificatesetrule.controller.ScdpOrgController",
    upperCase: true
});

//部门控件Grid
Ext.define('Sgl.Component.JScdpOrgGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JScdpOrgGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/certificatesetrule",
    layoutFile: "scdporg-query-layout.xml",
    valueField: 'orgName',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 310,
    gridHeight: 500,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//物料分类
Ext.define('Sgl.Component.JMaterial', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JMaterial'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "name|name",
    menuCode: '',
//    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'name',
    descField: 'name',
//    codeValue: '',
    controller: "Materialclass.controller.MaterialclassQueryController",
    upperCase: true
});

//根据JQeryField封装的JUser，可以省去controller/valueField/descField/defaultFields
Ext.define('Sgl.Component.JUser', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JUser'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "userId|userName",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: true,
    valueField: 'userId',
    descField: 'userName',
    codeValue: '',
    controller: "SysUser.controller.ScdpUserSearchController",
    upperCase: true
});

//币种
Ext.define('Sgl.Component.JCurrency', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JCurrency'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "currencyCode|currencyDesc",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'currencyDesc',
    descField: 'currencyDesc',
    codeValue: '',
    controller: "Certificate.controller.BdCurrtypeChoiceController",
    upperCase: true
});

//科目
Ext.define('Sgl.Component.JAccsubj', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JAccsubj'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "subjectCode|subjectName",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'subjectName',
    descField: 'subjectName',
    codeValue: '',
    controller: "Certificatesetrule.controller.AccsubjController",
    upperCase: true
});

//科目辅助核算项
Ext.define('Sgl.Component.JAccsubjRtfreevalue', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JAccsubjRtfreevalue'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "subjectCode|subjectName",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'subjectName',
    descField: 'subjectName',
    codeValue: '',
    controller: "Certificate.controller.AccsubjRtfreevalueController",
    upperCase: true
});

//辅助核算
Ext.define('Sgl.Component.JRtfreevalue', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JRtfreevalue'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "accountInfoCode|accountInfoName",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'accountInfoName',
    descField: 'accountInfoName',
    codeValue: '',
    controller: "Certificate.controller.RtfreevalueController",
    upperCase: true
});

//收付款对象
Ext.define('Sgl.Component.JReceiverOrPayer', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JReceiverOrPayer'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "receiverOrPayerCode|receiverOrPayerName",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'receiverOrPayerName',
    descField: 'receiverOrPayerName',
    codeValue: '',
    controller: "Certificate.controller.ReceiverOrPayerController",
    upperCase: true
});

//非项目费用科目
Ext.define('Sgl.Component.JNonProjectSubjectSubject', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JNonProjectSubjectSubject'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "financialSubjectCode|financialSubject",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'financialSubject',
    descField: 'financialSubject',
    codeValue: '',
    controller: "Certificatesetrule.controller.NonProjectSubjectSubjectController",
    upperCase: false
});

//非项目费用科目2
Ext.define('Sgl.Component.JNonProjectSubjectSubject2', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JNonProjectSubjectSubject2'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "financialSubjectCode|financialSubject",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'financialSubject',
    descField: 'financialSubject',
    codeValue: '',
    controller: "Certificatesetrule.controller.NonProjectSubjectSubject2Controller",
    upperCase: false
});

//项目费用科目
Ext.define('Sgl.Component.JFadBaseSubject', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JFadBaseSubject'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "subjectNo|subjectName",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'subjectName',
    descField: 'subjectName',
    codeValue: '',
    controller: "Certificatesetrule.controller.FadBaseSubjectController",
    upperCase: false
});

//代号类型
Ext.define('Sgl.Component.JPrmCodeTypePp', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JPrmCodeTypePp'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "prmCodeType|prmCodeTypeName",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'prmCodeTypeName',
    descField: 'prmCodeTypeName',
    codeValue: '',
    controller: "Certificatesetrule.controller.PrmCodeTypePpController",
    upperCase: false
});

//NC部门
Ext.define('Sgl.Component.JNcOrg', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JNcOrg'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "ncOrgCode|ncOrgName",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'ncOrgName',
    descField: 'ncOrgName',
    codeValue: '',
    controller: "Ncorg.controller.NcOrgController",
    upperCase: true
});

//合同明细
Ext.define('Sgl.Component.JContractDetail', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JContractDetail'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "uuid|scmContractId",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'scmContractId',
    descField: 'scmContractId',
    codeValue: '',
    controller: "Scmcontract.controller.ScmPickContractDetailController",
    upperCase: true
});

//辅助核算
Ext.define('Sgl.Component.JOperateBusiness', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JOperateBusiness'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "projectName",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: true,
    valueField: 'uuid',
    descField: 'projectName',
    codeValue: '',
    controller: "Businessbidinfo.controller.BusinessbidinfoQueryController",
    upperCase: false
});

//采购申请明细
Ext.define('Sgl.Component.JPrmPurchaseReqDetail', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JPrmPurchaseReqDetail'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "purchaseReqNo|purchaseReqNo",
    menuCode: '',
    readOnlyAsEditable: true,
    isPopup: false,
    displayDesc: false,
    valueField: 'purchaseReqNo',
    descField: 'purchaseReqNo',
    codeValue: '',
    controller: "Card.controller.PrmPurchaseReqDetailController",
    upperCase: true
});

//项目控件Grid
Ext.define('Sgl.Component.JProjectGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JProjectGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/prm/projectmain",
    layoutFile: "projectmain-grid-query-layout.xml",
    valueField: 'projectName',
    filterConditions: Ext.clone({'selfconditions': '1=1'}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//重点经营项目Grid
Ext.define('Sgl.Component.JMajorProjectGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JMajorProjectGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/operate/operatekeyprojectsinfo",
    layoutFile: "operatekeyprojectsinfo-grid-query-layout.xml",
    valueField: 'projectName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//客户控件Grid
Ext.define('Sgl.Component.JCustomerGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JCustomerGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/prm/prmcustomer",
    layoutFile: "jprmcustomer-grid-query-layout.xml",
    valueField: 'customerName',
    filterConditions: Ext.clone({'selfconditions': '1=1'}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//到货地址控件Grid
Ext.define('Sgl.Component.JArriveLocationGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JArriveLocationGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/prm/prmpurchasereq",
    layoutFile: "jarrdivelocation-grid-query-layout.xml",
    valueField: 'arriveLocation',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 300,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0,
    forceValidate: !1
});

//电商名称控件Grid
//Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE)
Ext.define('Sgl.Component.JEbunsinessGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JEbunsinessGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/cashreq",
    layoutFile: "jebusiness-grid-query-layout.xml",
    valueField: 'ebusinessName',
    filterConditions: Ext.clone({selfconditions: " office_id = '" + Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE) + "'"}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 300,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//银行控件Grid
Ext.define('Sgl.Component.JBankGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JBankGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/prm/prmcustomer",
    layoutFile: "jprmcustomerbank-grid-query-layout.xml",
    valueField: 'bankName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//供应商控件Grid
Ext.define('Sgl.Component.JSupplierGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JSupplierGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/scm/supplierinfor",
    layoutFile: "jsupplierinfor-grid-query-layout.xml",
    valueField: 'completeName',
    filterConditions: Ext.clone({selfconditions: '1 = 1'}),
    queryDelay: 1500,
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//供应商和客商数据组合控件Grid
Ext.define('Sgl.Component.JSFSupplierGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JSFSupplierGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/supplierinfo",
    layoutFile: "jsupplierinfo-grid-query-layout.xml",
    valueField: 'completeName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0,
    forceValidate: !1
});

//项目合同Grid，没有权限过滤，如果需要过滤，则使用Jquery类型
Ext.define('Sgl.Component.JPContractGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JPContractGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/prm/contract",
    layoutFile: "jcontract-grid-query-layout.xml",
    valueField: 'uuid',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//采购合同Grid
Ext.define('Sgl.Component.JSContractGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JSContractGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/scm/scmcontract",
    layoutFile: "jscmcontract-grid-query-layout.xml",
    valueField: 'scmContractCode',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//固定资产卡片Grid
Ext.define('Sgl.Component.JCardGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JCardGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/asset/card",
    layoutFile: "jcard-grid-query-layout.xml",
    valueField: 'assetName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//预算类别Grid
Ext.define('Sgl.Component.JFSubjectGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JFSubjectGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/nonprm/financialsubject",
    layoutFile: "jfinancialsubject-grid-query-layout.xml",
    valueField: 'subjectName',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 350,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//费用申请Grid
Ext.define('Sgl.Component.JCashGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JCashGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/cashreq",
    layoutFile: "jcashreq-grid-query-layout.xml",
    valueField: 'subjectName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//物料细目Grid
Ext.define('Sgl.Component.JMaterialItemGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JMaterialItemGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/scm/materialitem",
    layoutFile: "materialitem-grid-query-layout.xml",
    valueField: 'name',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//物料分类Grid
Ext.define('Sgl.Component.JMaterialGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JMaterialGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/scm/materialclass",
    layoutFile: "materialclass-grid-query-layout.xml",
    valueField: 'name',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//用户Grid
Ext.define('Sgl.Component.JUserGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JUserGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/erpsys/erpuser",
    layoutFile: "erpuser-grid-query-layout.xml",
    filterConditions: Ext.clone({orgfilter: ' 1=1 '}),
    valueField: 'userName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 380,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: 1,
    readOnlyAsEditable: !0
});

//考评方案
Ext.define('Sgl.Component.JCaseTypeGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JCaseTypeGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/scm/scmsaecase",
    layoutFile: "scmsaecase-grid-query-layout.xml",
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    valueField: 'uuid',
    descField: 'caseName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 400,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//考评人方案
Ext.define('Sgl.Component.JAppraiserCaseGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JAppraiserCaseGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/scm/scmsaeappraisercase",
    layoutFile: "scmsaeappraisercase-grid-query-layout.xml",
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    valueField: 'uuid',
    descField: 'caseName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 350,
    gridHeight: 300,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//币种Grid
Ext.define('Sgl.Component.JCurrencyGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JCurrencyGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/certificate",
    layoutFile: "bdcurrtypechoice-grid-query-layout.xml",
    valueField: 'currencyDesc',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//科目Grid
Ext.define('Sgl.Component.JAccsubjGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JAccsubjGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/certificatesetrule",
    layoutFile: "accsubj-grid-query-layout.xml",
    valueField: 'subjectName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//科目辅助核算项Grid
Ext.define('Sgl.Component.JAccsubjRtfreevalueGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JAccsubjRtfreevalueGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/certificate",
    layoutFile: "accsubjrtfreevalue-grid-query-layout.xml",
    valueField: 'subjectName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//合同明细Grid
Ext.define('Sgl.Component.JContractDetailGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JContractDetailGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/scm/scmcontract",
    layoutFile: "contractDetail-grid-query-layout.xml",
    valueField: 'scmContractId',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//辅助核算Grid
Ext.define('Sgl.Component.JRtfreevalueGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JRtfreevalueGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/certificate",
    layoutFile: "rtfreevalue-grid-query-layout.xml",
    valueField: 'accountInfoName',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//收付款对象Grid
Ext.define('Sgl.Component.JReceiverOrPayerGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JReceiverOrPayerGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/certificate",
    layoutFile: "receiverorpayer-grid-query-layout.xml",
    valueField: 'receiverOrPayerName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//非项目费用科目Grid
Ext.define('Sgl.Component.JNonProjectSubjectSubjectGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JNonProjectSubjectSubjectGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/certificatesetrule",
    layoutFile: "nonprojectsubjectsubject-grid-query-layout.xml",
    valueField: 'financialSubject',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//非项目费用科目2Grid
Ext.define('Sgl.Component.JNonProjectSubjectSubject2Grid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JNonProjectSubjectSubject2Grid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/certificatesetrule",
    layoutFile: "nonprojectsubjectsubject2-grid-query-layout.xml",
    valueField: 'financialSubject',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//项目费用科目Grid
Ext.define('Sgl.Component.JFadBaseSubjectGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JFadBaseSubjectGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/certificatesetrule",
    layoutFile: "fadbasesubject-grid-query-layout.xml",
    valueField: 'subjectName',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//代号类型Grid
Ext.define('Sgl.Component.JPrmCodeTypePpGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JPrmCodeTypePpGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/fad/certificatesetrule",
    layoutFile: "prmcodetypepp-grid-query-layout.xml",
    valueField: 'prmCodeTypeName',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//非项目预算Grid
Ext.define('Sgl.Component.JNonprmbudgetGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JNonprmbudgetGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/nonprm/budget",
    layoutFile: "nonprmbudget-grid-query-layout.xml",
    valueField: 'subjectCode',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//非项目预算Grid
Ext.define('Sgl.Component.JNonprmbudgetGrid2', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JNonprmbudgetGrid2'],
    modulePath: "com/csnt/scdp/bizmodules/modules/nonprm/budget",
    layoutFile: "nonprmbudget-grid-query-layout2.xml",
    valueField: 'subjectCode',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//项目预算Grid
Ext.define('Sgl.Component.JPrmbudgetGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JPrmbudgetGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/prm/prmprojectmainc",
    layoutFile: "prm-grid-budget-query-layout.xml",
    valueField: 'budgetName',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

Ext.define('Sgl.Component.JPrmPackageGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JPrmPackageGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/prm/prmpurchasereq",
    layoutFile: "prmpurchase-package-query-layout.xml",
    valueField: 'packageName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});


//PRM_PROJECT_MAIN_ID
Ext.define('Sgl.Component.JPrmProjectMainIdGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JPrmProjectMainIdGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/prm/prmpurchasereq",
    layoutFile: "prmpurchase-projectmainid-query-layout.xml",
    valueField: 'projectName',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//电商用户控件
Ext.define('Sgl.Component.JScmEbusinessUserGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JScmEbusinessUserGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/scm/scmebusinessuser",
    layoutFile: "jscmebusinessuser-query-layout.xml",
    valueField: 'uuid',
    descField: 'userCode',
    filterConditions: Ext.clone({selfconditions: ' 1=1 '}),
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//采购申请明细Grid
Ext.define('Sgl.Component.JPrmPurchaseReqDetailGrid', {
    extend: 'Scdp.form.JGridField',
    alias: ['widget.JPrmPurchaseReqDetailGrid'],
    modulePath: "com/csnt/scdp/bizmodules/modules/asset/card",
    layoutFile: "prmpurchasereqdetail-grid-query-layout.xml",
    valueField: 'purchaseReqNo',
    enableKeyEvents: !0,
    matchFieldWidth: !1,
    gridWidth: 500,
    gridHeight: 200,
    pageSize: 10,
    displayDesc: !1,
    readOnlyAsEditable: !0
});

//ERP文件下载模块函数
Erp.FileUtil.erpFilePreview = function (fileGrid) {
    var curRecord = fileGrid.getCurRecord();
    if (curRecord) {
        var filePreviewUrl = Scdp.getSysConfig("file_preview_url");
        if (Scdp.ObjUtil.isEmpty(filePreviewUrl)) {
            Scdp.MsgUtil.info("请配置文件预览服务器！");
            return;
        }
        var fileType = curRecord.get("fileType");
        var previewFileTypes = ['doc', 'xls', 'ppt', 'docx', 'xlsx', 'pptx', 'pdf', 'txt'];
        var picFileTypes = ['jpg', 'jpeg', 'gif', 'png', 'bmp'];
        if (Scdp.ObjUtil.isNotEmpty(fileType) && Ext.Array.contains(previewFileTypes, fileType.toLowerCase())) {
            var postData = {};
            var fileList = [curRecord.get("fileId")];
            postData.fileList = fileList;
            Scdp.doAction("erp-common-file-download", postData, function (result) {
                if (result && result.URL_LIST) {
                    var previewUrl = filePreviewUrl + "?ft=" + fileType + "&url=" + escape(encodeURI(result.URL_LIST[0]).replace(/\//g, "%2F"));
                    window.open(previewUrl);
                }
            });
        } else if (Scdp.ObjUtil.isNotEmpty(fileType) && Ext.Array.contains(picFileTypes, fileType.toLowerCase())) {
            var postData = {};
            var fileList = [curRecord.get("fileId")];
            postData.fileList = fileList;
            Scdp.doAction("erp-common-file-download", postData, function (result) {
                if (result && result.URL_LIST) {
                    var previewUrl = Scdp.getSysConfig("base_path") + "bizmodules/jsp/previewPic.jsp"
                        + "?picUrl=" + escape(encodeURI(result.URL_LIST[0]).replace(/\//g, "%2F"));
                    window.open(previewUrl);
                }
            });
        } else {
            Scdp.MsgUtil.info("该文件类型不支持预览！");
        }
    } else {
        Scdp.MsgUtil.info("未选择记录");
    }
};

//ERP文件下载模块函数
Erp.FileUtil.erpFileDownLoad = function (fileGrid) {
    var selectRecord = fileGrid.getSelectionModel().getSelection();
    if (selectRecord.length > 0) {
        var postData = {};
        var fileList = [];
        for (var i = 0; i < selectRecord.length; i++) {
            var fileId = selectRecord[i].data.fileId;
            fileList.push(fileId);
        }
        postData.fileList = fileList;
        var ret = Scdp.doAction("erp-common-file-download", postData, null, null, false, false);
        var urlList = ret.URL_LIST;
        Ext.each(urlList, function (item) {
            window.open(item);
        })
    } else {
        Scdp.MsgUtil.info("未选择记录");
    }
};

Erp.FileUtil.erpFileUpload = function (fileGrid, fileClassify, afterCallback, beforeCallback, fileCmpConfig, menuCode) {
    var itemsArray = [];
    var height = 150;
    var fileObj = {
        xtype: "JFile",
        fieldLabel: "文件名",
        cid: "uploadFile",
        allowBlank: !1,
        listeners: {
            change: function (a) {
                a = a.gotValue();
                a = Scdp.StrUtil.getLastSplit(a, "\\");
                a = Scdp.StrUtil.replaceNull(Scdp.StrUtil.getLastSplit(a,
                    ".")).toUpperCase();
                this.up("JForm").getCmp("fileType").sotValue(a);
            }
        }
    };
    if (Scdp.ObjUtil.isNotEmpty(fileCmpConfig)) {
        Ext.Object.merge(fileObj, fileCmpConfig);
    }
    itemsArray.push(fileObj);
    itemsArray.push({xtype: "JText", fieldLabel: "文件类型", cid: "fileType", hidden: true});
    if (Scdp.ObjUtil.isNotEmpty(fileClassify)) {
        if (fileClassify == "CLASSIFY_FOR_SCM_SUPPLIER") {
            itemsArray.push({
                xtype: "JCombo",
                fieldLabel: "业务文件类型",
                id: "fileClassify",
                cid: "fileClassify",
                menuCode: "SUPPLIERINFOR",
                comboType: "scdp_fmcode",
                codeType: "CDM_FILE_TYPE_DETAIL",
                allowBlank: !1,
                fullInfo: !0,
                displayDesc: !0,
                labelAlign: "right"
            });
            itemsArray.push({xtype: "JDate", fieldLabel: "有效期时间从", cid: "begindate"});
            itemsArray.push({xtype: "JDate", fieldLabel: "有效期时间到", cid: "enddate"});
            height = height + 60;
        } else if (fileClassify == "CDM_FILE_TYPE_DETAIL") {
            //如果使用该代码必须传入menuCode参数
            itemsArray.push({
                xtype: "JCombo",
                fieldLabel: "业务文件类型",
                id: "fileClassify",
                cid: "fileClassify",
                menuCode: menuCode,
                comboType: "scdp_fmcode",
                codeType: fileClassify,
                allowBlank: !1,
                fullInfo: !0,
                displayDesc: !0,
                labelAlign: "right"
            });
            height = height + 20;
        } else {
            itemsArray.push({xtype: "JHidden", cid: "fileClassify", value: fileClassify});
            height = height + 20;
        }
    }
    itemsArray.push({xtype: "JTextArea", fieldLabel: "备注", cid: "fileRemark"});
    if (Scdp.ObjUtil.isNotEmpty(fileCmpConfig) && Scdp.ObjUtil.isNotEmpty(fileCmpConfig.regexText)) {
        itemsArray.push({xtype: 'JDisplay', value: fileCmpConfig.regexText, fieldStyle: {color: 'red'}});
    }
    Ext.define("JFileUploadView0", {
        extend: "Scdp.container.JFileUploadView",
        height: height,
        alias: ["widget.JFileUploadView0"],
        fileUploadAction: "erp-common-file-upload",
        items: itemsArray,
        okFunction: function (a) {
            if (!a.validate())return !1;
            var postData = a.gotValue();
            var begindate = "";
            var enddate = "";
            if (Scdp.ObjUtil.isNotEmpty(a.getCmp("begindate"))) {
                begindate = a.getCmp("begindate").gotValue();
            }
            if (Scdp.ObjUtil.isNotEmpty(a.getCmp("enddate"))) {
                enddate = a.getCmp("enddate").gotValue();
            }
            //todo need convert date format in server
            if ((!beforeCallback) || beforeCallback(postData)) {
                Scdp.doAction(a.fileUploadAction, postData, function (result) {
                    if (result != "" || result != undefined) {
                        if (typeof afterCallback == 'function') {
                            result.fileData.begindate = begindate;
                            result.fileData.enddate = enddate;
                            afterCallback(fileGrid, result.fileData);
                        } else {
                            Erp.FileUtil.initFileUploadData(fileGrid, result.fileData);
                        }
                    }
                    a.up("window").close();
                    Scdp.MsgUtil.info("上传成功");
                }, null, null, null, a.getForm());
            }
        },
        cancelFunc: null
    });
    Scdp.openNewWinByView0(Ext.widget("JFileUploadView0"));
};

Erp.FileUtil.erpFileDelete = function (fileGrid, beforeCallback, isPersistence) {
    if ((!beforeCallback) || beforeCallback()) {
        var c = fileGrid.getStore();
        var b = fileGrid.getSelectionModel().getSelection();
        if (b.length > 0) {
            if (isPersistence) {
                //todo need goto server
                var uuids = [];
                Ext.Array.each(b, function (item) {
                    uuids.push(item.get("uuid"));
                });
                var postdata = {uuids: uuids};
                Scdp.doAction("erp-common-file-delete", postdata, function (result) {
                    Scdp.MsgUtil.info("删除成功！");
                    c.remove(b);
                });
            } else {
                c.remove(b);
            }
        } else {
            Scdp.MsgUtil.info("未选择记录");
        }
    }
};
//根据回传的数据，在页面上显示相关信息
Erp.FileUtil.initFileUploadData = function (fileGrid, obj) {
    obj.fileId = obj.uuid;
    obj.module = Scdp.getActiveModule().controller.menuCode;
    obj.cdmFileType = fileGrid.cdmFileType;
    if (!obj.needPersistence) {
        delete obj["uuid"];
        delete obj["editflag"];
    } else {
        obj["uuid"] = obj["cdmFileRelationId"];
    }
    fileGrid.addRowItem(obj, false);
};

Ext.define('Scdp.form.JMonth', {
    extend: 'Scdp.form.JDate',
    alias: 'widget.JMonth',
    requires: ['Ext.picker.Month'],
    alternateClassName: ['Ext.form.MonthField', 'Ext.form.Month'],
    selectMonth: null,
    format: 'Y-m-d',
    lastday: false,
    createPicker: function () {
        var me = this,
            format = Ext.String.format;
        return Ext.create('Ext.picker.Month', {
            pickerField: me,
            ownerCt: me.ownerCt,
            renderTo: document.body,
            floating: true,
            hidden: true,
            focusOnShow: true,
            minDate: me.minValue,
            maxDate: me.maxValue,
            disabledDatesRE: me.disabledDatesRE,
            disabledDatesText: me.disabledDatesText,
            disabledDays: me.disabledDays,
            disabledDaysText: me.disabledDaysText,
            format: me.format,
            showToday: me.showToday,
            startDay: me.startDay,
            minText: format(me.minText, me.formatDate(me.minValue)),
            maxText: format(me.maxText, me.formatDate(me.maxValue)),
            listeners: {
                select: {scope: me, fn: me.onSelect},
                monthdblclick: {scope: me, fn: me.onOKClick},
                yeardblclick: {scope: me, fn: me.onOKClick},
                OkClick: {scope: me, fn: me.onOKClick},
                CancelClick: {scope: me, fn: me.onCancelClick}
            },
            keyNavConfig: {
                esc: function () {
                    me.collapse();
                }
            }
        });
    },
    onCancelClick: function () {
        var me = this;
        me.selectMonth = null;
        me.collapse();
    },
    onOKClick: function () {
        var me = this;
        if (me.selectMonth) {
            me.setValue(me.selectMonth);
            me.fireEvent('select', me, me.selectMonth);
        }
        me.collapse();
    },
    onSelect: function (m, d) {
        var me = this;
        var month = d[0];
        var dayNum = '/1/';
        if (me.lastday) {
            dayNum = '/31/';
            if (month == 1) {
                dayNum = '/28/';
            } else if (month == 3 || month == 5 || month == 8 || month == 10) {
                //小月的天数
                dayNum = '/30/';
            }
        }
        me.selectMonth = new Date(( month + 1 ) + dayNum + +d[1]);
    },
    gotValue: function () {
        var me = this;
        return me.selectMonth;
    }
});

//项目控件
Ext.define('Sgl.Component.JUnknownReceipt', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JUnknownReceipt'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "uuid|receiptNo",
    menuCode: '',
    defaultSearchFields: '',
    afterWinShowFn: function (me, winCtl) {
        var filter = me.defaultSearchFields.split(",");
        var upForm = me.getUpForm();
        if (upForm) {
            winCtl.down('panel').getCmp('payerDesc').sotValue(upForm.getCmp('payerDesc').gotValue());
            winCtl.down('panel').getCmp('payer').sotValue(upForm.getCmp('payer').gotValue());
        }
        //Ext.each(filter, function (item) {
        //    var itemMapping = item.split("|");
        //    var value = upForm.getCmp(itemMapping[0]).gotValue();
        //    winCtl.down('panel').getCmp(itemMapping[1]).sotValue(value);
        //});
    },
    isPopup: false,
    displayDesc: true,
    valueField: 'uuid',
    descField: 'receiptNo',
    controller: "Prmunknownreceipts.controller.PrmunknownreceiptsPopupController",
    upperCase: false
});//项目控件

Ext.define('Sgl.Component.JErpDeptCode', {
    extend: 'Ext.ux.TreeCombo',
    alias: ['widget.JErpDeptCode'],
    store: 'SysOrg.store.ScdpDepartTreeStore',
    canSelectFolders: true,
    selectChildren: false,
    valueField: 'orgCode',
    displayField: 'orgName',
    displayDesc: true,
    editable: true,
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
        me.on("expand", function () {
            var rootNode = me.tree.getRootNode();
            if (rootNode && rootNode.childNodes) {
                me.tree.expandNode(rootNode.childNodes[0]);
            }
        });
    }
});

Ext.define('Sgl.Component.JErpDepartMent', {
    extend: 'Scdp.form.JComboTreeField',
    alias: ['widget.JErpDepartMent'],
    store: 'SysOrg.store.ScdpDepartTreeStore',
    multiSelect: false,
    displayDesc: true,
    onlyLeafCheckable: true,
    wHeight: 400,
    wWidth: 400,
    storeAction: 'sys-org-treeload',
    actionParams: {orgType: 'D'},
    codeField: 'orgCode',
    descField: 'orgName',
    showTools: false,
    withSearchBar: false,
    treeWidth: 250,
    treeHeight: 330,
    autoExpand: true,
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    }/*,
     createPicker:function(){
     var me=this;
     this.callParent(arguments);
     me.picker.store.on("datachanged", function () {
     var rootNode = me.picker.getRootNode();
     if (rootNode && rootNode.childNodes) {
     Ext.Array.each(rootNode.childNodes,function(item){
     me.picker.expandNode(item);
     });
     }
     });
     }*/
});


//项目合同选择界面
Ext.define('Sgl.Component.JSelContract', {
    extend: 'Scdp.form.JQueryField',
    alias: ['widget.JSelContract'],
    multiSelect: false,
    callbackFn: null,
    afterCallbackFn: null,
    afterWinShowFn: null,
    delimiter: '|',
    defaultFields: "uuid|scmContractCode",
    menuCode: '',
    isPopup: false,
    displayDesc: false,
    valueField: 'scmContractCode',
    descField: 'scmContractCode',
    controller: "Receipts.controller.PrmContractQueryController",
    upperCase: true
});

//五星评价选择界面
/*
 表格转码显示图片例子
 grid.getColumnBydataIndex("purchaseReqNo").renderer = function (value, p, record) {
 return Ext.String.format(
 Scdp.FiveStarWin.getImgStr(value)
 );
 };
 * */
Ext.define('Sgl.Component.JFiveStar', {
    extend: 'Ext.form.field.Trigger',
    mixins: ["Scdp.form.IField"],
    alias: ['widget.JFiveStar'],
    editable: false,
    hideTrigger: false,
    scoreRange: ["", "很差", "比较差", "一般", "比较好", "很好"],
    star_red_url: "star_red_icon",
    star_url: "star_icon",
    valueField: "code",
    codeValue: 0,
    /*over()是鼠标移过事件的处理方法*/
    over: function (param, prefix) {
        this.setScore(param, prefix + "_star");
        $("#" + prefix + "_message").html(this.scoreRange[param]);
    },
    out: function (prefix) {
        this.setScore(this.codeValue, prefix + "_star");
        $("#" + prefix + "_message").html("");
    },
    setScore: function (score, id) {
        if (score == 0) {
            $("#" + id + "1").attr("class", this.star_url);
            $("#" + id + "2").attr("class", this.star_url);
            $("#" + id + "3").attr("class", this.star_url);
            $("#" + id + "4").attr("class", this.star_url);
            $("#" + id + "5").attr("class", this.star_url);
        } else {
            for (var i = 1; i <= 5; i++) {
                if (i <= score)
                    $("#" + id + i).attr("class", this.star_red_url);
                else
                    $("#" + id + i).attr("class", this.star_url);
            }
        }
    },
    click: function (param, prefix) {
        this.codeValue = param;//记录当前打分
        this.out(prefix);//设置星星数
    },
    setValue: function (a) {
        this.codeValue = a;
        this.setRawValue(a);
    },
    sotCodeAndDesc: function (a, b) {
        if (this.isInForm()) {
            var c = this.getUpForm().getCmp(this.getDescCid());
            Scdp.ObjUtil.isNotEmpty(c) && c.sotValue(b);
            this.sotValue(a);
            this.oldValue = this.gotRawValue();
            this.focus()
        } else {
            var c = this.getUpGrid(), d = c.prepareEditContext.record;
            d.set(this.getDescCid(), b);
            d.set(c.prepareEditContext.field, a);
            c.startEditByPosition(c.prepareEditContext.rowIdx, c.prepareEditContext.colIdx);
            this.sotValue(a);
            this.oldValue = this.gotRawValue()
        }
    },
    getValue: function () {
        var a = this.codeValue;
        return Scdp.ObjUtil.isNotEmpty(a) && this.upperCase ? a.toUpperCase() : a
    },
    onTriggerClick: function () {
        var a = this;
        var htmlTem = '<tr>' +
            '<td><a score="1" class="star_icon" style="margin: 5px" id="@_star1" >&nbsp;&nbsp;&nbsp;</a>' +
            '<a score="2" class="star_icon" style="margin: 5px" id="@_star2"  >&nbsp;&nbsp;&nbsp;</a>' +
            '<a score="3" class="star_icon" style="margin: 5px" id="@_star3"  >&nbsp;&nbsp;&nbsp;</a>' +
            '<a score="4" class="star_icon" style="margin: 5px" id="@_star4"  >&nbsp;&nbsp;&nbsp;</a>' +
            '<a score="5" class="star_icon" style="margin: 5px"  id="@_star5" >&nbsp;&nbsp;&nbsp;</a>' +
            '<span id="@_message" style="margin: 5px"></span></td>' +
            '</tr>';
        var window = Ext.create('Ext.window.Window', {
            width: 200,
            height: 90,
            layout: 'border',
            items: [
                {
                    xtype: 'panel',
                    frame: true,
                    modal: true,
                    id: "fiveStar_html",
                    width: 200,
                    height: 90
                }
            ],
            listeners: {
                show: function () {
                    $("#fiveStar_html").html(htmlTem.replace(/@/g, 'dialog'));
                    for (var i = 1; i <= 5; i++) {
                        $("#dialog_star" + i).bind("click", function () {
                            a.click(this.getAttribute("score"), 'dialog');
                        });
                        $("#dialog_star" + i).bind("mouseover", function () {
                            a.over(this.getAttribute("score"), 'dialog');
                        });
                        $("#dialog_star" + i).bind("mouseout", function () {
                            a.out('dialog');
                        });
                    }
                    a.click(a.getValue(), 'dialog');
                }
            },
            buttons: [
                {
                    text: '确认',
                    handler: function () {
                        a.sotCodeAndDesc(a.codeValue, a.codeValue);
                        window.close();
                    }
                }
            ]
        });
        window.show();
    }
});

Erp.MathUtil.multiNumber = function (n1, n2, scale, retNullWhenEmpty) {
    if (retNullWhenEmpty && Scdp.ObjUtil.isEmpty(n1) && Scdp.ObjUtil.isEmpty(n2)) {
        return null;
    }
    if (Scdp.ObjUtil.isEmpty(n1)) {
        n1 = 0;
    } else if (Ext.isString(n1)) {
        n1 = Number(n1);
    }
    if (Scdp.ObjUtil.isEmpty(n2)) {
        n2 = 0;
    } else if (Ext.isString(n2)) {
        n2 = Number(n2);
    }
    if (isNaN(scale)) {
        scale = 2;
    }
    var pow = Math.pow(10, scale);
    var place1 = 0;
    var place2 = 0;
    var n1Str = n1 + "";
    var n2Str = n2 + "";
    if (n1Str.indexOf(".") > -1) {
        place1 = n1Str.substr(n1Str.indexOf(".") + 1).length;
    }
    if (n2Str.indexOf(".") > -1) {
        place2 = n2Str.substr(n2Str.indexOf(".") + 1).length;
    }
    var offset1 = Math.pow(10, place1);
    var offset2 = Math.pow(10, place2);
    return Math.round((n1 * offset1) * (n2 * offset2) / (offset1 * offset2) * pow) / pow;
};
Erp.MathUtil.divideNumber = function (n1, n2, scale, retNullWhenEmpty) {
    if (retNullWhenEmpty && Scdp.ObjUtil.isEmpty(n1)) {
        return null;
    }

    if (Scdp.ObjUtil.isEmpty(n1)) {
        n1 = 0;
    } else if (Ext.isString(n1)) {
        n1 = Number(n1);
    }
    if (n1 == 0) {
        return 0;
    }

    if (Scdp.ObjUtil.isNotEmpty(n2) && Ext.isString(n2)) {
        n2 = Number(n2);
    }
    if (Scdp.ObjUtil.isEmpty(n2) || n2 == 0) {
        throw "number is invalid";
    }
    if (isNaN(scale)) {
        scale = 2;
    }
    var pow = Math.pow(10, scale);
    var maxPlace = 0;
    var n1Str = n1 + "";
    var n2Str = n2 + "";
    if (n1Str.indexOf(".") > -1) {
        maxPlace = Math.max(maxPlace, n1Str.substr(n1Str.indexOf(".") + 1).length);
    }
    if (n2Str.indexOf(".") > -1) {
        maxPlace = Math.max(maxPlace, n2Str.substr(n2Str.indexOf(".") + 1).length);
    }
    var offset = Math.pow(10, maxPlace);
    return Math.round((n1 * offset * pow) / (n2 * offset)) / pow;
};
Erp.MathUtil.plusNumber = function (n1, n2, retNullWhenEmpty) {
    if (retNullWhenEmpty && Scdp.ObjUtil.isEmpty(n1) && Scdp.ObjUtil.isEmpty(n2)) {
        return null;
    }
    if (Scdp.ObjUtil.isEmpty(n1)) {
        n1 = 0;
    } else if (Ext.isString(n1)) {
        n1 = Number(n1);
    }
    if (Scdp.ObjUtil.isEmpty(n2)) {
        n2 = 0;
    } else if (Ext.isString(n2)) {
        n2 = Number(n2);
    }
    var maxPlace = 0;
    var n1Str = n1 + "";
    var n2Str = n2 + "";
    if (n1Str.indexOf(".") > -1) {
        maxPlace = Math.max(maxPlace, n1Str.substr(n1Str.indexOf(".") + 1).length);
    }
    if (n2Str.indexOf(".") > -1) {
        maxPlace = Math.max(maxPlace, n2Str.substr(n2Str.indexOf(".") + 1).length);
    }
    var offset = Math.pow(10, maxPlace);
    return Math.round(n1 * offset + n2 * offset) / offset;
};
Erp.MathUtil.minusNumber = function (n1, n2, retNullWhenEmpty) {
    if (retNullWhenEmpty && Scdp.ObjUtil.isEmpty(n1) && Scdp.ObjUtil.isEmpty(n2)) {
        return null;
    }
    if (Scdp.ObjUtil.isEmpty(n1)) {
        n1 = 0;
    } else if (Ext.isString(n1)) {
        n1 = Number(n1);
    }
    if (Scdp.ObjUtil.isEmpty(n2)) {
        n2 = 0;
    } else if (Ext.isString(n2)) {
        n2 = Number(n2);
    }
    var scale = 0;
    var n1Str = n1 + "";
    var n2Str = n2 + "";
    if (n1Str.indexOf(".") > -1) {
        scale = Math.max(scale, n1Str.substr(n1Str.indexOf(".") + 1).length);
    }
    if (n2Str.indexOf(".") > -1) {
        scale = Math.max(scale, n2Str.substr(n2Str.indexOf(".") + 1).length);
    }
    var pow = Math.pow(10, scale);
    return Math.round(n1 * pow - n2 * pow) / pow;
};

Erp.MathUtil.compare = function (n1, n2) {
    if (Scdp.ObjUtil.isEmpty(n1)) {
        n1 = 0;
    } else if (Ext.isString(n1)) {
        n1 = Number(n1);
    }
    if (Scdp.ObjUtil.isEmpty(n2)) {
        n2 = 0;
    } else if (Ext.isString(n2)) {
        n2 = Number(n2);
    }
    if (n1 > n2) {
        return 1;
    } else if (n1 < n2) {
        return -1;
    } else {
        return 0;
    }
};
Erp.MathUtil.max = function (n1, n2) {

    if (Scdp.ObjUtil.isEmpty(n1)) {
        n1 = 0;
    } else if (Ext.isString(n1)) {
        n1 = Number(n1);
    }
    if (Scdp.ObjUtil.isEmpty(n2)) {
        n2 = 0;
    } else if (Ext.isString(n2)) {
        n2 = Number(n2);
    }
    return n1 > n2 ? n1 : n2;
};
Erp.MathUtil.min = function (n1, n2) {

    if (Scdp.ObjUtil.isEmpty(n1)) {
        n1 = 0;
    } else if (Ext.isString(n1)) {
        n1 = Number(n1);
    }
    if (Scdp.ObjUtil.isEmpty(n2)) {
        n2 = 0;
    } else if (Ext.isString(n2)) {
        n2 = Number(n2);
    }
    return n1 < n2 ? n1 : n2;
};

Erp.Util.getCurrentDeptCode = function () {
    return Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE);
};
//只能输入数字
Ext.apply(Ext.form.field.VTypes, {
    numberCheck: function (val, field) {
        var pattern = new RegExp("^[0-9]*$");
        if (!pattern.test(val)) {
            return false;
        } else {
            return true;
        }
    }
});

Erp.Util.wfDisableToolbar = function (view, flag) {
    if (view) {
        view.getCmp("editPanel->modifyBtn").setDisabled(flag);
        view.getCmp("editPanel->deleteBtn").setDisabled(flag);
    }
};
Erp.Util.showLogView = function (log, okFn) {
    var width = 600;
    var height = 480;
    var title = '提示信息';
    var panel = Ext.widget("JPanel", {
        cid: 'erpLogPanel',
        height: height,
        width: width,
        items: [
            {
                xtype: 'JTextArea',
                cid: 'erpLogContent',
                value: log,
                readOnly: true,
                height: height - 30,
                width: width - 30
            }
        ]
    });
    if (!okFn) {
        okFn = function () {
            return true;
        };
    }
    var win = Scdp.openNewWinByView(panel, okFn, null, title);
    win.query("[cid='cancelBtn']")[0].setVisible(false);
};


Erp.Util.showWeixinMsgWindow = function (callback) {
    var width = 600;
    var height = 200;
    var title = '微信发送';
    var panel = Ext.widget("JForm", {
        height: height,
        width: width,
        layout: 'form',
        bodyPadding: '10 10 10 10',
        items: [
            {
                xtype: 'JMsgAddr',
                cid: 'weixinReceivers',
                controller: "Msgcenter.controller.MsgAddrController",
                fieldLabel: '发送人',
                allowBlank: false,
                msgType: 'WEIXIN',
                width: 480
            }
        ]
    });
    if (!callback) {
        callback = function () {
            return true;
        };
    }
    var win = Scdp.openNewWinByView(panel, callback, null, title);
};

Erp.Util.getCurrentUserRoleName = function () {
    if (Scdp.ObjUtil.isNotEmpty(Scdp.CacheUtil.get(Erp.Const.USER_ROLE_NAME)) &&
        Scdp.CacheUtil.get(Erp.Const.USER_ROLE_NAME).USERID == Scdp.getCurrentUserId()) {
        return Scdp.CacheUtil.get(Erp.Const.USER_ROLE_NAME);
    } else {
        var postData = {};
        Scdp.doAction("erpuser-queryrole", postData, function (result) {
            if (result.USER_ROLE_NAME) {
                Scdp.CacheUtil.set(Erp.Const.USER_ROLE_NAME, result.USER_ROLE_NAME);
                return Scdp.CacheUtil.get(Erp.Const.USER_ROLE_NAME);
            }
        }, null, false);
    }
};

//项目和合同界面是否需要判断重大项目的部门标识
Erp.Util.getDeptMajorProjectMoney = function (deptCode) {
    if (Scdp.ObjUtil.isNotEmpty(Scdp.CacheUtil.getTemp(Erp.Const.CACHE_ERP_TEMP, Erp.Const.DEPT_MAJOR_PROJECT_MONEY))) {
        if (Scdp.ObjUtil.isNotEmpty(deptCode)) {
            return Scdp.CacheUtil.getTemp(Erp.Const.CACHE_ERP_TEMP, Erp.Const.DEPT_MAJOR_PROJECT_MONEY)[deptCode];
        }
    } else {
        var postData = {};
        Scdp.doAction("erpofficemajor-query", postData, function (result) {
            Scdp.CacheUtil.setTemp(Erp.Const.CACHE_ERP_TEMP, Erp.Const.DEPT_MAJOR_PROJECT_MONEY, result);
        }, null, false);
    }
};

Erp.Util.parseScdpDate = function (strDate) {
    if (Scdp.ObjUtil.isNotEmpty(strDate)) {
        strDate = strDate.replace("T", " ");
        return new Date(Date.parse(strDate.replace(/-/g, "/")));
    } else {
        return strDate;
    }
};

Erp.Util.serialNoSortFn = function (value) {
    if (Scdp.ObjUtil.isNotEmpty(value)) {
        var orgNums = value.split("-");
        var convertedNums = "";
        Ext.Array.each(orgNums, function (item) {
            var newNum = item;
            if (item.length < 10) {
                for (var i = item.length; i < 10; i++) {
                    newNum = "0" + newNum;
                }
            }
            convertedNums += newNum + "-";
        });
        return convertedNums;
    } else {
        return '0';
    }
};

Erp.Util.stateSortFn = function (value) {
    if (Scdp.ObjUtil.isNotEmpty(value)) {
        var convertedNums = "";
        switch (value) {
            case '4':
                convertedNums = '10';
                break;
            case '8':
                convertedNums = '11';
                break;
            case '2':
                convertedNums = '12';
                break;
            case '1':
                convertedNums = '13';
                break;
            case '9':
                convertedNums = '14';
                break;
            case '7':
                convertedNums = '15';
                break;
            case '0':
                convertedNums = '16';
                break;
            case '5':
                convertedNums = '17';
                break;
            case '6':
                convertedNums = '18';
                break;
            case '3':
                convertedNums = '19';
                break;
        }
        return convertedNums;
    } else {
        return '0';
    }
};

Erp.Util.getReportBasePath = function (reportFilePath) {
    return Scdp.getSysConfig("base_path") + Scdp.getSysConfig("report_servlet") +
        '?userId=' + Scdp.getCurrentUserId() + "&departmentCode=" + Erp.Util.getCurrentDeptCode() + "&" + reportFilePath;
};

Erp.Util.goModuleFromIMsg = function (menuCode, uuid) {
    var postData = {};
    var erp_msg_business_obj = {};
    erp_msg_business_obj.uuid = uuid;
    postData.erp_msg_business_obj = erp_msg_business_obj;
    if (Scdp.ObjUtil.isNotEmpty(menuCode)) {
        if (Scdp.getActiveModule().menuCode == 'M_SCDP_MAINPAGE') {
            if (window.ACTIVE_WINDOW && Scdp.ObjUtil.isEmpty(window.ACTIVE_WINDOW.queryById("contentPnl"))) {
                window.ACTIVE_WINDOW.close();
            }
        }
        Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
    }
};
Erp.Util.goFadCashReqFromIMsg = function (reqType, isProject, uuid) {
    var postData = {};
    var erp_msg_business_obj = {};
    erp_msg_business_obj.uuid = uuid;
    postData.erp_msg_business_obj = erp_msg_business_obj;
    var menuCode = 'FAD_CASHREQ_PURCHASE';
    if (reqType == '0') {
        menuCode = 'FAD_CASHREQ_PURCHASE';
    }
    else {
        if (isProject == "1") {
            menuCode = 'FAD_CASHREQ_PROJECT';
        }
        else {
            menuCode = 'FAD_CASHREQ_NON';
        }
    }
    if (Scdp.ObjUtil.isNotEmpty(menuCode)) {
        if (Scdp.getActiveModule().menuCode == 'M_SCDP_MAINPAGE') {
            if (window.ACTIVE_WINDOW && Scdp.ObjUtil.isEmpty(window.ACTIVE_WINDOW.queryById("contentPnl"))) {
                window.ACTIVE_WINDOW.close();
            }
        }
        Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
    }
};

Erp.Util.gotoCertificateModule = function (fadCertificateUuid) {
    if (Scdp.ObjUtil.isEmpty(fadCertificateUuid)) {
        Scdp.MsgUtil.warn("找不到该相关凭证!");
    } else {
        var menuCode = 'CERTIFICATE';
        var param = {};
        param.fadCertificateUuid = fadCertificateUuid;
        Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
    }
};
Erp.ArrayUtil.anyContains = function (arr, value) {
    if (Ext.isArray(arr) && Scdp.ObjUtil.isNotEmpty(value)) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].indexOf(value) > -1) {
                return true;
            }
        }
    }
    return false;
};
//处理从报表服务器返回的消息  用于数据挖掘
function receiveMessage(event) {
    var param = event.data;
    if (typeof  param === "string") {
        var arrayParm = param.split(',');
        var postData = {};
        if (arrayParm[0] == 'NonFadReqDetail') {
            var menuCode = "REPORT_FAD_NON_CASHREQ";
            postData.year = arrayParm[1];
            postData.officeId = arrayParm[2];
            postData.fadSubjectCode = arrayParm[3];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        } else if (arrayParm[0] == 'ScmAccountsPayableCheck') {
            var menuCode = "ACCOUNTS_PAYABLE_CHECK";
            postData.supplierName = arrayParm[1];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        } else if (arrayParm[0] == 'CdmTableRelation') {
            var menuCode = "CDM_TABLE_RELATION";
            postData.tableName = arrayParm[1];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true)
        } else if (arrayParm[0] == 'CdmColRelation') {
            var menuCode = "CDM_COL_RELATION";
            postData.column_uuid = arrayParm[1];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true)
        } else if (arrayParm[0] == 'JyhtmxbFromDepOperationCompletionSchedule') {
            var menuCode = "ERP_OP_REPORT_YGB_6";
            postData.contractorOffice = arrayParm[1];
            postData.year = arrayParm[2];
            postData.month = 13;
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        } else if (arrayParm[0] == 'ProjectDynamicsFromDepOperationCompletionSchedule') {
            var menuCode = "PRM_PROJECT_DYNAMIC";
            postData.officeId = arrayParm[1];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        } else if (arrayParm[0] == 'KjsxmhzbFromDepOperationCompletionSchedule') {
            var menuCode = "SETTLEMENTSUMMARY";
            postData.contractorOffice = arrayParm[1];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        } else if (arrayParm[0] == 'ProjectLowMarginTotalFromDepOperationCompletionSchedule') {
            var menuCode = "PROJECTLOWMARGINTOTALREPORT";
            postData.contractorOffice = arrayParm[1];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        } else if (arrayParm[0] == 'ProjectCostTicketUpSideDownTotalFromDepOperationCompletionSchedule') {
            var menuCode = "PROJECTCOSTTICKETUPSIDEDOWNTOTALREPORT";
            postData.contractorOffice = arrayParm[1];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        } else if (arrayParm[0] == 'ProjectDelayTotalFromDepOperationCompletionSchedule') {
            var menuCode = "PROJECTDELAYTOTALREPORT";
            postData.contractorOffice = arrayParm[1];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        }
        else if (arrayParm[0] == 'BmndcwzbwcjdFromNdcwzbwcjd') {
            var menuCode = "DEPOPERATIONSCOMPLETIONSTATUS";
            postData.officeId = arrayParm[1];
            postData.officeIdDesc = arrayParm[2];
            postData.year = arrayParm[3];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        }
        else if (arrayParm[0] == 'ErpopreportygbjyhthzbkhFromNdcwzbwcjd') {
            var menuCode = "ERP_OP_REPORT_YGB_JYHTHZB_KH";
            postData.officeId = "";
            postData.officeIdDesc = "";
            postData.year = "";
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        }
        else if (arrayParm[0] == 'ErpopreportygbxmlxhzbkhzFromNdcwzbwcjd') {
            var menuCode = "ERP_OP_REPORT_YGB_XMLXHZB_KHZ";
            postData.officeId = "";
            postData.officeIdDesc = "";
            postData.year = "";
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        }
        else if (arrayParm[0] == 'Erpopreportygb9zdFromNdcwzbwcjd') {
            var menuCode = "ERP_OP_REPORT_YGB_9ZD";
            postData.officeId = "";
            postData.officeIdDesc = "";
            postData.year = "";
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        }
        else if (arrayParm[0] == 'ReportskhzbkhzFromNdcwzbwcjd') {
            var menuCode = "REPORT_SKHZB_KHZ";
            postData.officeId = "";
            postData.officeIdDesc = "";
            postData.year = "";
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        }
        else if (arrayParm[0] == 'ReportszyykhzFromNdcwzbwcjd') {
            var menuCode = "REPORT_SZYY_KHZ";
            postData.officeId = "";
            postData.officeIdDesc = "";
            postData.year = "";
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        }
        else if (arrayParm[0] == 'Erpopreportygb4zFromNdcwzbwcjd') {
            var menuCode = "ERP_OP_REPORT_YGB_4Z";
            postData.officeId = "";
            postData.officeIdDesc = "";
            postData.year = "";
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        }
        else if (arrayParm[0] == 'LxxmmxbFromXmlxhzbkh') {
            var menuCode = "ERP_OP_REPORT_YGB_8Z";
            postData.contractorOffice = arrayParm[1];
            postData.year = arrayParm[2];
            postData.month = 13;
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        }
        else if (arrayParm[0] == 'JsxmmxbFromJsfbmhzb') {
            var menuCode = "ERP_OP_REPORT_YGB_10Z";
            postData.contractorOffice = arrayParm[1];
            postData.year = arrayParm[2];
            postData.buildRegion = arrayParm[3];
            postData.affiliatedInstitutions = arrayParm[4];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        }
        else if (arrayParm[0] == 'XmszmxbFromZcxmhz') {
            var menuCode = "PRM_PROJECT_ACCOUNTDETAIL";
            postData.contractorOffice = arrayParm[1];
            postData.buildRegion = arrayParm[2];
            postData.affiliatedInstitutions = arrayParm[3];
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        }
    }
};
addEventListener("message", receiveMessage, false);


Scdp.MsgUtil.yesNoCancel = function (msgText, fn, width, height, yesLabel, noLabel, cancelLabel) {
    var _width = width;
    var _height = height;
    if (Scdp.ObjUtil.isEmpty(_width)) {
        _width = Scdp.Const.MSGBOX_DEFAULT_WIDTH;
    }
    if (Scdp.ObjUtil.isEmpty(_height)) {
        _height = Scdp.Const.MSGBOX_DEFAULT_HEIGHT;
    }
    var cfg = {
        title: Scdp.I18N.SYSTEM_CONFIRM,
        closable: false,
        frame: true,
        frameHeader: true,
        msg: msgText,
        minWidth: _width,
        minHeight: _height,
        buttons: Ext.Msg.YESNOCANCEL,
        icon: Ext.Msg.QUESTION,
        fn: fn
    };
    if (Scdp.ObjUtil.isNotEmpty(yesLabel) && Scdp.ObjUtil.isNotEmpty(noLabel) && Scdp.ObjUtil.isNotEmpty(cancelLabel)) {
        cfg.buttonText = {
            yes: yesLabel,
            no: noLabel,
            cancel: cancelLabel
        }
    }
    Ext.Msg.show(cfg);
};

function doCacheClearAfterLogin(curBuildID) {//Override this function to customize cache clean policy after login.
    if (Ext.getCmp("clearCache").gotValue()) {
        Scdp.CacheUtil.removeAllTemp();
        Scdp.CacheUtil.removeAllTempByType(Scdp.Const.CACHE_JQGRID_COLUMN_DEF);//DO NOT put this line into removeAllTemp()
        Scdp.CacheUtil.removeAllTempByType(Erp.Const.CACHE_ERP_TEMP);
        Scdp.CacheUtil.remove(Erp.Const.USER_ROLE_NAME);
    } else if (Scdp.CacheUtil.get(Scdp.Const.BUILD_ID_CACHE) !== curBuildID) {
        Scdp.CacheUtil.removeAllTemp();
        Scdp.CacheUtil.removeAllTempByType(Scdp.Const.CACHE_JQGRID_COLUMN_DEF);
    } else {
        Scdp.CacheUtil.removeAllTempByType(Scdp.Const.CACHE_TYPE_SYS_MENU);//clear menu cache BEFORE login.
    }
}


/*
 * MAP对象，实现MAP功能
 *
 * 接口：
 * size()     获取MAP元素个数
 * isEmpty()    判断MAP是否为空
 * clear()     删除MAP所有元素
 * put(key, value)   向MAP中增加元素（key, value)
 * remove(key)    删除指定KEY的元素，成功返回True，失败返回False
 * get(key)    获取指定KEY的元素值VALUE，失败返回NULL
 * element(index)   获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
 * containsKey(key)  判断MAP中是否含有指定KEY的元素
 * containsValue(value) 判断MAP中是否含有指定VALUE的元素
 * values()    获取MAP中所有VALUE的数组（ARRAY）
 * keys()     获取MAP中所有KEY的数组（ARRAY）
 *
 * 例子：
 * var map = new Map();
 *
 * map.put("key", "value");
 * var val = map.get("key")
 * ……
 *
 */
function Map() {
    this.elements = new Array();

    //获取MAP元素个数
    this.size = function () {
        return this.elements.length;
    };
    //判断MAP是否为空
    this.isEmpty = function () {
        return (this.elements.length < 1);
    };
    //删除MAP所有元素
    this.clear = function () {
        this.elements = new Array();
    };
    //向MAP中增加元素（key, value)
    this.put = function (_key, _value) {
        this.elements.push({
            key: _key,
            value: _value
        });
    };
    //删除指定KEY的元素，成功返回True，失败返回False
    this.removeByKey = function (_key) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };
    //删除指定VALUE的元素，成功返回True，失败返回False
    this.removeByValue = function (_value) {//removeByValueAndKey
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };
    //删除指定VALUE的元素，成功返回True，失败返回False
    this.removeByValueAndKey = function (_key, _value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value && this.elements[i].key == _key) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };
    //获取指定KEY的元素值VALUE，失败返回NULL
    this.get = function (_key) {
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    return this.elements[i].value;
                }
            }
        } catch (e) {
            return false;
        }
        return false;
    };
    //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
    this.element = function (_index) {
        if (_index < 0 || _index >= this.elements.length) {
            return null;
        }
        return this.elements[_index];
    };
    //判断MAP中是否含有指定KEY的元素
    this.containsKey = function (_key) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };
    //判断MAP中是否含有指定VALUE的元素
    this.containsValue = function (_value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };
    //判断MAP中是否含有指定VALUE的元素
    this.containsObj = function (_key, _value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value && this.elements[i].key == _key) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };
    //获取MAP中所有VALUE的数组（ARRAY）
    this.values = function () {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].value);
        }
        return arr;
    };
    //获取MAP中所有VALUE的数组（ARRAY）
    this.valuesByKey = function (_key) {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            if (this.elements[i].key == _key) {
                arr.push(this.elements[i].value);
            }
        }
        return arr;
    };
    //获取MAP中所有KEY的数组（ARRAY）
    this.keys = function () {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].key);
        }
        return arr;
    };
    //获取key通过value
    this.keysByValue = function (_value) {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            if (_value == this.elements[i].value) {
                arr.push(this.elements[i].key);
            }
        }
        return arr;
    };
    //获取MAP中所有KEY的数组（ARRAY）
    this.keysRemoveDuplicate = function () {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            var flag = true;
            for (var j = 0; j < arr.length; j++) {
                if (arr[j] == this.elements[i].key) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                arr.push(this.elements[i].key);
            }
        }
        return arr;
    };
}

/*
 * 五星评价弹出框  by lijx
 * 使用方法：Scdp.FiveStarWin.show({items:['到货时间',"设备质量"]},回调函数)
 * 返回值：items对应的分数和评论
 * */
Scdp.FiveStarWin = {
    star_red_url: "star_red_icon",
    star_url: "star_icon",
    checkMap: null, //该变量是记录当前选择的评分
    scoreRange: ["", "很差", "比较差", "一般", "比较好", "很好"],
    getImgStr: function (socre) {
        var template = "<a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a>";
        var desc = "";
        for (var i = 1; i <= socre; i++) {
            desc += template;
        }
        return desc;
    },
    /*over()是鼠标移过事件的处理方法*/
    over: function (param, prefix) {
        this.setScore(param, prefix + "_star");
        $("#" + prefix + "_message").html(this.scoreRange[param]);
    },
    out: function (prefix) {
        this.setScore(this.checkMap.get(prefix), prefix + "_star");
        $("#" + prefix + "_message").html("");
    },
    setScore: function (score, id) {
        if (score == 0) {
            $("#" + id + "1").attr("class", this.star_url);
            $("#" + id + "2").attr("class", this.star_url);
            $("#" + id + "3").attr("class", this.star_url);
            $("#" + id + "4").attr("class", this.star_url);
            $("#" + id + "5").attr("class", this.star_url);
        } else {
            for (var i = 1; i <= 5; i++) {
                if (i <= score)
                    $("#" + id + i).attr("class", this.star_red_url);
                else
                    $("#" + id + i).attr("class", this.star_url);
            }
        }
    },
    click: function (param, prefix) {
        //this.check = param;//记录当前打分
        this.checkMap.removeByKey(prefix);
        this.checkMap.put(prefix, param);
        this.out(prefix);//设置星星数
    },
    show: function (params, callback, allowBlank, showCancel, contentHeight) {
        allowBlank = allowBlank || false;
        showCancel = showCancel && true;
        this.checkMap = new Map();
        contentHeight = Scdp.ObjUtil.isEmpty(contentHeight) ? (10 + params.items.length * 25) : contentHeight;
        var width = 500;
        var height = 200 + contentHeight;
        var title = "五星评价";
        var check = 0;//该变量是记录当前选择的评分
        var window = Ext.create('Ext.window.Window', {
            width: width,
            height: height,
            layout: 'border',
            title: title,
            modal: true,
            closable: true,
            scrollable: true,
            //closeAction:'hide',
            items: [
                {
                    xtype: 'form',
                    region: 'center',
                    frame: true,
                    scrollable: true,
                    items: [
                        {
                            xtype: 'panel',
                            id: "fiveStar_html",
                            frame: true,
                            modal: true,
                            width: 460,
                            height: contentHeight,
                            scrollable: true,
                            margin: '0 10 10 0'

                        },
                        {
                            xtype: 'textarea',
                            id: 'fiveStar_suggest',
                            name: 'suggestCancel',
                            fieldLabel: '评价',
                            maxLength: 1000,
                            editable: true,
                            width: 460,
                            height: 100
                        }
                    ]
                }
            ],
            buttons: [
                {
                    text: '确认',
                    handler: function () {
                        if (!allowBlank) {
                            var msg = "";
                            Ext.Array.forEach(params.items, function (a) {
                                if (Scdp.FiveStarWin.checkMap.get(a) == 0)
                                    msg += a + ",";
                            })
                            /*
                             if (Scdp.ObjUtil.isEmpty(Ext.getCmp('fiveStar_suggest').getValue()))
                             msg += "评价,"*/
                            if (!Scdp.ObjUtil.isEmpty(msg)) {
                                Scdp.MsgUtil.info(msg.substr(0, msg.length - 1) + " 不能为空");
                                return;
                            }
                        }
                        var result = {
                            scoreArray: new Array(),
                            suggest: Ext.getCmp('fiveStar_suggest').getValue()
                        }
                        var i = 0;
                        Ext.Array.forEach(params.items, function (a) {
                            result.scoreArray[i] = Scdp.FiveStarWin.checkMap.get(a);
                            i++;
                        })
                        if (callback) callback(result);
                        window.close();
                    }
                },
                {
                    text: '取消',
                    id: "fiveStar_cancel",
                    handler: function () {
                        window.close()
                    }
                }
            ]
        });

        var htmlTem = '<tr>' +
            '<td id="@_label" width="100px" >@ :</td>' +
            '<td><a href="javascript:Scdp.FiveStarWin.click(1,\'@\')"  class="star_icon" style="margin: 5px" id="@_star1" onMouseOver="Scdp.FiveStarWin.over(1,\'@\')" onMouseOut="Scdp.FiveStarWin.out(\'@\')">&nbsp;&nbsp;&nbsp;</a>' +
            '<a href="javascript:Scdp.FiveStarWin.click(2,\'@\')" class="star_icon" style="margin: 5px" id="@_star2" onMouseOver="Scdp.FiveStarWin.over(2,\'@\')" onMouseOut="Scdp.FiveStarWin.out(\'@\')">&nbsp;&nbsp;&nbsp;</a>' +
            '<a href="javascript:Scdp.FiveStarWin.click(3,\'@\')" class="star_icon" style="margin: 5px" id="@_star3" onMouseOver="Scdp.FiveStarWin.over(3,\'@\')" onMouseOut="Scdp.FiveStarWin.out(\'@\')">&nbsp;&nbsp;&nbsp;</a>' +
            '<a href="javascript:Scdp.FiveStarWin.click(4,\'@\')" class="star_icon" style="margin: 5px" id="@_star4" onMouseOver="Scdp.FiveStarWin.over(4,\'@\')" onMouseOut="Scdp.FiveStarWin.out(\'@\')">&nbsp;&nbsp;&nbsp;</a>' +
            '<a href="javascript:Scdp.FiveStarWin.click(5,\'@\')" class="star_icon" style="margin: 5px"  id="@_star5" onMouseOver="Scdp.FiveStarWin.over(5,\'@\')" onMouseOut="Scdp.FiveStarWin.out(\'@\')">&nbsp;&nbsp;&nbsp;</a>' +
            '<span id="@_message" style="margin: 5px"></span></td>' +
            '</tr>';
        var html = "";
        Ext.Array.forEach(params.items, function (a) {
            html += htmlTem.replace(/@/g, a);
            Scdp.FiveStarWin.checkMap.put(a, 0);
        })
        Ext.getCmp('fiveStar_html').html = '<table>' + html + '</table>';

        window.show()
        if (!showCancel)
            Ext.getCmp('fiveStar_cancel').hide();
    },
    show2: function (params, callback, allowBlank, showCancel, contentHeight) {
        allowBlank = allowBlank || false;
        showCancel = showCancel && true;
        this.checkMap = new Map();
        contentHeight = Scdp.ObjUtil.isEmpty(contentHeight) ? (10 + params.items.length * 25) : contentHeight;
        var width = 500;
        var height = 80 + contentHeight;
        var title = "五星评价";
        var check = 0;//该变量是记录当前选择的评分
        var window = Ext.create('Ext.window.Window', {
            width: width,
            height: height,
            layout: 'border',
            title: title,
            modal: true,
            closable: false,
            scrollable: true,
            //closeAction:'hide',
            items: [
                {
                    xtype: 'form',
                    region: 'center',
                    frame: true,
                    scrollable: true,
                    items: [
                        {
                            xtype: 'panel',
                            id: "fiveStar_html",
                            frame: true,
                            modal: true,
                            width: 460,
                            height: contentHeight,
                            scrollable: true,
                            margin: '0 10 10 0'

                        }
                        /*,{
                         xtype: 'textarea',
                         id: 'fiveStar_suggest',
                         name: 'suggestCancel',
                         fieldLabel: '评价',
                         maxLength: 1000,
                         editable: true,
                         width: 460,
                         height: 100
                         }*/
                    ]
                }
            ],
            buttons: [
                {
                    text: '确认',
                    handler: function () {
                        if (!allowBlank) {
                            var msg = "";
                            Ext.Array.forEach(params.items, function (a) {
                                if (Scdp.FiveStarWin.checkMap.get(a) == 0)
                                    msg += a + ",";
                            })
                            /*
                             if (Scdp.ObjUtil.isEmpty(Ext.getCmp('fiveStar_suggest').getValue()))
                             msg += "评价,"
                             if (!Scdp.ObjUtil.isEmpty(msg)) {
                             Scdp.MsgUtil.info(msg.substr(0, msg.length - 1) + " 不能为空");
                             return;
                             }*/
                        }
                        var result = {
                            scoreArray: new Array()//,suggest: Ext.getCmp('fiveStar_suggest').getValue()
                        }
                        var i = 0;
                        Ext.Array.forEach(params.items, function (a) {
                            result.scoreArray[i] = Scdp.FiveStarWin.checkMap.get(a);
                            i++;
                        })
                        if (callback) callback(result);
                        window.close();
                    }
                },
                {
                    text: '取消',
                    id: "fiveStar_cancel",
                    handler: function () {
                        window.close()
                    }
                }
            ]
        });

        var htmlTem = '<tr>' +
            '<td id="@_label" width="100px" >@ :</td>' +
            '<td><a href="javascript:Scdp.FiveStarWin.click(1,\'@\')"  class="star_icon" style="margin: 5px" id="@_star1" onMouseOver="Scdp.FiveStarWin.over(1,\'@\')" onMouseOut="Scdp.FiveStarWin.out(\'@\')">&nbsp;&nbsp;&nbsp;</a>' +
            '<a href="javascript:Scdp.FiveStarWin.click(2,\'@\')" class="star_icon" style="margin: 5px" id="@_star2" onMouseOver="Scdp.FiveStarWin.over(2,\'@\')" onMouseOut="Scdp.FiveStarWin.out(\'@\')">&nbsp;&nbsp;&nbsp;</a>' +
            '<a href="javascript:Scdp.FiveStarWin.click(3,\'@\')" class="star_icon" style="margin: 5px" id="@_star3" onMouseOver="Scdp.FiveStarWin.over(3,\'@\')" onMouseOut="Scdp.FiveStarWin.out(\'@\')">&nbsp;&nbsp;&nbsp;</a>' +
            '<a href="javascript:Scdp.FiveStarWin.click(4,\'@\')" class="star_icon" style="margin: 5px" id="@_star4" onMouseOver="Scdp.FiveStarWin.over(4,\'@\')" onMouseOut="Scdp.FiveStarWin.out(\'@\')">&nbsp;&nbsp;&nbsp;</a>' +
            '<a href="javascript:Scdp.FiveStarWin.click(5,\'@\')" class="star_icon" style="margin: 5px"  id="@_star5" onMouseOver="Scdp.FiveStarWin.over(5,\'@\')" onMouseOut="Scdp.FiveStarWin.out(\'@\')">&nbsp;&nbsp;&nbsp;</a>' +
            '<span id="@_message" style="margin: 5px"></span></td>' +
            '</tr>';
        var html = "";
        Ext.Array.forEach(params.items, function (a) {
            html += htmlTem.replace(/@/g, a);
            Scdp.FiveStarWin.checkMap.put(a, 0);
        })
        Ext.getCmp('fiveStar_html').html = '<table>' + html + '</table>';

        window.show()
        if (!showCancel)
            Ext.getCmp('fiveStar_cancel').hide();
    }
};

//凭证编辑自定义方法开始
//去除空格方法開始
Erp.Util.Trim = function (value) {
    return value.toString().replace(/(^\s*)|(\s*$)/g, "");
};
Erp.Util.LTrim = function (value) {
    return value.toString().replace(/(^\s*)/g, "");
};
Erp.Util.RTrim = function (value) {
    return value.toString().replace(/(\s*$)/g, "");
};
//去除空格方法結束

//去除null方法开始
Erp.Util.isEmptyReturnZero = function (value) {
    if (Erp.Util.Trim(value).length == 0) {
        return 0;
    }
    else {
        return Erp.Util.Trim(value);
    }
};
Erp.Util.isZeroReturnEmpty = function (value) {
    if (value == 0) {
        return "";
    } else {
        return Erp.Util.Trim(value);
    }
};
Erp.Util.isNullReturnEmpty = function (value) {
    if (value == null || value == "null" || value == undefined || value == "undefined") {
        return "";
    }
    else {
        return Erp.Util.Trim(value);
    }
};

Erp.Util.nvl = function (value1, value2) {
    if (Erp.Util.isNullReturnEmpty(value1).length == 0) {
        return Erp.Util.isNullReturnEmpty(value2);
    } else {
        return Erp.Util.isNullReturnEmpty(value1);
    }
};
//去除null方法结束

//不满2位则前面补0
Erp.Util.addZero = function (num) {
    if (Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(num)) < 10) {
        return "0" + Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(num)).toString();
    }
    else {
        return Erp.Util.isEmptyReturnZero(Erp.Util.isNullReturnEmpty(num)).toString();
    }
};

//是否是日期
Erp.Util.isDate = function (date) {
    try {
        if (Erp.Util.isNullReturnEmpty(date).length > 0) {
            date = new Date(date);
            if (parseInt(date.getFullYear()) == date.getFullYear()) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    } catch (e) {
        return false;
    }
};

//返回yyyy/MM/dd日期
Erp.Util.getDateForStandard = function (dateStr) {
    try {
        var date;
        if (Erp.Util.isNullReturnEmpty(dateStr).length > 0) {
            if (Erp.Util.isDate(dateStr)) {
                date = new Date(dateStr);
            }
            else {
                return "";
            }
        }
        else {
            date = new Date();
        }
        return (date.getFullYear().toString() + "/" + Erp.Util.addZero(date.getMonth() + 1) + "/" + Erp.Util.addZero(date.getDate())).toString();
    }
    catch (e) {
        return "";
    }
};

//返回yyyy/MM/dd hh:mm:ss日期时间
Erp.Util.getDateTimeForStandard = function (dateStr) {
    try {
        var date;
        if (Erp.Util.isNullReturnEmpty(dateStr).length > 0) {
            if (Erp.Util.isDate(dateStr)) {
                date = new Date(dateStr);
            }
            else {
                return "";
            }
        }
        else {
            date = new Date();
        }
        return (date.getFullYear().toString() + "/" + Erp.Util.addZero(date.getMonth() + 1) + "/" + Erp.Util.addZero(date.getDate()) + " " + Erp.Util.addZero(date.getHours()) + ":" + Erp.Util.addZero(date.getMinutes()) + ":" + Erp.Util.addZero(date.getSeconds())).toString();
    }
    catch (e) {
        return "";
    }
};

//返回当前年
Erp.Util.getCurrentYear = function () {
    return new Date().getFullYear().toString();
};

//返回当前数字年
Erp.Util.getCurrentYearForInt = function () {
    return new Date().getFullYear();
};

//返回当前月
Erp.Util.getCurrentMonth = function () {
    if (new Date().getMonth() < 9) {
        return ("0" + (new Date().getMonth() + 1)).toString();
    }
    else {
        return (new Date().getMonth() + 1).toString();
    }
};

//返回当前数字月
Erp.Util.getCurrentMonthForInt = function () {
    return (new Date().getMonth() + 1);
};

//将字符串转换成XMLDoc
Erp.Util.stringToXMLDoc = function (str) {
    var xmlDoc = null;
    try {
        var xmlDOMObj = new ActiveXObject("Microsoft.XMLDOM");
        xmlDOMObj.async = false;
        xmlDOMObj.loadXML(str);
        xmlDoc = xmlDOMObj;
    }
    catch (e) {
        try {
            var domParser = new DOMParser;
            xmlDoc = domParser.parseFromString(str, 'text/xml');
        }
        catch (e) {
            xmlDoc = null;
        }
    }
    return xmlDoc;
};

//字符串全部替换
Erp.Util.replaceAll = function (ASourceText, AFindText, ARepText) {
    var raRegExp = new RegExp(AFindText, "g");
    return ASourceText.replace(raRegExp, ARepText);
};

//Cookie操作
Erp.Util.SetCookie = function (name, value) {
    Scdp.CacheUtil.set(name, value);
};
Erp.Util.GetCookie = function (name) {
    return Scdp.CacheUtil.get(name);
};
//凭证编辑自定义方法结束

//批量打印功能
Erp.batchPrintReport = function (items) {
    var f = "", g = Scdp.getSysConfig("base_path") + Scdp.getSysConfig("report_servlet");
    if (!Scdp.ObjUtil.isEmpty(items) && Ext.isArray(items) && !(0 == items.size || g == Scdp.getSysConfig("base_path"))) {
        Ext.Array.forEach(items, function (item) {
            f += "{reportlet:" + item + "," + Scdp.getSysConfig("REPORT_SYSUSER_PARAM") + ":'" + Scdp.CacheUtil.get(Scdp.Const.USER_ID) + "'},";
        });
        f = Scdp.StrUtil.cjkEncode("[" + f.substr(0,
                f.length - 1) + "]");
        items = {url: g, isPopUp: !1, data: {reportlets: Scdp.StrUtil.Base64.encode(f)}};
        var e = Ext.get("mockReportForm").dom, g = Ext.get("reportType").dom, i = Ext.get("reportConfig").dom, j = Ext.get("signature").dom, h = Ext.get("userId").dom;
        g.value = "pdf";
        i.value = Ext.encode(items);
        j.value = Scdp.getSign(Scdp.Const.REPORT_PRINT_ACTION, items);
        h.value = Scdp.CacheUtil.get(Scdp.Const.USER_ID);
        e.target = Ext.isIE || !1 ? "_black" : "reportframe";
        e.action = "framework/jsp/report_print.jsp";
        e.submit();
        Scdp.doAction("sys-time-expend", {}, null, null, !1)
    }
};