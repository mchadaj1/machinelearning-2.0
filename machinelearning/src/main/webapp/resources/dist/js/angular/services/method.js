/**
 * Created by mateusz on 04.03.16.
 */
angular.module('myApp').factory('Method', function ($http) {
    return {
        getMethods: function(){
            return $http.get('services/method/');
        },
        createMethod: function(method){
            return $http.post('services/method/',method);
        },
        deleteMethod: function(id){
            return $http.delete('services/method/'+id);
        },
        editMethod: function(id,method){
            return $http.put('services/method/'+id,method);
        },
        getMethod: function(id){
            return $http.get('services/method/'+id);
        }
    }

});