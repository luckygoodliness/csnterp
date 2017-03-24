Ext.define("Purchaseplan.store.PrmPurchasePackageRecordStore", {
    extend: 'Ext.data.Store',
    model: 'Purchaseplan.model.PrmPurchasePackageRecordModel',
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