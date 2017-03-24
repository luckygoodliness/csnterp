Ext.define("Prmprojectmainc.store.PrmContractDetailCStore", {
    extend: 'Ext.data.Store',
    model: 'Prmprojectmainc.model.PrmContractDetailCModel',
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