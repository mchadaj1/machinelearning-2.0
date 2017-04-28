/**
 * Created by mateusz on 04.03.16.
 */
angular.module('myApp').factory('Problem', function ($http) {
    return {
        getProblems: function(){
            return $http.get('services/problem/');
        },
        createProblem: function(problem){
            return $http.post('services/problem/',problem);
        },
        deleteProblem: function(id){
            return $http.delete('services/problem/'+id);
        },
        editProblem: function(id,problem){
            return $http.put('services/problem/'+id,problem);
        },
        getProblem: function(id){
            return $http.get('services/problem/'+id);
        }
    }

});