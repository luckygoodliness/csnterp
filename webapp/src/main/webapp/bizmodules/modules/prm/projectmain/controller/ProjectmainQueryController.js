/**
 * Created by lenovo on 2015/9/28.
 */
Ext.define("Projectmain.controller.ProjectmainQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Projectmain.view.ProjectmainQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'projectmain-query'
});