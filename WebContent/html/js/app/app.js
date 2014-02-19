'use strict';

/* App Module */

angular.module('senseItWeb', ['ngSanitize', 'ui.router', 'senseItServices', ], null).config([
    '$stateProvider',
    '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('home', {
                url: '/home',
                templateUrl: 'partials/home.html',
                controller: 'HomeCtrl'
            })
            .state('projects', {
                url: '/projects',
                templateUrl: 'partials/projects.html',
                controller: 'ProjectListCtrl'
            })
            .state('create', {
                url: '/create',
                templateUrl: 'partials/create.html',
                controller: 'CreateCtrl'
            })
            .state('project-view', {
                url: '/project/{projectId}',
                templateUrl: 'partials/project-view.html',
                controller: 'ProjectViewCtrl'
            })
            .state('project-admin', {
                url: '/project/{projectId}/admin',
                templateUrl: 'partials/project-admin.html',
                controller: 'ProjectAdminCtrl'
            })
            .state('project-edit', {
                url: '/project/{projectId}/edit',
                templateUrl: 'partials/project-edit.html',
                controller: 'ProjectEditCtrl'
            })
        .state('profile', {
            url: '/profile',
            templateUrl: 'partials/profile.html',
            controller: 'ProfileCtrl'
        });

        $urlRouterProvider.otherwise('/home');
    }
]);
