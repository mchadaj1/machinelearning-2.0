// AdminLTE Gruntfile
module.exports = function (grunt) {

  'use strict';

  grunt.initConfig({
    watch: {
      // If any .less file changes in directory "src/main/webapp/resources/build/less/" run the "less"-task.
      files: ["src/main/webapp/resources/build/less/*.less", "src/main/webapp/resources/build/less/skins/*.less", "src/main/webapp/resources/dist/js/app.js", "src/main/webapp/resources/dist/js/angular/app.js"],
      tasks: ["less", "uglify"]
    },
    // "less"-task configuration
    // This task will compile all less files upon saving to create both AdminLTE.css and AdminLTE.min.css
    less: {
      // Development not compressed
      development: {
        options: {
          // Whether to compress or not
          compress: false
        },
        files: {
          // compilation.css  :  source.less
          "src/main/webapp/resources/dist/css/AdminLTE.css": "src/main/webapp/resources/build/less/AdminLTE.less",
          //Non minified skin files
          "src/main/webapp/resources/dist/css/skins/skin-blue.css": "src/main/webapp/resources/build/less/skins/skin-blue.less",
          "src/main/webapp/resources/dist/css/skins/skin-black.css": "src/main/webapp/resources/build/less/skins/skin-black.less",
          "src/main/webapp/resources/dist/css/skins/skin-yellow.css": "src/main/webapp/resources/build/less/skins/skin-yellow.less",
          "src/main/webapp/resources/dist/css/skins/skin-green.css": "src/main/webapp/resources/build/less/skins/skin-green.less",
          "src/main/webapp/resources/dist/css/skins/skin-red.css": "src/main/webapp/resources/build/less/skins/skin-red.less",
          "src/main/webapp/resources/dist/css/skins/skin-purple.css": "src/main/webapp/resources/build/less/skins/skin-purple.less",
          "src/main/webapp/resources/dist/css/skins/skin-blue-light.css": "src/main/webapp/resources/build/less/skins/skin-blue-light.less",
          "src/main/webapp/resources/dist/css/skins/skin-black-light.css": "src/main/webapp/resources/build/less/skins/skin-black-light.less",
          "src/main/webapp/resources/dist/css/skins/skin-yellow-light.css": "src/main/webapp/resources/build/less/skins/skin-yellow-light.less",
          "src/main/webapp/resources/dist/css/skins/skin-green-light.css": "src/main/webapp/resources/build/less/skins/skin-green-light.less",
          "src/main/webapp/resources/dist/css/skins/skin-red-light.css": "src/main/webapp/resources/build/less/skins/skin-red-light.less",
          "src/main/webapp/resources/dist/css/skins/skin-purple-light.css": "src/main/webapp/resources/build/less/skins/skin-purple-light.less",
          "src/main/webapp/resources/dist/css/skins/_all-skins.css": "src/main/webapp/resources/build/less/skins/_all-skins.less"
        }
      },
      // Production compresses version
      production: {
        options: {
          // Whether to compress or not
          compress: true
        },
        files: {
          // compilation.css  :  source.less
          "src/main/webapp/resources/dist/css/AdminLTE.min.css": "src/main/webapp/resources/build/less/AdminLTE.less",
          // Skins minified
          "src/main/webapp/resources/dist/css/skins/skin-blue.min.css": "src/main/webapp/resources/build/less/skins/skin-blue.less",
          "src/main/webapp/resources/dist/css/skins/skin-black.min.css": "src/main/webapp/resources/build/less/skins/skin-black.less",
          "src/main/webapp/resources/dist/css/skins/skin-yellow.min.css": "src/main/webapp/resources/build/less/skins/skin-yellow.less",
          "src/main/webapp/resources/dist/css/skins/skin-green.min.css": "src/main/webapp/resources/build/less/skins/skin-green.less",
          "src/main/webapp/resources/dist/css/skins/skin-red.min.css": "src/main/webapp/resources/build/less/skins/skin-red.less",
          "src/main/webapp/resources/dist/css/skins/skin-purple.min.css": "src/main/webapp/resources/build/less/skins/skin-purple.less",
          "src/main/webapp/resources/dist/css/skins/skin-blue-light.min.css": "src/main/webapp/resources/build/less/skins/skin-blue-light.less",
          "src/main/webapp/resources/dist/css/skins/skin-black-light.min.css": "src/main/webapp/resources/build/less/skins/skin-black-light.less",
          "src/main/webapp/resources/dist/css/skins/skin-yellow-light.min.css": "src/main/webapp/resources/build/less/skins/skin-yellow-light.less",
          "src/main/webapp/resources/dist/css/skins/skin-green-light.min.css": "src/main/webapp/resources/build/less/skins/skin-green-light.less",
          "src/main/webapp/resources/dist/css/skins/skin-red-light.min.css": "src/main/webapp/resources/build/less/skins/skin-red-light.less",
          "src/main/webapp/resources/dist/css/skins/skin-purple-light.min.css": "src/main/webapp/resources/build/less/skins/skin-purple-light.less",
          "src/main/webapp/resources/dist/css/skins/_all-skins.min.css": "src/main/webapp/resources/build/less/skins/_all-skins.less"
        }
      }
    },
    // Uglify task info. Compress the js files.
    uglify: {
      options: {
        mangle: true,
        preserveComments: 'some'
      },
      my_target: {
        files: {
          'src/main/webapp/resources/dist/js/app.min.js': ['src/main/webapp/resources/dist/js/app.js'],
          'src/main/webapp/resources/dist/js/angular/app.min.js': ['src/main/webapp/resources/dist/js/angular/app.js']
        }
      }
    },
    // Build the documentation files
    includes: {
      build: {
        src: ['*.html'], // Source files
        dest: 'src/main/webapp/resources/documentation/', // Destination directory
        flatten: true,
        cwd: 'src/main/webapp/resources/documentation/build',
        options: {
          silent: true,
          includePath: 'src/main/webapp/resources/documentation/src/main/webapp/resources/build/include'
        }
      }
    },

    // Optimize images
    image: {
      dynamic: {
        files: [{
          expand: true,
          cwd: 'src/main/webapp/resources/build/img/',
          src: ['**/*.{png,jpg,gif,svg,jpeg}'],
          dest: 'src/main/webapp/resources/dist/img/'
        }]
      }
    },

    // Validate JS code
    jshint: {
      options: {
        jshintrc: '.jshintrc'
      },
      core: {
        src: 'src/main/webapp/resources/dist/js/app.js'
      },
      demo: {
        src: 'src/main/webapp/resources/dist/js/demo.js'
      },
      pages: {
        src: 'src/main/webapp/resources/dist/js/pages/*.js'
      }
    },

    // Validate CSS files
    csslint: {
      options: {
        csslintrc: 'src/main/webapp/resources/build/less/.csslintrc'
      },
      dist: [
        'src/main/webapp/resources/dist/css/AdminLTE.css',
      ]
    },

    // Validate Bootstrap HTML
    bootlint: {
      options: {
        relaxerror: ['W005']
      },
      files: ['pages/**/*.html', '*.html']
    },

    // Delete images in build directory
    // After compressing the images in the src/main/webapp/resources/build/img dir, there is no need
    // for them
    clean: {
      build: ["src/main/webapp/resources/build/img/*"]
    }
  });

  // Load all grunt tasks

  // LESS Compiler
  grunt.loadNpmTasks('grunt-contrib-less');
  // Watch File Changes
  grunt.loadNpmTasks('grunt-contrib-watch');
  // Compress JS Files
  grunt.loadNpmTasks('grunt-contrib-uglify');
  // Include Files Within HTML
  grunt.loadNpmTasks('grunt-includes');
  // Optimize images
  grunt.loadNpmTasks('grunt-image');
  // Validate JS code
  grunt.loadNpmTasks('grunt-contrib-jshint');
  // Delete not needed files
  grunt.loadNpmTasks('grunt-contrib-clean');
  // Lint CSS
  grunt.loadNpmTasks('grunt-contrib-csslint');
  // Lint Bootstrap
  grunt.loadNpmTasks('grunt-bootlint');

  // Linting task
  grunt.registerTask('lint', ['jshint', 'csslint', 'bootlint']);

  // The default task (running "grunt" in console) is "watch"
  grunt.registerTask('default', ['watch']);
};
