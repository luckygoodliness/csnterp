Ext.define('Scmcontractchange.view.ScmcontractchangeView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmcontractchange',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 180,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'scmcontractchange-query-layout.xml',
    editLayoutFile: 'scmcontractchange-edit-layout.xml',
    //extraLayoutFile: 'scmcontractchange-extra-layout.xml',
    bindings: ['scmContractChangeDto', 'scmContractChangeDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Scm_Contract_Revise',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    gotoScmContract: function () {
        //var me = this;
        //var scmContractIdPlusId = me.id.substr(0, me.id.lastIndexOf("-inputEl"));
        //var scmContractId = Ext.getCmp(scmContractIdPlusId).up("IView").getCmp("scmContractChangeDto->scmContractId").gotValue();
        //if (Scdp.ObjUtil.isNotEmpty(scmContractId)) {
        //    var param = {uuid: scmContractId};
        //    var menuCode = 'CONTRACTESTABLISHMENT';
        //    Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
        //}

        var me = this;
        if (me.dom.readOnly == true && Scdp.ObjUtil.isNotEmpty(me.id)) {
            var purchaseContractIdPlusId = me.id.substr(0, me.id.lastIndexOf("-inputEl"));
            var scmContractId = Ext.getCmp(purchaseContractIdPlusId).up("IView").getCmp("scmContractChangeDto->scmContractId").gotValue();
            if (Scdp.ObjUtil.isNotEmpty(scmContractId)) {
                var param = {uuid: scmContractId};
                var menuCode = 'CONTRACTESTABLISHMENT';
                Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
            }
        }
    },
    afterInit: function () {
        var me = this;
        //var editToolbar = me.getEditToolbar();
        //me.getCmp('editToolbar->copyAddBtn').setVisible(false);
        //editToolbar.add({
        //	text:"提交",
        //	cid:"submit",
        //	iconCls:"modify_btn",
        //	disabled:"true"
        //});
        //editToolbar.add({
        //	text:"审核",
        //	cid:"approved",
        //	iconCls:"modify_btn",
        //	disabled:"true"
        //});
        me.getCmp("copyAddBtn").setVisible(false);
        var contractnameCmp = me.getCmp("scmContractChangeDto->contractname");
        //contractnameCmp.filterConditions = {selfconditions: " is_virtual='0' and state=2 and nvl(is_closed,0)=0 "};
        contractnameCmp.filterConditions = {selfconditions: " is_virtual='0' and state=2 "};
        me.getCmp("scmContractChangeDto->contractname").inputEl.on('click', me.gotoScmContract);

        //init iframe

        me.initReportIframe();
    },
    UIStatusChanged: function (view, uistatus) {
        view.getCmp("scmContractChangeDto->contractname").setFieldStyle("color: black;line-height:0;height:21px;text-decoration: none");
        if (uistatus == Scdp.Const.UI_INFO_STATUS_VIEW) {
            view.getCmp("scmContractChangeDto->contractname").setFieldStyle("color: blue;line-height:1;height:21px;text-decoration: underline;cursor:pointer");
        }
        if(!(uistatus == Scdp.Const.UI_INFO_STATUS_VIEW||uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY)){
            //clear
            var frame = window.frames["scmContractChangeReportIframe"];
            if(Scdp.ObjUtil.isNotEmpty(frame)){
                frame.document.body.innerHTML = "";
            }
        }

    },
    initReportIframe: function () {
        //var me = this;
        var view = this;
        var reportJPanel = view.getCmp("scmContractReport");
        var scmContractId = view.getCmp("scmContractChangeDto->scmContractId").gotValue();
        //var param = {};
        //Scdp.printReport("erp/scm/ScmContractQuery.cpt", [param], false, "applet");


        reportJPanel.add({
            xtype: 'JPanel',
            cid: 'resultPanel',
            margin: '2 4 2 3 ',
            flex: 1,
            html: '<iframe id="scmContractChangeReportIframe" name="scmContractChangeReportIframe" frameborder="0" style="width: 100% ; height: 100%;background-color: #FFFFFF" src=""></iframe>'
        });



    }
});