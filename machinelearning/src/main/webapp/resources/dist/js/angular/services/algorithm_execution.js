/**
 * Created by mateusz on 04.03.16.
 */
angular.module('myApp').factory('Algorithm_execution', function ($http) {
    return {
        getExecutions: function(){
            return $http.get('services/algorithm_execution/');
        },
        createExecution: function(algorithm_execution){
            return $http.post('services/algorithm_execution/',algorithm_execution);
        },
        getExecution: function(id){
            return $http.get('services/algorithm_execution/'+id);
        }
    }

});