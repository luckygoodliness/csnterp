Ext.define("Certificate.controller.BdCurrtypeChoiceController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Certificate.view.BdCurrtypeChoiceView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'certificate-query',
    afterInit: function () {
        var me = this;
        if (window.parent.bdCurrtypeChoiceCurrencyCode == undefined) {
            me.view.getCmp("currencyCode").sotValue(Erp.Util.GetCookie("bdCurrtypeChoiceCurrencyCode"));
        }
        else {
            me.view.getCmp("currencyCode").sotValue(window.parent.bdCurrtypeChoiceCurrencyCode);
        }
        me.doQuery();
    }
});