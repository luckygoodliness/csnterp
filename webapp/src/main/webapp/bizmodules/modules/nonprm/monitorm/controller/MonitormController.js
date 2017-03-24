Ext.define("Monitorm.controller.MonitormController", {
    extend: 'Scdp.mvc.AbstractController',
    viewClass: 'Monitorm.view.MonitormView',
    loadAction: 'monitorm-load',
    extraEvents: [
    ],
    afterInit : function(){
        var me = this;
        var view = me.view;
        var operateAgreementGrid = view.getCmp("operateAgreementDto");
        var otherNoPrmOutGrid = view.getCmp("otherNoPrmOutDto");
        operateAgreementGrid.store.on('update', function (store, record, operation, mdColumns) {
        });
        otherNoPrmOutGrid.store.on('update', function (store, record, operation, mdColumns) {
        });
    }

});