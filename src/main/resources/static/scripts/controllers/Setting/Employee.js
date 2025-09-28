app.controller('EmployeeController', function($rootScope, $scope, $httpService, $location, $translate, $aside,
	$ocLazyLoad, $alert, $stateParams) {
	$ocLazyLoad.load([__RESOURCE + "vendor/modules/angular-ui-select/select.min.js?" + __VERSION,
		__RESOURCE + "vendor/modules/angular-ui-select/select.min.css?" + __VERSION
	]);
	var _channel = $stateParams.channel,treeData = [];
	var urlParam = 'channel=' + _channel;
	$scope.param = {};$scope.param.sector = {};$scope.sectorHash = {};$scope.companyList = {};$scope.companyHash = {}; //定义变量
	$scope.company = {};$scope.activeSector = 0;$scope.activeTab = 1;
	var tree;$scope.my_tree = tree = {};
	$scope.loading.show();
	//获取页面数据
	$httpService.header('method', 'getSector');
	$httpService.post('app.do?' + urlParam, $scope, function(result) {
		$scope.loading.hide();
		$httpService.deleteHeader('method');
		if (result.data.success == false) {
			return;
		}
		let sectorHash = {};
		let _companySectorList = result.data.item.companySectorList; //部门职位
		for (var index = 0; index < _companySectorList.length; index++) {
			sectorHash[_companySectorList[index].sector_id] = _companySectorList[index]; //哈希值
		}
		$scope.sectorHash = sectorHash;
		$scope.companyList = result.data.item.companyList;
		for (var index = 0; index < $scope.companyList.length; index++) {
			$scope.companyHash[$scope.companyList[index].company_id] = $scope.companyList[index];
		}
		var roleList = result.data.item.channelRoleList;
		var channelRoleList = [],
			k = 0;
		if (roleList != '') {
			for (var i in roleList) {
				channelRoleList[k] = roleList[i];
				k++;
			}
		}
		//如果有father 自己就是[],
		$scope.channelRoleList = channelRoleList;
		//组织架构树
		treeData = getTreeData(result.data.item.companySectorList);
		$scope.my_data = treeData;

		function getTreeData(list) {
			var treeData = [];
			let my_data = [];my_data[0] = {};my_data[0].company_id = 1;my_data[0].label = '部门职位';
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
	});
	$scope.my_data = treeData;
	//***********添加修改	//SectorPosition/////////////////////////////////////////////////////////////////////////////
	$scope.SectorPosition = function(branch) {
		console.log(branch);
		$scope.company.selected = $scope.companyHash[branch.company_id];
		$scope.param.sector.company_id = $scope.company.selected.company_id;
	}
	$scope.selectChange = function() {
		$scope.param.sector.company_id = $scope.company.selected.company_id;
		console.log($scope.param.sector);
	}
	$scope.setActiveSector = function(sector) {
		$scope.activeSector = sector.sector_id;
		$scope.param.sector = sector;
	}
	var asideSectorPosition = '';
	$scope.sectorPositionEditType = '';
	$scope.sectorPositionEdit = function(branch_type) {
		var title = '',branch = tree.get_selected_branch();
		$scope.sectorPositionEditType = branch_type;
		if (branch_type == 'sector') { //部门 
			title = '部门';
			$scope.param.sector.sector_type = 'sector';
			if (branch == null) {$alert({title: 'Error',content: '选择节点，再点击添加！',templateUrl: '/modal-warning.html',show: true});return;}
			if (branch.level >= 4) {$alert({title: 'Error',content: '部门只能添加3级！',templateUrl: '/modal-warning.html',show: true});return;}
			$scope.param.sector.sector_father_id = 0; //頂級部門
			if (angular.isDefined(branch.sector_id)) $scope.param.sector.sector_father_id = branch.sector_id;
		} else if (branch_type == 'edit') {
			if (branch == null) {
				$alert({title: 'Error',content: '选择节点，再点击修改！',templateUrl: '/modal-warning.html',show: true});
				return;
			}
			if(branch.level == 1) return;
			title = '部门';
			$scope.param.edit_id = branch.sector_id;
			$scope.param.sector.sector_name = branch.label;
		} else if (branch_type == 'position') { //职位
			title = '职位';
			$scope.param.sector.sector_type = 'position';
			$scope.param.sector.company_id = 1;
			$scope.param.sector.sector_father_id = 0;
			if(branch != null) {
				$scope.param.sector.company_id = branch.company_id;
				$scope.param.sector.sector_father_id = branch.sector_id;
			}
		} else if(branch_type == 'editPosition') {
			if (typeof($scope.param.sector.sector_name) == 'undefined') {
				$alert({title: 'Error',content: '选择需要修改的职位，再点击修改！',templateUrl: '/modal-warning.html',show: true});
				return;
			}
			$scope.param.edit_id = $scope.param.sector.sector_id;
			title = '职位';
		}
		//if(branch != null) $scope.param = branch;
		$scope.action = '添加/编辑' + title;
		asideSectorPosition = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',
			backdrop: "static",container: '#MainController',templateUrl: __RESOURCE + 'views/Setting/SectorPositionAddEdit.tpl.html?' + __VERSION
		});
		asideSectorPosition.$promise.then(function() {
			asideSectorPosition.show();
			$(document).ready(function() {

			});
		})
	}
	$scope.saveSectorPositionData = function() {
		console.log($scope.company.selected);
		asideSectorPosition.hide();
		var message = '您确定要添加/修改吗？';
		$scope.confirm({'content': message,'callback': close});
		var branch = tree.get_selected_branch();
		function close() {
			console.log($scope.param);
			var sector_name = angular.copy($scope.param.sector.sector_name);
			$httpService.header('method', 'saveSectorPosition');
			$httpService.post('app.do?' + urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {return;}
				if ($scope.sectorPositionEditType == 'edit') {
					branch.label = sector_name;
				} else if($scope.sectorPositionEditType == 'position') {
					let sector_id = result.data.item.sector_id;
					$scope.sectorHash[sector_id] = angular.copy($scope.param.sector);
					$scope.sectorHash[sector_id].sector_id = sector_id;
				} else if($scope.sectorPositionEditType == 'editPosition') {
					let sector_id = result.data.item.sector_id;
					$scope.sectorHash[sector_id].sector_name = sector_name;
				} else if($scope.sectorPositionEditType == 'sector') {
					return tree.add_branch(branch, {label: sector_name,
						company_id: branch.company_id
					});
				}
			});
		}
	};
	$scope.sectorPositionDelete = function(branch_type) {
		if(branch_type == 'delete') {
			var branch = tree.get_selected_branch();
			var parent = tree.get_parent_branch(branch);
			var children = tree.get_children(branch);
			//children 有不能删
			if (children.length > 0) {
				$alert({title: 'Error',content: '有子目录，删除失败！',templateUrl: '/modal-warning.html',show: true});
				return;
			}
		}
		
		var message = '您确定要删除吗？';
		$scope.confirm({'content': message,'callback': deleteData});

		function deleteData() {
			if(branch_type == 'delete') $scope.param.delete_id = branch.sector_id;
			if(branch_type == 'deletePosition') $scope.param.delete_id = $scope.param.sector.sector_id;
			$httpService.header('method', 'deleteSectorPosition');
			$httpService.post('app.do?' + urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == '0') {
					return;
				}
				if(branch_type == 'delete') {
					var uid = branch.uid;
					if (parent != null && angular.isDefined(parent)) {
						var newCildren = [],
							k = 0;
						for (var i in parent.children) {
							var children = parent.children[i];
							if (children.uid == uid) continue;
							newCildren[k] = children;
							k++;
						}
						parent.children = newCildren;
					} else { //parent is undefined
						var newData = [],
							k = 0;
						var my_data = $scope.my_data;
						for (var i in my_data) {
							var my = my_data[i];
							if (my.uid == uid) continue;
							newData[k] = my;
							k++;
						}
					}
				}
				if(branch_type == 'deletePosition') {
					delete $scope.sectorHash[$scope.param.sector.sector_id]; 
				}
				
			});
		}
	}
	/////////////员工编辑//////////////////////////////////////////////////////////////////////////////
	var edit_employee_id = 0;
	$scope.addEditEmployee = function(employee) {
		if (typeof(employee) != 'undefined') {
			$scope.employee = employee;
			edit_employee_id = angular.copy(employee.employee_id);
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑员工");
		$scope.action = '添加/编辑员工';
		aside = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',backdrop: "static",
			container: '#MainController',templateUrl: 'AddEditEmployee.html' + __VERSION
		});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function() {
	
			});
		})
	}
	
	$scope.saveDataEmployee = function() {
		if (this.employee == null || this.employee == '') {
			$alert({title: 'Notice',content: '没有数据保存！',templateUrl: '/modal-warning.html',show: true});
			return;
		}
		if (!angular.isDefined(this.employee.employee_name)) {
			$alert({title: 'Notice',content: '姓名必须填写！',templateUrl: '/modal-warning.html',show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.seaport = angular.copy(this.seaport);
		$httpService.header('method', 'saveEmployee');
		$httpService.post(__WEB + 'app.do?channel=' + $stateParams.channel + "&edit_id=" + $scope.edit_id, $scope, function(result) {
			$scope.loading.percent();
			$httpService.deleteHeader('method');
			if (result.data.success == '0') {
				return;
			}
			$scope.seaport = {};
			$scope.edit_employee_id = 0;
			aside.hide();
			$scope.reload($stateParams);
		});
	}
	//权限///////////////////////////////////////////////////////////////////////
	$scope.edit_permission_id = 0;let asidePermi = {};
	$scope.role_module = {};
	$scope.addEditPermission = function(permission) {
		if (typeof(permission) != 'undefined' && typeof(permission.role_id) != 'undefined') {
			$scope.role_module.role_name = permission.role_name;
			$scope.edit_permission_id = angular.copy(permission.role_id);
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑权限");
		$scope.action = '添加/编辑权限';
		asidePermi = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',backdrop: "static",
			container: '#MainController',templateUrl: 'AddEditPermission.html' + __VERSION
		});
		asidePermi.$promise.then(function() {
			asidePermi.show();
			$(document).ready(function() {
	
			});
		})
	}
	
	$scope.saveDataPermission = function() {
		let formParam = $.serializeFormat('#thisPermissionForm');
		if (this.role_module == null || this.role_module == '') {
			$alert({title: 'Notice',content: '没有数据保存！',templateUrl: '/modal-warning.html',show: true});
			return;
		}
		if (!angular.isDefined(this.role_module.role_name)) {
			$alert({title: 'Notice',content: '权限名必须填写！',templateUrl: '/modal-warning.html',show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.role_module = angular.copy(formParam);
		$scope.param.role_module.role_name =this.role_module.role_name;
		$httpService.header('method', 'savePermission');
		$httpService.post(__WEB + 'app.do?channel=' + $stateParams.channel + "&edit_id=" + $scope.edit_permission_id, $scope, function(result) {
			$scope.loading.percent();
			$httpService.deleteHeader('method');
			if (result.data.success == '0') {
				return;
			}
			$scope.role_module = {};
			$scope.edit_permission_id = 0;
			asidePermi.hide();
			$scope.reload($stateParams);
		});
	}
	$scope.checkboxChange = function(evn, module_father_id) {
		if(evn) $('#role'+module_father_id).prop("checked", true);
	}
	$scope.checkboxCancel = function(evn, module_father_id) {
		console.log(evn, module_father_id);
		if(!evn) $("[role='role"+module_father_id+"']").prop("checked", false);
	}
});
