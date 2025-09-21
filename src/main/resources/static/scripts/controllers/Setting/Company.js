app.controller('CompanyController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
	$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
	$scope.addEdit = function() {
		$scope.action = '添加/编辑';
		let asideCompany = $aside({scope : $scope, title: $scope.action_nav_name, placement:'bottom',animation:'am-fade-and-slide-top',backdrop:"static",container:'#MainController', templateUrl: __RESOURCE + 'views/Setting/CompanyAddEditAside.html?'+__VERSION});
		asideCompany.$promise.then(function() {
			asideCompany.show();
			$(document).ready(function(){
				
			});
		})
	}
});