Ext.define("FadSupplierinfo.store.FadSupplierStore", {
    extend: 'Ext.data.Store',
    model: 'FadSupplierinfo.model.FadSupplierModel',
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