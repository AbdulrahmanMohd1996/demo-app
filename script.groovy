def test_func()
{
    sh "mvn test"
}

def deployApp()
{
    def dockerCommand= "docker run -d -p 3080:3080 abdolee\demo-app:1.0"
    sshagent(['EC2-SERVER-KEY']) 
    {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@34.253.46.29 '${dockerCommand}' "
    }
} 

return this