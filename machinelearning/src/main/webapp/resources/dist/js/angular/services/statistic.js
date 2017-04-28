/**
 * Created by mateusz on 04.03.16.
 */
angular.module('myApp').factory('Statistic', function ($http) {
    return {
        getStatistics: function(id){
            return $http.get('services/statistics/'+id);
        },
        getPointStatistics : function(id) {
            return $http.get('services/statistics/points/'+id);
        }
    }

});