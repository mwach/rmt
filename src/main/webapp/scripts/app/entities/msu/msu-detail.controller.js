'use strict';

angular.module('rmtApp')
    .controller('MsuDetailController', function ($scope, $rootScope, $stateParams, entity, Msu) {
        $scope.msu = entity;
        $scope.load = function (id) {
            Msu.get({id: id}, function(result) {
                $scope.msu = result;
            });
        };
        var unsubscribe = $rootScope.$on('rmtApp:msuUpdate', function(event, result) {
            $scope.msu = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
