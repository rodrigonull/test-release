import groovy.json.JsonSlurper

def createGHRelease(String repository, String name, String tag, String commit, Boolean draft, Boolean preRelease, String credentialsId) {
    withCredentials([string(credentialsId: credentialsId, variable: 'GITHUB_TOKEN')]) {
        response = sh returnStdout: true, script: """
        set +x
        curl -L \
        -X POST \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${GITHUB_TOKEN}" \
        -H "X-GitHub-Api-Version: 2022-11-28" \
        https://api.github.com/repos/${repository}/releases \
        -d '{"tag_name": "${tag}", "target_commitish": "${commit}", "name": "${name}", "draft": ${draft}, "prerelease": ${preRelease}'
        """.trim()

        return new JsonSlurper().parseText(response as String)
    }
}
