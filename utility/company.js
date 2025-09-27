//**//Employee////////////////////////////////////////////////////////////////////////////
	var asideEmployee = '';$scope.this_nav_menu_name = '选择部门';
	$scope.addEmployee = function(_this) {
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
	$scope.saveData = function() {
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
		$httpService.post('app.do?'+urlParam, $scope, function(result){
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
        $httpService.post('app.do?'+urlParam+'&'+sectorParam, $scope, function(result){
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