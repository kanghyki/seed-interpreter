package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                    "Binary     : Expr left, Token operator, Expr right",
                    "Grouping   : Expr expression",
                    "Literal    : Object value",
                    "Unary      : Token operator, Expr right"
                    ));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        // Write the package declaration and imports
        write(writer, 0, "package seed;");
        write(writer, 0, "");
        write(writer, 0, "import java.util.List;");
        write(writer, 0, "");

        // Write abstract class declaration
        write(writer, 0, "abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        write(writer, 1, "abstract <R> R accept(Visitor<R> visitor);");
        write(writer, 0, "}");
        writer.close();
    }

    private static void write(PrintWriter writer, int tabLevel, String s) {
        for (int i = 0; i < tabLevel; ++i) {
            writer.print("    ");
        }
        writer.println(s);
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        write(writer, 1, "interface Visitor<R> {");
        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            write(writer, 2, "R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }
        write(writer, 1, "}");
        write(writer, 0, "");
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fields) {
        // static class
        write(writer, 1, "static class " + className + " extends " + baseName + " {");

        // fields
        String[] fieldList = fields.split(", ");
        for (String field : fieldList) {
            write(writer, 2, "final " + field + ";");
        }
        writer.println();

        // constructor
        write(writer, 2, className + "(" + fields + ") {");

        for (String field : fieldList) {
            String name = field.split(" ")[1];
            write(writer, 3, "this." + name + " = " + name + ";");
        }
        write(writer, 2, "}");
        write(writer, 0, "");

        // visitor
        write(writer, 2, "@Override");
        write(writer, 2, "<R> R accept(Visitor<R> visitor) {");
        write(writer, 3, "return visitor.visit" + className + baseName + "(this);");
        write(writer, 2, "}");
        write(writer, 1, "}");
        writer.println();
    }
}
