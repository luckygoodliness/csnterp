Ext.define('Scmpricedatabase.view.ScmPriceDatabaseView', {
    extend: 'Scdp.mvc.AbstractReportFormView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmpricedatabase',
    cpHeight: 110,
    //prWinWidth: 800,//预览窗口初始宽度
    prWinHeight: 600,//预览窗口初始高度
    allowNullConditions: true,//是否允许空查询条件
    queryLayoutFile: 'scmpricedatabase-query-layout.xml',
    editLayoutFile: 'scmpricedatabase-edit-layout.xml',
    hideScroll: true,
    canEdit: false,
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.getCmp("conditionPanel->countryCode").sotValue("CN");
        me.scmPriceDatabaseGridInit(me);
        me.scmPriceDatabaseGridInitT(me);

    },
    scmPriceDatabaseGridInit: function (view) {
        openScmPriceDatabase = function (uuid) {
            var postData = {};
            postData.erp_msg_business_obj ={uuid:uuid};
            var menuCode = "CONTRACTESTABLISHMENT";
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        };
        var resultPanel = view.getCmp('resultPanel');
        var  scmPriceDatabaseGridColumns = resultPanel.getColumnBydataIndex("scmContractCode");
        scmPriceDatabaseGridColumns.renderer = function (value, p, record) {
            return Ext.String.format(
                '<a href="javascript:openScmPriceDatabase(\'' + record.data.uuid + '\');" target="_blank" style="color: blue">' + value + '</a>'
            );
        };
    },
    scmPriceDatabaseGridInitT: function (view) {
        openScmPriceDatabaseT = function (uuid) {
            var postData = {};
            postData.erp_msg_business_obj ={uuid:uuid};
            var menuCode = "SUPPLIERINFOR";
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        };
        var resultPanel = view.getCmp('resultPanel');
        var  scmPriceDatabaseGridColumnsT = resultPanel.getColumnBydataIndex("completeName");
        scmPriceDatabaseGridColumnsT.renderer = function (value, p, record) {
            return Ext.String.format(
                '<a href="javascript:openScmPriceDatabaseT(\'' + record.data.supplierId + '\');" target="_blank" style="color: blue">' + value + '</a>'
            );
        };
    }


});