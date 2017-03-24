Ext.define("Projectmain.store.ProgressDetailPStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.ProgressDetailPModel',
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