/**
 * Created by lenovo on 2015/9/28.test
 */
Ext.define("Projectmain.controller.PickProjectmainQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Projectmain.view.PickProjectmainQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'projectmain-query'
});