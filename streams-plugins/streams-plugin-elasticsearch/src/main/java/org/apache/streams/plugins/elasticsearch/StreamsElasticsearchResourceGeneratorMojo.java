package org.apache.streams.plugins.elasticsearch;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

@Mojo(  name = "elasticsearch",
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES
)
@Execute(   goal = "elasticsearch",
            phase = LifecyclePhase.GENERATE_RESOURCES
)
public class StreamsElasticsearchResourceGeneratorMojo extends AbstractMojo {

    private final static Logger LOGGER = LoggerFactory.getLogger(StreamsElasticsearchResourceGeneratorMojo.class);

    private volatile MojoFailureException mojoFailureException;

    @Component
    private MavenProject project;

//    @Component
//    private Settings settings;
//
//    @Parameter( defaultValue = "${localRepository}", readonly = true, required = true )
//    protected ArtifactRepository localRepository;
//
//    @Parameter( defaultValue = "${plugin}", readonly = true ) // Maven 3 only
//    private PluginDescriptor plugin;
//
    @Parameter( defaultValue = "${project.basedir}", readonly = true )
    private File basedir;

    @Parameter( defaultValue = "src/main/jsonschema", readonly = true ) // Maven 3 only
    public String sourceDirectory;

    @Parameter( readonly = true ) // Maven 3 only
    public List<String> sourcePaths;

    @Parameter(defaultValue = "./target/generated-resources/streams-plugin-elasticsearch", readonly = true)
    public String targetDirectory;

    public void execute() throws MojoExecutionException, MojoFailureException {

        //addProjectDependenciesToClasspath();

        StreamsElasticsearchGenerationConfig config = new StreamsElasticsearchGenerationConfig();

        if( sourcePaths != null && sourcePaths.size() > 0)
            config.setSourcePaths(sourcePaths);
        else
            config.setSourceDirectory(sourceDirectory);
        config.setTargetDirectory(targetDirectory);

        StreamsElasticsearchResourceGenerator streamsElasticsearchResourceGenerator = new StreamsElasticsearchResourceGenerator(config);

        Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread th, Throwable ex) {
                LOGGER.error("Exception", ex);
                mojoFailureException = new MojoFailureException("Exception", ex);
            }
        };
        Thread.setDefaultUncaughtExceptionHandler(h);
        Thread thread = new Thread(streamsElasticsearchResourceGenerator);
        thread.setUncaughtExceptionHandler(h);
        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            mojoFailureException = new MojoFailureException("Exception", e);
        }

        if( mojoFailureException != null )
            throw mojoFailureException;

        return;
    }

}