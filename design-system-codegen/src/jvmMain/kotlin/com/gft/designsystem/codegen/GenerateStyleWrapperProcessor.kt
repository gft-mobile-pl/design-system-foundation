package com.gft.designsystem.codegen

import androidx.compose.runtime.Composable
import com.gft.designsystem.GenerateStyleWrapper
import com.gft.designsystem.Style
import com.gft.designsystem.Token
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

private const val STYLE_PROVIDER_PROPERTY_NAME = "styleProvider"
private const val WRAPPER_NAME_SUFFIX = "Wrapper"
private const val TOKEN_REFERENCE_METHOD_NAME = "TokenReference"

internal class GenerateStyleWrapperProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver
            .getSymbolsWithAnnotation(GenerateStyleWrapper::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { symbol ->
                if (symbol.classKind != ClassKind.INTERFACE) {
                    logger.error("@GenerateStyleWrapper annotation can only be applied to interfaces", symbol)
                    return@forEach
                }

                if (!symbol.getAllSuperTypes().any { ksType -> ksType.toTypeName() == Style::class.asTypeName() }) {
                    logger.error("@GenerateStyleWrapper can only be applied to interfaces extending ${Style::class.qualifiedName}", symbol)
                }

                generateWrapper(symbol)
            }

        return emptyList()
    }

    private fun generateWrapper(symbol: KSClassDeclaration) {
        val packageName = symbol.packageName.asString()
        val styleName = symbol.simpleName.asString()
        val styleClassName = symbol.toClassName()
        val wrapperName = "$styleName$WRAPPER_NAME_SUFFIX"

        val styleProviderSpec = LambdaTypeName.get(
            returnType = styleClassName,
            parameters = emptyList()
        ).copy(
            annotations = listOf(AnnotationSpec.builder(Composable::class).build())
        )

        val wrapperClassSpec = TypeSpec
            .classBuilder(wrapperName)
            .addModifiers(KModifier.OPEN)
            .addSuperinterface(styleClassName)
            .primaryConstructor(
                FunSpec
                    .constructorBuilder()
                    .addParameter(
                        STYLE_PROVIDER_PROPERTY_NAME,
                        styleProviderSpec
                    )
                    .build()
            )
            .addProperty(
                PropertySpec
                    .builder(STYLE_PROVIDER_PROPERTY_NAME, styleProviderSpec, KModifier.PRIVATE)
                    .initializer(STYLE_PROVIDER_PROPERTY_NAME)
                    .build()
            )
            .apply {
                symbol
                    .getAllProperties()
                    .map { property ->
                        val propertyName = property.simpleName.asString()
                        val propertyType = property.type.toTypeName()
                        PropertySpec
                            .builder(propertyName, propertyType)
                            .addModifiers(KModifier.OVERRIDE)
                            .initializer("$TOKEN_REFERENCE_METHOD_NAME { $STYLE_PROVIDER_PROPERTY_NAME().$propertyName }")
                            .build()
                    }
                    .forEach { property ->
                        addProperty(property)
                    }
            }
            .build()

        FileSpec
            .builder(packageName, wrapperName)
            .addImport(Token::class.java.`package`!!.name, TOKEN_REFERENCE_METHOD_NAME)
            .addType(wrapperClassSpec)
            .build()
            .writeTo(codeGenerator, Dependencies(false, symbol.containingFile!!))
    }
}
