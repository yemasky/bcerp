app.controller('CountryCityController', function($rootScope, $scope, $httpService, $location, $translate, $aside,
	$ocLazyLoad, $alert, $stateParams) {
	//$ocLazyLoad.load([__RESOURCE + "vendor/libs/md5.min.js"]);
	$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
	var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = ""; $scope.editType = "";
	//定义变量
	$scope.cityList = [];
	$scope.provinceHash = {}; $scope.provinceList = []; $scope.city = {};
	let aside;
	$httpService.header('method', 'getCountryCity');
	$httpService.post(urlParam, $scope, function(result) {
		$scope.loading.hide();
		$httpService.deleteHeader('method');
		if (result.data.success == false) {
			return;
		}
		let cityList = result.data.item.cityList;
		for (let i in cityList) {
			if (cityList[i].city_father_id == 0) {
				$scope.provinceHash[cityList[i].city_id] = cityList[i];
				$scope.provinceList.push(cityList[i]);
			}
		}
		$scope.cityList = $scope.arrayToTree(cityList);
		console.log($scope.cityList);
		//$scope.$apply();//刷新数据
	})

	$scope.addEdit = function(editType, city, i) {
		$scope.editType = editType;
		if (typeof (city) != 'undefined') {
			$scope.city = city;
			$scope.edit_id = angular.copy(city.city_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({
			scope: $scope, title: $scope.action_nav_name, placement: 'center', animation: 'am-fade-and-slide-top',
			backdrop: "static", container: '#MainController', templateUrl: 'AddEditCountryCity.html'
		});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function() {

			});
		})
	}

	$scope.saveData = function() {
		if (this.city == null || this.city == '') {
			$alert({ title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true });
			return;
		}
		if (!angular.isDefined(this.city.city_name)) {
			$alert({ title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true });
			return;
		}
		$scope.loading.show();
		$scope.param.city = angular.copy(this.city);
		$httpService.header('method', 'saveCountryCity');
		$httpService.post(urlParam + "&edit_id=" + $scope.edit_id, $scope, function(result) {
			$scope.loading.percent();
			$httpService.deleteHeader('method');
			if (result.data.success == false) {
				return;
			}

			if ($scope.editType == "add") {
				$scope.param.city.city_id = result.data.item.city_id;
				$scope.cityList.push(angular.copy($scope.param.city));
			}
			if ($scope.editType == "edit") {
				$scope.cityList[$scope.edit_index] = angular.copy($scope.param.city);
			}
			$scope.city = {};
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
			$httpService.header('method', 'deleteCountryCity');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.cityList[i];
			});
		}
	}
	$scope.arrayToTree = function(data) {
		const map = {};
		const tree = {};
		// 创建一个映射，方便查找节点
		data.forEach(item => {
			map[item.city_id] = { ...item, children: [] };
		});
		// 构建树形结构
		/*data.forEach(item => {
			if (item.city_father_id) {
				map[item.city_father_id].children.push(map[item.city_id]);
			} else {
				tree.push(map[item.city_id]);
			}
		});*/
		data.forEach(item => {
			if (item.city_father_id) {
				if(typeof(tree[item.city_father_id]) == 'undefined') tree[item.city_father_id] = {};
				tree[item.city_father_id][item.city_id] = item;
			} else {
				if(typeof(tree[item.city_id]) == 'undefined') tree[item.city_id] = {};
				tree[item.city_id][item.city_id] = item;
			}
		});
		return tree;
	}
});