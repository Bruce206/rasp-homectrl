var homectrlapp = angular.module('homectrlapp', ["ngResource", "ui.router"]);

homectrlapp.controller('LedController', function($scope, $rootScope){
console.log("data")

	$.get('/led/', function(data) {
		$scope.colors = data;
	});
});
