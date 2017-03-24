Ext.define("Prmcustomer.store.PrmCustomerBankStore", {
	extend: 'Ext.data.Store',
model: 'Prmcustomer.model.PrmCustomerBankModel',
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