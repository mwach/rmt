 'use strict';

angular.module('rmtApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-rmtApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-rmtApp-params')});
                }
                return response;
            }
        };
    });
