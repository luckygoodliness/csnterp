Ext.define('Scmcontractchange.view.ScmNewContractchangeView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmcontractchange',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 180,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'scmnewcontractchange-query-layout.xml',
    editLayoutFile: 'scmnewcontractchange-edit-layout.xml',
    //extraLayoutFile: 'scmcontractchange-extra-layout.xml',
    bindings: ['scmContractChangeDto', 'scmContractChangeDto.scmContractChangeDDto','scmContractChangeDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Scm_Purchase_Change',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
        me.getCmp("scmContractChangeDDto->copyAddRowBtn").hide();
        me.getCmp("scmContractChangeDDto").doAddRow = function(model){
            var  comtroller = Scdp.getActiveModule().controller;
            comtroller.pickChasereqPack();
        }
        me.getCmp("scmContractChangeDDto").beforeDeleteRow = function(a){
            var  comtroller = Scdp.getActiveModule().controller;
            return comtroller.doBeforeDelete();
        }
        me.getCmp("scmContractChangeDDto").afterDeleteRow = function(a){
            var  comtroller = Scdp.getActiveModule().controller;
            comtroller.setTotalMoney();
        }
        me.getCmp("scmContractChangeDDto").rowColorConfigFn=function (record) {
            if(record.data.changeState == '1') {
                    return 'x-grid-row-color-green';
            }
            if(record.data.changeState == '2') {
                    return 'x-grid-row-color-red';
            }
        }
    },
    gotoScmContract: function () {

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
        me.getCmp("copyAddBtn").setVisible(false);
        var contractnameCmp = me.getCmp("scmContractChangeDto->contractname");
        //contractnameCmp.filterConditions = {selfconditions: " is_virtual='0' and state=2 "};
        contractnameCmp.inputEl.on('click', me.gotoScmContract);
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


        reportJPanel.add({
            xtype: 'JPanel',
            cid: 'resultPanel',
            margin: '2 4 2 3 ',
            flex: 1,
            html: '<iframe id="scmContractChangeReportIframe" name="scmContractChangeReportIframe" frameborder="0" style="width: 100% ; height: 100%;background-color: #FFFFFF" src=""></iframe>'
        });



    }
});