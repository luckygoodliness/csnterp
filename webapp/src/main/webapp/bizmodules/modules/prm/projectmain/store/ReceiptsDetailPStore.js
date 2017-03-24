Ext.define("Projectmain.store.ReceiptsDetailPStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.ReceiptsDetailPModel',
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