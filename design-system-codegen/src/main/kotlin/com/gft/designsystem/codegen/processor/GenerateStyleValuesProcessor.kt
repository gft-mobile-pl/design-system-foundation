package com.gft.designsystem.codegen.processor

import androidx.compose.runtime.Composable
import com.gft.designsystem.StyleValues
import com.gft.designsystem.Styles
import com.gft.designsystem.codegen.annotation.GenerateStyleValues
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

private const val RESOLVE_STYLE_VALUES_METHOD_NAME = "resolve"
private const val PRODUCE_STYLE_VALUES_METHOD_NAME = "produceStyleValues"
private const val STYLE_VALUES_CLASS_NAME_PATTERN = "%sValues"

internal class GenerateStyleValuesProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(GenerateStyleValues::class.qualifiedName!!)
        symbols.filterIsInstance<KSClassDeclaration>().forEach { symbol ->
            if (symbol.classKind != ClassKind.INTERFACE) {
                logger.error("@GeneratedStyleValues annotation can only be applied to interfaces", symbol)
                return@forEach
            }

            val packageName = symbol.packageName.asString()
            val name = symbol.simpleName.asString()
            val generatedName = STYLE_VALUES_CLASS_NAME_PATTERN.format(name)

            val interfaceSpec = TypeSpec.interfaceBuilder(generatedName)
                .addSuperinterface(StyleValues::class)
                .apply {
                    val properties = symbol
                        .getAllProperties()
                        .map { property ->
                            val propertyName = property.simpleName.asString()
                            val type = property.type.resolve().arguments[0].type!!.resolve().toTypeName()
                            PropertySpec.builder(propertyName, type).build()
                        }
                        .asIterable()
                    addProperties(properties)
                }
                .build()

            val generatedClassName = ClassName(packageName, generatedName)
            val funSpec = FunSpec.builder(RESOLVE_STYLE_VALUES_METHOD_NAME)
                .addAnnotation(Composable::class)
                .receiver(ClassName(packageName, name))
                .returns(generatedClassName)
                .addCode(
                    CodeBlock.builder()
                        .add("return $PRODUCE_STYLE_VALUES_METHOD_NAME { style ->\n")
                        .indent()
                        .add("object : %T {\n", generatedClassName)
                        .indent()
                        .add(
                            CodeBlock.builder()
                                .apply {
                                    symbol
                                        .getAllProperties()
                                        .forEach { property ->
                                            val propertyName = property.simpleName.asString()
                                            addStatement("override val $propertyName = style.$propertyName.$RESOLVE_STYLE_VALUES_METHOD_NAME()")
                                        }
                                }
                                .build()
                        )
                        .unindent()
                        .add("}\n")
                        .unindent()
                        .add("}")
                        .build()
                )
                .build()

            FileSpec.builder(packageName, generatedName)
                .addImport(Styles::class.java.`package`!!.name, PRODUCE_STYLE_VALUES_METHOD_NAME)
                .addType(interfaceSpec)
                .addFunction(funSpec)
                .build()
                .writeTo(codeGenerator, Dependencies(false, symbol.containingFile!!))
        }

        return emptyList()
    }
}
