app.controller('EmployeeController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
	var _channel = $stateParams.channel, common = '', sectorList = {}, sectorChildrenList = {}, positionList = {}, sectorPositionList = [];
	var param = 'channel='+_channel;$scope.param = {};//定义变量
	$scope.loading.show();
	$httpService.header('method', 'getSector');
	$httpService.post('app.do?'+param, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		let hashSectorList = {};
		let companySectorList = result.data.item.companySectorList;//部门职位
		for (var index = 0; index < companySectorList.length; index++) {
			hashSectorList[companySectorList[index].sector_id] = companySectorList[index];
		}
		$scope.companySectorList = hashSectorList;//哈希值
		sortChannelSector(hashSectorList);
		var roleList = result.data.item.channelRoleList;
		var channelRoleList = [], k = 0;
		if(roleList != '') {
			for(var i in roleList) {
				channelRoleList[k] = roleList[i];k++;
			}
		}
		$scope.channelRoleList = channelRoleList;
		function sortChannelSector(companySectorList) {
			var positionList = {}; sectorI = 0,sectorJ = 0, sectorPosition = {}, sectorPositionChildren = {};
			if(companySectorList != '') {
				for(var sector_id in companySectorList) {
					var listData = companySectorList[sector_id];
					var father_id = listData.sector_father_id;
					if(listData.sector_type == 'sector') {//1级部门//father
						if(angular.isUndefined(sectorList[father_id])) {
							sectorChildrenList[father_id] = [];//father
							sectorList[father_id] = companySectorList[father_id];//father
							sectorList[father_id].label = companySectorList[father_id].sector_name;
                            sectorList[father_id]['children'] = {};
							//
							sectorPosition[father_id] = {};
							sectorPosition[father_id]['father_id'] = sectorI;
							sectorPosition[father_id]['children_id'] = 0;
							sectorPositionList[sectorI] = {};
							sectorPositionList[sectorI].label = companySectorList[father_id].sector_name;
							sectorPositionList[sectorI].data = companySectorList[father_id];
							sectorPositionList[sectorI].children = [];
							sectorI++;
						}
					}
					if(father_id!=sector_id) {//children
						if(listData.sector_type == 'sector') {//2级部门
							var sector_length = sectorChildrenList[father_id].length;
							sectorChildrenList[father_id][sector_length] = sector_id;
							sectorList[father_id]['children'][sector_id] = listData;
							sectorList[father_id]['children'][sector_id].label = listData.sector_name;
							//
							var sectorIfather_id=sectorPosition[father_id].father_id,children_id = sectorPosition[father_id].children_id;
							sectorPositionList[sectorIfather_id].children[children_id] = {};
							sectorPositionList[sectorIfather_id].children[children_id].label = listData.sector_name;
							sectorPositionList[sectorIfather_id].children[children_id].data = listData;
							sectorPositionList[sectorIfather_id].children[children_id].children = [];
							sectorPositionChildren[sector_id] = {};//二级部门职位
							sectorPositionChildren[sector_id]['father_id'] = children_id;
							sectorPositionChildren[sector_id]['children_id'] = 0;
							//2级部门children_id++
							sectorPosition[father_id].children_id++;
						} else {//职位, 职位的father_id是部门ID
							/*if(angular.isUndefined(positionList[father_id])) {
								positionList[father_id] = {};//father
								positionList[father_id]['children'] = {};
							} 
							positionList[father_id]['children'][sector_id] = listData;
							//
							if(angular.isDefined(sectorPosition[father_id])) {//存在第一级部门的职位
								//
								var sectorIfather_id=sectorPosition[father_id].father_id,children_id = sectorPosition[father_id].children_id;
								sectorPositionList[sectorIfather_id].children[children_id] = {};
								sectorPositionList[sectorIfather_id].children[children_id].label = listData.sector_name;
								sectorPositionList[sectorIfather_id].children[children_id].data = listData;
								sectorPositionList[sectorIfather_id].children[children_id].children = [];
								sectorPosition[father_id].children_id++;
							}
							if(angular.isDefined(sectorPositionChildren[father_id])) {//存在第二级部门的职位
								var sector_father_id = companySectorList[father_id].sector_father_id;//第一级的sector_id
								var sectorIfather_id = sectorPosition[sector_father_id].father_id,children_id = sectorPositionChildren[father_id].father_id;
								//
								var positionChildren_id = sectorPositionChildren[father_id].children_id;
								//
								sectorPositionList[sectorIfather_id].children[children_id].children[positionChildren_id] = {};
								sectorPositionList[sectorIfather_id].children[children_id].children[positionChildren_id].label = listData.sector_name;
								sectorPositionList[sectorIfather_id].children[children_id].children[positionChildren_id].data = listData;
								sectorPositionList[sectorIfather_id].children[children_id].children[positionChildren_id].children = [];
								sectorPositionChildren[father_id].children_id++;
							}*/
						}
					}                        
				}
			}
			$scope.sectorList = sectorList;$scope.positionList = positionList;
		}
	});
	//组织架构树
	var my_data = [];my_data[0] = {};my_data[0].data = {'label':'所有部门'};
	my_data[0].label = '所有部门';my_data[0].children = sectorPositionList;
	$scope.my_data = my_data;
//**//Employee////////////////////////////////////////////////////////////////////////////
	var asideEmployee = '';$scope.this_nav_menu_name = '选择部门';
	$scope.addEmployee = function(_this) {
		$scope.param["valid"] = "1";
		if(_this != 0) {//edit
			$scope.newPassword = false;$scope.setPassword = false;
			$scope.param = angular.copy(_this);
			$('#main_images').attr('src', _this.photo);
			var thisSector = $scope.companySectorList[_this.sector_father_id];//部门
			$scope.this_nav_menu_name = thisSector.sector_name;
			$scope.selectCommonNavMenu(thisSector, '');
			var thisPosition = $scope.companySectorList[_this.sector_id];
			$scope.param.position_id = angular.copy(_this.sector_id);//职位ID
			$scope.param._s_id = angular.copy(thisPosition.s_id);//职位ID
		} else {
			$scope.newPassword = true;$scope.setPassword = true;
			$scope.param.photo = '/data/images/userimg/user_b.png';
			$('#main_images').attr('src', '/data/images/userimg/user_b.png');
		}
		$scope.action = '添加/编辑';
		asideEmployee = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',backdrop:"static",container:'#MainController', templateUrl: 'resource/views/Management/EmployeeAddEdit.tpl.html?'+__VERSION});
		asideEmployee.$promise.then(function() {
			asideEmployee.show();
			$(document).ready(function(){
				$scope.setImage();
			});
		})
		
	};
	$scope.setNewPassword = function() {
		$scope.setPassword = !$scope.setPassword;
	}
	$scope.saveData = function(thisForm) {
		var thisParam = this.param;
		if(thisParam == null || thisParam == '' || !angular.isDefined(thisParam.sector_id)) {
			$alert({title: 'Notice', content: '部门必须选择！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(thisParam.position_id)) {
			$alert({title: 'Notice', content: '职位必须选择！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		var isIdCard = isIdCardNo(thisParam.id_card);
		if(!isIdCard) {
			$alert({title: 'Notice', content: '身份证填写不正确！', templateUrl: '/modal-warning.html', show: true});
			return;
		} else {
			var sex = thisParam.id_card.substr(16,1) % 2;if(sex == 0) {thisParam.sex = 0 } else {thisParam.sex = 1}
			thisParam.birthday = getBirthdayByIdCard(thisParam.id_card);
		}
		//if(isIdCard) {}
		//if(!thisForm.$invalid) {
			//return;
		//}
		$scope.loading.show();
		$scope.param = thisParam;
		var password = $scope.param.password;
		$scope.param.password = md5(md5($.trim(password)));
		$httpService.header('method', 'saveEmployee');
		$httpService.post('app.do?'+param, $scope, function(result){
			$scope.loading.percent();
            $httpService.deleteHeader('method');
			if(result.data.success == '0') {
				return;
			}
			$scope.dataList = result.data.item;
			
			asideEmployee.hide();
			
		});
	}
    $scope.employeeList = []; var thisSectorParam = '';
    $scope.callEmployeeServer = function callEmployeeServer(tableState, sectorParam) {
        $scope.param = {};        
        $scope.param.tableState = tableState;
        $scope.loading.start();
		if(!angular.isString(sectorParam)) sectorParam = '';
		if(thisSectorParam != '') sectorParam = thisSectorParam;
        $httpService.header('method', 'EmployeePagination');
        $httpService.post('app.do?'+param+'&'+sectorParam, $scope, function(result){
            $scope.loading.percent();
            $httpService.deleteHeader('method');
			if(result.data.success == '0') {
				return;
			}
            $scope.employeeList = result.data.item.employeeList.data;
            tableState.pagination.numberOfPages = result.data.item.employeeList.numberOfPages;
        });
    };
	///////////////////////
	var tree;
	$scope.my_tree_handler = function(branch) {
		var _ref, sectorChildren = '';thisSectorParam = '';
		$scope.output = branch.label;
		if (angular.isDefined(branch.data) && (_ref = branch.data) != null) {
			var sector_id = branch.data.sector_id;
			var sector_father_id = branch.data.sector_father_id;
			var sector_type = branch.data.sector_type;
			if(sector_type == 'sector' && sector_id == sector_father_id) {
				//加上自己
				var sectorChildrenParam = angular.copy(sectorChildrenList[sector_father_id]);
				var length = sectorChildrenParam.length;
				sectorChildrenParam[length] = ""+sector_id;
			    sectorChildren = angular.toJson(sectorChildrenParam);
			}
			thisSectorParam = 'sector_id='+sector_id+'&sector_father_id='+sector_father_id+'&sector_type='+sector_type+'&sectorChildren='+sectorChildren;
		}
		$scope.callEmployeeServer($scope.param.tableState, thisSectorParam);
	};
	$scope.my_tree = tree = {};
	//
	$scope.showCommonNavMenu = function() {
		$('#commonNavMenu').next().show();
	}
	$scope.thisPosition = [];
	$scope.selectCommonNavMenu = function(_this, tree) {
		var thisPosition = [];
		if(angular.isDefined($scope.positionList[_this.sector_id])) {
			var k = 0;
			for(var i in $scope.positionList[_this.sector_id].children) {
				thisPosition[k] = $scope.positionList[_this.sector_id].children[i];k++;
			}
		}
		$scope.thisPosition = thisPosition;
		$scope.param.sector_id = _this.sector_id;
		$scope.param.sector_father_id = _this.sector_father_id;
		$scope.this_nav_menu_name = _this.sector_name;
		if(tree == 'father') {
			
		} else {
			
		}
		$('#commonNavMenu').next().hide();
	}
	//
	//images

//***********	//SectorPosition/////////////////////////////////////////////////////////////////////////////
	$scope.SectorPosition = function(branch) {
		console.log(branch);
	}
	var asideSectorPosition = '';
	$scope.SectorPositionEditType = '';
	$scope.SectorPositionEdit = function(branch_type) {
		var title = '', branch = tree.get_selected_branch();
		$scope.SectorPositionEditType = 'Add';
		if(branch_type == 'sector') {
			title = '部门';$scope.param.sector_type = 'sector';
			if(branch == null) {
				$alert({title: 'Error', content: '选择节点，再点击添加！', templateUrl: '/modal-warning.html', show: true});
				return;
			}
			if(branch.level >= 3) {
				$alert({title: 'Error', content: '部门只能添加2级！', templateUrl: '/modal-warning.html', show: true});
				return;
			}
			$scope.param.sector_father_id = '';
			if(angular.isDefined(branch.data.sector_id)) $scope.param.sector_father_id = branch.data.sector_id;
		} else if(branch_type == 'position') {
			title = '职位';$scope.param.sector_type = 'position';
			if(branch.level == 1) {
				$alert({title: 'Error', content: '不能添加顶级职位！', templateUrl: '/modal-warning.html', show: true});
				return;
			}
			if(branch.data.sector_type == 'position') {
				$alert({title: 'Error', content: '职位下面不能添加职位！', templateUrl: '/modal-warning.html', show: true});
				return;
			}
			$scope.param.sector_father_id = branch.data.sector_id;
		} else if(branch_type == 'edit') {
			$scope.SectorPositionEditType = 'edit';$scope.param.sector_type = 'edit';
			$scope.param.s_id = branch.data.s_id;
			$scope.param.sector_name = branch.label;
		}
		$scope.param["valid"] = "1";
		//if(branch != null) $scope.param = branch;
		$scope.action = '添加/编辑'+title;
		asideSectorPosition = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
			backdrop:"static",container:'#MainController', templateUrl: __RESOURCE + 'views/Setting/SectorPositionAddEdit.tpl.html?'+__VERSION});
		asideSectorPosition.$promise.then(function() {
			asideSectorPosition.show();
			$(document).ready(function(){
				
			});
		})
	}
	$scope.saveSectorPositionData = function() {
		asideSectorPosition.hide();
		var message = '您确定要添加/修改吗？';
		$scope.confirm({'content':message,'callback': close});
		var branch = tree.get_selected_branch();
		function close() {
			console.log(branch);
			var sector_name = angular.copy($scope.param.sector_name);
			$httpService.header('method', 'saveSectorPosition');
			$httpService.post('app.do?'+param, $scope, function(result){
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if(result.data.success == '0') {
					return;
				}
				if($scope.SectorPositionEditType == 'edit') {
					branch.label = sector_name;
					branch.data = $scope.param;
					branch.data.sector_name = sector_name;
					branch.data.label = sector_name;
				} else {
					var data = $scope.param;
					data.s_id = result.data.item.s_id;
					data.sector_id = result.data.item.sector_id;
					return tree.add_branch(branch, {
					  label: sector_name,
					  data: data
					});
				}
			});
		}
	};
	$scope.SectorPositionDelete = function() {
		var branch = tree.get_selected_branch();
		var parent = tree.get_parent_branch(branch);
		var children = tree.get_children(branch);
		//children 有不能删
		if(children.length>0) {
			$alert({title: 'Error', content: '有子目录，删除失败！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		var message = '您确定要删除吗？';
		$scope.confirm({'content':message,'callback': deleteData});
		function deleteData() {
			$scope.param.s_id = branch.data.s_id;
			$httpService.header('method', 'deleteSectorPosition');
			$httpService.post('app.do?'+param, $scope, function(result){
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if(result.data.success == '0') {
					return;
				}
				var uid = branch.uid;
				if(parent != null && angular.isDefined(parent)) {
					var newCildren = [], k = 0;
					for(var i in parent.children) {
						var children = parent.children[i];
						if(children.uid == uid) continue;
						newCildren[k] = children; k++;
					}
					parent.children = newCildren;
				} else {//parent is undefined
					var newData = [], k = 0;
					var my_data = $scope.my_data;
					for(var i in my_data) {
						var my = my_data[i];
						if(my.uid == uid) continue;
						newData[k] = my; k++;
					}
				}
			});
		}
	}	
});