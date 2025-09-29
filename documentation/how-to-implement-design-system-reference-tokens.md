# Defining _value tokens_ (reference tokens)

Typically, when UX designers begin working on a design system, they select a limited set of colors, spacing values, 
and fonts that will be used. These decisions are documented in the form of _value tokens_ (also known as _reference tokens_).
At this stage, the appearance of specific components or screens is not yet defined — the purpose of creating a constrained set of potential or acceptable values is to help maintain visual consistency throughout the design process.
> 💡 It’s rarely possible to anticipate everything at this point, so the set of _value tokens_ often undergoes multiple revisions during the component development phase or while designing individual screens of the application.  

Important notes on _value tokens_:

1. Once defined, _value tokens_ should not change their values.

   For example, when the color of a visual element needs to change, it usually involves referencing a different _value token_ rather than modifying the existing one.

2. _Value token_ names should not imply their intended usage.

   A common mistake is naming _value tokens_ that define dominant colors as `primaryColor0`, `primaryColor10` … `primaryColor100`.
   This approach is flawed because the primary color may vary depending on the context — for example,
   some sections of the application might use a completely different primary color. Likewise, the primary color may differ in dark mode.

   Assigning semantic roles to colors is the responsibility of _system tokens_, as described in [Defining the set of system tokens](/documentation/how-to-implement-design-system-system-tokens.md).

   > 💡 The general naming rule for _value tokens_ is that their names should describe the visual appearance of the value.

3. _Value tokens_ should remain consistent across all tools used in the design process.

   The first iterations of a design system are usually created using tools like Figma or Zeplin.
   Although such tools typically support defining both _value tokens_ and _system tokens_,
   UX designers often omit one of these categories or merge their responsibilities in practice.   
   A short guide on mapping token names between tools like Figma and the Compose implementation can be found [here](/documentation/token-names-mapping.md).

4. _Value tokens_ should generally remain internal to the module that defines the design system.

   This can be enforced using the `internal` access modifier.

5. Not all _value tokens_ need to be used in every context.

6. Not all defined _value tokens_ have to be used in the current version of the design system.


## Colors

Most often, the color scheme of a design system is based on:
- one or two dominant colors, typically used in various intensities,
- a grayscale palette,
- individual colors used only in one shade.

Additionally, separate color sets may be defined for different contexts — for example, light and dark themes, or distinct sections of the application.

For example, a company might use the color `0xff1A377A` in its visual identity, referred to by the UX team as `Royal Blue`, 
which is intended to be the dominant color in the application. Additionally, there's a requirement for the section of the app involving sensitive operations (such as account deletion) 
to be presented in shades of red. All links should appear in blue, using a color named `Sky Blue`. The remaining visual elements are styled using various shades of warm gray.

These decisions can be expressed as a set of _value tokens_:

```kotlin
internal object ExampleThemeReferenceColors {
    val RoyalBlue0 = Color(0xff000000)
    val RoyalBlue5 = Color(0xff000E33)
    val RoyalBlue10 = Color(0xff001849)
    val RoyalBlue15 = Color(0xff00215F)
    val RoyalBlue20 = Color(0xff0A2C6F)
    val RoyalBlue25 = Color(0xff1A377A)
    val RoyalBlue30 = Color(0xff284386)
    val RoyalBlue35 = Color(0xff354F93)
    val RoyalBlue40 = Color(0xff425BA0)
    val RoyalBlue50 = Color(0xff5B74BB)
    val RoyalBlue60 = Color(0xff758ED7)
    val RoyalBlue70 = Color(0xff90A9F3)
    val RoyalBlue80 = Color(0xffB3C5FF)
    val RoyalBlue90 = Color(0xffDBE1FF)
    val RoyalBlue95 = Color(0xffEEF0FF)
    val RoyalBlue98 = Color(0xffFAF8FF)
    val RoyalBlue99 = Color(0xffFEFBFF)
    val RoyalBlue100 = Color(0xffFFFFFF)

    val Red0 = Color(0xff000000)
    val Red10 = Color(0xff410002)
    val Red20 = Color(0xff690005)
    val Red30 = Color(0xff93000A)
    val Red40 = Color(0xffBA1A1A)
    val Red50 = Color(0xffDE3730)
    val Red60 = Color(0xffFF5449)
    val Red70 = Color(0xffFF897D)
    val Red80 = Color(0xffFFB4AB)
    val Red90 = Color(0xffFFDAD6)
    val Red95 = Color(0xffFFEDEA)
    val Red99 = Color(0xffFFFBFF)
    val Red100 = Color(0xffFFFFFF)

    val SkyBlue60 = Color(0xff0F99DC)

    val WarmGray0 = Color(0xff000000)
    val WarmGray10 = Color(0xff1C1B1B)
    val WarmGray15 = Color(0xff262625)
    val WarmGray20 = Color(0xff313030)
    val WarmGray25 = Color(0xff3C3B3B)
    val WarmGray30 = Color(0xff484646)
    val WarmGray35 = Color(0xff535252)
    val WarmGray40 = Color(0xff605E5E)
    val WarmGray50 = Color(0xff797776)
    val WarmGray60 = Color(0xff929090)
    val WarmGray70 = Color(0xffADAAAA)
    val WarmGray80 = Color(0xffC9C6C5)
    val WarmGray90 = Color(0xffE5E2E1)
    val WarmGray95 = Color(0xffF4F0EF)
    val WarmGray99 = Color(0xffF4F0EF)
    val WarmGray100 = Color(0xffFFFFFF)    
}
```

## Dimensions

A common decision is to limit the number of available options for element sizes and spacing.
Defining a list of allowed sizes before component design begins helps maintain consistency across the entire system.

> 💡 The most frequent challenge is keeping the UX team disciplined — it’s usually inevitable that some values outside the predefined set will occasionally be used.
> If a particular size is used only in exceptional cases, for example on a single screen, it’s not worth creating a _value token_ or a design token for it.

```kotlin
internal object ExampleThemeReferenceDimensions {
    val Dimension0 = 0.dp
    val Dimension1 = 1.dp
    val Dimension2 = 2.dp
    val Dimension4 = 4.dp
    val Dimension6 = 6.dp
    val Dimension8 = 8.dp
    val Dimension12 = 12.dp
    val Dimension16 = 16.dp
    val Dimension20 = 20.dp
    val Dimension24 = 24.dp
    val Dimension32 = 32.dp
    val Dimension36 = 36.dp
    val Dimension40 = 40.dp
    val Dimension48 = 48.dp
    val Dimension56 = 56.dp
    val Dimension64 = 64.dp
    val Dimension128 = 128.dp
}
```

> ⚠ A common idea is to define dimension tokens like `val DimensionXXS = 1.dp`, `val DimensionXS = 2.dp`, ... `val DimensionXXL = 128.dp`. 
> However, this strategy has several drawbacks:
> 1. If an intermediate size needs to be added — for example, between `DimensionL` and `DimensionXL` — it would require renaming all tokens above DimensionL, as well as updating every component that uses them.
> 2. It may be tempting to adjust the token’s value to better fit a specific component, but this can inadvertently affect the appearance of many other components. 
>    To change a component’s appearance, it is better to assign a different dimension token in that component’s style instead.
>
>    Modifying a _value token_ in this way also contradicts the principle that _value tokens_ should generally remain stable over time.

## Typography

Design systems typically avoid using too many font variants to help maintain consistency across different areas of an application.
This is especially evident in mobile applications, where the range of font sizes is limited by the physical dimensions of the display.

When defining typography reference tokens, ensure that their names reflect the font’s appearance rather than its intended use.
This is important because the same font may be used multiple times for different functions (e.g., as a button label and as a small heading) and in different contexts (e.g., standard mode and accessibility mode).

```kotlin
internal object ExampleReferenceTypography {
    val Figtree_32: TextStyle = DefaultTextStyle.copy(
       fontFamily = ExampleReferenceFonts.Figtree,
       fontWeight = FontWeight.Normal,
       fontSize = 56.sp,
       lineHeight = 66.sp,
       letterSpacing = (-0.25).sp
    )
    val Figtree_24_Bold: TextStyle = DefaultTextStyle.copy(
       fontFamily = ExampleReferenceFonts.Figtree,
       fontWeight = FontWeight.Normal,
       fontSize = 44.sp,
       lineHeight = 52.sp,
       letterSpacing = 0.sp
    )
    val Figtree_18: TextStyle = ...
    val Arial_72: TextStyle = ...
    val Arial_64: TextStyle = ...
    val Arial_48: TextStyle = ...
    val SF_Pro_32: TextStyle = ...
    val SF_Pro_24: TextStyle = ...
    val SF_Pro_16: TextStyle = ...
    val SF_Pro_12: TextStyle = ...
    val SF_Pro_10: TextStyle = ...
}

private val DefaultTextStyle = TextStyle.Default.copy(
   platformStyle = PlatformTextStyle(
      includeFontPadding = false
   ),
   lineHeightStyle = LineHeightStyle(
      alignment = LineHeightStyle.Alignment.Center,
      trim = LineHeightStyle.Trim.None
   ),
)

internal object ExampleReferenceFonts {
   val Figtree = FontFamily(
      Font(R.font.figtree_regular, FontWeight.Normal, FontStyle.Normal),
      Font(R.font.figtree_regular, FontWeight.W400, FontStyle.Normal),
      Font(R.font.figtree_italic, FontWeight.Normal, FontStyle.Italic),
      ...
      Font(R.font.figtree_semi_bold_italic, FontWeight.W600, FontStyle.Italic),
   )
}
```
> 💡 Note that although `TextStyle` defines many font properties, it is typically used as a single design token for convenience.

### Typography in Compose Multiplatform

#### Native font padding

In the previous example, the following code appears:
```kotlin
   platformStyle = PlatformTextStyle(
      includeFontPadding = false
   ),
```
For backward compatibility reasons, Compose used to apply extra padding to the first and last lines of text, 
which significantly complicated maintaining alignment with other platforms, including Figma. 
Therefore, it is recommended to disable this extra padding on Android.   
As of Compose 1.6.0, the extra padding is disabled by default.

However, if you need to support  a Compose version earlier than 1.6.0, you can disable font padding like this:
```kotlin
// Common
internal expect fun providePlatformTextStyle(): PlatformTextStyle?

// Android
internal actual fun providePlatformTextStyle(): PlatformTextStyle? = PlatformTextStyle(includeFontPadding = false)

// Other platforms 
internal actual fun providePlatformTextStyle(): PlatformTextStyle? = null
```
```kotlin
// Common
private val DefaultTextStyle = TextStyle.Default.copy(
   platformStyle = providePlatformTextStyle(),
   lineHeightStyle = LineHeightStyle(
      alignment = LineHeightStyle.Alignment.Center,
      trim = LineHeightStyle.Trim.None
   ),
)
```

#### Font initialization

In the example above, the `FontFamily` instances were defined using the following code:
```kotlin
internal object ExampleReferenceFonts {
   val Figtree = FontFamily(
      Font(R.font.figtree_regular, FontWeight.Normal, FontStyle.Normal),
      Font(R.font.figtree_regular, FontWeight.W400, FontStyle.Normal),
      Font(R.font.figtree_italic, FontWeight.Normal, FontStyle.Italic),
      ...
      Font(R.font.figtree_semi_bold_italic, FontWeight.W600, FontStyle.Italic),
   )
}
```
In the case of Compose Multiplatform, this static approach is unfortunately not possible, 
as in Compose Multiplatform the `FontFamily` method is marked as `@Composable`, 
which prevents the use of `ExampleReferenceFonts` either directly or indirectly in constructs such as `staticCompositionLocalOf` or during static fields initialization.

This issue can be easily resolved by deferring the initialization of `FontFamily`. To begin, delegate the `val Figtree: FontFamily` to `lateinit` backing field and expose a `loadFonts()` method:
```kotlin
internal object ExampleReferenceFonts {
    
    private lateinit var _Figtree: FontFamily
    
    val Figtree: FontFamily
        get() = if (::_Figtree.isInitialized) _Figtree
        else throw IllegalStateException("Trying to use Figtree, but the font is not loaded yet. Call the MobiusReferenceFonts.loadFonts() before using default Mobius typography.")

    @Composable
    fun loadFonts() {
        if (::_Figtree.isInitialized) return
        _Figtree = FontFamily(
           Font(R.font.figtree_regular, FontWeight.Normal, FontStyle.Normal),
           Font(R.font.figtree_regular, FontWeight.W400, FontStyle.Normal),
           Font(R.font.figtree_italic, FontWeight.Normal, FontStyle.Italic),
           ...
           Font(R.font.figtree_semi_bold_italic, FontWeight.W600, FontStyle.Italic),
       )
    }
}
```

The ideal moment to load the fonts is during the initialization of the typography `Module`.

```kotlin
open class DefaultExampleTypography : ExampleTypography {

    @Composable
    override fun initialize() {
       ExampleReferenceFonts.loadFonts()
    }

    ...
}
```
> 💡 See other sections of this documentation for more details on initializing `Modules`.



