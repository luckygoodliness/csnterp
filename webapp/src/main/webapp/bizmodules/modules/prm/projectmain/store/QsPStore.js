Ext.define("Projectmain.store.QsPStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.QsPModel',
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