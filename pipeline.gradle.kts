
class PipelinePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("runCodeCheckBaseline") {
            group = "pipeline"

            project.childProjects.forEach {
                dependsOn("${it.value.path}:detektBaseline")
            }
            doLast {
                println("Successfully wrote baseline for projects")
            }
        }

        project.task("runCodeReview") {
            group = "pipeline"

            project.childProjects.forEach {
                dependsOn("${it.value.path}:detekt")
            }
            doLast {
                println("Successfully passed code style check")
            }
        }

        project.task("runTests") {
            group = "pipeline"

            project.childProjects.forEach {
                dependsOn("${it.value.path}:test")
            }
            doLast {
                println("Successfully passed all tests")
            }
        }

        project.task("buildAndAssemble") {
            group = "pipeline"
            project.allprojects.forEach {
                if (it.name == "app") {
                    dependsOn("${it.path}:assemble")
                }
            }
            doLast {
                println("Successfully built and assembled all.")
            }
        }
    }
}

apply<PipelinePlugin>()