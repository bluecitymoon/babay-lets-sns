'use strict';

angular.module('dtsnsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('openFireConfiguration', {
                parent: 'entity',
                url: '/openFireConfigurations',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'OpenFireConfigurations'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/openFireConfiguration/openFireConfigurations.html',
                        controller: 'OpenFireConfigurationController'
                    }
                },
                resolve: {
                }
            })
            .state('openFireConfiguration.detail', {
                parent: 'entity',
                url: '/openFireConfiguration/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'OpenFireConfiguration'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/openFireConfiguration/openFireConfiguration-detail.html',
                        controller: 'OpenFireConfigurationDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'OpenFireConfiguration', function($stateParams, OpenFireConfiguration) {
                        return OpenFireConfiguration.get({id : $stateParams.id});
                    }]
                }
            })
            .state('openFireConfiguration.new', {
                parent: 'openFireConfiguration',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/openFireConfiguration/openFireConfiguration-dialog.html',
                        controller: 'OpenFireConfigurationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {authenticationToken: null, serverAddress: null, restApiPort: null, identifier: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('openFireConfiguration', null, { reload: true });
                    }, function() {
                        $state.go('openFireConfiguration');
                    })
                }]
            })
            .state('openFireConfiguration.edit', {
                parent: 'openFireConfiguration',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/openFireConfiguration/openFireConfiguration-dialog.html',
                        controller: 'OpenFireConfigurationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['OpenFireConfiguration', function(OpenFireConfiguration) {
                                return OpenFireConfiguration.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('openFireConfiguration', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
