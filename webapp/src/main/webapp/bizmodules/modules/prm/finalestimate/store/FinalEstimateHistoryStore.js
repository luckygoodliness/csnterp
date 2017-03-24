Ext.define("Finalestimate.store.FinalEstimateHistoryStore", {
    extend: 'Ext.data.Store',
    model: 'Finalestimate.model.FinalEstimateHistoryModel',
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