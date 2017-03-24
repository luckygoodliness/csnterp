Ext.define("Invoice.store.FadInvoiceMaterialStore", {
    extend: 'Ext.data.Store',
    model: 'Invoice.model.FadInvoiceMaterialModel',
    autoLoad: false,
    sorters: [
        {
            property: 'seqNo',
            direction: 'ASC'
        }
    ],
    proxy: {
        type: 'memory',
        reader: {
            type: 'json'
        }
    }
});