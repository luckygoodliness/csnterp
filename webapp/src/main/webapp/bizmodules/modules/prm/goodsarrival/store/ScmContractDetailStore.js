Ext.define("Goodsarrival.store.ScmContractDetailStore", {
	extend: 'Ext.data.Store',
model: 'Goodsarrival.model.ScmContractDetailModel',
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