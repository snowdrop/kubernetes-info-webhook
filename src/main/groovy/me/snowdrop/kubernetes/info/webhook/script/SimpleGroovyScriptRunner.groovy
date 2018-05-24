package me.snowdrop.kubernetes.info.webhook.script


import org.springframework.util.FileCopyUtils

import java.nio.file.Files
import java.nio.file.Paths

class SimpleGroovyScriptRunner implements ScriptRunner {

    private static final String SCRIPT_NAME = "mutator.groovy"

    private final GroovyScriptEngine engine

    SimpleGroovyScriptRunner(InputStream scriptInputStream) {
        final scriptsTempPath = Files.createTempDirectory("mutating-groovy-scripts")

        engine = new GroovyScriptEngine(scriptsTempPath.toUri().toURL())

        FileCopyUtils.copy(
            scriptInputStream,
            Paths.get(scriptsTempPath.toAbsolutePath().toString(), SCRIPT_NAME).newOutputStream()
        )
    }

    @Override
    Map<String, Object> run(Input input) {
        final binding = new Binding()
        binding.setVariable("input", input)
        return (Map<String, Object>) engine.run(SCRIPT_NAME, binding)
    }
}
