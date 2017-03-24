/**
 * Created by lenovo on 2015/9/29.
 */
Ext.define("Card.controller.PrmPurchaseReqDetailController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Card.view.PrmPurchaseReqDetailView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'prmpurchasereqdetail-query'
});