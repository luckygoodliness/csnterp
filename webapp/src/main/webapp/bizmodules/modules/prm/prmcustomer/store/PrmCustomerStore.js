Ext.define("Prmcustomer.store.PrmCustomerStore", {
	extend: 'Ext.data.Store',
model: 'Prmcustomer.model.PrmCustomerModel',
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