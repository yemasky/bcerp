app.controller('CommDictController', function($rootScope, $scope, $httpService, $location, $translate, $aside,
	$ocLazyLoad, $alert, $stateParams) {
	//$ocLazyLoad.load([__RESOURCE + "vendor/libs/md5.min.js"]);
	$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
	var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = ""; $scope.editType = "";
	//定义变量
	$scope.dictList = [];$scope.dict = {}; $scope.moduleHash = [];$scope.fieldHash = {};
	$scope.moduleFather = [];$scope.module = [];$scope.field = [];
	let aside;
	$httpService.header('method', 'getCommDict');
	$httpService.post(urlParam, $scope, function(result) {
		$scope.loading.hide();
		$httpService.deleteHeader('method');
		if (result.data.success == false) {
			return;
		}
		let dictModuleList = result.data.item.dictModuleList;
		let moduleList = result.data.item.moduleList;
		let dictList = result.data.item.dictList;
		moduleList.forEach(item => {
			$scope.moduleHash[item.module_id] = { ...item };
		});
		for (let i in dictModuleList) {
			let dictModule = dictModuleList[i];
			let module_father_name = $scope.moduleHash[dictModule.module_father_id].module_name;
			let module_name = $scope.moduleHash[dictModule.module_id].module_name;
			$scope.moduleFather.push({'module_id':dictModule.module_father_id,'module_name':module_father_name});
			if(typeof($scope.module[dictModule.module_father_id]) == 'undefined') 
				$scope.module[dictModule.module_father_id] = [];
			$scope.module[dictModule.module_father_id].push({'module_id':dictModule.module_id,'module_name':module_name});
			if(typeof($scope.field[dictModule.module_id]) == 'undefined')
				$scope.field[dictModule.module_id] = [];
			$scope.field[dictModule.module_id].push({'field_id':dictModule.dict_field,'field_name':dictModule.dict_field_name});
			$scope.fieldHash[dictModule.dict_field] = dictModule.dict_field_name;
		}
		if(angular.isDefined(dictList) && dictList != '') $scope.dictList = $scope.arrayToTree(dictList);
		console.log($scope.dictList);
		//$scope.$apply();//刷新数据
	})

	$scope.addEdit = function(editType, dict, i) {
		$scope.editType = editType;
		if (editType == 'edit' && typeof (dict) != 'undefined') {
			$scope.dict = dict;
			$scope.edit_id = angular.copy(dict.dict_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({
			scope: $scope, title: $scope.action_nav_name, placement: 'center', animation: 'am-fade-and-slide-top',
			backdrop: "static", container: '#MainController', templateUrl: 'AddEditCommDict.html'
		});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function() {

			});
		})
	}

	$scope.saveData = function() {
		if (this.dict == null || this.dict == '') {
			$alert({ title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true });
			return;
		}
		if (!angular.isDefined(this.dict.dict_val)) {
			$alert({ title: 'Notice', content: '值必须填写！', templateUrl: '/modal-warning.html', show: true });
			return;
		}
		$scope.loading.show();
		$scope.param.dict = angular.copy(this.dict);
		$httpService.header('method', 'saveCommDict');
		$httpService.post(urlParam + "&edit_id=" + $scope.edit_id, $scope, function(result) {
			$scope.loading.percent();
			$httpService.deleteHeader('method');
			if (result.data.success == false) {
				return;
			}

			if ($scope.editType == "add") {
				//$scope.param.city.city_id = result.data.item.city_id;
				//$scope.cityList.push(angular.copy($scope.param.city));
				//Array.prototype.push.apply($scope.cityList, $scope.param.city);
			}
			if ($scope.editType == "edit") {
				//$scope.cityList[$scope.edit_index] = angular.copy($scope.param.city);
			}
			$scope.dict = {};
			$scope.edit_id = 0;
			aside.hide();
		});
	}
	$scope.delete = function(delete_id, i) {
		var message = '您确定要删除吗？';
		$scope.confirm({ 'content': message, 'callback': deleteData });
		function deleteData() {
			$scope.param = {};
			$scope.param.delete_id = delete_id;
			$httpService.header('method', 'deleteCommDict');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				//delete $scope.cityList[i];
			});
		}
	}
	$scope.arrayToTree = function(data) {
		const map = {};
		// 创建一个映射，方便查找节点
		data.forEach(item => {
			if(!angular.isDefined(map[item.module_father_id])) {
				map[item.module_father_id] = {};
				map[item.module_father_id][item.module_id] = {};
				map[item.module_father_id][item.module_id][item.dict_field] = [];
			}
			map[item.module_father_id][item.module_id][item.dict_field].push(item);
		});
		return map;
	}
});