Ext.define("Projectmain.store.PayDetailPStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.PayDetailPModel',
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