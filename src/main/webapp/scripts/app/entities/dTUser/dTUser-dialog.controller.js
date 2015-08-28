'use strict';

angular.module('dtsnsApp').controller('DTUserDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'DTUser',
        function($scope, $stateParams, $modalInstance, entity, DTUser) {

        $scope.dTUser = entity;
        $scope.load = function(id) {
            DTUser.get({id : id}, function(result) {
                $scope.dTUser = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('dtsnsApp:dTUserUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.dTUser.id != null) {
                DTUser.update($scope.dTUser, onSaveFinished);
            } else {
                DTUser.save($scope.dTUser, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
