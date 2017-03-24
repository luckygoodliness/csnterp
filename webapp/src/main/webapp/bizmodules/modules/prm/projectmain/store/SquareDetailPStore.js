Ext.define("Projectmain.store.SquareDetailPStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.SquareDetailPModel',
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