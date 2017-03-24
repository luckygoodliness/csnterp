Ext.define("Projectmain.store.InnerProjectInfosStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.InnerProjectInfosModel',
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