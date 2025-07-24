# Anatomy of a design system

Design System Foundation offers great flexibility in terms of design system architecture, 
while also providing support for mechanisms that are common across many design systems — such as design tokens or styles.
As you read through the documentation, you may find that some concepts feel familiar or closely related to Material Design 3. 
This is not a coincidence — both Design System Foundation and Material Design 3 make use of modules and design tokens, 
a concept being developed by the [Design Tokens Community Group](https://www.w3.org/community/design-tokens/), which is expected to become a W3C standard.

A typical design system built on top of Design System Foundation consists of two main parts:

1. **A set of components**
 
   Compose includes only a few low-level components, such as `BasicText`, `Surface`, or `BasicTextField`. 
   Providing higher-level components is the responsibility of the design system.
   > 💡 If you're familiar with Material Design 3, you may have noticed that commonly used components like `Button` or even `Text` are provided by Material Design 3 library, not by Compose itself..

2. **A design tokens repository**

   The visual appearance of components is defined by design tokens.
   Design System Foundation uses local composition to provide tokens grouped in `Modules` (similarly to Material Design 3) or in `Styles`.

## Modules

In Design System Foundation, the design token repository exposes a set of `Modules`, which can contain tokens, `Styles`, or any other arbitrary data.
Three module types are supported out of the box:
- a container for design tokens describing colors,
- a container for design tokens describing typography,
- a container for Styles.

Additional `Modules` can be added as needed, and any unused `Modules` can be hidden.
From Kotlin's perspective, a Module can be an instance of any type. By convention, however, Modules are plain Kotlin objects (POJOs) whose fields contain either tokens or `Styles`.
More information on declaring and managing modules can be found in the [How to implement a design system with Design System Foundation](/documentation/how-to-implement-design-system.md) section.

Together, `Modules`, `Styles`, and tokens form a hierarchical structure, for example:
```
Design System
│
├─ Colors
│    ├─ primary: Color
│    ├─ onPrimary: Color
│    └─ etc.
│
├─ Typography
│    ├─ titleLarge: TextStyle
│    ├─ titleMedium: TextStyle
│    └─ etc.
│
└─ Styles
     │
     ├─ ButtonStyle
     │    ├─ background: Token<Brush>
     │    ├─ textStyle: Token<TextStyle>
     │    └─ etc.
     │
     ├─ ButtonStyle
     │    ├─ background: Token<Brush>
     │    ├─ textStyle: Token<TextStyle>
     │    └─ etc.
     │
     └─ etc.
```

## Design tokens
The concept of design tokens has been defined in various ways depending on the context and audience.
The Design Tokens Community Group offers a concise, technical definition:
```
A (Design) Token is information associated with a human readable name, at minimum a name/value pair.
```
In contrast, [Material Design 3 documentation](https://m3.material.io/foundations/design-tokens/overview) presents a more human-friendly description:
```
Design tokens are small, reusable design decisions that make up a design system's visual style. 
Tokens replace static values with self-explanatory names.
```
In this documentation, a design token is defined as a named value that describes a particular part of an application's visual appearance.

### Implementation
Neither the Design Tokens Community Group nor the Material Design 3 documentation specifies how tokens should be implemented, 
as the implementation depends on the medium, technology, programming language, libraries used etc. 

A sample design token definition in JSON format could look as follows:
```json
{
  "primary-color": "#ffa3b3f5"
}
```

Design System Foundation supports two object-oriented approaches to declaring tokens — either directly, using fields typed as `Color`, `TextStyle`, `Dp`, etc., 
or by using a `Token<T>` container:

```kotlin
interface ColorScheme : Colors {
    val primaryColor: Color
}
```
```kotlin
interface ButtonStyle : Style {
    val textColor: Token<Color>    
}
```
For further details on defining tokens, please refer to the [How to implement a design system with Design System Foundation](/documentation/how-to-implement-design-system.md) section of the documentation.

### Token types

A large portion of design systems introduces a purely conceptual distinction between types of tokens, which makes them easier to manage — especially across different contexts (like light and dark theme).
Although different systems may use different terminology, the overall categorization tends to be quite similar.

> 💡 For a more in-depth understanding of design token categorization, including illustrative examples, refer to the following articles.
> - Figma: [Update 1: Tokens, variables, and styles](https://help.figma.com/hc/en-us/articles/18490793776023-Update-1-Tokens-variables-and-styles#token-organization)
> - Material Design 3: [Classes of tokens](https://m3.material.io/foundations/design-tokens/overview#c2c235dd-05db-4248-be47-48dfd8a21669) 

1. **Value tokens (aka reference tokens / primitive tokens / global tokens)**

   These are the tokens that point to static values such as a color hex code or font size, but can also point to another token. 
   The most important characteristic of values tokens is that they never change depending on the context (e.g. Light/Dark theme).
   > 💡 Value tokens can also be thought of as the full set of predefined style options from which a design system is built. 
   > By constraining the available values, they help ensure visual consistency across the entire UI.
   
   Examples of value tokens:
   ```kotlin
   internal object MobiusReferenceColors { 
      val RoyalBlue0 = Color(0xff000000)
      val RoyalBlue5 = Color(0xff000E33)
      ...
      val RoyalBlue100 = Color(0xffFFFFFF)
   }
   ```
   ```kotlin
   internal object MobiusReferenceDimensions {
      val Dimension0 = 0.dp
      val Dimension1 = 1.dp
      val Dimension2 = 2.dp
      ...
      val Dimension128 = 128.dp
   }
   ```
   > ❗ A common mistake is treating the `primary color` as a value token and defining tokens like `primary0`, `primary100`... `primary900`. 
   > However, the `primary color` may change depending on the context (e.g. when a dark theme is applied).
   > 
   > Even Material Design 3 is not free from this misconception.
   
   > 💡 The term `reference token` can be confusing from a developer's perspective, as it may suggest a reference to an object or memory address.
   > However, in the context of design systems, `reference` simply means that the token holds a reusable reference value, such as a specific color or size.
   > To avoid this ambiguity, the term `value token` is often preferred. 

2. **System tokens (aka semantic tokens)**

   System tokens in are tokens that represent semantic roles within a design system, such as `primary`, `onBackground`, or `titleLarge`. 
   They adapt to different contexts (e.g. light/dark themes) and are usually mapped to value tokens (rarely to hardcoded values), e.g.
   ```kotlin
   open class MobiusLightColors : MobiusColors {
      override val primary: Color = MobiusReferenceColors.RoyalBlue20
      override val inversePrimary: Color = MobiusReferenceColors.RoyalBlue80
      override val secondary: Color = MobiusReferenceColors.MidnightBlue40
      ...
   }
   ```
   ```kotlin
   open class DefaultMobiusTypography : MobiusTypography {
       override val displayLarge: TextStyle = MobiusReferenceTypography.largeTitle1
       ... 
   }   
   ```

3. **Component tokens (aka style tokens)**
   
   These tokens are used to describe the visual aspects of the components (buttons, containers, labels etc.). 
   Whenever possible, component tokens should point to system or value tokens rather than hardcoded values like hex codes. 
   Component tokens are grouped using **styles**.

   ```kotlin
   open class DefaultButtonStyle : ButtonStyle {
      override val shape: Token<Shape> = Token(RoundedCornerShape(50))
      override val contentColor: Token<Color> = Token { Mobius.colors.onPrimary }
      override val background: Token<Brush?> = Token { SolidColor(Mobius.colors.primary) }
      override val disabledContentColor: Token<Color> = Token { Mobius.colors.onSurface.copy(alpha = 0.38f) }
      override val disabledBackground: Token<Brush?> = Token { SolidColor(Mobius.colors.onSurface.copy(alpha = 0.12f)) }
      ...
   }
   ```
 

### Grouping tokens
According to the Design Tokens Community Group, tokens can be arbitrarily nested within groups.
The sequence of group names leading to a given token — including the token’s own name — is referred to as the token’s _path_.
The exact implementation of grouping may vary depending on the medium, technology, programming language, or libraries used.

Design System Foundation provides two built-in mechanisms for grouping tokens: `Styles` and `Modules`, but it can be extended to support any other grouping approach as needed.

## Styles

`Style` is a collection of **component token** declarations that describe a single component (such as a button, container, label, etc.).
Each style can have multiple implementations, enabling the component to appear in various variants.
Design System Foundation provides mechanisms to create overridable, hierarchical, declarative, and optionally dynamic styles that can reference properties from other styles.

Typically, a style declaration extends the `Style` interface and uses `Token<T>` as the value type for the component tokens that make up the style.

```kotlin
interface IconButtonStyle : Style {
   val size: Token<Dp>
   val iconSize: Token<IconSize>
   val backgroundColor: Token<Color>
   val contentColor: Token<Color>
   val border: Token<BorderStroke?>
   val disabledBackgroundColor: Token<Color>
   val disabledContentColor: Token<Color>
   val disabledBorder: Token<BorderStroke?>
   val shape: Token<Shape>
}
```

The implementation of style 

```kotlin
open class DefaultIconButtonStyle : IconButtonStyle {
    override val size: Token<Dp> = TokenReference { Mobius.styles.buttonStyle.height }
    override val iconSize: Token<IconSize> = Token { IconSize.Medium }
    override val backgroundColor: Token<Color> = Token(Color.Unspecified)
    override val contentColor: Token<Color> = Token { Mobius.colors.onSurfaceVariant }
    override val border: Token<BorderStroke?> = Token(null)
    override val disabledBackgroundColor: Token<Color> = Token(Color.Unspecified)
    override val disabledContentColor: Token<Color> = Token { Mobius.colors.onSurface.copy(alpha = 0.38f) }
    override val disabledBorder: Token<BorderStroke?> = Token(null)
    override val shape: Token<Shape> = Token(CircleShape)
}
```

Usually all `Styles` are grouped withing a single `Module` (see [Modules](#Modules)).

Refer to [How to implement a design system with Design System Foundation](/documentation/how-to-implement-design-system.md) for guidelines on declaring, defining and overriding `Styles`.
This section also covers the usage of the `Token<T>` wrapper.