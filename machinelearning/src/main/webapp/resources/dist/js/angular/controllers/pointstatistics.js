/**
 * Kontroler dostarczający statystyki punktowe.
 * Created by mateusz on 14.05.16.
 */
angular.module('myApp').controller('pointstatisticsCtrl',function ($scope,$location,$routeParams,Statistic,Problem_configuration,Algorithm_execution) {

   $scope.experiments = [];

    /**
     * Funkcja pobiera statystyki i dostarcza do widoku.
     * @param id
     */
    var getStatistics = function(id) {
        Statistic.getPointStatistics(id).success(function(data,status){
           for(var i = 0; i < data.experiments; i++) {

               var experiment = {
                   serie : ["Eksperyment: "+(i+1)],
                   labels : [],
                   data : [data.earnedPoints[i]]
               }

               if(i == data.experiments-1) {
                   experiment.serie = ["Wykres średnich wartości"];
               }

                $scope.experiments.push(experiment);

                for(var j = 0; j < data.simulations; j ++) {
                    $scope.experiments[i].labels.push(j+1);
                }

            }

        })
    }
    /**
     * Funkcja ustawia wartości początkowe.
     */
    var init = function() {
        getStatistics($routeParams.id);
        $scope.id = $routeParams.id;
    }

    init();
    $scope.go = function ( path ) {
        $location.path( path );
    };

});