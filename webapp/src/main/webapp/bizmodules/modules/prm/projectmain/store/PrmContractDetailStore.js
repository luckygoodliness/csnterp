Ext.define("Projectmain.store.PrmContractDetailStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.PrmContractDetailModel',
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