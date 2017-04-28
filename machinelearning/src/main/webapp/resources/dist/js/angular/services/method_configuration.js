/**
 * Created by mateusz on 05.03.16.
 */
angular.module('myApp').factory('Method_configuration', function ($http) {
    return {
        getMethod_configurations: function(){
            return $http.get('services/method_configuration/');
        },

        createMethod_configuration: function(configuration){
            return $http.post('services/method_configuration/',configuration);
        },
        deleteConfiguration: function(id){
            return $http.delete('services/method_configuration/'+id);
        },
        editMethodConfiguration: function(id,configuration){
            return $http.put('services/method_configuration/'+id,configuration);
        },
        getMethod: function(id){
            return $http.get('services/method_configuration/'+id);
        },
        getMethod_configuration: function(id){
            return $http.get('services/method_configuration/'+id);
        }

    }

});