'use strict';

angular.module('rmtApp').controller('MsuDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Msu',
        function($scope, $stateParams, $uibModalInstance, entity, Msu) {

        $scope.msu = entity;
        $scope.load = function(id) {
            Msu.get({id : id}, function(result) {
                $scope.msu = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('rmtApp:msuUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.msu.id != null) {
                Msu.update($scope.msu, onSaveSuccess, onSaveError);
            } else {
                Msu.save($scope.msu, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
