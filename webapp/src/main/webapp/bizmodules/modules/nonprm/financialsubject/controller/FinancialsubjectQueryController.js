/**
 * Created by lenovo on 2015/9/29.
 */
Ext.define("Financialsubject.controller.FinancialsubjectQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Financialsubject.view.FinancialsubjectQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'financialsubject-query'
});