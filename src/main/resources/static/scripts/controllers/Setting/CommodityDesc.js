app.controller('CommodityDescController', function($rootScope, $scope, $httpService, $location, $translate, $aside,
	$ocLazyLoad, $alert, $stateParams) {
	//$ocLazyLoad.load([__RESOURCE + "vendor/libs/md5.min.js"]);
	$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
	var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = ""; $scope.editType = "";
	//定义变量
	$scope.descList = [];$scope.desc = {};
	let aside;
	$httpService.header('method', 'getDesc');
	$httpService.post(urlParam, $scope, function(result) {
		$scope.loading.hide();
		$httpService.deleteHeader('method');
		if (result.data.success == false) {
			return;
		}
		$scope.descList = result.data.item.descList;
		//$scope.$apply();//刷新数据
	})

	$scope.addEdit = function(editType, desc, i) {
		$scope.editType = editType;
		if (editType == 'edit' && typeof (desc) != 'undefined') {
			$scope.desc = desc;
			$scope.edit_id = angular.copy(desc.desc_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({
			scope: $scope, title: $scope.action_nav_name, placement: 'center', animation: 'am-fade-and-slide-top',
			backdrop: "static", container: '#MainController', templateUrl: 'AddEditCommodityDesc.html'
		});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function() {

			});
		})
	}

	$scope.saveData = function() {
		if (this.desc == null || this.desc == '') {
			$alert({ title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true });
			return;
		}
		if (!angular.isDefined(this.desc.desc_cn)) {
			$alert({ title: 'Notice', content: '值必须填写！', templateUrl: '/modal-warning.html', show: true });
			return;
		}
		$scope.loading.show();
		$scope.param.desc = angular.copy(this.desc);
		$httpService.header('method', 'saveDesc');
		$httpService.post(urlParam + "&edit_id=" + $scope.edit_id, $scope, function(result) {
			$scope.loading.percent();
			$httpService.deleteHeader('method');
			if (result.data.success == false) {
				return;
			}

			if ($scope.editType == "add") {
				$scope.param.desc.desc_id = result.data.item.desc_id;
				$scope.descList.push(angular.copy($scope.param.desc));
				//Array.prototype.push.apply($scope.descList, $scope.param.desc);
			}
			if ($scope.editType == "edit") {
				$scope.descList[$scope.edit_index] = angular.copy($scope.param.desc);
			}
			$scope.desc = {};
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
			$httpService.header('method', 'deleteDesc');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.descList[i];
			});
		}
	}
});