/**
 * Created by mateusz on 05.03.16.
 */
angular.module('myApp').factory('Method_param', function ($http) {
    return {
        getMethod_params: function(id){
            return $http.get('services/method_param/'+id);
        },

        createMethod_params: function(method_param){
            console.log(method_param);
            return $http.post('services/method_param/',method_param);
        },
        deleteParam: function(id){
            return $http.delete('services/method_param/'+id);
        },
        getValue: function (configuration_id,param_id) {
            return $http.get('resources/methods/configurations/'+configuration_id+'/params/'+param_id+"/value");

        },
        setValue: function(method_param,configuration_id){
            console.log(method_param.id);
            return $http.put('resources/methods/configurations/'+configuration_id+'/params/'+method_param.id+"/value",method_param.value)
        }
        //editProblem: function(id,problem){
        //    return $http.put('services/problem/'+id,problem);
        //},
        //getProblem: function(id){
        //    return $http.get('services/problem/'+id);
        //}
    }

});