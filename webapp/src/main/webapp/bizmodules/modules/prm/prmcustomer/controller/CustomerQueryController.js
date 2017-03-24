/**
 * Created by lenovo on 2015/9/29.
 */
Ext.define("Prmcustomer.controller.CustomerQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Prmcustomer.view.CustomerQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'prmcustomer-query'
});