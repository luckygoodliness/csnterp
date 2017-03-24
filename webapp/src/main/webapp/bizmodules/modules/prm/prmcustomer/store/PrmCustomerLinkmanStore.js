Ext.define("Prmcustomer.store.PrmCustomerLinkmanStore", {
	extend: 'Ext.data.Store',
model: 'Prmcustomer.model.PrmCustomerLinkmanModel',
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