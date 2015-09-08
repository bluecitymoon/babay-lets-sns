'use strict';

angular.module('dtsnsApp')
    .factory('PostImage', function ($resource, DateUtils) {
        return $resource('api/postImages/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
