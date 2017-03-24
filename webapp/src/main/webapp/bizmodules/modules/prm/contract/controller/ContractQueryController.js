/**
 * Created by lenovo on 2015/9/29.
 */
Ext.define("Contract.controller.ContractQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Contract.view.ContractQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'prm-pickcontract-query'
});