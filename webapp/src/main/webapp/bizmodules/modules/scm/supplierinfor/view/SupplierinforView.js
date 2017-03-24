Ext.define('Supplierinfor.view.SupplierinforView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/supplierinfor',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'supplierinfor-query-layout.xml',
    editLayoutFile: 'supplierinfor-edit-layout.xml',
    //extraLayoutFile: 'supplierinfor-extra-layout.xml',
    bindings: ['scmSupplierDto', 'scmSupplierDto.scmSupplierBankDto','scmSupplierDto.scmSupplierEvaluationDto', 'scmSupplierDto.scmSupplierContactsDto', 'scmSupplierDto.cdmFileRelationDto', 'scmSupplierDto.scmSukpplierKeyDto', 'scmSupplierDto.prmBlacklistMonthDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Scm_Supplier_Apporve',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        //增加失效按钮
        var editToolbar = me.getEditToolbar();
        me.getCmp("editPanel->copyAddBtn").hide();
        editToolbar.add({
            text: '失效',
            cid: 'cancelEffect_edit',
            iconCls: 'close_other_modules'
            //disabled: "true"
        });
        var queryToolbar = me.getQueryToolbar();
        queryToolbar.add({
            text: '失效',
            cid: 'cancelEffect_query',
            iconCls: 'close_other_modules'
            //disabled: "true"
        });
        editToolbar.add({
            text: '设置NC编号',
            cid: 'updateNcCode_edit',
            iconCls: 'setting_btn'
            //disabled: "true"
        });
        editToolbar.add({
            text: '供方评价',
            cid: 'supplierEvaluation',
            iconCls: 'setting_btn'
            //disabled: "true"
        });
        me.initScmSupplierContactsGrid();
        var departMent = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME);
        if (departMent == "计划财务部") {
            me.getCmp("scmSupplierDto->taxRegistrationNo").allowBlank = true;
            me.getCmp("scmSupplierDto->taxRegistrationNo").labelEl.dom.innerHTML = me.getCmp("scmSupplierDto->taxRegistrationNo").labelEl.dom.innerHTML.replace('*', '');
            me.getCmp("scmSupplierDto->taxTypes").allowBlank = true;
            me.getCmp("scmSupplierDto->taxTypes").labelEl.dom.innerHTML = me.getCmp("scmSupplierDto->taxTypes").labelEl.dom.innerHTML.replace('*', '');
        }
    },
    initScmSupplierContactsGrid: function () {
        var me = this;
        var detailGrid = me.getCmp("scmSupplierContactsDto");
        detailGrid.afterEditGrid = me.changeScmSupplierContacts;
    },
    changeScmSupplierContacts: function (eventObj, isChanged) {
        if (!isChanged) {
            return;
        }
        var field = eventObj.field;
        var record = eventObj.record;
        if (field == "mobilePhone") {
            if (!Scdp.StrUtil.isMobile(record.get("mobilePhone"))) {
                record.set("mobilePhone", "");
                Scdp.MsgUtil.info("手机号码格式不正确！");
                return false;
            }
        } else if (field == "email") {
            if (!Scdp.StrUtil.isEmail(record.get("email"))) {
                record.set("email", "");
                Scdp.MsgUtil.info("邮箱格式不正确！");
                return false;
            }
        }
    }
});