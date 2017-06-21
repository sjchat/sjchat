module.exports = function(grunt){
  grunt.initConfig({
    cucumberjs: {
      options:{
        steps: "step_definitions",
        support: "support"
      },
      /*options: {
        format: 'html',
        output: './public/report.html',
        theme: 'foundation'
      },*/
      features : [],

    }
  });  //grunt.loadNpmTasks('grunt-cucumber');
  grunt.loadNpmTasks('grunt-cucumber');
  grunt.registerTask('default', 'cucumberjs');

};
