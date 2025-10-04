app.controller('AuditingController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE + "vendor/modules/angular-ui-select/select.min.js?" + __VERSION,
		__RESOURCE + "vendor/modules/angular-ui-select/select.min.css?" + __VERSION,]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];$scope.__IMGWEB = __IMGWEB;
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = "";
	//定义变量
	var tree,treeData = [];$scope.my_tree = tree = {};$scope.step = "0";$scope.step_length = 0;
	$scope.auditingList = [];$scope.auditing = {};$scope.auditing.examine = {};$scope.sectorHash = {};$scope.positionList = [];
	$scope.auditingModuleList = {};$scope.auditingModuleHash = {};$scope.employeeList = {};$scope.auditingEmployeeList = [];
	//$scope.employee-Hash = {};
	let aside;
	$httpService.header('method', 'getAuditing');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.auditingModuleList = result.data.item.auditingModuleList;
		for (var index = 0; index < $scope.auditingModuleList.length; index++) {
			$scope.auditingModuleHash[$scope.auditingModuleList[index].module_id] = $scope.auditingModuleList[index]; //哈希值
		}
		$scope.auditingList = result.data.item.auditingVoList;//
		$scope.employeeList = result.data.item.employeeList;
		for (var index = 0; index < $scope.employeeList.length; index++) {
			//$scope.employee-Hash[$scope.employeeList[index].e_id] = $scope.employeeList[index]; //哈希值
		}
		let sectorHash = {}; //部门职位
		let _companySectorList = result.data.item.companySectorList;
		for (var index = 0; index < _companySectorList.length; index++) {
			sectorHash[_companySectorList[index].sector_id] = _companySectorList[index]; //哈希值
			if(_companySectorList[index].sector_type == 'position') $scope.positionList.push(_companySectorList[index]);
		}
		//$scope.sectorHash = sectorHash;
			//组织架构树
		treeData = getTreeData(result.data.item.companySectorList);
		$scope.my_data = treeData;

		function getTreeData(list) {
			var treeData = [];
			let my_data = [];my_data[0] = {};my_data[0].company_id = 1;my_data[0].label = '博威利尔';
			list.forEach(function(item) {
				if (item.sector_type == 'sector') {
					var parent = {};
					if (item.sector_father_id != item.sector_id) {
						parent = sectorHash[item.sector_father_id];
					} //console.log(parent);
					item.label = item.sector_name; 
					item.children = [];
					if (parent) {
						if (angular.isUndefined(parent.children)) parent.children = [];
						parent.children.push(item);
					} else {
						treeData.push(item);
					}
				}
			})
			my_data[0].children = treeData;
			return my_data;
		}
	})
	
	$scope.addEdit = function(editType, auditing, i) {
		$scope.editType = editType;
		$scope.auditing = {};
		if(editType == 'edit' && typeof(auditing) != 'undefined') {
			$scope.auditing = angular.copy(auditing);//编辑开始
			$scope.auditing.module_id = angular.copy($scope.auditingModuleHash[auditing.module_id]);//模块
			$scope.step = "";
			for(j in $scope.auditing.examine) {//解析examine
				let examine = $scope.auditing.examine[j];
				let position_id =  examine.position_id;
				let sector_id = examine.sector_id;
				$scope.auditing.examine[j].position_id = angular.copy($scope.sectorHash[position_id]);
				if(typeof($scope.auditingEmployeeList[i]) == "undefined") {
					$scope.auditingEmployeeList[i] = [];
				} else {
					$scope.auditingEmployeeList[i] = [];//清空之前的数据
				}
				for(e in $scope.employeeList) {
					let employee = $scope.employeeList[e];
					if(employee.position_id == position_id && employee.sector_id == sector_id) 
						$scope.auditingEmployeeList[i].push(employee);
				}
				$scope.auditing.examine[j].employee_id = angular.copy($scope.employeeHash[examine.employee_id]);
				$scope.auditing.examine[j].agent =  $scope.auditing.examine[j].agent == 1 ? true : false;
				$scope.step = $scope.step + "" + j;
			}
			$scope.edit_id = angular.copy(auditing.auditing_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditAuditing.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		console.log(this.auditing, $scope.auditing);
		if(this.auditing == null || this.auditing == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.auditing.auditing_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.auditing.module_id) || this.auditing.module_id == "") {
			$alert({title: 'Notice', content: '审核模块必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.auditing = angular.copy(this.auditing);
		for(i in this.auditing.examine) {
			$scope.param.auditing.examine[i].employee_id = angular.copy(this.auditing.examine[i].employee_id.e_id);
			$scope.param.auditing.examine[i].position_id = angular.copy(this.auditing.examine[i].position_id.sector_id);
			$scope.param.auditing.module_id = angular.copy(this.auditing.module_id.module_id);
			$scope.param.auditing.examine[i].step = i;
		}
		$httpService.header('method', 'saveAuditing');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return;
			} 
			if($scope.editType == "add") {
				$scope.param.auditing.auditing_id = result.data.item.auditing_id;
				$scope.auditingList.push(angular.copy($scope.param.auditing));
			}
			if($scope.editType == "edit") {
				$scope.auditingList[$scope.edit_index] = angular.copy($scope.param.auditing);
			}
			$scope.auditing = {};
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
			$httpService.header('method', 'deleteAuditing');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.auditingList[i]; 				
			});
		}
	}
	var examine_index = 0;
	$scope.showSector = function(evn) {
		console.log(evn);
		examine_index = evn;
		asideSector = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',backdrop: "static",
			container: '#MainController',templateUrl: 'ShowSector.html' + __VERSION
		});
		asideSector.$promise.then(function() {
			asideSector.show();
			$(document).ready(function() {
			});
		})
	}
	var branchSetSector = {};
	$scope.setSector = function(branch) {
		branchSetSector = branch;//
	}
	$scope.setSectorOk = function() {
		if(typeof(branchSetSector.sector_id) != "undefined") {
			//angular.extend({}, $scope.auditing.examine, { examine_index: branchSetSector.sector_id });
			if(typeof($scope.auditing.examine)== 'undefined') $scope.auditing.examine = {};
			if(typeof($scope.auditing.examine[examine_index])== 'undefined') $scope.auditing.examine[examine_index] = {};
			$scope.auditing.examine[examine_index].sector_id = branchSetSector.sector_id;
		}
		//
		asideSector.hide();
		console.log($scope.auditing);
	}
	$scope.selectPosition = function(i) {
		let sector_id = $scope.auditing.examine[i].sector_id;
		let position = $scope.auditing.examine[i].position_id;
		console.log(i, sector_id, position);
		let position_id = position.sector_id;
		if(typeof($scope.auditingEmployeeList) == "undefined") $scope.auditingEmployeeList = {};
		if(typeof($scope.auditingEmployeeList[i]) == "undefined") {
			$scope.auditingEmployeeList[i] = [];
		} else {
			$scope.auditingEmployeeList[i] = [];//清空之前的数据
		}
		for(e in $scope.employeeList) {
			let employee = $scope.employeeList[e];
			if(employee.position_id == position_id && employee.sector_id == sector_id) $scope.auditingEmployeeList[i].push(employee);
		}
	}
	$scope.addStep = function() {
		if($scope.step_length > 4) {
			$alert({title: 'Notice', content: '最多允许6个审核步骤', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.step_length++;
		$scope.step=$scope.step+''+$scope.step_length;
	}
	$scope.reduceStep = function() {
		if($scope.step_length < 1) {
			$alert({title: 'Notice', content: '最少需要1个审核步骤', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.step=$scope.step.substring(0,$scope.step.length-1);
		$scope.step_length--;
	}
});