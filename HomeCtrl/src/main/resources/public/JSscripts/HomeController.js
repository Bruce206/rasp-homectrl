var homectrlapp = angular.module('homectrlapp', ["ngResource", "ui.router", "angularSpectrumColorpicker"]);

homectrlapp.controller('LedController', function($scope, $rootScope, $timeout){
	$.get('/led/', function(data) {
		$scope.data = data;
	});

	$scope.options = {
		showInput: true, 
		flat: true, 
		preferredFormat: "rgb"
	}

	$.get('/led/1', function(data) {
		$scope.color1 = data;
	});

	$.get('/led/2', function(data) {
		$scope.color2 = data;
	});

	$scope.$watch("color1", function(newValue) {
		if (!$scope.sending) {
			$scope.sending = true;
			$timeout(function() {$scope.sending = false;}, 1000);
			$.post('/led/1', {rgb: $scope.color1}, function(data) {});
		}
	})

	$scope.$watch("color2", function(newValue) {
		if (!$scope.sending) {
			$scope.sending = true;
			$timeout(function() {$scope.sending = false;}, 1000);
			$.post('/led/2', {rgb: $scope.color2}, function(data) {});
		}
	})


});
