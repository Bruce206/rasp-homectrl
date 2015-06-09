skinapp.config(function($stateProvider, $urlRouterProvider) {

  $stateProvider

    .state('customer', {
       url: '/customer',
       template: "<div ui-view />"
    })

    .state('customer.list', {
        url: '/list',
        templateUrl: 'templates/customer/list.html',
        controller: 'CustomerListCtrl'
    })

    .state('customer.detail', {
        url: '/detail/:id',
        templateUrl: 'templates/customer/detail.html',
        controller: 'CustomerDetailCtrl',
        resolve: {
          customer: function($stateParams, $q, $state, Customer) {
            console.log($stateParams.id);
                return Customer.get({
                    id: $stateParams.id
                }, function(success) {
                    console.log(success);
                }, function(error) {
                  alert("Kunde konnte nicht geladen werden!");
                }).$promise;
            }
        }
    })

});