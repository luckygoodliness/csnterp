Ext.define("Scmdepartmentbuyer.store.ScmDepartmentBuyerStore", {
    extend: 'Ext.data.Store',
    model: 'Scmdepartmentbuyer.model.ScmDepartmentBuyerModel',
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