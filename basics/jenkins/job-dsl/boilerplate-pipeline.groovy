pipelineJob('boilerplate-pipeline') {
    cps {
        script(readFileFromWorkspace('./basics/misc/Jenkinsfile'))
        sandbox()
    }

    cpsScm {
        scm {
            git{
                remote {
                    url('git://github.com/MusTangLee/docker-cicd.git')
                }
            }
        }
    }
}