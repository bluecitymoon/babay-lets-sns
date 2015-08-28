'use strict';

angular.module('dtsnsApp').controller('OpenFireConfigurationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'OpenFireConfiguration',
        function($scope, $stateParams, $modalInstance, entity, OpenFireConfiguration) {

        $scope.openFireConfiguration = entity;
        $scope.load = function(id) {
            OpenFireConfiguration.get({id : id}, function(result) {
                $scope.openFireConfiguration = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('dtsnsApp:openFireConfigurationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.openFireConfiguration.id != null) {
                OpenFireConfiguration.update($scope.openFireConfiguration, onSaveFinished);
            } else {
                OpenFireConfiguration.save($scope.openFireConfiguration, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
