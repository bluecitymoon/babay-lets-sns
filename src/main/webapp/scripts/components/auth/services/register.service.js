'use strict';

angular.module('dtsnsApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


