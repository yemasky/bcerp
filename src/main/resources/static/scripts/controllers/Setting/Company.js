app.controller('CompanyController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
	$scope.param = {}; $scope.company = {}; $scope.param.company = {}, $scope.__IMGWEB = __IMGWEB; 
	$scope.company_edit_id = "";
	$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
	$ocLazyLoad.load([__RESOURCE+"vendor/libs/moment.min.js?"+__VERSION,
					  __RESOURCE+"vendor/libs/daterangepicker.js?"+__VERSION,
					  __RESOURCE+"vendor/modules/angular-ui-select/select.min.js?"+__VERSION,
					  __RESOURCE+"vendor/modules/angular-ui-select/select.min.css?"+__VERSION,
					  __RESOURCE+"editor/kindeditor/kindeditor-all.js?"+__VERSION,
					  //__RESOURCE+"editor/kindeditor/kindeditor-all-min.js?"+__VERSION,
					  __RESOURCE+"editor/kindeditor/themes/default/default.css",
					  __RESOURCE+"vendor/libs/md5.min.js",__RESOURCE + "vendor/libs/utils.js"]);
	/////////
	$scope.people = [
	{ name: 'Adam',      email: 'adam@email.com',      age: 12, country: 'United States' },
	{ name: 'Amalie',    email: 'amalie@email.com',    age: 12, country: 'Argentina' },
	{ name: 'Estefanía', email: 'estefania@email.com', age: 21, country: 'Argentina' },
	{ name: 'Adrian',    email: 'adrian@email.com',    age: 21, country: 'Ecuador' },
	{ name: 'Wladimir',  email: 'wladimir@email.com',  age: 30, country: 'Ecuador' },
	{ name: 'Samantha',  email: 'samantha@email.com',  age: 30, country: 'United States' },
	{ name: 'Nicole',    email: 'nicole@email.com',    age: 43, country: 'Colombia' },
	{ name: 'Natasha',   email: 'natasha@email.com',   age: 54, country: 'Ecuador' },
	{ name: 'Michael',   email: 'michael@email.com',   age: 15, country: 'Colombia' },
	{ name: 'Nicolás',   email: 'nicolas@email.com',    age: 43, country: 'Colombia' }
	];
	
	$scope.availableColors = ['Red','Green','Blue','Yellow','Magenta','Maroon','Umbra','Turquoise'];
	
	$scope.multipleDemo = {};
	$scope.multipleDemo.colors = ['Blue','Red'];
	$scope.multipleDemo.selectedPeople = [$scope.people[5], $scope.people[4]];
	$scope.multipleDemo.selectedPeopleWithGroupBy = [$scope.people[8], $scope.people[6]];
	//////////
	let asideCompany;
	$scope.addEdit = function(company) {
		if(typeof(company) != 'undefined') {
			$scope.company_edit_id = angular.copy(company.company_id);
			$scope.company = company;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		asideCompany = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
		backdrop:"static",container:'#MainController', templateUrl: __RESOURCE + 'views/Setting/CompanyAddEditAside.html?'+__VERSION});
		asideCompany.$promise.then(function() {
			asideCompany.show();
			$(document).ready(function(){
				$scope.setImage();
			});
		})
	}
	//<!--图片上传及操作-->
	//images
	$scope.setImage = function() {
		//$scope.__WEB+'app.do?method=uploadCompanyLogo&action=Upload&='+$rootScope.__ImagesUploadUrl;
		var uploadJsonUrl = $scope.__WEB+'index/uploadCompanyLogo?action=Upload&='+$rootScope.__ImagesUploadUrl;
		var fileManagerJsonUrl = $scope.__WEB+'index/fileManager&action=Upload&='+$rootScope.__ImagesManagerUrl;
		window.K = KindEditor;
		var editor = K.editor({
			uploadJson : uploadJsonUrl,fileManagerJson : fileManagerJsonUrl,allowFileManager : true,formatUploadUrl: false
		});
		K('.set_image_src').click(function() {
			editor.loadPlugin('image', function() {
				editor.plugin.imageDialog({
					//clickFn : function(url, title, width, height, border, align) {
					clickFn : function(url) {
						editor.hideDialog();
						$scope.company.company_logo = url;
						$scope.$apply();//刷新数据
					}
				});
			});
		});
	} 
	$scope.saveData = function() {
		console.log(this.company, $scope.company);
		if(this.company == null || this.company == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.company.company_name)) {
			$alert({title: 'Notice', content: '企业名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.company = angular.copy(this.company);
		$httpService.header('method', 'saveCompany');
		$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel+"&company_edit_id="+$scope.company_edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == '0') { 
				return;
			} 
			$scope.dataList = result.data.item;
			$scope.company = {};
			asideCompany.hide();
			let path = '/app/'+$rootScope._self_module.module_channel+'/'+$stateParams.view+'/'
							+$stateParams.id+'/'+$stateParams.channel;
			console.log(path);
			$location.path();
		});
	}
});