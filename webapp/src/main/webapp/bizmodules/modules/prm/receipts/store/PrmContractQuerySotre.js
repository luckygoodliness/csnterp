/**
 * Created by lijx on 2016/8/12.
 */
Ext.define("Receipts.store.PrmContractQuerySotre", {
    extend: 'Ext.data.Store',
    model: 'Receipts.model.PrmContractQueryModel',
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