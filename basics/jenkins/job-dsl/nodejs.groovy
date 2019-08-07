job('NodeJS example') {
    scm {
        git('git://github.com/MusTangLee/docker-cicd.git') {  node -> // is hudson.plugins.git.GitSCM
            node / gitConfigName('DSL User')
            node / gitConfigEmail('jenkins-dsl@newtech.academy')
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
    wrappers {
        nodejs('nodejs11') // this is the name of the NodeJS installation in 
                         // Manage Jenkins -> Configure Tools -> NodeJS Installations -> Name
    }
    steps {
        shell("cd ./basics && npm install")
    }
}

job('NodeJS Docker example') {
    scm {
        git('git://github.com/MusTangLee/docker-cicd.git') {  node -> // is hudson.plugins.git.GitSCM
            node / gitConfigName('DSL User')
            node / gitConfigEmail('jenkins-dsl@newtech.academy')
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
    wrappers {
        nodejs('nodejs11') 
    }
    steps {
        dockerBuildAndPublish {
            repositoryName('navecohen/cicd') //qa / dev
            tag('${GIT_REVISION,length=9}')
            registryCredentials('dockerhub')
            forcePull(false)
            forceTag(false)
            createFingerprints(false)
            skipDecorate()
            buildContext('./basics/')
        }
    }
}

pipelineJob('boilerplate-pipeline') {

    def repo = 'git://github.com/MusTangLee/docker-cicd.git'

    triggers {
            scm('H/5 * * * *')
    }

    description('pipeline for $repo')
    definition{
        cpsScm {
            scriptPath('./basics/misc/Jenkinsfile')
            scm {
                git{
                    remote {
                        url(repo)
                        branches('master', '**/feature')
                        extensions {} // required as otherwise it may try to tag the repo, which you may not want
                    }
                }
            }
        }
    }
}