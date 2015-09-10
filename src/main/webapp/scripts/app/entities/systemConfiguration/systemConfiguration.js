'use strict';

angular.module('dtsnsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('systemConfiguration', {
                parent: 'entity',
                url: '/systemConfigurations',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'SystemConfigurations'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/systemConfiguration/systemConfigurations.html',
                        controller: 'SystemConfigurationController'
                    }
                },
                resolve: {
                }
            })
            .state('systemConfiguration.detail', {
                parent: 'entity',
                url: '/systemConfiguration/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'SystemConfiguration'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/systemConfiguration/systemConfiguration-detail.html',
                        controller: 'SystemConfigurationDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'SystemConfiguration', function($stateParams, SystemConfiguration) {
                        return SystemConfiguration.get({id : $stateParams.id});
                    }]
                }
            })
            .state('systemConfiguration.new', {
                parent: 'systemConfiguration',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/systemConfiguration/systemConfiguration-dialog.html',
                        controller: 'SystemConfigurationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {identity: null, currentValue: null, column1: null, value1: null, column2: null, value2: null, column3: null, value3: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('systemConfiguration', null, { reload: true });
                    }, function() {
                        $state.go('systemConfiguration');
                    })
                }]
            })
            .state('systemConfiguration.edit', {
                parent: 'systemConfiguration',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/systemConfiguration/systemConfiguration-dialog.html',
                        controller: 'SystemConfigurationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SystemConfiguration', function(SystemConfiguration) {
                                return SystemConfiguration.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('systemConfiguration', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
