/**
 * Created by lenovo on 2015/9/28.
 */
Ext.define("Projectmain.controller.InnerProjectQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Projectmain.view.InnerProjectQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'inner-project-query'
});