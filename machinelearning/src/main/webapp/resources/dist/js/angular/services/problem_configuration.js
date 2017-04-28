/**
 * Created by mateusz on 05.03.16.
 */
angular.module('myApp').factory('Problem_configuration', function ($http) {
    return {
        getProblem_configurations: function(){
            return $http.get('services/problem_configuration/');
        },

        createProblem_configuration: function(configuration){
            return $http.post('services/problem_configuration/',configuration);
        },
        deleteConfiguration: function(id){
            return $http.delete('services/problem_configuration/'+id);
        },
        editProblemConfiguration: function(id,configuration){
            return $http.put('services/problem_configuration/'+id,configuration);
        },
        getProblem: function(id){
            return $http.get('services/problem_configuration/'+id);
        },
        getProblem_configuration: function(id){
            return $http.get('services/problem_configuration/'+id);
        }

    }

});