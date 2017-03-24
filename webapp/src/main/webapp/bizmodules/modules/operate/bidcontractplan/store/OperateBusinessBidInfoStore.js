Ext.define("Bidcontractplan.store.OperateBusinessBidInfoStore", {
    extend: 'Ext.data.Store',
    model: 'Businessbidinfo.model.OperateBusinessBidInfoModel',
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