Ext.define("Projectmain.store.PrmAssociatedUnitsStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.PrmAssociatedUnitsModel',
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