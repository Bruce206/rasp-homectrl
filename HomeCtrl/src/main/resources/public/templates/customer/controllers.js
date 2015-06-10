homectrlapp.factory('Customer', function($resource) {
	return $resource('/customer', {}, {
		'list': {
			url: '/customer/list',
			method: 'GET',
			isArray: true
		}
	});      
});

homectrlapp.controller('CustomerListCtrl', function($scope, Customer, $http) {
	$scope.customers = Customer.list();
	$scope.remove = function(customer){

		Customer.remove({"id":customer.id});
		//"id" = der parametername für den http-request, also das was in java bei der methode erwartet wird
		// customerid ist der wert der id zugewiesen wird
		// : der zuweiser
		// key -> value
		$scope.customers = _.without($scope.customers, customer);

	}
});

homectrlapp.controller('CustomerDetailCtrl', function($scope, customer) {
	$scope.customer = customer;

});

