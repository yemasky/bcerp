app.controller('CountryCityController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE+"vendor/libs/md5.min.js"]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
	var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = "";$scope.editType = "";
	//定义变量
	$scope.cityList = [{"city_id":0,"country_id":1,"city_name":"作为省份","city_py":"","city_father_id":0}];
	$scope.provinceHash = {};
	let aside;
	$httpService.header('method', 'getCountryCity');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.cityList = $scope.cityList.concat(result.data.item.cityList);
		for(let i in $scope.cityList) {
			if($scope.cityList[i].city_father_id == 0) $scope.provinceHash[$scope.cityList[i].city_id] = $scope.cityList[i];
		}		
		//$scope.$apply();//刷新数据
	})
	
	$scope.addEdit = function(editType, category, i) {
		$scope.editType = editType;
		if(typeof(category) != 'undefined') {
			$scope.category = category;
			$scope.edit_id = angular.copy(category.category_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditCountryCity.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		if(this.category == null || this.category == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.category.category_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.category = angular.copy(this.category);
		$httpService.header('method', 'saveCountryCity');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			
			if($scope.editType == "add") {
				$scope.param.category.category_id = result.data.item.category_id;
				$scope.categoryList.push(angular.copy($scope.param.category));
			}
			if($scope.editType == "edit") {
				$scope.categoryList[$scope.edit_index] = angular.copy($scope.param.category);
			}
			$scope.category = {};
			$scope.edit_id = 0;
			aside.hide(); 
		});
	}
	$scope.delete = function(delete_id, i) {
		var message = '您确定要删除吗？';
		$scope.confirm({'content': message,'callback': deleteData});
		function deleteData() {
			$scope.param = {};
			$scope.param.delete_id = delete_id;
			$httpService.header('method', 'deleteCountryCity');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.categoryList[i]; 				
			});
		}
	}
});