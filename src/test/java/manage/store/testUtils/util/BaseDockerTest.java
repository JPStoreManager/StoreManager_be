package manage.store.testUtils.util;

import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

public class BaseDockerTest {

    /**
     * Test용 Docker Compose Container 생성 및 반환
     */
    protected static DockerComposeContainer getDockerComposeContainer(){
        File file = new File("../docker-compose.test.yml");
        return new DockerComposeContainer(file);
    }

}
