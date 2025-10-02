app.controller('SeaportController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE+"vendor/modules/angular-ui-select/select.min.js?"+__VERSION,
					  __RESOURCE+"vendor/modules/angular-ui-select/select.min.css?"+__VERSION]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
	$scope.param = {}; $scope.edit_id = 0;
	$scope.seaport = {}; $scope.seaportCountry = {}; $scope.seaportList = {};$scope.countryList = {};
	//定义变量
	let aside;$scope.countryHash = {};
	$httpService.header('method', 'getSeaport');
	$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) { 
			return;
		} 
		$scope.seaportList = result.data.item.seaportList;//
		$scope.countryList = result.data.item.countryList;//
		for(i in $scope.countryList) {
			$scope.countryHash[$scope.countryList[i].country_id] = $scope.countryList[i];
		}
	})
	
	$scope.addEdit = function(seaport) {
		if(typeof(seaport) != 'undefined') {
			$scope.seaport = seaport;
			$scope.edit_id = angular.copy(seaport.seaport_id);
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditSeaport.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		if(this.seaport == null || this.seaport == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.seaport.seaport_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.seaport = angular.copy(this.seaport);
		$httpService.header('method', 'saveSeaport');
		$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			$scope.seaport = {};
			$scope.edit_id = 0;
			aside.hide(); 
			$scope.reload($stateParams);
		});
	}
	
	//
	$scope.disabled = undefined;
    $scope.searchEnabled = undefined;

    $scope.enable = function() {
    $scope.disabled = false;
    };

    $scope.disable = function() {
    $scope.disabled = true;
    };

    $scope.enableSearch = function() {
    $scope.searchEnabled = true;
    }

    $scope.disableSearch = function() {
    $scope.searchEnabled = false;
    }

    $scope.clear = function() {
    $scope.person.selected = undefined;
    $scope.address.selected = undefined;
    $scope.country.selected = undefined;
    };
	$scope.selectSeaport = function() {
		$scope.seaport.country_enname = $scope.seaport.country.country_enname;
		console.log($scope.seaport,$scope.seaportCountry);
	}
});