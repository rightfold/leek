load('//tools:antlr4.bzl', 'antlr4_sources')

java_binary(
    name = "leekc",
    main_class = "leek.c.Main",
    deps = [
        "@org_ow2_asm_asm//jar",
        ":libleekc",
    ],
    srcs = [
        "src/main/java/leek/c/Main.java",
    ],
)

java_library(
    name = "libleekc",
    deps = [
        "@org_antlr_antlr4_runtime//jar",
        "@org_ow2_asm_asm//jar",
    ],
    srcs = [
        "src/main/java/leek/c/analysis/AnalysisException.java",
        "src/main/java/leek/c/analysis/LocalScope.java",
        "src/main/java/leek/c/analysis/Variable.java",
        "src/main/java/leek/c/diagnostics/SourceLocation.java",
        "src/main/java/leek/c/syntax/ArrayType.java",
        "src/main/java/leek/c/syntax/BoolType.java",
        "src/main/java/leek/c/syntax/Definition.java",
        "src/main/java/leek/c/syntax/Expression.java",
        "src/main/java/leek/c/syntax/Node.java",
        "src/main/java/leek/c/syntax/SubroutineDefinition.java",
        "src/main/java/leek/c/syntax/Type.java",
        "src/main/java/leek/c/syntax/ValueParameter.java",
        "src/main/java/leek/c/syntax/VariableExpression.java",
        ":libleekcparse",
    ],
)

antlr4_sources(
    name = "libleekcparse",
    package = "leek.c.parse",
    srcs = [
        "src/main/antlr4/leek/c/parse/Grammar.g4",
    ],
)
