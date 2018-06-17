load('//tools:antlr4.bzl', 'antlr4_sources')

java_binary(
    name = "driver",
    visibility = ["//visibility:public"],
    main_class = "leek.c.Main",
    deps = [
        "@org_ow2_asm_asm//jar",
        ":compiler",
    ],
    srcs = [
        "Main.java",
    ],
)

java_library(
    name = "compiler",
    visibility = ["//visibility:public"],
    deps = [
        "@org_antlr_antlr4_runtime//jar",
        "@org_ow2_asm_asm//jar",
    ],
    srcs = [
        "analysis/AnalysisException.java",
        "analysis/LocalScope.java",
        "analysis/Variable.java",
        "diagnostics/SourceLocation.java",
        "syntax/ArrayType.java",
        "syntax/BoolType.java",
        "syntax/Definition.java",
        "syntax/Expression.java",
        "syntax/Node.java",
        "syntax/SubroutineDefinition.java",
        "syntax/Type.java",
        "syntax/ValueParameter.java",
        "syntax/VariableExpression.java",
        ":antlr4_sources",
    ],
)

antlr4_sources(
    name = "antlr4_sources",
    package = "leek.c.parse",
    srcs = [
        "parse/Grammar.g4",
    ],
)
