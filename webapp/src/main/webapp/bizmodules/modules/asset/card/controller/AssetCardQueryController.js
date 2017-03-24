/**
 * Created by lenovo on 2015/9/29.
 */
Ext.define("Card.controller.AssetCardQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Card.view.AssetCardQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'card-query'
});