/**
 * Created by lenovo on 2015/10/09.
 */
Ext.define("Scmcontract.controller.ScmcontractdetailQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Scmcontract.view.ScmcontractdetailQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'scmcontract-query'
});
