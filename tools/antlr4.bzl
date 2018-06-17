def _antlr4_sources(ctx):
    antlr4 = ctx.file.antlr4
    srcs = ctx.files.srcs
    outputs = ctx.outputs
    ctx.actions.run_shell(
        inputs = [antlr4] + srcs,
        outputs = [outputs.src_jar],
        mnemonic = "antlr4",
        command = """
            set -o errexit
            java -jar "$1" -package "$3" -o antlr4_sources "${@:4}"
            zip -r "$2" antlr4_sources
        """,
        arguments = [
            antlr4.path,
            outputs.src_jar.path,
            ctx.attr.package,
        ] + [src.path for src in srcs],
    )

antlr4_sources = rule(
    implementation = _antlr4_sources,
    attrs = {
        "srcs": attr.label_list(
            non_empty = True,
            allow_files = True,
        ),
        "package": attr.string(
            mandatory = True,
        ),
        "antlr4": attr.label(
            default = Label("@antlr4//file"),
            allow_single_file = True,
        ),
    },
    outputs = {
        "src_jar": "%{name}.srcjar",
    },
)
