app.controller('BuyerPaymentController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		//$ocLazyLoad.load([__RESOURCE + "vendor/modules/angular-ui-select/select.min.js?" + __VERSION]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];$scope.__IMGWEB = __IMGWEB;
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = "";
	//定义变量
	$scope.payment = {};$scope.paymentList = [];
	let aside;
	$httpService.header('method', 'getBuyerPayment');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.paymentList = result.data.item.paymentList;
	})
	
	$scope.addEdit = function(editType, payment, i) {
		$scope.editType = editType;
		$scope.payment = {};
		if(editType == 'edit' && typeof(payment) != 'undefined') {
			$scope.edit_id = angular.copy(payment.payment_id);
			$scope.edit_index = i;
			$scope.payment = angular.copy(payment);
			//工作流审核相关
			$rootScope.param = {};$rootScope.param.auditing = $scope.payment;$rootScope.param.id = $scope.payment.payment_id;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditBuyerPayment.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		console.log(this.payment, $scope.payment);
		if(this.payment == null || this.payment == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.payment.payment_num)) {
			$alert({title: 'Notice', content: '序号必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.payment = angular.copy(this.payment);
		$httpService.header('method', 'saveBuyerPayment');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return;
			} 
			if($scope.editType == "add") {
				$scope.param.payment.payment_id = result.data.item.payment_id;
				$scope.paymentList.push(angular.copy($scope.param.payment));
			}
			if($scope.editType == "edit") {
				$scope.paymentList[$scope.edit_index] = angular.copy($scope.param.payment);
			}
			$scope.payment = {};
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
			$httpService.header('method', 'deleteBuyerPayment');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.currencyRateList[i]; 				
			});
		}
	}
	$scope.setNewVal =function () {
		$scope.payment.payment_sdesc = $scope.payment.payment_deposit+"%定金，发货前"+$scope.payment.payment_before
										+"%，入库后"+$scope.payment.payment_days+"天付"+$scope.payment.payment_after
										+"%，凭发票付"+$scope.payment.payment_receipt+"%";
		//$scope.payment.payment_desc = "";
		
	}

});