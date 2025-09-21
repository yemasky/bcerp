app.controller('CompanyAddEditController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
	$scope.setActionNavName($rootScope._self_module.module_id, "添加公司");
});