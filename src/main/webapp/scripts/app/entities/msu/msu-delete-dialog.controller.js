'use strict';

angular.module('rmtApp')
	.controller('MsuDeleteController', function($scope, $uibModalInstance, entity, Msu) {

        $scope.msu = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Msu.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
