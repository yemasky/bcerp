app.controller('VehicleModelController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE+"vendor/libs/daterangepicker.js?"+__VERSION]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = 0;
	//定义变量
	$scope.vehicleModel = {};$scope.vehicleModelList = [];
	$scope.classifyHash = {};$scope.classifyList = [];
	$scope.edit_index = 0;$scope.editType = "";
	let aside;
	$httpService.header('method', 'getVehicleModel');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.vehicleModelList = result.data.item.vehicleModelList;//
		$scope.classifyList = result.data.item.classifyList;//
		for(i in $scope.classifyList) {
			$scope.classifyHash[$scope.classifyList[i].classify_id] = $scope.classifyList[i];
		}
		//$scope.$apply();//刷新数据
	})
	
	$scope.addEdit = function(editType, vehicleModel, i) {
		$scope.editType = editType;
		if(typeof(vehicleModel) != 'undefined') {
			$scope.vehicleModel = angular.copy(vehicleModel);
			$scope.edit_id = angular.copy(vehicleModel.vehicle_model_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditVehicleModel.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		if(this.vehicleModel == null || this.vehicleModel == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.vehicleModel.vehicle_model)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.vehicleModel = angular.copy(this.vehicleModel);
		$httpService.header('method', 'saveVehicleModel');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			
			if($scope.editType == "add") {
				$scope.param.vehicleModel.vehicle_model_id = result.data.item.vehicle_model_id;
				$scope.vehicleModelList.push(angular.copy($scope.param.vehicleModel));
			}
			if($scope.editType == "edit") {
				$scope.vehicleModelList[$scope.edit_index] = angular.copy($scope.param.vehicleModel);
			}
			$scope.vehicleModel = {};
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
			$httpService.header('method', 'deleteVehicleModel');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.vehicleModelList[i]; 				
			});
		}
	}
});