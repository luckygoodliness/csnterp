Ext.define("Contractremove.store.ContractremoveStore", {
    extend: 'Ext.data.Store',
    model: 'ContractC.model.ContractCModel',
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