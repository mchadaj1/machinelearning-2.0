/**
 * Kontroler zarządzający problemami
 * Created by mateusz on 04.03.16.
 */
angular.module('myApp').controller('problemCtrl',function ($scope,$location,$routeParams, Problem, Problem_param) {
    $scope.problems = [];
    $scope.problem = {
        params :[]
    }
    $scope.problem_params = [];


    /**
     * Funkcja pobiera problem o podanym Id.
     * @param id Id problemu.
     */
    problemSelected = function(id){
        Problem.getProblem(id).then(function(data,status){
            console.log(data);
            $scope.problem=data.data;
        },function(error,status) {
            alert("Operacja nie powiodła się! " + error.statusText);
        });

        Problem_param.getProblem_params(id).success(function(data,status){

            $scope.problem_params = data;
            console.log(data);
        },function(error,status) {
            alert("Operacja nie powiodła się! " + error.statusText);
        })

    };
    /**
     * Funkcja pobiera listę problemów.
     */
    var updateProblems = function() {
        Problem.getProblems().success(function (data, status) {
            $scope.problems = data;
        },function(error,status) {
            alert("Operacja nie powiodła się! " + error.statusText);
        });
    }
    var init = function () {
        if ($routeParams.id) {
            problemSelected($routeParams.id);
        }else
        updateProblems();
    };
    init();


    /**
     * Funkcja pozwala na dodanie problemu.
     */
    $scope.add = function () {
        Problem.createProblem($scope.problem).then(function (data, status) {
          $scope.problem=null;


            $location.path("/problem");

        },function(error,status) {
            alert("Operacja nie powiodła się! " + error.statusText);
        })
    };
    /**
     * Funkcja pozwala na usunięcie problemu.
     * @param id Id problemu.
     */
    $scope.delete = function(id){

        Problem.deleteProblem(id).then(function(data,status){
            Problem.getProblems().then(function (data, status) {
                $scope.problems = data.data;
            },function(error,status) {
                alert("Operacja nie powiodła się! " + error.statusText);
            });
        },function(error,status){
            alert("Operacja nie powiodła się! " + error.statusText);

        });
    };

    /**
     * Funkcja pozwala na edycję problemu.
     * @param id Id problemu.
     * @param problem Problem.
     */
    $scope.edit = function(id,problem){

        var id = $routeParams.id;

        Problem.editProblem(id,problem).then(function() {
            Problem.getProblems().success(function (data, status) {
                $scope.problems = data;
            });
                $location.path("/problem");
            },function(error,status) {
                alert("Operacja nie powiodła się! " + error.statusText);
            }
        )
    };
    /**
     * Funkcja pozwala na Redirect strony.
     * @param path Nowa ścieżka.
     */
    $scope.go = function ( path ) {
        $location.path( path );
    };


    /**
     * Funkcja pozwala na dodanie nowego parametru problemu.
     */
    $scope.addParam = function(){

        $scope.newproblemparam.problemsid=$scope.problem.id;

        Problem_param.createProblem_params($scope.newproblemparam).then(function(data,status){
            if($scope.problem_params.length===0)
                $scope.problem_params = [data.data];
            else
                $scope.problem_params.push(data.data);
            $scope.newproblemparam.name="";
            $scope.newproblemparam.type="";
        },function(error,status) {
            alert("Operacja nie powiodła się! " + error.statusText);
        })
    };

    /**
     * Funkcja pozwala na usunięcie parametru problemu.
     * @param id Id problemu.
     * @param param Id parametru.
     */
    $scope.deleteParam = function(id,param){
        console.log(id);
        Problem_param.deleteParam(id).then(function(data,status){

            var index = $scope.problem_params.indexOf(param);
            console.log(param);
            $scope.problem_params.splice(index, 1);

        },function(error,status) {
            alert("Operacja nie powiodła się! " + error.statusText);
        })

    };
});
