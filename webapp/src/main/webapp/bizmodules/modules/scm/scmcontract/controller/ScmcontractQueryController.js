/**
 * Created by lenovo on 2015/9/29.
 */
Ext.define("Scmcontract.controller.ScmcontractQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Scmcontract.view.ScmcontractQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'scmcontract-query'
});