app.controller('DepotController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		//$ocLazyLoad.load([__RESOURCE+"vendor/libs/md5.min.js",__RESOURCE + "vendor/libs/utils.js"]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = 0; $scope.edit_index = 0; $scope.editType = "";
	//定义变量
	$scope.tab = 1; $scope.depot = {}; $scope.depotList = {};$scope.depotFather = {};
	$scope.depotChild = {};
	let aside;
	$httpService.header('method', 'getDepot');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		let depotList = result.data.item.depotList;//部门职位
		depotList.forEach(item => {
			if(item.depot_father_id == 0) {
				$scope.depotFather.push(item)
			} else {//二级科目
				if(!angular.isDefined($scope.depotChild[item.depot_father_id])) {
					$scope.depotChild[item.depot_father_id] = []
				}
				$scope.depotChild[item.depot_father_id].push(item);
			}
		});
		console.log($scope.depotFather);
		//$scope.$apply();//刷新数据
	})
	
	$scope.addEdit = function(editType, depot, i) {
		$scope.editType = editType;
		if(editType == 'edit' && typeof(depot) != 'undefined') {
			$scope.depot = depot;
			$scope.edit_id = angular.copy(depot.depot_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditDepot.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		if(this.depot == null || this.depot == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.depot.depot_address)) {
			$alert({title: 'Notice', content: '仓库地址必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.depot = angular.copy(this.depot);
		$httpService.header('method', 'saveDepot');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			
			if($scope.editType == "add") {
				$scope.param.depot.depot_id = result.data.item.depot_id;
				$scope.depotList.push(angular.copy($scope.param.depot));
			}
			if($scope.editType == "edit") {
				$scope.depotList[$scope.edit_index] = angular.copy($scope.param.depot);
			}
			$scope.depot = {};
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
			$httpService.header('method', 'deleteDepot');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.depotList[i]; 				
			});
		}
	}
});