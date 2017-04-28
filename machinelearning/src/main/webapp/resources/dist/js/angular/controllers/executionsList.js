/**
 * Kontroler pozwalający na zarządzanie listą wykonań algorytmów.
 * Created by mateusz on 04.03.16.
 */

    //var mlApp = angular.module('myApp');
angular.module('myApp').controller('executionsListCtrl',function ($scope,$location,$routeParams, Algorithm_execution, Method_configuration, Problem_configuration) {
    $scope.executions = [];
    $scope.method_configurations = [];
    $scope.problem_configurations = [];

    /**
     * Funkcja ładuje wykonania z bazy danych.
     */
    loadExecutions = function() {
        Algorithm_execution.getExecutions().success(function (data, status) {
            $scope.executions = data;
            console.log(data);
        });
    };
    /**
     * Funkcja ładuje konfiguracje problemów.
     */
    loadProblem_configurations = function() {
        Problem_configuration.getProblem_configurations().success(function(data,status){
            console.log("aa");
            $scope.problem_configurations = data;
            console.log(data);
        })
    }
    /**
     * Funkcja ładuje konfiguracje metod.
     */
    loadMethod_configurations = function() {
        Method_configuration.getMethod_configurations().success(function(data,status){
            $scope.method_configurations = data;
            console.log(data);
        })
    }

    /**
     * Funkcja ustawia wartości początkowe.
     */
    init = function() {
        loadExecutions();
        loadMethod_configurations();
        loadProblem_configurations();
    }
    init();

    $scope.go = function ( path ) {
        $location.path( path );
    };

    /**
     * Funkcja pozwala na porównanie metod.
     */
    $scope.compare = function () {

        var argument = "";
        for ( var i = 0; i<  $scope.executions.length; i++) {
            var execution = $scope.executions[i];

            if(execution.selected) {
                if(argument) {
                    argument = argument + "a" + execution.id;
                }
                else {
                    argument += execution.id;
                }
            }
        }

        $location.path("/comparestatistics/"+argument);
    }

});
