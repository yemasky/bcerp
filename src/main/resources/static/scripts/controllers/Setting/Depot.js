app.controller('DepotController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		//$ocLazyLoad.load([__RESOURCE+"vendor/libs/md5.min.js",__RESOURCE + "vendor/libs/utils.js"]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = 0; $scope.edit_index = 0; $scope.editType = "";
	//定义变量
	$scope.tab = 1; $scope.depot = {}; $scope.depot.depot_type = 1; $scope.depotList = {};$scope.depotFather = {};
	$scope.depotChild = {};
	//增加项相关
	$scope.step = {};$scope.step['edit'] = {};$scope.step['edit'].step = "0";$scope.step['edit'].length = 0;
	$scope.step['add'] = {};$scope.step['add'].step = "0";$scope.step['add'].length = 0;
	let aside;
	$httpService.header('method', 'getDepot');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.depotList = result.data.item.depotList;//
		$scope.parseData();
		//$scope.$apply();//刷新数据
	})
	$scope.parseData = function() {
		let depotList = $scope.depotList;
		depotList.forEach(item => {
			if(item.depot_father_id == 0) {
				$scope.depotFather[item.depot_id] = item
			} else if(item.depot_type == 0) {//二级科目
				if(!angular.isDefined($scope.depotChild[item.depot_father_id])) {
					$scope.depotChild[item.depot_father_id] = {};
				}
				$scope.depotChild[item.depot_father_id][item.depot_id] = item;
			} if(item.depot_type == 0) {
			
			}
		});
		console.log($scope.depotChild);
	}
	$scope.addEdit = function(editType, depot, i) {
		$scope.editType = editType;
		$scope.edit_id = 0;
		if(editType == 'edit' && typeof(depot) != 'undefined') {
			$scope.depot = depot;
			$scope.depot.depot_temp_code = depot.depot_code;
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
		if(!angular.isDefined(this.depot.depot_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
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
			$scope.parseData();
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
	
	$scope.addStep = function(key) {
		if(!angular.isDefined($scope.step[key])) {
			$scope.step[key] = {};$scope.step[key].length = 0;$scope.step[key].step = "0";
		}
		$scope.step[key].length++;
		$scope.step[key].step=$scope.step[key].step+''+$scope.step[key].length;
	}
	$scope.reduceStep = function(key) {
		if($scope.step[key].length < 1) {$alert({title: 'Notice', content: '最少需要1个', templateUrl: '/modal-warning.html', show: true});return;}
		$scope.step[key].step=$scope.step[key].step.substring(0,$scope.step[key].step.length-1);
		$scope.step[key].length--;
	}
	$scope.setChange = function() {
		if(angular.isDefined($scope.depotChild[$scope.depot.depot_father_id]) && 
			angular.isDefined($scope.depotChild[$scope.depot.depot_father_id][$scope.depot.depot_id])) {
			$scope.depot = angular.copy($scope.depotChild[$scope.depot.depot_father_id][$scope.depot.depot_id]);
			$scope.depot.depot_type = 1;
		} else {
			//$scope.depot = {};
		}
		console.log($scope.depot);
	}
});