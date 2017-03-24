/**
 * Created by lenovo on 2015/9/29.
 */
Ext.define("Supplierinfor.controller.SupplierQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Supplierinfor.view.SupplierQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'supplierinfor-query'
});