'use strict';

angular.module('dtsnsApp')
    .factory('DTUser', function ($resource, DateUtils) {
        return $resource('api/dTUsers/:id', {}, {
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
