Ext.define("ContractC.store.ContractCStore", {
    extend: 'Ext.data.Store',
    model: 'Contract.model.ContractCModel',
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