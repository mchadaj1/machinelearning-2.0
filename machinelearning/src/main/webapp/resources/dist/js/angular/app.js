var mlApp = angular.module('myApp',['ngRoute','chart.js','ui.codemirror']);

angular.module('myApp.controllers',[]);
angular.module('myApp.factories',[]);
mlApp.config(['$routeProvider', '$locationProvider', function AppConfig($routeProvider, $locationProvider) {
    $routeProvider.

    when('/problem', {
        templateUrl: 'resources/templates/problem/index.html',
        controller: 'problemCtrl'

    }).
    when('/problem/add',{
        templateUrl: 'resources/templates/problem/add.html',
        controller: 'problemCtrl'
    }).when('/problem/edit/:id',{
        templateUrl: 'resources/templates/problem/edit.html',
        controller: 'problemCtrl'
    }).when('/method', {
        templateUrl: 'resources/templates/method/index.html',
        controller: 'methodCtrl'

    }).when('/method/add/:problem_id?',{
        templateUrl: 'resources/templates/method/add.html',
        controller: 'methodCtrl'
    }).when('/method/edit/:id',{
        templateUrl: 'resources/templates/method/edit.html',
        controller: 'methodCtrl'
    }).when('/configuration',{
        templateUrl:'/resources/templates/configuration/index.html',
        controller: 'configurationCtrl'
    }).when('/configuration/add',{
        templateUrl:'/resources/templates/configuration/add.html',
        controller: 'configurationCtrl'
    }).when('/configuration/edit/:id',{
        templateUrl:'/resources/templates/configuration/edit.html',
        controller: 'configurationCtrl'
    }).when('/methodconfiguration',{
        templateUrl:'/resources/templates/methodconfiguration/index.html',
        controller: 'methodconfigurationCtrl'
    }).when('/methodconfiguration/add',{
        templateUrl:'/resources/templates/methodconfiguration/add.html',
        controller: 'methodconfigurationCtrl'
    }).when('/methodconfiguration/edit/:id',{
        templateUrl:'/resources/templates/methodconfiguration/edit.html',
        controller: 'methodconfigurationCtrl'
    }).when('/executions', {
        templateUrl: 'resources/templates/executions/index.html',
        controller: 'executionsListCtrl'

    }).when('/executions/add', {
        templateUrl: 'resources/templates/executions/add.html',
        controller: 'executionsListAddCtrl'

    }).when('/statistics/:id', {
        templateUrl: 'resources/templates/statistics/index.html',
        controller: 'statisticsCtrl'
    }).when('/comparestatistics/:array', {
        templateUrl: 'resources/templates/statistics/compare.html',
        controller: 'comparestatisticsCtrl'
    }).when('/comparepointsstatistics/:array', {
        templateUrl: 'resources/templates/statistics/compare2.html',
        controller: 'comparepointsstatisticsCtrl'
    }).when('/pointstatistics/:id', {
        templateUrl: 'resources/templates/statistics/index2.html',
        controller: 'pointstatisticsCtrl'
    });
}]);





