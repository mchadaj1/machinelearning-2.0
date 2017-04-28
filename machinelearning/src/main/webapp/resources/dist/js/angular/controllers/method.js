/**
 * Kontroler metod
 * Created by mateusz on 04.03.16.
 */
angular.module('myApp').controller('methodCtrl',function ($scope,$location,$routeParams, Method,Method_param, Problem) {
    $scope.methods = [];
    $scope.problems = [];
    $scope.method_params = [];
    $scope.method = {
        problem_id: null,
        name: "",
        problem: null
    }

    /**
     * Funkcja pobiera metodę o zadanym Id.
     * @param id Id metody.
     */
    $scope.methodSelected = function(id){
        Method.getMethod(id).success(function(data,status){
            $scope.method=data;
        });

        Method_param.getMethod_params(id).success(function(data,status){

            $scope.method_params = data;
            console.log(data);
        })

    };

    /**
     * Funkcja pobiera problem o zadanym Id.
     * @param problem_id Id problemu.
     */
    problemSelected2 = function(problem_id){
        Problem.getProblem(problem_id).success(function(data,status){
            $scope.method.problem = data;
        })
    }
    /**
     * Funkcja pobiera listę metod.
     */
    updateMethods = function() {
        Method.getMethods().success(function (data, status) {
            $scope.methods = data;

        });
    };
    init = function () {
        Problem.getProblems().success(function (data, status) {
            $scope.problems = data;
            console.log($scope.problems);

        });
        $scope.method_params = [];

        if ($routeParams.id) {
            $scope.methodSelected($routeParams.id);
        } else {
            $scope.method.code="Position myPosition = (Position)mapInfo.get(\"position\");\n" +
                "Position newPosition = new Position(myPosition);\n\nList<Position> preys = (List<Position>)mapInfo.get(\"preys\");" +
                "\nList<Position> predators = (List<Position>)mapInfo.get(\"predators\");\nInteger lastMovePoints = (Integer) mapInfo.get(\"lastMovePoints\");   \n\nnewPosition.setX(myPosition.getX()+x); \nnewPosition.setY(myPosition.getY()+y); \n return newPosition;";
            $scope.method.imports="import com.example.piqle.src.algorithms.AbstractMemorySelector;";
        }
        if($routeParams.problem_id){
            $scope.method.problem_id = $routeParams.problem_id;
            problemSelected2($routeParams.problem_id);
        }

        updateMethods();


    };
    init();


    /**
     * Funkcja dodaje metodę do bazy danych.
     */
    $scope.add = function () {
        //$scope.method.problem_id=1;
        console.log($scope.method);
        $scope.method.user_id=1;
        Method.createMethod($scope.method).then(function (data, status) {
            $scope.method=null;

            updateMethods();
            $location.path("/method");

        },function(error,status) {
            alert("Operacja nie powiodła się! " + error.statusText);
        })
    };
    /**
     * Funkcja usuwa metodę o zadanym id.
     * @param id Id metody.
     */
    $scope.delete = function(id){

        Method.deleteMethod(id).then(function(data,status){
            Method.getMethods().success(function (data, status) {
                $scope.methods = data;
            });
        },function(error,status) {
            alert("Operacja nie powiodła się! " + error.statusText);
        })
    };
    /**
     * Funkcja edytuje metodę
     * @param id Id metody.
     * @param method Metoda.
     */
    $scope.edit = function(id,method){

        var id = $routeParams.id;

        Method.editMethod(id,method).then(function() {
                Method.getMethods().success(function (data, status) {
                    $scope.methods = data;
                });
                $location.path("/method");
            },function(error,status) {
                alert("Operacja nie powiodła się! " + error.statusText);
            }
        )
    };
    /**
     * Funkcja pozwala na redirect.
     * @param path Nowa ścieżka.
     */
    $scope.go = function ( path ) {
        $location.path( path );
    };


    /**
     * Funkcja pozwala na dodanie parametru do metody.
     */
    $scope.addParam = function(){

        $scope.newmethodparam.methodsid=$scope.method.id;
        Method_param.createMethod_params($scope.newmethodparam).then(function(data,status){
          if($scope.method_params.length===0)
                $scope.method_params = [data.data];
            else
                $scope.method_params.push(data.data);
            $scope.newmethodparam.name="";
            $scope.newmethodparam.type="";
        },function(error,status) {
            alert("Operacja nie powiodła się! " + error.statusText);
        })
    };
    /**
     * Funkcja pozwala na usunięcie parametru metody.
     * @param id Id parametru.
     * @param param Parametr.
     */
    $scope.deleteParam = function(id,param){
        Method_param.deleteParam(id).success(function(data,status){
            var index = $scope.method_params.indexOf(param);
            $scope.method_params.splice(index, 1);
        },function(error,status) {
            alert("Operacja nie powiodła się! " + error.statusText);
        })

    };
});

