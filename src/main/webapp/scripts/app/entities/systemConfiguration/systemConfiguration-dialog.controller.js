'use strict';

angular.module('dtsnsApp').controller('SystemConfigurationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SystemConfiguration',
        function($scope, $stateParams, $modalInstance, entity, SystemConfiguration) {

        $scope.systemConfiguration = entity;
        $scope.load = function(id) {
            SystemConfiguration.get({id : id}, function(result) {
                $scope.systemConfiguration = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('dtsnsApp:systemConfigurationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.systemConfiguration.id != null) {
                SystemConfiguration.update($scope.systemConfiguration, onSaveFinished);
            } else {
                SystemConfiguration.save($scope.systemConfiguration, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
