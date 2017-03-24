Ext.define("ContractE.store.ContractEStore", {
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