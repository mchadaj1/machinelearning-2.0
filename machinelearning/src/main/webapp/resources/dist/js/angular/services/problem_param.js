/**
 * Created by mateusz on 05.03.16.
 */
angular.module('myApp').factory('Problem_param', function ($http) {
    return {
        getProblem_params: function(id){
            return $http.get('services/problem_param/'+id);
        },

        createProblem_params: function(problem_param){
            console.log(problem_param);
            return $http.post('services/problem_param/',problem_param);
        },
        deleteParam: function(id){
            return $http.delete('services/problem_param/'+id);
        },
        getValue: function (configuration_id,param_id) {
            return $http.get('services/problem_param_value/'+configuration_id+'/'+param_id);

        },
        setValue: function(problem_param,configuration_id){
            console.log(problem_param.id);
            return $http.put('services/problem_param_value/'+configuration_id+'/'+problem_param.id,problem_param.value)
        }


        //editProblem: function(id,problem){
        //    return $http.put('services/problem/'+id,problem);
        //},
        //getProblem: function(id){
        //    return $http.get('services/problem/'+id);
        //}
    }

});