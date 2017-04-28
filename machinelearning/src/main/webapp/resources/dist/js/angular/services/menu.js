/**
 * Created by mateusz on 22.03.16.
 */
angular.module('myApp').factory('Menu', function ($http) {
    return {
        getMenu: function () {
            return
            new Array([
                {
                'link':'/problem',
                'ico':'/ fa-hand-o-right',
                'name':'Problemy',


                },
                {
                    'link':'/method',
                    'ico':'/ fa-hand-o-right',
                    'name':'Metody',

                }
                ])
        }
    }
});