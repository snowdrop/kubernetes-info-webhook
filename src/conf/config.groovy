import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ClassNode

withConfig(configuration) {

    //all Groovy classes under the root package are configured to be annotated with CompileStatic
    source(classValidator: { ClassNode cn ->
        cn.packageName.startsWith("me.snowdrop.kubernetes.info.webhook")
    }) {
        ast(CompileStatic)
    }
}