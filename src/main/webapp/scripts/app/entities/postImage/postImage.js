'use strict';

angular.module('dtsnsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('postImage', {
                parent: 'entity',
                url: '/postImages',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'PostImages'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/postImage/postImages.html',
                        controller: 'PostImageController'
                    }
                },
                resolve: {
                }
            })
            .state('postImage.detail', {
                parent: 'entity',
                url: '/postImage/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'PostImage'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/postImage/postImage-detail.html',
                        controller: 'PostImageDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PostImage', function($stateParams, PostImage) {
                        return PostImage.get({id : $stateParams.id});
                    }]
                }
            })
            .state('postImage.new', {
                parent: 'postImage',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/postImage/postImage-dialog.html',
                        controller: 'PostImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {src: null, src1: null, src2: null, src3: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('postImage', null, { reload: true });
                    }, function() {
                        $state.go('postImage');
                    })
                }]
            })
            .state('postImage.edit', {
                parent: 'postImage',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/postImage/postImage-dialog.html',
                        controller: 'PostImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PostImage', function(PostImage) {
                                return PostImage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('postImage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
