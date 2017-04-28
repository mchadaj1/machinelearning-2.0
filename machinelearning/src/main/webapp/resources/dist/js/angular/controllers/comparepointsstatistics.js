/**
 * Kontroler dostarczający statystyki punktowego porównywania metod.
 * Created by mateusz on 23.05.16.
 */
angular.module('myApp').controller('comparepointsstatisticsCtrl',function ($scope, $q, $location,$routeParams,Statistic,Problem_configuration,Algorithm_execution) {

    $scope.experiments = [];
    $scope.statistics = [];
    $scope.dataforcanvas = [];
    $scope.series = [];
    $scope.labels = [];
    var executions = [];
    var getStatistics = function(array) {

        var minsimulations = -1;
        var arrayLength = array.length;
        var maxMove = arrayLength-1;



    }
    var arrayLength;
    var minsimulations = -1;
    /**
     * Funkcja rekurencyjnie pobiera statystyki i umieszcza w widoku.
     * @param array
     */
    var loadStatistics = function(array) {
        if(array.length == 0) {
            for(var j = 0; j < arrayLength; j ++ ) {
                $scope.dataforcanvas.push(executions[j].earnedPoints[executions[j].experiments-1].splice(0,minsimulations));
            }
            for(var k = 0; k < minsimulations; k++) {
                $scope.labels.push(k+1);
            }

        }
        else {
            Statistic.getPointStatistics(parseInt(array.shift())).success(function (data, status) {
                executions.push(data);
                var simulations = data.earnedPoints[0].length;
                if(minsimulations == -1) {
                    minsimulations = simulations;

                }
                if(simulations < minsimulations) {
                    minsimulations = simulations;
                }
                $scope.series.push([data.name]);

                loadStatistics(array);

            });
        }
    }


    /**
     * Funkcja inicjuje widok.
     */
    var init = function() {
        $scope.source = $routeParams.array;
        var argument = $routeParams.array;
        var array = argument.split('a');
        arrayLength = array.length;
        loadStatistics(array);

    }

    init();
    $scope.go = function ( path ) {
        $location.path( path );
    };
});